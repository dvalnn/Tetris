package com.apontadores.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.validator.routines.InetAddressValidator;

import com.apontadores.main.Game;
import com.apontadores.main.TimerBasedService;
import com.apontadores.networking.NetworkControl.ClientStates;
import com.apontadores.networking.NetworkControl.ConnectionPhases;
import com.apontadores.packets.Packet;
import com.apontadores.packets.Packet.PacketTypesEnum;
import com.apontadores.packets.Packet00Login;
import com.apontadores.packets.Packet02Redirect;
import com.apontadores.packets.Packet03Start;
import com.apontadores.packets.Packet100Update;
import com.apontadores.packets.Packet200Heartbeat;

//NOTE: Client - Server architecture overview
//      Client: TetrisClient class
//                           - sends the local game state to the server
//                           - receives the remote game state from the server
//      Server: TetrisServer module (artifact tetris-server)
//                           - serves multiple game sessions at the same time
//                           (one for each pair of players) into separate
//                           threads.
//                           - different game sessions are independent,
//                           represented by a "room", to whicht the players
//                           connect.
//                           - synchronizes the game state between clients
//
//NOTE: The server is not responsible for the game logic, it only acts as a
//      relay serveice for client packets. The game logic is implemented
//      on the client side. This is a Peer2Peer architecture, with a dedicated
//      broker server. This solution was chosen because the game needs to be
//      playable even if the server is offline. Implementing a broker server
//      provides the ability to play outside of a LAN environment while offering
//      some safety to the players (as the packets are redirected through the 
//      server, the players' IP addresses are not revealed to each other).
//
//NOTE: The game state is represented by the PlayerData class, which also acts
//      as a communication buffer between the game logic and the game client.
//
//NOTE: Client-Server communication protocol:
//      - Login phase:
//        - Client sends a login request packet to the server, on the public port
//          The packet contains the player's username and room name he wants to
//          join.
//          - Should the room not exist, the server creates it.
//          - Should the room be full, the server sends a packet to the client.
//          - Should the room be available, but the player's username already
//            taken, the server sends a packet to the client.
//        - Server redicts the player to the room's private port.
//        - Client connects to the room's private port.
//        - If the room is full, the game starts. Otherwise the client waits for
//          the other player to connect while trading heartbeats with the server.
//      - Game phase:
//        - When both players are connected, the server sends a packet to both
//        signalling the start of the game. Each players username is also sent
//        to each other.
//        - Afterwards the server acts as a relay service, forwarding the packets
//        from one client to the other.
//        - The server also handles player disconnect detection.
//        - The server also implements a buffer for each client to store the most
//        recent packets in case retransmission is needed.
//
public class TetrisClient implements Runnable {

  public static final int SERVER_DEFAULT_PORT = 42069;
  public static final int MAX_PACKET_SIZE = 1024;
  public static final long MAX_PACKET_DELTA = 2_500_000_000L;
  private static final int FULL_SYNC_THRESHOLD = 100;

  private ClientStates state;
  private ConnectionPhases phase;

  private final TimerBasedService packetProcessor;

  private long lastReceivedPacketTime;
  private boolean connectionTimeout;

  private final ArrayBlockingQueue<DatagramPacket> receivedPackets;
  private final ArrayBlockingQueue<DatagramPacket> outgoingPackets;

  public ArrayBlockingQueue<Packet100Update> receivedUpdates;
  public ArrayBlockingQueue<Packet100Update> outgoingUpdates;

  private InetAddress serverAddress;
  private int serverPort;

  private int transactionErrorCount = 0;
  private int lastSentTransactionId = 0;
  private int lastReceivedTransactionId = 0;

  private boolean connectionAborted = false;

  private String playerName;
  private String roomName;

