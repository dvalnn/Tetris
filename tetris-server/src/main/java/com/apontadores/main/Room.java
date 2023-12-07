package com.apontadores.main;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

import com.apontadores.packets.RedirectPacket;
import com.apontadores.packets.GameStartPacket;
import com.apontadores.packets.HeartbeatPacket;
import com.apontadores.packets.LoginPacket;
import com.apontadores.packets.Packet;
import com.apontadores.packets.PacketException;

public class Room implements Runnable {

  public enum RoomStatesEnum {
    WAITING_P1, // waiting for the owner to be redirected from the server
    WAITING_P2,
    STARTING,
    PLAYING,
    FINISHED;
  }

  private class Service {
    private Timer timer;
    private TimerTask task;
    private int delay;
    private int period;
    private boolean active = false;

    public Service(
        final TimerTask task,
        final int delay,
        final int period) {
      this.task = task;
      this.delay = delay;
      this.period = period;
      this.timer = new Timer();
    }

    public void start() {
      if (active) {
        return;
      }
      active = true;
      timer.schedule(task, delay, period);
    }

    public void stop() {
      if (!active) {
        return;
      }
      active = false;
      task.cancel();
    }

  }

  public static final int MAX_PLAYERS = 2;
  public static final int MAX_MISSES = 100;

  public final String name;
  public final int id;

  private boolean full;
  private Player p1;
  private Player p2;

  private int timeoutCount = 0;
  private static final int MAX_SOCKET_TIMEOUTS = 5;

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
  private Service playerRedirect;
  private Service playerSync;

  private Service packetHandler;

