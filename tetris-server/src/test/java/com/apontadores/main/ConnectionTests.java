package com.apontadores.main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.apontadores.packets.Packet;
import com.apontadores.packets.Packet.PacketTypesEnum;
import com.apontadores.packets.Packet00Login;
import com.apontadores.packets.Packet02Redirect;
import com.apontadores.packets.PacketException;

public class ConnectionTests {
  private final static int PORT = 42069;
  private final static String ADDRESS = "127.0.0.1";
  private final static Server server = new Server();

  static {
    new Thread(server).start();
  }

  public void testPlayerConnection(
      String playerName,
      String roomName)
      throws IOException {

    DatagramSocket mockClient = new DatagramSocket();
    mockClient.setSoTimeout(10000);

    System.out.println("[TEST] mock client on port: "
        + mockClient.getLocalPort());

    Packet00Login loginPacket = new Packet00Login(playerName, roomName);
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
    assertNotEquals(0, inPacket.getLength(),
        "[TEST] Received packet should not be empty");

    Packet02Redirect redirectPacket;
    String tokens[] = Packet.tokenize(inPacket.getData(), inPacket.getLength());
    PacketTypesEnum packetType = Packet.lookupPacket(tokens);

    assertEquals(PacketTypesEnum.REDIRECT, packetType,
        "[TEST] Received packet should be a redirect packet");

    try {
      redirectPacket = new Packet02Redirect().fromTokens(tokens);
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
