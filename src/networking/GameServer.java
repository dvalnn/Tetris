package networking;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import main.Game;
import networking.packets.Packet;
import networking.packets.Packet.PacketTypes;
import networking.packets.Packet00Login;

public class GameServer extends Thread {
  private final int serverPort = 1331;
  private DatagramSocket socket;
  private Game game;

  private String hostName;

  private InetAddress clientAddress;
  private int clientPort;

  public GameServer(Game game, String hostName) {
    System.out.println("[Server] Hello!");

    this.game = game;
    this.hostName = hostName;

    try {
      this.socket = new DatagramSocket(serverPort);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void sendData(byte[] data) {
    System.out.println("[Server] Sending data!");
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
    System.out.println("[Server] Parsing packet!");
    String message = new String(data).trim();
    System.out.println("[Server] Received: " + message);

    PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
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
        break;

      case MOVE:
        break;
    }
  }

  private void handleLogin(Packet00Login packet, InetAddress address, int port) {
    if (clientAddress == null) {
      addConnection(packet, address, port);
      Packet00Login reply = new Packet00Login(hostName);
      reply.writeData(this);
    } else {
      // TODO: send "server full" packet
    }
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
}
