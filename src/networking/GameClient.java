package networking;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import main.Game;

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
    }
    try {
      this.socket = new DatagramSocket();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void parsePacket(byte[] data) {
    System.out.println("[Client] Parsing packet!");
    String message = new String(data).trim();
    System.out.println("[Client] Received: " + message);
  }

  public void sendData(byte[] data) {
    System.out.println("[Client] Sending data!");
    DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, serverPort);
    try {
      socket.send(packet);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    System.out.println("[Client] Running!");
    while (true) {
      try {
        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        socket.receive(packet);
        parsePacket(packet.getData());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
