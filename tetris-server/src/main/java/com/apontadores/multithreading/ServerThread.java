package com.apontadores.multithreading;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.apontadores.packets.Packet00Login;

public class ServerThread implements Runnable {
  private Room room;
  private final DatagramSocket socket;
  private final DatagramPacket packet;
  private final byte[] dataBuffer;

  public ServerThread(final Room room) throws SocketException {
    this.room = room;
    this.dataBuffer = new byte[1024];
    this.packet = new DatagramPacket(dataBuffer, dataBuffer.length);
    this.socket = new DatagramSocket();
  }

  @Override
  public void run() {

    System.out.println("[ServerThread] Running");
    System.out.println("[ServerThread] Room name: " + room.name);
    System.out.println("[ServerThread] Room id: " + room.id);

    while (true) {
      try {
        System.out.println("[ServerThread] Listening on port " + socket.getLocalPort());
        socket.receive(packet);
      } catch (final Exception e) {
        e.printStackTrace();
      }

      parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
    }
  }

  private void parsePacket(byte[] data, InetAddress address, int port) {
    Packet00Login packet = Packet00Login.fromDatagram(data);
    if (packet == null) {
      System.out.println("[ServerThread] Invalid login packet");
      return;
    }

    System.out.println("[ServerThread] Loged in to  " + room.name);
    System.out.println("[ServerThread] Login for " + packet.getUsername());
    System.out.println("[ServerThread] Address: " + address.toString());
    System.out.println("[ServerThread] Port: " + port);

  }

}
