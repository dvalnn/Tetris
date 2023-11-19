package com.psw.tetris.networking;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.psw.tetris.main.Game;
import com.psw.tetris.networking.packets.Packet;
import com.psw.tetris.networking.packets.Packet.PacketTypes;
import com.psw.tetris.networking.packets.Packet00Login;
import com.psw.tetris.networking.packets.Packet01Disconnect;
import com.psw.tetris.networking.packets.Packet03Board;
import com.psw.tetris.networking.packets.Packet04Shape;

public class GameClient extends Thread {

  private InetAddress serverAddress;
  private int serverPort;
  private DatagramSocket socket;
  private String username;

  public GameClient(final String ipAddress, final String username) {
    System.out.println("[Client] Hello!");

    this.serverPort = 1331;
    this.username = username;

    try {
      this.serverAddress = InetAddress.getByName(ipAddress);
    } catch (final Exception e) {
      e.printStackTrace();
      return;
    }
    try {
      this.socket = new DatagramSocket();
    } catch (final Exception e) {
      e.printStackTrace();
    }
    Game.setClientActive(true);
  }

  public void parsePacket(final byte[] data, final InetAddress address, final int port) {
    // System.out.println("[Client] Received: " + new String(data));
    final String[] message = new String(data).split(",");
    final PacketTypes type = Packet.lookupPacket(message[0]);

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
        handleBoard((Packet03Board) packet);
        break;

      case SHAPE:
        packet = new Packet04Shape(data);
        handleShape((Packet04Shape) packet);
    }
  }

  private void handleShape(final Packet04Shape packet) {
    if (packet.getUsername().equals(this.username)) {
      return;
    }

    final Point2D[] points = packet.getPoints();
    final Color color = packet.getColor();

    Game.updateShapeMP(points, color);
  }

  private void handleBoard(final Packet03Board packet) {
    if (packet.getUsername().equals(this.username)) {
      return;
    }

    final int row = packet.getRow();
    final Color[] lineColors = packet.getLineColors();

    Game.updateBoardMP(row, lineColors);
  }

  private void handleDisconnect(final Packet01Disconnect packet) {
    System.out.println("[Client] " + packet.getUsername() + " has disconnected!");
    Game.removePlayer(packet.getUsername());
  }

  private void handleLogin(final Packet00Login packet, final InetAddress address, final int port) {
    System.out.println(
        "[Client] Connected to " + packet.getUsername() + " at " + address.toString() + ":" + port);
    Game.addPlayer(packet.getUsername(), address, port);
  }

  public void sendData(final byte[] data) {
    final DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, serverPort);
    try {
      socket.send(packet);
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  public void sendShapeUpdate(final Point2D[] points, final Color color) {
    final Packet packet = new Packet04Shape(username, points, color);
    packet.writeData(this);
  }

  public void sendBoardUpdate(final int row, final Color[] color) {
    final Packet packet = new Packet03Board(username, row, color);
    packet.writeData(this);
  }

  public void terminateConnection() {
    final Packet01Disconnect packet = new Packet01Disconnect(username);
    packet.writeData(this);
  }

  @Override
  public void run() {
    System.out.println("[Client] Running!");
    final byte[] data = new byte[1024];
    final DatagramPacket packet = new DatagramPacket(data, data.length);
    while (true) {
      try {
        socket.receive(packet);
      } catch (final Exception e) {
        e.printStackTrace();
      }
      parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
    }
  }
}
