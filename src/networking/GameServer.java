package networking;

import static utils.Constants.GameConstants.*;

import gameElements.BoardMP;
import java.awt.Color;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import main.Game;
import networking.packets.Packet;
import networking.packets.Packet.PacketTypes;
import networking.packets.Packet00Login;

public class GameServer extends Thread {

  private final int serverPort = 1331;
  private DatagramSocket socket;
  private Game game;

  private List<BoardMP> connectedPlayers = new ArrayList<BoardMP>();

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
    DatagramPacket packet =
        new DatagramPacket(data, data.length, address, port);
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

  public void sendDataToAllClients(byte[] data) {
    for (BoardMP player : connectedPlayers) {
      sendData(data, player.ipAddress, player.port);
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
      System.out.println("[Server] " + address.toString() + ":" + port + " " +
                         ((Packet00Login)packet).getUsername() +
                         " has connected...");
      addConnection((Packet00Login)packet, address, port);
      break;
    case DISCONNECT:
      break;
    case MOVE:
      break;
    }
  }

  private void addConnection(Packet00Login packet, InetAddress address,
                             int port) {
    boolean alreadyConnected = false;
    for (BoardMP player : connectedPlayers) {
      if (packet.getUsername().equalsIgnoreCase(player.getUsername())) {
        if (player.ipAddress == null) {
          player.ipAddress = address;
        }
        if (player.port == -1) {
          player.port = port;
        }
        alreadyConnected = true;
      } else {
        sendData(packet.getData(), player.ipAddress, player.port);
        packet = new Packet00Login(player.getUsername());
        sendData(packet.getData(), address, port);
      }
    }
    if (!alreadyConnected) {
      connectedPlayers.add(new BoardMP(BOARD_SQUARE, 0, 0, Color.black, address,
                                       port, packet.getUsername()));
    }
  }
}