  public TetrisClient() {
    this.state = ClientStates.INACTIVE;
    this.phase = ConnectionPhases.INACTIVE;

    // create a player data instance for local and remote players

    receivedPackets = new ArrayBlockingQueue<>(100);
    outgoingPackets = new ArrayBlockingQueue<>(100);

    receivedUpdates = new ArrayBlockingQueue<>(1000);
    outgoingUpdates = new ArrayBlockingQueue<>(1000);

    packetProcessor = new TimerBasedService(
        new TimerTask() {
          private int missedPackets;

          @Override
          public void run() {
            if (state != ClientStates.RUNNING)
              this.cancel();

            processConPhase();

            if (receivedPackets.isEmpty())
              return;

            final DatagramPacket received = receivedPackets.poll();
            final int receivedTransactionId = processPacket(received);

            if (receivedTransactionId == -1) {
              transactionErrorCount++;
              if (transactionErrorCount < FULL_SYNC_THRESHOLD)
                return;
              else {
                // TODO: request full sync instead of disconnecting
                System.out.println("[TetrisClient] Too many transaction errors!");
                connectionTimeout = true;
                this.cancel();
              }
            }

            transactionErrorCount = 0;

            final int transactionDelta = receivedTransactionId -
                lastReceivedTransactionId;

            missedPackets += transactionDelta - 1;

            if (missedPackets > FULL_SYNC_THRESHOLD) {
              System.out.println("[TetrisClient] Missed packet count: " + missedPackets);
              System.out.println("[TetrisClient] Full sync threshold: " + FULL_SYNC_THRESHOLD);
              if (phase == ConnectionPhases.PLAYING) {
                missedPackets = 0;
                System.out.println("[TetrisClient] Requesting full sync!");
                // sendPacket(new Packet200Heartbeat());
              }
            }

            lastReceivedTransactionId = receivedTransactionId;
          }
        }, 0, 5);
  }

  @Override
  public void run() {
    DatagramSocket socket;

    try {
      socket = new DatagramSocket();
      socket.setSoTimeout(1);
    } catch (final SocketException e) {
      e.printStackTrace();
      return;
    }

    serverPort = SERVER_DEFAULT_PORT;

    state = ClientStates.RUNNING;
    phase = ConnectionPhases.DISCONNECTED;

    // TODO: start packet sender only after the game starts
    // packetSender.start();

    while (true) {
      if (phase == ConnectionPhases.DISCONNECTED) {
        try {
          Thread.sleep(10);
        } catch (final InterruptedException e) {
          e.printStackTrace();
        }

        lastReceivedPacketTime = System.nanoTime();
        continue;
      }

      final long packetDelta = System.nanoTime() - lastReceivedPacketTime;
      // System.out.println("[TetrisClient] Packet delta: " + packetDelta);
      if (packetDelta > MAX_PACKET_DELTA) {
        System.out.println("[TetrisClient] Max packet delta surpassed!");
        connectionTimeout = true;
      }

      if (connectionTimeout) {
        System.out.println("[TetrisClient] Connection lost!");
        state = ClientStates.CONNECTION_TIMEOUT;
        break;
      }

      if (connectionAborted) {
        System.out.println("[TetrisClient] Connection aborted!");
        state = ClientStates.SOCKET_ERROR;
        break;
      }

      try {
        final DatagramPacket receivedPacket = new DatagramPacket(
            new byte[MAX_PACKET_SIZE], MAX_PACKET_SIZE);
        socket.receive(receivedPacket);
        lastReceivedPacketTime = System.nanoTime();
        receivedPackets.add(receivedPacket);
      } catch (final SocketTimeoutException e) {
        // NOTE: timeouts are expected
        // Do nothing and continue
      } catch (final IOException e) {
        e.printStackTrace();
        break;
      }

      final DatagramPacket packet = outgoingPackets.poll();
      if (packet == null)
        continue;

      try {
        socket.send(packet);
      } catch (final IOException e) {
        e.printStackTrace();
        break;
      }

    }

    // packetProcessor.stop();
    socket.close();
  }

  public boolean connectToServer(
      final String serverAddress,
      final String playerName,
      final String roomName) {

    if (serverAddress == null || playerName == null || roomName == null)
      return false;

    final InetAddressValidator validator = new InetAddressValidator();

    if (!validator.isValidInet4Address(serverAddress))
      return false;
    try {
      this.serverAddress = InetAddress.getByName(serverAddress);
    } catch (final Exception e) {
      e.printStackTrace();
      return false;
    }

    this.playerName = playerName;
    this.roomName = roomName;

    packetProcessor.start();
    phase = ConnectionPhases.CONNECTING;
    lastReceivedPacketTime = System.nanoTime();

    return true;
  }

  public ClientStates getState() {
    return state;
  }

  public ConnectionPhases getPhase() {
    return phase;
  }

