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

public class GameServer extends Thread {
  private final int serverPort = 1331;
  private DatagramSocket socket;
  private Game game;

  private String hostname;

  private InetAddress clientAddress;
  private int clientPort;

  public GameServer(Game game, String hostName) {
    System.out.println("[Server] Hello!");

    this.game = game;
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
      // System.out.println("[Server] Sending: " + new String(data));
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

      case UPDATE:
        packet = new Packet03Update(data);
        handleUpdate((Packet03Update) packet);
        break;
    }
  }

  private void handleUpdate(Packet03Update packet) {
    if (packet.getUsername().equals(this.hostname)) {
      return;
    }

    int x = packet.getX();
    int y = packet.getY();
    Color color = packet.getColor();

    // System.out.println(
    //     "[Server] Received: " + hostname + " " + x + " " + y + " " + color.toString());

    game.getPlayingMP().getOpponentBoard().updateBoard(x, y, color);
  }

  private void handleDisconnect(Packet01Disconnect packet) {
    System.out.println("[Server] " + packet.getUsername() + " has disconnected!");
    game.removePlayer(packet.getUsername());
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

  public void sendUpdate(int row, int col, Color color) {
    Packet packet = new Packet03Update(hostname, row, col, color);
    packet.writeData(this);
  }

  private void addConnection(Packet00Login packet, InetAddress address, int port) {
    game.addPlayer(packet.getUsername(), address, port);
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
