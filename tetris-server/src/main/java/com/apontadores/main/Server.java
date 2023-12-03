package com.apontadores.main;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import com.apontadores.multithreading.Room;
import com.apontadores.multithreading.ServerThread;
import com.apontadores.packets.Packet00Login;

public class Server {

  public class roomThreadPair {
    public Room room;
    public Thread thread;

    public roomThreadPair(Room room, Thread thread) {
      this.room = room;
      this.thread = thread;
    }
  }

  private final int port;
  private final int maxPacketSize = 1024;

  private ArrayList<roomThreadPair> rooms;

  Server(final int port) throws Exception {
    this.port = port;
    rooms = new ArrayList<>();
    start();
  }

  public void start() throws SocketException {
    // configurantion for the connection listener loop
    byte recvBuf[] = new byte[maxPacketSize];
    DatagramSocket connectionSocket = new DatagramSocket(port);
    DatagramPacket loginPacket = new DatagramPacket(recvBuf, maxPacketSize);

    while (true) {
      try {
        System.out.println("[Server] Listening on port " + port);
        System.out.flush();
        connectionSocket.receive(loginPacket);
      } catch (Exception e) {
        connectionSocket.close();
        System.out.println("[Server] Connection closed");
        exitIfNoThreads();
      }

      Room newRoom = parsePacket(
          loginPacket.getData(),
          loginPacket.getAddress(),
          loginPacket.getPort());

      if (newRoom == null)
        continue;

      Thread newThread = null;
      try {
        newThread = new Thread(new ServerThread(newRoom));
      } catch (Exception e) {
        System.out.println("[Server] Failed to create new thread");
        continue;
      }

      roomThreadPair newPair = new roomThreadPair(newRoom, newThread);
      if (roomIsDuplicate(newRoom))
        continue;

      newPair.thread.start();
      rooms.add(newPair);
      deleteFinishedRooms();
    }
  }

  private boolean roomIsDuplicate(Room newRoom) {
    for (roomThreadPair pair : rooms) {
      if (pair.room.name.equals(newRoom.name)) {
        System.out.println("[Server] Room already exists");
        return true;
      }
    }

    return false;
  }

  private void deleteFinishedRooms() {
    ArrayList<roomThreadPair> toRemove = new ArrayList<>();
    for (roomThreadPair pair : rooms) {
      if (pair.room.isFinished() || !pair.thread.isAlive()) {
        toRemove.add(pair);
      }
    }
    for (roomThreadPair pair : toRemove) {
      rooms.remove(pair);
      System.out.println("[Server] Room " + pair.room.name + " removed");
    }
  }

  private void exitIfNoThreads() {
    if (rooms.size() == 0) {
      System.out.println("[Server] No rooms left");
      System.exit(0);
    }
  }

  private Room parsePacket(byte[] data, InetAddress address, int port) {
    Packet00Login packet = Packet00Login.fromDatagram(data);
    if (packet == null) {
      System.out.println("[Server] Invalid login packet");
      return null;
    }

    System.out.println("[Server] Login for " + packet.getUsername());
    System.out.println("[Server] Address: " + address.toString());
    System.out.println("[Server] Port: " + port);

    Room room = new Room(rooms.size(), address, port, packet.getUsername());
    return room;
  }

}