  public Room(
      final int id,
      final String roomName)
      throws SocketException {

    this.id = id;
    this.name = roomName;

    System.out.println("[Room] Created room: " + name + " with id: " + id);

    roomSocket = new DatagramSocket();
    roomSocket.setSoTimeout(1000);
    outSocket = new DatagramSocket();

    playerQueue = new ArrayBlockingQueue<Player>(QUEUE_SIZE);
    outPacketQueue = new ArrayBlockingQueue<DatagramPacket>(QUEUE_SIZE);
    inPacketQueue = new ArrayBlockingQueue<DatagramPacket>(QUEUE_SIZE);

    playerRedirect = new Service(
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
            System.out.println("[Room-playerRedirect] Sending redirect packet to player " + p.username);
            System.out.println("[Room-playerRedirect] Redirecting to port: " + roomSocket.getLocalPort());
            byte data[] = new RedirectPacket(roomSocket.getLocalPort()).asBytes();
            DatagramPacket packet = new DatagramPacket(
                data,
                data.length,
                p.address,
                p.port);

            try {
              System.out.println("[Room] Sending redirect packet to player " + p.username);
              System.out.println("[Room] Packet address : " + packet.getAddress());
              System.out.println("[Room] Packet port : " + packet.getPort());
              outPacketQueue.add(packet);
              System.out.println("[Room] Sent redirect packet to player " + p.username);
            } catch (Exception e) {
              System.out.println("[Room] Failed to send redirect packet");
              System.out.println("[Room] Terminating thread");
              this.cancel();
              state = RoomStatesEnum.FINISHED;
              return;
            }
          }
        }, 200, 1000);

    playerSync = new Service(
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

    packetHandler = new Service(
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

  public void sendPacket(Packet p, Player player) {
    outPacketQueue.add(new DatagramPacket(
        p.asBytes(),
        p.asBytes().length,
        player.address,
        player.port));
  }

  public void parsePacket(
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
          System.out.println("[Room] New state: " + state.name());
        }
        break;

      case WAITING_P2:
        if (datagramAddress.equals(p1.address) && datagramPort == p1.port) {
          HeartbeatPacket heartbeatPacket = checkForHeartbeatPacket(
              datagramData,
              dataLength,
              datagramAddress,
              datagramPort);

          if (heartbeatPacket != null) {
            sendPacket(heartbeatPacket, p1);
            p1.packetHit();
            p1.setReady(true);
            return;
          }

          LoginPacket packet = checkForLoginPacket(
              datagramData,
              dataLength,
              datagramAddress,
              datagramPort);

          if (packet != null) {
            System.out.println("[Room] Player 1 re-sent a login packet");
            System.out.println("[Room] Replying to " + p1.address + ":" + p1.port);
            sendPacket(packet, p1);
            p1.packetMiss();
            return;
          }

          p1.packetMiss();
          return;
        }

        p1.packetMiss();

        p2 = handleLogin(
            datagramData,
            dataLength,
            datagramAddress,
            datagramPort);

        if (p2 != null) {
          full = true;
          state = RoomStatesEnum.STARTING;
        }

        break;

      case STARTING:
        if (datagramAddress.equals(p1.address) && datagramPort == p1.port) {
          HeartbeatPacket heartbeatPacket = checkForHeartbeatPacket(
              datagramData,
              dataLength,
              datagramAddress,
              datagramPort);

          if (heartbeatPacket != null) {
            sendPacket(heartbeatPacket, p1);
            p1.packetHit();
          }
        }

        if (datagramAddress.equals(p2.address) && datagramPort == p2.port) {
          HeartbeatPacket heartbeatPacket = checkForHeartbeatPacket(
              datagramData,
              dataLength,
              datagramAddress,
              datagramPort);

          if (heartbeatPacket != null) {
            sendPacket(heartbeatPacket, p2);
            p2.packetHit();
            p2.setReady(true);
          }
        }

        if (p1.isReady() && p2.isReady()) {
          System.out.println("[Room] Both players ready");
          // send game start packet
          GameStartPacket packet1 = new GameStartPacket(p1.username);
          GameStartPacket packet2 = new GameStartPacket(p2.username);
          packet1.setTransactionID(1);
          packet2.setTransactionID(1);
          sendPacket(packet2, p1);
          sendPacket(packet1, p2);
          state = RoomStatesEnum.PLAYING;
          System.out.println("[Starting Game]");
        }
        break;

      case PLAYING:
        if (datagramAddress.equals(p1.address) && datagramPort == p1.port) {
          outPacketQueue.add(new DatagramPacket(
              datagramData,
              dataLength,
              p2.address,
              p2.port));

          p1.packetHit();
          p2.packetMiss();

        } else if (datagramAddress.equals(p2.address) && datagramPort == p2.port) {
          outPacketQueue.add(new DatagramPacket(
              datagramData,
              dataLength,
              p1.address,
              p1.port));

          p2.packetHit();
          p1.packetMiss();

        } else {
          System.out.println("[Room] Invalid packet received");
          p1.packetMiss();
          p2.packetMiss();
        }
        break;

      default:
        System.out.println("[Room] Invalid state");
        System.out.println("[Room] Terminating thread");
        state = RoomStatesEnum.FINISHED;
        break;
    }

  }

  public Player handleLogin(
      byte[] datagramData,
      int dataLenght,
      InetAddress datagramAddress,
      int datagramPort) {

    LoginPacket packet = checkForLoginPacket(
        datagramData,
        dataLenght,
        datagramAddress,
        datagramPort);

    if (packet == null) {
      return null;
    }

    Player player = playerQueue.poll();
    if (player == null) {
      return null;
    }

    System.out.println("[Room-parsePacket] Processing login packet for player "
        + player.username);

    if (!datagramAddress.equals(player.address) || datagramPort != player.port) {
      System.out.println("[Room] Address mismatch");
      return null;
    }

    if (p1 != null && packet.getUsername().equals(p1.username)) {
      System.out.println("[Room] Username already in use");
      // TODO: send error packet
      return null;
    }

    sendPacket(packet, player);
    return player;
  }

  public String getName() {
    return name;
  }

  public LoginPacket checkForLoginPacket(
      byte[] datagramData,
      int dataLenght,
      InetAddress datagramAddress,
      int datagramPort) {

    LoginPacket packet;
    try {
      packet = new LoginPacket().fromBytes(datagramData, dataLenght);
    } catch (PacketException e) {
      System.out.println("[Room] Invalid login packet: " + e.getMessage());
      return null;
    }

    return packet;
  }

  public HeartbeatPacket checkForHeartbeatPacket(
      byte[] datagramData,
      int dataLenght,
      InetAddress datagramAddress,
      int datagramPort) {

    HeartbeatPacket packet;
    try {
      packet = new HeartbeatPacket().fromBytes(datagramData, dataLenght);
    } catch (PacketException e) {
      System.out.println("[Room] Invalid heartbeat packet" + e.getMessage());
      return null;
    }

    return packet;
  }

}
