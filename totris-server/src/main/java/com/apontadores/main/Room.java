package com.apontadores.main;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

import com.apontadores.packets.Packet;
import com.apontadores.packets.Packet.PacketTypesEnum;
import com.apontadores.packets.Packet00Login;
import com.apontadores.packets.Packet02Redirect;
import com.apontadores.packets.Packet03Start;
import com.apontadores.packets.Packet200Heartbeat;
import com.apontadores.packets.Packet201Error;
import com.apontadores.packets.PacketException;

public class Room implements Runnable {

  public enum RoomStatesEnum {
    WAITING_P1, // waiting for the owner to be redirected from the server
    WAITING_P2,
    STARTING,
    PLAYING,
    FINISHED;
  }

  private final int MAX_PACKET_SIZE = 1024;
  public final String name;
  public final int id;

  private Player p1;
  private Player p2;
  private boolean full;

  private RoomStatesEnum state = RoomStatesEnum.WAITING_P1;

  // Player waiting list for the room
  private ArrayBlockingQueue<Player> playerQueue;

  // incoming packets from the players to the server
  private ArrayBlockingQueue<DatagramPacket> inPacketQueue;

  // outgoing packets from the server to the players
  private ArrayBlockingQueue<DatagramPacket> outPacketQueue;

  private DatagramSocket roomSocket;
  private DatagramSocket outSocket;

