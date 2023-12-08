package com.apontadores.networking;

import static com.apontadores.utils.Constants.GameConstants.BOARD_HEIGHT;

import java.awt.Color;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.validator.routines.InetAddressValidator;

import com.apontadores.gameElements.boards.PlayerBoard;
import com.apontadores.main.TimerBasedService;
import com.apontadores.networking.NetworkControl.ClientStates;
import com.apontadores.networking.NetworkControl.ConnectionPhases;
import com.apontadores.packets.Packet;
import com.apontadores.packets.Packet00Login;
import com.apontadores.packets.Packet02Redirect;
import com.apontadores.packets.Packet03Start;
import com.apontadores.packets.Packet100Update;
import com.apontadores.packets.Packet200Heartbeat;
import com.apontadores.packets.Packet.PacketTypesEnum;

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

  private PlayerData playerData;

  private ClientStates state;
  private ConnectionPhases phase;

  private TimerBasedService packetProcessor;
  private TimerBasedService packetSender;

  private long lastReceivedPacketTime;
  private boolean connectionTimeout;

  private ArrayBlockingQueue<DatagramPacket> receivedPackets;
  private ArrayBlockingQueue<DatagramPacket> outgoingPackets;

  public ArrayBlockingQueue<Packet100Update> receivedUpdates;
  public ArrayBlockingQueue<Packet100Update> outgoingUpdates;

  private InetAddress serverAddress;
  private int serverPort;

  private int lastSentTransactionId = 0;
  private int lastReceivedTransactionId = 0;
  private int transactionErrorCount = 0;
  private int maxTransactionErrorCount = 1000;

  private boolean connectionAborted = false;

  private DatagramSocket outSocket;

  public TetrisClient(String username, String roomName) {
    this.state = ClientStates.INACTIVE;
    this.phase = ConnectionPhases.INACTIVE;

    // create a player data instance for local and remote players
    playerData = new PlayerData()
        .setPlayerName(username)
        .setRoomName(roomName);

    receivedPackets = new ArrayBlockingQueue<>(1000);
    outgoingPackets = new ArrayBlockingQueue<>(1000);

    receivedUpdates = new ArrayBlockingQueue<>(1000);
    outgoingUpdates = new ArrayBlockingQueue<>(1000);

    packetProcessor = new TimerBasedService(
        new TimerTask() {
          @Override
          public void run() {
            if (state != ClientStates.RUNNING)
              this.cancel();

            if (transactionErrorCount > maxTransactionErrorCount) {
              System.out.println("[TetrisClient] Too many transaction errors!");
              connectionTimeout = true;
              this.cancel();
            }

            int receivedTransactionId = -1;

            if (!receivedPackets.isEmpty()) {
              DatagramPacket received = receivedPackets.poll();
              receivedTransactionId = processPacket(received);
            }

            if (receivedTransactionId == -1)
              transactionErrorCount++;
            else {
              int transactionDelta = receivedTransactionId -
                  lastReceivedTransactionId;
              if (transactionDelta > 1) {
                // TODO: NACK missing packets
                // transactionErrorCount += transactionDelta - 1;
                System.out.println("[TetrisClient] Missed " +
                    (transactionDelta - 1) + "packets !");
              } else
                transactionErrorCount--;

              lastReceivedTransactionId = receivedTransactionId;
            }
            processConPhase();
          }
        }, 0, 5);

    packetSender = new TimerBasedService(
        new TimerTask() {
          @Override
          public void run() {
            if (state != ClientStates.RUNNING)
              this.cancel();

            if (phase != ConnectionPhases.PLAYING)
              return;

            if (!outgoingPackets.isEmpty()) {
              DatagramPacket packet = outgoingPackets.poll();
              try {
                outSocket.send(packet);
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
          }
        }, 0, 2);

  }

  @Override
  public void run() {
    DatagramSocket socket;

    try {
      socket = new DatagramSocket();
      outSocket = new DatagramSocket();
      socket.setSoTimeout(1);
    } catch (SocketException e) {
      e.printStackTrace();
      return;
    }

    serverPort = SERVER_DEFAULT_PORT;

    state = ClientStates.RUNNING;
    phase = ConnectionPhases.DISCONNECTED;

    lastReceivedPacketTime = System.nanoTime();
    packetProcessor.start();

    // TODO: start packet sender only after the game starts
    packetSender.start();

    while (true) {
      if (phase == ConnectionPhases.DISCONNECTED) {
        try {
          Thread.sleep(10);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        lastReceivedPacketTime = System.nanoTime();
        continue;
      }

      long packetDelta = System.nanoTime() - lastReceivedPacketTime;
      if (packetDelta > MAX_PACKET_DELTA) {
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
        DatagramPacket receivedPacket = new DatagramPacket(
            new byte[MAX_PACKET_SIZE], MAX_PACKET_SIZE);
        socket.receive(receivedPacket);
        lastReceivedPacketTime = System.nanoTime();
        receivedPackets.add(receivedPacket);
      } catch (SocketTimeoutException e) {
        ;// NOTE: timeouts are expected
         // Do nothing and continue
      } catch (IOException e) {
        e.printStackTrace();
        break;
      }

      if (phase != ConnectionPhases.PLAYING) {
        DatagramPacket packet = outgoingPackets.poll();
        if (packet == null)
          continue;

        try {
          socket.send(packet);
        } catch (IOException e) {
          e.printStackTrace();
          break;
        }
      }
    }

    packetProcessor.stop();
    socket.close();
  }

  private int processPacket(DatagramPacket packet) {
    String tokens[] = Packet.tokenize(packet.getData(), packet.getLength());
    PacketTypesEnum packetType = Packet.lookupPacket(tokens);
    if (packetType == null) {
      System.out.println("[GameClient] Packet type not found: " + tokens[0]);
      return -1;
    }

    Packet receivedPacket = buildReceivedPacket(packetType, tokens);
    if (receivedPacket == null)
      return -1;

    switch (phase) {
      case CONNECTING: {
        if (packetType == PacketTypesEnum.REDIRECT) {
          serverPort = ((Packet02Redirect) receivedPacket).getPort();

        } else if (packetType == PacketTypesEnum.LOGIN) {
          String username = ((Packet00Login) receivedPacket).getUsername();
          String roomName = ((Packet00Login) receivedPacket).getRoomName();

          if (username.equals(playerData.getPlayerName())
              && roomName.equals(playerData.getRoomName())) {
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
        break;

      case WAITING_FOR_OPPONENT: {
        if (packetType == PacketTypesEnum.REDIRECT)
          serverPort = ((Packet02Redirect) receivedPacket).getPort();
        else if (packetType == PacketTypesEnum.HEARTBEAT) {
          ; // NOTE: Heartbeats are expected. Do nothing and continue
        } else if (packetType == PacketTypesEnum.START) {
          playerData.setOpponentName(((Packet03Start) receivedPacket).getOpponentName());
          phase = ConnectionPhases.PLAYING;
        } else {
          System.out.println("[GameClient - WAITING_FOR_OPPONENT] Unexpected packet type: " +
              packetType);
          return -1;
        }
      }
        break;

      case PLAYING: {
        if (packetType == PacketTypesEnum.UPDATE) {
          receivedUpdates.add((Packet100Update) receivedPacket);
          // playerData.parsePacket((Packet100Update) receivedPacket);
        }
      }
        break;

      case GAME_OVER: {
        // TODO: handle game over packets
      }
        break;

      default:
        break;
    }

    return receivedPacket.getTransactionID();
  }

  private void processConPhase() {
    switch (phase) {
      case CONNECTING: {
        sendPacket(new Packet00Login(
            playerData.getPlayerName(),
            playerData.getRoomName()));
      }
        break;

      case WAITING_FOR_OPPONENT: {
        sendPacket(new Packet200Heartbeat());
      }
        break;

      case PLAYING: {
        if (!outgoingUpdates.isEmpty())
          sendPacket(outgoingUpdates.poll());
      }
        break;

      case GAME_OVER: {
        // TODO: send game over packet
      }
        break;

      default:
        break;
    }
  }

  private void sendPacket(Packet packet) {
    packet.setTransactionID(++lastSentTransactionId);
    outgoingPackets.add(
        new DatagramPacket(
            packet.asBytes(),
            packet.asBytes().length,
            serverAddress,
            serverPort));
  }

  private Packet buildReceivedPacket(
      PacketTypesEnum packetType,
      String[] tokens) {

    try {
      switch (packetType) {
        case LOGIN:
          return new Packet00Login().fromTokens(tokens);
        case REDIRECT:
          return new Packet02Redirect().fromTokens(tokens);
        case START:
          return new Packet03Start().fromTokens(tokens);
        case UPDATE:
          return new Packet100Update().fromTokens(tokens);
        case HEARTBEAT:
          return new Packet200Heartbeat().fromTokens(tokens);

        default:
          return null;
      }
    } catch (Packet.PacketException e) {
      System.out.println("[GameClient] Packet error: " + e.getMessage());
      return null;
    }

  }

  public boolean connectToServer(String serverAddress) {
    InetAddressValidator validator = new InetAddressValidator();
    if (!validator.isValidInet4Address(serverAddress))
      return false;
    try {
      this.serverAddress = InetAddress.getByName(serverAddress);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    phase = ConnectionPhases.CONNECTING;
    return true;
  }

  public PlayerData getPlayerData() {
    return playerData;
  }

  public ClientStates getState() {
    return state;
  }

  public ConnectionPhases getPhase() {
    return phase;
  }

  public void start() {
    new Thread(this).start();
  }

  public void abortConnection() {
    connectionAborted = true;
  }
}
