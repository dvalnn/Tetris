package networking;

import java.awt.Color;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import main.Game;
import networking.packets.Packet;
import networking.packets.Packet.PacketTypes;
import networking.packets.Packet00Login;
import networking.packets.Packet01Disconnect;
import networking.packets.Packet03Update;

public class GameClient extends Thread {

  private InetAddress serverAddress;
  private int serverPort;
  private DatagramSocket socket;
  private Game game;
  private String username;

  public GameClient(Game game, String ipAddress, String username) {
    System.out.println("[Client] Hello!");

    this.serverPort = 1331;
    this.game = game;
    this.username = username;

    try {
      this.serverAddress = InetAddress.getByName(ipAddress);
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
    try {
      this.socket = new DatagramSocket();
    } catch (Exception e) {
      e.printStackTrace();
    }
    game.setClientActive(true);
  }

  public void parsePacket(byte[] data, InetAddress address, int port) {
    // System.out.println("[Client] Received: " + new String(data));
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

      case UPDATE:
        packet = new Packet03Update(data);
        handleUpdate((Packet03Update) packet);
        break;
    }
  }

  private void handleUpdate(Packet03Update packet) {
    if (packet.getUsername().equals(this.username)) {
      return;
    }

    int x = packet.getX();
    int y = packet.getY();
    Color color = packet.getColor();

    game.getPlayingMP().getOpponentBoard().updateBoard(x, y, color);
  }

  private void handleDisconnect(Packet01Disconnect packet) {
    System.out.println("[Client] " + packet.getUsername() + " has disconnected!");
    game.removePlayer(packet.getUsername());
  }

  private void handleLogin(Packet00Login packet, InetAddress address, int port) {
    System.out.println(
        "[Client] Connected to " + packet.getUsername() + " at " + address.toString() + ":" + port);
    game.addPlayer(packet.getUsername(), address, port);
  }

  public void sendData(byte[] data) {
    DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, serverPort);
    try {
      socket.send(packet);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void sendUpdate(int row, int col, Color color) {
    Packet packet = new Packet03Update(username, row, col, color);
    packet.writeData(this);
  }

  public void terminateConnection() {
    Packet01Disconnect packet = new Packet01Disconnect(username);
    packet.writeData(this);
  }

  @Override
  public void run() {
    System.out.println("[Client] Running!");
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
}
