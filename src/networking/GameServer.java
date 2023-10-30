package networking;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import main.Game;

public class GameServer extends Thread {

  private final int serverPort = 1331;
  private DatagramSocket socket;
  private Game game;

  public GameServer(Game game) {
    System.out.println("[Server] Hello!");

    this.game = game;

    try {
      this.socket = new DatagramSocket(serverPort);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void echoPacket(DatagramPacket packet) {
    System.out.println("[Server] Parsing packet!");
    String message = new String(packet.getData()).trim();
    System.out.println("[Server] Received: " + message);
    if (message.equals("ping")) {
      sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
    }
  }

  public void sendData(byte[] data, InetAddress address, int port) {
    System.out.println("[Server] Sending data!");
    DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
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
      echoPacket(packet);
    }
  }
}
