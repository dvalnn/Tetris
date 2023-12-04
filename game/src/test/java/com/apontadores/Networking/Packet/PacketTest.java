package com.apontadores.Networking.Packet;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.apontadores.networking.packets.Packet00Login;

public class PacketTest {
  @Test
  public void testConstructor1() {
    String username = "user";
    String data = "00," + username;
    Packet00Login newP = new Packet00Login(data.getBytes());
    assertNotNull(newP);
    assertEquals(newP.getUsername(), username);
  }
}
