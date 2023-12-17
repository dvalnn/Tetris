package com.apontadores.main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
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

  private final int port = PUBLIC_PORT;
  public boolean calledFromMain = false;
  private boolean exitWhenEmpty = false;

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

          if (roomsToRemove.size() == 0) {
            return;
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

  Server() {
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
      socket.setSoTimeout(1000);
    } catch (SocketException e) {
      System.out.println("[Server] Failed to open socket");
      return;
    }

    DatagramPacket loginDatagram = new DatagramPacket(recvBuf, MAX_PACKET_SIZE);
    roomCleaner.start();

    System.out.println("[Server] Running from main: " + calledFromMain);
    System.out.println("[Server] Listening on port " + port);

    while (true) {
      if (forceExit) {
        System.out.println("[Server] Terminating thread");
        socket.close();
        return;
      }

      if (exitWhenEmpty && rooms.size() == 0) {
        System.out.println("[Server] No rooms left - exiting");
        socket.close();
        return;
      }

      try {
        socket.receive(loginDatagram);
      } catch (SocketTimeoutException et) {
        continue; // NOTE: timeouts are expected
      } catch (Exception e) {
        forceExit = true;
        continue;
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
        exitWhenEmpty = true;
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
          Packet201Error.ErrorTypesEnum.ROOM_FULL);

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

    if (exitWhenEmpty) {
      System.out.println("[Server] Blocking further room creation");
      Packet201Error errorPacket = new Packet201Error(
          Packet201Error.ErrorTypesEnum.ERROR);

      socket.send(new DatagramPacket(
          errorPacket.asBytes(),
          errorPacket.asBytes().length,
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

    if (!calledFromMain) {
      exitWhenEmpty = true;
    }

    new Thread(room).start();
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
