package networking;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import main.Game;
import networking.packets.Packet;
import networking.packets.Packet.PacketTypes;
import networking.packets.Packet00Login;

public class GameClient extends Thread {

  private InetAddress serverAddress;
  private int serverPort;
  private DatagramSocket socket;
  private Game game;

  public GameClient(Game game, String ipAddress) {
    System.out.println("[Client] Hello!");

    this.serverPort = 1331;
    this.game = game;

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
    System.out.println("[Client] Parsing packet!");
    String message = new String(data).trim();
    PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
    Packet packet = null;
    switch (type) {
    default:
    case INVALID:
      break;
    case LOGIN:
      packet = new Packet00Login(data);
      handleLogin((Packet00Login)packet, address, port);
      break;
    case DISCONNECT:
      break;
    case MOVE:
      break;
    }
  }

  private void handleLogin(Packet00Login packet, InetAddress address,
                           int port) {
    System.out.println("[Client] " + address.toString() + ":" + port + " " +
                       packet.getUsername() + " has joined the game");
    game.addBoardMP(packet.getUsername(), address, port);
  }

  public void sendData(byte[] data) {
    System.out.println("[Client] Sending data!");
    DatagramPacket packet =
        new DatagramPacket(data, data.length, serverAddress, serverPort);
    try {
      socket.send(packet);
    } catch (Exception e) {
      e.printStackTrace();
    }
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
