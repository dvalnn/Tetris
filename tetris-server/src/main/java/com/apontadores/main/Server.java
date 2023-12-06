package com.apontadores.main;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

import com.apontadores.packets.LoginPacket;
import com.apontadores.packets.PacketException;

public class Server implements Runnable {

  public static final int MAX_PACKET_SIZE = 1024;

  private final int port;

  private ArrayList<Room> rooms;

  private boolean forceExit = false;

  Server(final int port) {
    this.port = port;
    rooms = new ArrayList<>();
  }

  public void close() {
    System.out.println("[Server] Closing server");
    System.out.flush();
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

      LoginPacket loginPacket;
      try {
        loginPacket = new LoginPacket().fromBytes(
            loginDatagram.getData(),
            loginDatagram.getLength());
      } catch (PacketException e) {
        // TODO: log exception
        System.out.println("[Server] Invalid packet");
        continue;
      }

      Room room = findRoom(loginPacket.getRoomName());
      if (room != null && room.isFull()) {
        System.out.println("[Server] Room is full");
        // TODO: send a message to the client
        continue;
      }

      if (room != null) {
        room.addPlayerToQueue(new Player(
            loginPacket.getUsername(),
            loginDatagram.getAddress(),
            loginDatagram.getPort()));

        continue;
      }

      try {
        room = new Room(
            loginPacket.getRoomName().hashCode(),
            loginPacket.getRoomName());
      } catch (SocketException e) {
        System.out.println("[Server] Failed to create room");
        continue;
      }

      room.addPlayerToQueue(new Player(
          loginPacket.getUsername(),
          loginDatagram.getAddress(),
          loginDatagram.getPort()));

      rooms.add(room);

      new Thread(room).start();
    }
  }

  private void exitIfNoThreads() {
    if (rooms.size() == 0) {
      System.out.println("[Server] No rooms left");
      System.exit(0);
    }
  }

  public Room findRoom(final String roomName) {
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
