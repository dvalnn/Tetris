package com.apontadores.main;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.apontadores.packets.RedirectPacket;
import com.apontadores.packets.LoginPacket;
import com.apontadores.packets.PacketException;

public class ConnectionTests {
  private final static int PORT = 42069;
  private final static String ADDRESS = "127.0.0.1";
  private final static Server server = new Server(ConnectionTests.PORT);

  static {
    new Thread(server).start();
  }

  public void testPlayerConnection(
      String playerName,
      String roomName)
      throws IOException {

    DatagramSocket mockClient = new DatagramSocket();
    mockClient.setSoTimeout(10000);

    System.out.println("[TEST] mock client on port: " + mockClient.getLocalPort());

    LoginPacket loginPacket = new LoginPacket(playerName, roomName);
    InetAddress serverInetAddress = InetAddress.getByName(ConnectionTests.ADDRESS);

    DatagramPacket outPacket = new DatagramPacket(
        loginPacket.asBytes(),
        loginPacket.asBytes().length,
        serverInetAddress,
        ConnectionTests.PORT);

    mockClient.send(outPacket);

    DatagramPacket inPacket = new DatagramPacket(
        new byte[Server.MAX_PACKET_SIZE],
        Server.MAX_PACKET_SIZE);

    mockClient.receive(inPacket);
    assertNotEquals(0, inPacket.getLength(), "[TEST] Received packet should not be empty");

    RedirectPacket redirectPacket;
    try {
      redirectPacket = new RedirectPacket().fromBytes(
          inPacket.getData(),
          inPacket.getLength());
    } catch (PacketException e) {
      Assertions.fail(e.getMessage());
      mockClient.close();
      return;
    }

    System.out.println(
        "[TEST] Received redirect port: "
            + redirectPacket.getPort());

    DatagramPacket outPacket2 = new DatagramPacket(
        loginPacket.asBytes(),
        loginPacket.asBytes().length,
        serverInetAddress,
        redirectPacket.getPort());

    assertNotNull(outPacket2);
    mockClient.send(outPacket2);
    mockClient.close();
  }

  @Test
  public void testServer() {
    assertNotNull(server);

    String p1Name = "testP1";
    String p2Name = "testP2";
    String roomName = "testRoom";

    try {
      testPlayerConnection(p1Name, roomName);
    } catch (IOException e) {
      Assertions.fail(e.getMessage());
    }

    try {
      testPlayerConnection(p2Name, roomName);
    } catch (IOException e) {
      Assertions.fail(e.getMessage());
    }

    server.close();
  }

}
