package com.apontadores.entities;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

import com.apontadores.packets.Packet00Redirect;
import com.apontadores.packets.Packet01Login;

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
  public String name;

  public final int id;

  private boolean full;
  private Player p1;
  private Player p2;

  private ArrayBlockingQueue<Player> playerQueue;
  private ArrayBlockingQueue<DatagramPacket> outPacketQueue;

  private ArrayBlockingQueue<DatagramPacket> inPacketQueue;
  private RoomStatesEnum state = RoomStatesEnum.WAITING_P1;
  private DatagramSocket roomSocket;

  private DatagramSocket outSocket;
  private final int MAX_PACKET_SIZE = 1024;

  private final int QUEUE_SIZE = 100;
  private Service playerRedirect;
  private Service playerSync;

  private Service packetHandler;

  private boolean exit = false;

  public Room(
      final int id,
      final String roomName) throws SocketException {

    this.id = id;
    this.name = roomName;

    System.out.println("[Room] Created room: " + name + " with id: " + id);

    roomSocket = new DatagramSocket();
    outSocket = new DatagramSocket();

    playerQueue = new ArrayBlockingQueue<Player>(QUEUE_SIZE);
    outPacketQueue = new ArrayBlockingQueue<DatagramPacket>(QUEUE_SIZE);
    inPacketQueue = new ArrayBlockingQueue<DatagramPacket>(QUEUE_SIZE);

    playerRedirect = new Service(
        new TimerTask() {
          @Override
          public void run() {
            if (state == RoomStatesEnum.STARTING) {
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
            byte data[] = new Packet00Redirect(roomSocket.getLocalPort()).getBytes();
            DatagramPacket packet = new DatagramPacket(
                data,
                data.length,
                p.address,
                p.port);

            try {
              System.out.println("[Room] Sending redirect packet to player " + p.username);
              System.out.println("[Room] Packet address : " + packet.getAddress());
              System.out.println("[Room] Packet port : " + packet.getPort());
              outSocket.send(packet);
              System.out.println("[Room] Sent redirect packet to player " + p.username);
            } catch (Exception e) {
              System.out.println("[Room] Failed to send redirect packet");
              // System.out.println("[Room] Terminating thread");
              // this.cancel();
              // exit = true;
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
            if (outPacket == null || outPacket.getLength() == 0) {
              return;
            }

            try {
              outSocket.send(outPacket);
            } catch (Exception e) {
              System.out.println("[Room] Failed to send packet");
              System.out.println("[Room] Terminating thread");
              this.cancel();
              exit = true;
              return;
            }

          }
        }, 0, 10);

    packetHandler = new Service(
        new TimerTask() {
          @Override
          public void run() {
            DatagramPacket inPacket = inPacketQueue.poll();
            if (inPacket == null)
              return;

            parsePacket(
                inPacket.getData(),
                inPacket.getLength(),
                inPacket.getAddress(),
                inPacket.getPort());
          }
        }, 0, 5);

    packetHandler.start();
    playerRedirect.start();
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
      if (exit || state == RoomStatesEnum.FINISHED) {
        return;
      }

      try {
        DatagramPacket packet = new DatagramPacket(
            new byte[MAX_PACKET_SIZE],
            MAX_PACKET_SIZE);
        roomSocket.receive(packet);
        System.out.println("[Room] Received packet");
        inPacketQueue.add(packet);
      } catch (final Exception e) {
        System.out.println("[Room] Failed to receive packet");
        System.out.println("[Room] Terminating thread");
        return;
      }

    }
  }

  public void parsePacket(
      byte[] data,
      int dataLenght,
      InetAddress address,
      int port) {

    switch (state) {
      case WAITING_P1:
      case WAITING_P2:
        Packet01Login packet = Packet01Login.fromBytes(data, dataLenght);
        if (packet == null) {
          return;
        }

        Player p = playerQueue.poll();
        if (p == null) {
          return;
        }

        System.out.println("[Room-parsePacket] Processing login packet");

        if (!packet.getUsername().equals(p.username)) {
          System.out.println("[Room] User mismatch");
          return;
        }

        if (!address.equals(p.address)) {
          System.out.println("[Room] Address mismatch");
          return;
        }

        if (p1 == null) {
          p1 = p;
          System.out.println("[Room] Player 1 joined room " + name);
          state = RoomStatesEnum.WAITING_P2;
        } else {
          p2 = p;
          state = RoomStatesEnum.STARTING;
        }

        break;

      case STARTING:
        playerRedirect.stop();
        playerSync.start();
        System.out.println("[Room] Not implemented");
        System.out.println("[Room] Terminating thread");
        exit = true;
        break;

      default:
        System.out.println("[Room] Invalid state");
        System.out.println("[Room] Terminating thread");
        exit = true;
        break;
    }
  }

  public String getName() {
    return name;
  }

}
