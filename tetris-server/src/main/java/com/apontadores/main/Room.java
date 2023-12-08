package com.apontadores.main;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

import com.apontadores.packets.Packet02Redirect;
import com.apontadores.packets.Packet.PacketException;
import com.apontadores.packets.Packet.PacketTypesEnum;
import com.apontadores.packets.Packet03Start;
import com.apontadores.packets.Packet200Heartbeat;
import com.apontadores.packets.Packet00Login;
import com.apontadores.packets.Packet;

public class Room implements Runnable {

  public enum RoomStatesEnum {
    WAITING_P1, // waiting for the owner to be redirected from the server
    WAITING_P2,
    STARTING,
    PLAYING,
    FINISHED;
  }

  public static final int MAX_PLAYERS = 2;
  public static final int MAX_MISSES = 100;

  private static final int MAX_SOCKET_TIMEOUTS = 5;
  public final String name;

  public final int id;
  private boolean full;
  private Player p1;

  private Player p2;
  private int timeoutCount = 0;

  // Player waiting list for the room
  private ArrayBlockingQueue<Player> playerQueue;

  // incoming packets from the players to the server
  private ArrayBlockingQueue<DatagramPacket> inPacketQueue;

  // outgoing packets from the server to the players
  private ArrayBlockingQueue<DatagramPacket> outPacketQueue;

  private RoomStatesEnum state = RoomStatesEnum.WAITING_P1;
  private DatagramSocket roomSocket;

  private DatagramSocket outSocket;
  private final int MAX_PACKET_SIZE = 1024;

  private final int QUEUE_SIZE = 100;
  private TimerBasedService playerRedirect;
  private TimerBasedService playerSync;
  private TimerBasedService packetHandler;

  public Room(
      final int id,
      final String roomName)
      throws SocketException {

    this.id = id;
    this.name = roomName;

    System.out.println("[Room] Created room: \"" + name + "\" with id: " + id);

    roomSocket = new DatagramSocket();
    roomSocket.setSoTimeout(1000);
    outSocket = new DatagramSocket();

    playerQueue = new ArrayBlockingQueue<Player>(QUEUE_SIZE);
    outPacketQueue = new ArrayBlockingQueue<DatagramPacket>(QUEUE_SIZE);
    inPacketQueue = new ArrayBlockingQueue<DatagramPacket>(QUEUE_SIZE);

    playerRedirect = new TimerBasedService(
        new TimerTask() {
          @Override
          public void run() {
            if (state == RoomStatesEnum.STARTING) {
              System.out.println("[Room-playerRedirect] Starting game");
              System.out.println("[Room-playerRedirect] Terminating thread");
              this.cancel();
              return;
            }

            Player p = playerQueue.peek();
            if (p == null) {
              return;
            }

            // send a packet with the room's private port to the player
            byte data[] = new Packet02Redirect(roomSocket.getLocalPort()).asBytes();
            DatagramPacket packet = new DatagramPacket(
                data,
                data.length,
                p.address,
                p.port);

            try {
              outPacketQueue.add(packet);
              System.out.println("[Room - playerRedirect] " + p.username +
                  " to port: " + roomSocket.getLocalPort());
            } catch (Exception e) {
              System.out.println("[Room - playerRedirect] Failed to send redirect packet");
              System.out.println("[Room - playerRedirect] Terminating thread");
              this.cancel();
              state = RoomStatesEnum.FINISHED;
              return;
            }
          }
        }, 5, 50);

    playerSync = new TimerBasedService(
        new TimerTask() {
          @Override
          public void run() {
            if (state == RoomStatesEnum.FINISHED) {
              this.cancel();
              return;
            }

            DatagramPacket outPacket = outPacketQueue.poll();
            if (outPacket == null) {
              return;
            }

            try {
              outSocket.send(outPacket);
            } catch (Exception e) {
              this.cancel();
              state = RoomStatesEnum.FINISHED;
              return;
            }
          }
        }, 0, 2);

    packetHandler = new TimerBasedService(
        new TimerTask() {
          @Override
          public void run() {
            if (state == RoomStatesEnum.FINISHED) {
              this.cancel();
              return;
            }

            DatagramPacket inPacket = inPacketQueue.poll();
            if (inPacket == null) {
              return;
            }

            parsePacket(
                inPacket.getData(),
                inPacket.getLength(),
                inPacket.getAddress(),
                inPacket.getPort());
          }
        }, 0, 2);

    packetHandler.start();
    playerRedirect.start();
    playerSync.start();
  }