  public void start() {
    if (state == ClientStates.RUNNING)
      return;

    phase = ConnectionPhases.DISCONNECTED;
    new Thread(this).start();
  }

  public void abortConnection() {
    connectionAborted = true;
  }

  private int processPacket(final DatagramPacket packet) {
    final String[] tokens = Packet.tokenize(packet.getData(), packet.getLength());
    final PacketTypesEnum packetType = Packet.lookupPacket(tokens);
    if (packetType == null) {
      System.out.println("[GameClient] Packet type not found: " + tokens[0]);
      return -1;
    }

    final Packet receivedPacket = buildReceivedPacket(packetType, tokens);
    if (receivedPacket == null) {
      System.out.println("[GameClient] Packet error: " + tokens[0]);
      return -1;
    }

    switch (phase) {
      case CONNECTING -> {
        if (packetType == PacketTypesEnum.REDIRECT) {
          serverPort = ((Packet02Redirect) receivedPacket).getPort();
          System.out.println("[GameClient - CONNECTING] Redirecting to port: " +
              serverPort);

        } else if (packetType == PacketTypesEnum.LOGIN) {
          final String username = ((Packet00Login) receivedPacket).getUsername();
          final String roomName = ((Packet00Login) receivedPacket).getRoomName();

          System.out.println("[GameClient - CONNECTING] Login echo: " +
              username + " " + roomName);

          if (username.equals(playerName)
              && roomName.equals(roomName)) {

            phase = ConnectionPhases.WAITING_FOR_OPPONENT;
            System.out.println("[GameClient - CONNECTING] Login successful");
            System.out.println("[GameClient - CONNECTING] Waiting for opponent...");
          }
        } else {
          System.out.println("[GameClient - CONNECTING] Unexpected packet type: " +
              packetType);
          return -1;
        }
        // TODO: handle error packets
      }
      case WAITING_FOR_OPPONENT -> {
        if (packetType == PacketTypesEnum.REDIRECT) {
          serverPort = ((Packet02Redirect) receivedPacket).getPort();
        } else if (packetType == PacketTypesEnum.HEARTBEAT) {
          // NOTE: Heartbeats are expected. Do nothing and continue
        } else if (packetType == PacketTypesEnum.START) {
          Game.setOpponentName(((Packet03Start) receivedPacket).getOpponentName());
          phase = ConnectionPhases.PLAYING;
        } else {
          System.out.println("[GameClient - WAITING_FOR_OPPONENT] Unexpected packet type: " +
              packetType);
          return -1;
        }
      }
      case PLAYING -> {
        if (packetType == PacketTypesEnum.UPDATE) {
          receivedUpdates.add((Packet100Update) receivedPacket);
        }
      }
      case GAME_OVER -> {
        // TODO: handle game over packets
      }
      default -> {
      }
    }

    return receivedPacket.getTransactionID();
  }

  private void processConPhase() {
    switch (phase) {
      case CONNECTING -> sendPacket(new Packet00Login(
          playerName,
          roomName));
      case WAITING_FOR_OPPONENT -> sendPacket(new Packet200Heartbeat());
      case PLAYING -> {
        if (!outgoingUpdates.isEmpty())
          sendPacket(outgoingUpdates.poll());
      }
      case GAME_OVER -> {
        // TODO: send game over packet
      }
      default -> {
      }
    }
  }

  private void sendPacket(final Packet packet) {
    packet.setTransactionID(++lastSentTransactionId);
    outgoingPackets.add(
        new DatagramPacket(
            packet.asBytes(),
            packet.asBytes().length,
            serverAddress,
            serverPort));
  }

  private Packet buildReceivedPacket(
      final PacketTypesEnum packetType,
      final String[] tokens) {

    try {
      return switch (packetType) {
        case LOGIN -> new Packet00Login().fromTokens(tokens);
        case REDIRECT -> new Packet02Redirect().fromTokens(tokens);
        case START -> new Packet03Start().fromTokens(tokens);
        case UPDATE -> new Packet100Update().fromTokens(tokens);
        case HEARTBEAT -> new Packet200Heartbeat().fromTokens(tokens);
        default -> null;
      };
    } catch (final Packet.PacketException e) {
      System.out.println("[GameClient] Packet error: " + e.getMessage());
      return null;
    }
  }

}