  public Room(
      final int id,
      final String roomName)
      throws SocketException {

    this.id = id;
    this.name = roomName;

    System.out.println("[Room] Created room: \"" + name + "\" with id: " + id);

    outSocket = new DatagramSocket();
    roomSocket = new DatagramSocket();
    roomSocket.setSoTimeout(1000);

    final int QUEUE_SIZE = 100;
    playerQueue = new ArrayBlockingQueue<Player>(QUEUE_SIZE);
    outPacketQueue = new ArrayBlockingQueue<DatagramPacket>(QUEUE_SIZE);
    inPacketQueue = new ArrayBlockingQueue<DatagramPacket>(QUEUE_SIZE);

    TimerBasedService playerRedirect = new TimerBasedService(
        new TimerTask() {
          @Override
          public void run() {
            if (state == RoomStatesEnum.STARTING) {
              System.out.println("[Room-playerRedirect] Starting game");
              System.out.println("[Room-playerRedirect] Terminating thread");
              this.cancel();
              return;
            }

            final Player p = playerQueue.peek();
            if (p == null) {
              return;
            }

            if (p1 != null && p1.username.equals(p.username)) {
              playerQueue.poll();
              sendPacket(
                  new Packet201Error(Packet201Error.ErrorTypesEnum.USERNAME_IN_USE),
                  p);
              return;
            }

            if (p2 != null && p2.username.equals(p.username)) {
              playerQueue.poll();
              return;
            }

            try {
              // send a packet with the room's private port to the player
              final Packet packet = new Packet02Redirect(roomSocket.getLocalPort());
              sendPacket(packet, p);
              System.out.println("[Room - playerRedirect] " + p.username +
                  " to port: " + roomSocket.getLocalPort());
            } catch (final Exception e) {
              System.out.println("[Room - playerRedirect] Failed to send redirect packet");
              System.out.println("[Room - playerRedirect] Terminating thread");
              this.cancel();
              state = RoomStatesEnum.FINISHED;
              return;
            }
          }
        }, 5, 50);

    TimerBasedService playerSync = new TimerBasedService(
        new TimerTask() {
          @Override
          public void run() {
            if (state == RoomStatesEnum.FINISHED) {
              this.cancel();
              return;
            }

            final DatagramPacket outPacket = outPacketQueue.poll();
            if (outPacket == null) {
              return;
            }

            try {
              outSocket.send(outPacket);
            } catch (final Exception e) {
              this.cancel();
              state = RoomStatesEnum.FINISHED;
              return;
            }
          }
        }, 0, 2);

    TimerBasedService packetHandler = new TimerBasedService(
        new TimerTask() {
          @Override
          public void run() {
            if (state == RoomStatesEnum.FINISHED) {
              this.cancel();
              return;
            }

            final DatagramPacket inPacket = inPacketQueue.poll();
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
    Player player = playerQueue.peek();

    if (player != null &&
        player.address.equals(p.address) &&
        player.port == p.port) {
      return;
    }

    if (playerQueue.size() >= 2) {
      return;
    }

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

    int timeoutCount = 0;

    while (true) {
      if (p1 != null && !p1.isAlive()) {
        System.out.println("[Room] Player 1 timed out");
        state = RoomStatesEnum.FINISHED;
      }

      if (p2 != null && !p2.isAlive()) {
        System.out.println("[Room] Player 2 timed out");
        state = RoomStatesEnum.FINISHED;
      }

      final int MAX_SOCKET_TIMEOUTS = 5;
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
        final DatagramPacket packet = new DatagramPacket(
            new byte[MAX_PACKET_SIZE],
            MAX_PACKET_SIZE);
        roomSocket.receive(packet);
        inPacketQueue.add(packet);
      } catch (final SocketTimeoutException t) {
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

  private void sendPacket(final Packet p, final Player player) {
    outPacketQueue.add(new DatagramPacket(
        p.asBytes(),
        p.asBytes().length,
        player.address,
        player.port));
  }

  private void parsePacket(
      final byte[] datagramData,
      final int dataLength,
      final InetAddress datagramAddress,
      final int datagramPort) {

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
        final Player sender = getPacketSender(datagramAddress, datagramPort);
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

        // if the previous condition was not hit then the packet was sent by p1,
        // and needs to be processed
        final String tokens[] = Packet.tokenize(datagramData, dataLength);
        final PacketTypesEnum packetType = Packet.lookupPacket(tokens);

        switch (packetType) {
          case LOGIN:
            try {
              final Packet00Login loginPacket = new Packet00Login()
                  .fromTokens(tokens);
              sendPacket(loginPacket, p1);
            } catch (final PacketException e) {
              System.out.println("[Room-WaitingP2] : " + e.getMessage());
            }
            return;

          case HEARTBEAT:
            try {
              final Packet heartbeatPacket = new Packet200Heartbeat()
                  .fromTokens(tokens);
              sendPacket(heartbeatPacket, p1);
              p1.packetHit();
            } catch (final PacketException e) {
              System.out.println("[Room-WaitingP2] : " + e.getMessage());
            }
            return;

          case DISCONNECT:
            System.out.println("[Room-WaitingP2] Room terminated by player "
                + sender.username);
            state = RoomStatesEnum.FINISHED;
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
        final String tokens[] = Packet.tokenize(datagramData, dataLength);
        final PacketTypesEnum packetType = Packet.lookupPacket(tokens);
        final Player sender = getPacketSender(datagramAddress, datagramPort);
        if (sender == null) {
          System.out.println("[Room-WaitingP2] Received packet from unknown origin");
          return;
        }

        switch (packetType) {
          case LOGIN:
            try {
              final Packet00Login loginPacket = new Packet00Login()
                  .fromTokens(tokens);
              sendPacket(loginPacket, sender);
            } catch (final PacketException e) {
              System.out.println("[Room-WaitingP2] : " + e.getMessage());
            }
            return;
          case HEARTBEAT:
            try {
              final Packet heartbeatPacket = new Packet200Heartbeat()
                  .fromTokens(tokens);
              sendPacket(heartbeatPacket, sender);
              sender.packetHit();
              sender.setReady(true);
            } catch (final PacketException e) {
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
          final Packet03Start packet1 = new Packet03Start(p1.username);
          final Packet03Start packet2 = new Packet03Start(p2.username);

          packet1.setTransactionID(1);
          packet2.setTransactionID(2);

          sendPacket(packet2, p1);
          sendPacket(packet1, p2);
          state = RoomStatesEnum.PLAYING;
          System.out.println("[Starting Game]");
        }
        break;
      }

      case PLAYING: {
        final Player sender = getPacketSender(datagramAddress, datagramPort);
        if (sender == null) {
          System.out.println("[Room-Playing] Received packet from unknown origin");
          return;
        }

        final Player target = sender == p1 ? p2 : p1;

        PacketTypesEnum packetType = Packet.lookupPacket(
            Packet.tokenize(datagramData, dataLength));
        switch (packetType) {
          case INVALID -> {
            System.out.println("[Room-Playing] Invalid packet received");
            return;
          }

          case DISCONNECT -> {
            System.out.println("[Room-Playing] Room terminated by player "
                + sender.username);
            state = RoomStatesEnum.FINISHED;
            return;
          }

          default -> {
          }
        }

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

  private Player getPacketSender(final InetAddress address, final int port) {
    if (p1 != null && p1.address.equals(address) && p1.port == port)
      return p1;
    else if (p2 != null && p2.address.equals(address) && p2.port == port)
      return p2;
    else
      return null;
  }

  private Player handleLogin(
      final byte[] datagramData,
      final int dataLenght,
      final InetAddress datagramAddress,
      final int datagramPort) {

    final String tokens[] = Packet.tokenize(datagramData, dataLenght);
    final PacketTypesEnum packetType = Packet.lookupPacket(tokens);
    if (packetType != PacketTypesEnum.LOGIN) {
      System.out.println("[Room - handleLogin] Unexpected packet type: "
          + packetType.name());
      return null;
    }

    Packet00Login packet;
    try {
      packet = new Packet00Login().fromTokens(tokens);
    } catch (final PacketException e) {
      System.out.println("[Room - handleLogin] : " + e.getMessage());
      return null;
    }

    final Player player = playerQueue.poll();
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
      Packet201Error errorPacket = new Packet201Error(
          Packet201Error.ErrorTypesEnum.USERNAME_IN_USE);

      sendPacket(errorPacket, player);
      return null;
    }

    sendPacket(packet, player);
    return player;
  }

}
