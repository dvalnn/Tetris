package com.apontadores.main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.TimerTask;

import com.apontadores.packets.Packet;
import com.apontadores.packets.Packet.PacketTypesEnum;
import com.apontadores.packets.Packet00Login;
import com.apontadores.packets.Packet201Error;
import com.apontadores.packets.PacketException;

public class Server implements Runnable {

  public static final int MAX_PACKET_SIZE = 1024;
  public static final int PUBLIC_PORT = 42069;

  private final int port;

  private ArrayList<Room> rooms;
  private ArrayList<Room> roomsToRemove;

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

  private DatagramSocket socket;

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
    try {
      socket = new DatagramSocket(port);
    } catch (SocketException e) {
      System.out.println("[Server] Failed to open socket");
      return;
    }

    DatagramPacket loginDatagram = new DatagramPacket(recvBuf, MAX_PACKET_SIZE);
    roomCleaner.start();

    System.out.println("[Server] Listening on port " + port);

    while (true) {
      if (forceExit) {
        System.out.println("[Server] Terminating thread");
        return;
      }

      try {
        socket.receive(loginDatagram);
      } catch (Exception e) {
        socket.close();
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

      try {
        loginPacketHandler(loginPacket, loginDatagram);
      } catch (IOException e) {
        System.out.println("[Server] Failed to send error packet");
        exitIfNoThreads();
      }
    }
  }

  private void loginPacketHandler(
      Packet00Login loginPacket,
      DatagramPacket loginDatagram)
      throws IOException {
    Room room = findRoom(loginPacket.getRoomName());
    if (room != null && room.isFull()) {
      System.out.println("[Server] Room \"" + room.name + "\" is full");
      Packet201Error errorPacket = new Packet201Error(
          Packet201Error.ErrorType.ROOM_FULL);

      socket.send(new DatagramPacket(
          errorPacket.asBytes(),
          errorPacket.asBytes().length,
          loginDatagram.getAddress(),
          loginDatagram.getPort()));

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
  }

  private void exitIfNoThreads() {
    if (rooms.size() == 0) {
      System.out.println("[Server] No rooms left - exiting");
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
