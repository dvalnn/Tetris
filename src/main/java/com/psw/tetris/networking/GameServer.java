package com.psw.tetris.networking;

import com.psw.tetris.main.Game;
import com.psw.tetris.networking.packets.Packet;
import com.psw.tetris.networking.packets.Packet.PacketTypes;
import com.psw.tetris.networking.packets.Packet00Login;
import com.psw.tetris.networking.packets.Packet01Disconnect;
import com.psw.tetris.networking.packets.Packet03Board;
import com.psw.tetris.networking.packets.Packet04Shape;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class GameServer extends Thread {

  private final int serverPort = 1331;
  private DatagramSocket socket;
  private String hostname;
  private InetAddress clientAddress;
  private int clientPort;

  public GameServer(String hostName) {
    System.out.println("[Server] Hello!");

    this.hostname = hostName;

    try {
      this.socket = new DatagramSocket(serverPort);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void sendData(byte[] data) {
    DatagramPacket packet = new DatagramPacket(data, data.length, clientAddress, clientPort);
    try {
      socket.send(packet);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    System.out.println("[Server] Running!");
    byte[] data = new byte[1024];
    DatagramPacket packet = new DatagramPacket(data, data.length);
    while (true) {
      try {
        socket.receive(packet);
      } catch (Exception e) {
        e.printStackTrace();
      }
      parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
    }
  }

  private void parsePacket(byte[] data, InetAddress address, int port) {
    String[] message = new String(data).split(",");
    PacketTypes type = Packet.lookupPacket(message[0]);

    Packet packet = null;

    switch (type) {
      default:
      case INVALID:
        break;

      case LOGIN:
        packet = new Packet00Login(data);
        handleLogin((Packet00Login) packet, address, port);
        break;

      case DISCONNECT:
        packet = new Packet01Disconnect(data);
        handleDisconnect((Packet01Disconnect) packet);
        break;

      case BOARD:
        packet = new Packet03Board(data);
        handleUpdate((Packet03Board) packet);
        break;

      case SHAPE:
        packet = new Packet04Shape(data);
        handleShape((Packet04Shape) packet);
    }
  }

  private void handleShape(Packet04Shape packet) {
    if (packet.getUsername().equals(this.hostname)) {
      return;
    }

    Point2D[] points = packet.getPoints();
    Color color = packet.getColor();

    Game.updateShapeMP(points, color);
  }

  private void handleUpdate(Packet03Board packet) {
    if (packet.getUsername().equals(this.hostname)) {
      return;
    }

    int row = packet.getRow();
    Color[] lineColors = packet.getLineColors();

    Game.updateBoardMP(row, lineColors);
  }

  private void handleDisconnect(Packet01Disconnect packet) {
    System.out.println("[Server] " + packet.getUsername() + " has disconnected!");
    Game.removePlayer(packet.getUsername());
  }

  private void handleLogin(Packet00Login packet, InetAddress address, int port) {
    if (clientAddress == null) {
      addConnection(packet, address, port);
      Packet00Login reply = new Packet00Login(hostname);
      reply.writeData(this);
    } else {
      // TODO: send "server full" packet
    }
  }

  public void sendShapeUpdate(Point2D[] points, Color color) {
    Packet packet = new Packet04Shape(hostname, points, color);
    packet.writeData(this);
  }

  public void sendBoardUpdate(int row, Color[] lineColors) {
    Packet packet = new Packet03Board(hostname, row, lineColors);
    packet.writeData(this);
  }

  private void addConnection(Packet00Login packet, InetAddress address, int port) {
    Game.addPlayer(packet.getUsername(), address, port);
    clientAddress = address;
    clientPort = port;
    System.out.println(
        "[Server] "
            + address.toString()
            + ":"
            + port
            + " "
            + packet.getUsername()
            + " has connected...");
  }

  public void terminateConnection() {
    Packet01Disconnect disconnectPacket = new Packet01Disconnect(hostname);
    disconnectPacket.writeData(this);
  }
}
