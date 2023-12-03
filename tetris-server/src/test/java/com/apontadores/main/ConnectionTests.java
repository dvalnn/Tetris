package com.apontadores.main;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.apontadores.packets.Packet00Redirect;
import com.apontadores.packets.Packet01Login;

public class ConnectionTests {
  private final static int defaultPort = 42069;

  @Test
  public void testServerCreation() {
    Server s;
    try {
      s = new Server(ConnectionTests.defaultPort);
    } catch (Exception e) {
      e.printStackTrace();
      Assertions.fail("Server should not throw an exception");
      s = null;
    }

    if (s != null) {
      s.close();
    }

  }

  @Test
  public void testConnection() throws Exception {
    Server s = new Server(ConnectionTests.defaultPort);

    assertNotNull(s);
    new Thread(s).start();

    DatagramSocket mockClient = new DatagramSocket();
    mockClient.setSoTimeout(10000);

    System.out.println("[MockClient] client on port: " + mockClient.getLocalPort());

    Packet01Login loginPacket = new Packet01Login("test", "test");
    String serverAddress = "127.0.0.1";
    InetAddress serverInetAddress = InetAddress.getByName(serverAddress);
    DatagramPacket outPacket = new DatagramPacket(
        loginPacket.getBytes(),
        loginPacket.getBytes().length,
        serverInetAddress,
        ConnectionTests.defaultPort);

    mockClient.send(outPacket);

    DatagramPacket inPacket = new DatagramPacket(new byte[1024], 1024);
    mockClient.receive(inPacket);
    assertNotEquals(0, inPacket.getLength(), "Received packet should not be empty");

    Packet00Redirect redirectPacket = Packet00Redirect.fromBytes(inPacket.getData(), inPacket.getLength());
    assertNotNull(redirectPacket);
    System.out.println("[ConnectionTests] Received redirect port: " + redirectPacket.getPort());

    DatagramPacket outPacket2 = new DatagramPacket(
        loginPacket.getBytes(),
        loginPacket.getBytes().length,
        serverInetAddress,
        redirectPacket.getPort());
    assertNotNull(outPacket2);

    mockClient.send(outPacket2);
    mockClient.close();
  }
}