  public void addPlayerToQueue(final Player p) {
    System.out.println("[Room] Adding player to queue");
    playerQueue.add(p);
  }

  public boolean isFull() {
    return full;
  }

  public boolean isFinished() {
    return state == RoomStatesEnum.FINISHED;
  }

  public RoomStatesEnum getState() {
    return state;
  }

  @Override
  public void run() {
    System.out.println("[Room] Listening on port " + roomSocket.getLocalPort());

    while (true) {
      System.out.println("[Room] outgoing queue size: " + outPacketQueue.size());
      System.out.println("[Room] incoming queue size: " + inPacketQueue.size());
      System.out.println("[Room] --------------------------");

      if (p1 != null && !p1.isAlive()) {
        System.out.println("[Room] Player 1 timed out");
        state = RoomStatesEnum.FINISHED;
      }

      if (p2 != null && !p2.isAlive()) {
        System.out.println("[Room] Player 2 timed out");
        state = RoomStatesEnum.FINISHED;
      }

      if (timeoutCount >= MAX_SOCKET_TIMEOUTS) {
        System.out.println("[Room] Socket timed out " + timeoutCount + " times");
        System.out.println("[Room] Terminating thread");
        state = RoomStatesEnum.FINISHED;
        return;
      }

      if (state == RoomStatesEnum.FINISHED) {
        return;
      }

      try {
        DatagramPacket packet = new DatagramPacket(
            new byte[MAX_PACKET_SIZE],
            MAX_PACKET_SIZE);
        roomSocket.receive(packet);
        inPacketQueue.add(packet);
      } catch (SocketTimeoutException t) {
        timeoutCount++;
        continue;
      } catch (final Exception e) {
        System.out.println("[Room] Failed to receive packet");
        System.out.println("[Room] Terminating thread");
        return;
      }

    }
  }

  public String getName() {
    return name;
  }

  private void sendPacket(Packet p, Player player) {
    outPacketQueue.add(new DatagramPacket(
        p.asBytes(),
        p.asBytes().length,
        player.address,
        player.port));
  }

