package com.apontadores.main;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.TimerTask;

import com.apontadores.packets.Packet;
import com.apontadores.packets.Packet00Login;
import com.apontadores.packets.Packet.PacketException;
import com.apontadores.packets.Packet.PacketTypesEnum;

public class Server implements Runnable {

  public static final int MAX_PACKET_SIZE = 1024;
  public static final int PUBLIC_PORT = 42069;

  private final int port;

  private ArrayList<Room> rooms;
  private ArrayList<Room> roomsToRemove;

  private String lastRoomName = "";
  private String lastUsername = "";

  private TimerBasedService roomCleaner = new TimerBasedService(
      new TimerTask() {
        @Override
        public void run() {
          for (final Room r : rooms) {
            if (r.isFinished()) {
              roomsToRemove.add(r);
            }
          }
          for (final Room r : roomsToRemove)
            rooms.remove(r);

          roomsToRemove.clear();
          if (forceExit) {
            System.out.println("[Server] Terminating room cleaner");
            this.cancel();
          }
        }
      }, 1000, 1000);

  private boolean forceExit = false;

  Server(final int port) {
    this.port = port;
    rooms = new ArrayList<>(100);
    roomsToRemove = new ArrayList<>(100);
  }

  public void close() {
    System.out.println("[Server] Closing server");
    System.out.flush();
    roomCleaner.stop();
    forceExit = true;
  }

  @Override
  public void run() {
    // configurantion for the connection listener loop
    byte recvBuf[] = new byte[MAX_PACKET_SIZE];
    DatagramSocket connectionSocket;
    try {
      connectionSocket = new DatagramSocket(port);
    } catch (SocketException e) {
      System.out.println("[Server] Failed to open socket");
      return;
    }
    DatagramPacket loginDatagram = new DatagramPacket(recvBuf, MAX_PACKET_SIZE);
    roomCleaner.start();

    while (true) {
      if (forceExit) {
        System.out.println("[Server] Terminating thread");
        return;
      }

      try {
        System.out.println("[Server] Listening on port " + port);
        System.out.flush();
        connectionSocket.receive(loginDatagram);
      } catch (Exception e) {
        connectionSocket.close();
        System.out.println("[Server] Connection closed");
        exitIfNoThreads();
      }

      String tokens[] = Packet.tokenize(
          loginDatagram.getData(),
          loginDatagram.getLength());

      PacketTypesEnum packetType = Packet.lookupPacket(tokens);

      if (packetType != PacketTypesEnum.LOGIN) {
        System.out.println("[Server] Unexpected packet type: "
            + packetType.name());
        continue;
      }

      Packet00Login loginPacket;
      try {
        loginPacket = new Packet00Login().fromTokens(tokens);
      } catch (PacketException e) {
        // TODO: log exception
        System.out.println("[Server] Invalid Login packet");
        continue;
      }

      if (loginPacket.getRoomName().equals(lastRoomName)
          && loginPacket.getUsername().equals(lastUsername)) {
        // System.out.println("[Server] Duplicate login packet");
        continue;
      }

      lastUsername = loginPacket.getUsername();
      lastRoomName = loginPacket.getRoomName();

      loginPacketHandler(loginPacket, loginDatagram);
    }
  }

  private void loginPacketHandler(Packet00Login loginPacket, DatagramPacket loginDatagram) {
    Room room = findRoom(loginPacket.getRoomName());
    if (room != null && room.isFull()) {
      System.out.println("[Server] Room is full");
      // TODO: send a message to the client
      return;
    }

    if (room != null) {
      room.addPlayerToQueue(new Player(
          loginPacket.getUsername(),
          loginDatagram.getAddress(),
          loginDatagram.getPort()));
      return;
    }

    try {
      room = new Room(rooms.size(), loginPacket.getRoomName());
    } catch (SocketException e) {
      System.out.println("[Server] Failed to create room");
      return;
    }

    room.addPlayerToQueue(new Player(
        loginPacket.getUsername(),
        loginDatagram.getAddress(),
        loginDatagram.getPort()));

    rooms.add(room);
    new Thread(room).start();

    lastRoomName = "";
    lastUsername = "";
  }

  private void exitIfNoThreads() {
    if (rooms.size() == 0) {
      System.out.println("[Server] No rooms left");
      System.exit(0);
    }
  }

  private Room findRoom(final String roomName) {
    if (rooms.size() == 0) {
      return null;
    }

    for (final Room r : rooms) {
      if (r.getName().equals(roomName)) {
        return r;
      }
    }

    return null;
  }

}