  private void parsePacket(
      byte[] datagramData,
      int dataLength,
      InetAddress datagramAddress,
      int datagramPort) {

    switch (state) {
      case WAITING_P1:
        p1 = handleLogin(
            datagramData,
            dataLength,
            datagramAddress,
            datagramPort);

        if (p1 != null) {
          System.out.println("[Room] Player 1 joined room " + name);
          state = RoomStatesEnum.WAITING_P2;
          p1.packetHit();
          System.out.println("[Room] New state: " + state.name());
        }
        break;

      case WAITING_P2: {
        Player sender = getPacketSender(datagramAddress, datagramPort);
        if (sender == null) {
          System.out.println("[Room-WaitingP2] Received packet from unknown origin");
          p2 = handleLogin(
              datagramData,
              dataLength,
              datagramAddress,
              datagramPort);

          if (p2 != null) {
            full = true;
            state = RoomStatesEnum.STARTING;
          }

          return;
        }

        // if the privous condition was not hit then the packet was sent by p1,
        // handle it
        String tokens[] = Packet.tokenize(datagramData, dataLength);
        PacketTypesEnum packetType = Packet.lookupPacket(tokens);

        switch (packetType) {
          case LOGIN:
            try {
              Packet00Login loginPacket = new Packet00Login()
                  .fromTokens(tokens);
              sendPacket(loginPacket, p1);
            } catch (PacketException e) {
              System.out.println("[Room-WaitingP2] : " + e.getMessage());
            }
            return;

          case HEARTBEAT:
            try {
              Packet200Heartbeat heartbeatPacket = new Packet200Heartbeat()
                  .fromTokens(tokens);
              sendPacket(heartbeatPacket, p1);
              p1.packetHit();
            } catch (PacketException e) {
              System.out.println("[Room-WaitingP2] : " + e.getMessage());
            }
            return;

          case INVALID:
            System.out.println("[Room-WaitingP2] Invalid packet received");
            return;

          default:
            System.out.println("[Room-WaitingP2] Unexpected packet type: "
                + packetType.name());
            return;
        }
      }

      case STARTING: {
        String tokens[] = Packet.tokenize(datagramData, dataLength);
        PacketTypesEnum packetType = Packet.lookupPacket(tokens);
        Player sender = getPacketSender(datagramAddress, datagramPort);
        if (sender == null) {
          System.out.println("[Room-WaitingP2] Received packet from unknown origin");
          return;
        }

        switch (packetType) {
          case LOGIN:
            try {
              Packet00Login loginPacket = new Packet00Login()
                  .fromTokens(tokens);
              sendPacket(loginPacket, sender);
            } catch (PacketException e) {
              System.out.println("[Room-WaitingP2] : " + e.getMessage());
            }
            return;
          case HEARTBEAT:
            try {
              Packet200Heartbeat heartbeatPacket = new Packet200Heartbeat()
                  .fromTokens(tokens);
              sendPacket(heartbeatPacket, sender);
              sender.packetHit();
              sender.setReady(true);
            } catch (PacketException e) {
              System.out.println("[Room-WaitingP2] : " + e.getMessage());
            }
            break;

          case INVALID:
            System.out.println("[Room-WaitingP2] Invalid packet received");
            return;

          default:
            System.out.println("[Room-WaitingP2] Unexpected packet type: "
                + packetType.name());
            return;
        }

        if (p1.isReady() && p2.isReady()) {
          System.out.println("[Room] Both players ready");
          // send game start packet
          Packet03Start packet1 = new Packet03Start(p1.username);
          Packet03Start packet2 = new Packet03Start(p2.username);

          // reset transaction ID's
          packet1.setTransactionID(1);
          packet2.setTransactionID(1);

          sendPacket(packet2, p1);
          sendPacket(packet1, p2);
          state = RoomStatesEnum.PLAYING;
          System.out.println("[Starting Game]");
        }
        break;
      }

      case PLAYING: {
        Player sender = getPacketSender(datagramAddress, datagramPort);
        if (sender == null) {
          System.out.println("[Room-Playing] Received packet from unknown origin");
          return;
        }

        Player target = sender == p1 ? p2 : p1;

        outPacketQueue.add(new DatagramPacket(
            datagramData,
            dataLength,
            target.address,
            target.port));

        sender.packetHit();
        break;
      }

      default:
        System.out.println("[Room] Invalid state");
        System.out.println("[Room] Terminating thread");
        state = RoomStatesEnum.FINISHED;
        break;
    }

  }

  private Player getPacketSender(InetAddress address, int port) {
    if (p1 != null && p1.address.equals(address) && p1.port == port)
      return p1;
    else if (p2 != null && p2.address.equals(address) && p2.port == port)
      return p2;
    else
      return null;
  }

  private Player handleLogin(
      byte[] datagramData,
      int dataLenght,
      InetAddress datagramAddress,
      int datagramPort) {

    String tokens[] = Packet.tokenize(datagramData, dataLenght);
    PacketTypesEnum packetType = Packet.lookupPacket(tokens);
    if (packetType != PacketTypesEnum.LOGIN) {
      System.out.println("[Room - handleLogin] Unexpected packet type: " + packetType.name());
      return null;
    }

    Packet00Login packet;
    try {
      packet = new Packet00Login().fromTokens(tokens);
    } catch (PacketException e) {
      System.out.println("[Room - handleLogin] : " + e.getMessage());
      return null;
    }

    Player player = playerQueue.poll();
    if (player == null) {
      return null;
    }

    System.out.println("[Room - handleLogin] Processing login packet for player "
        + player.username);

    if (!datagramAddress.equals(player.address) || datagramPort != player.port) {
      System.out.println("[Room - handleLogin] Address mismatch");
      return null;
    }

    if (p1 != null && packet.getUsername().equals(p1.username)) {
      System.out.println("[Room - handleLogin] Username already in use");
      // TODO: send error packet
      return null;
    }

    sendPacket(packet, player);
    return player;
  }

}
