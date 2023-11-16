package com.psw.tetris.Networking.Packet;

import com.psw.tetris.networking.GameClient;
import com.psw.tetris.networking.GameServer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.psw.tetris.networking.packets.Packet00Login;
import com.psw.tetris.networking.packets.Packet;

public class PacketTest {
  @Test
  public void testConstructor1() {
    String username = "tiago";
    int id = 0;
    String data = "00," + username;
    Packet00Login newP = new Packet00Login(data.getBytes());
    assertNotNull(newP);
    assertEquals(newP.getUsername(),username);
    assertEquals(newP.getId(),id);
  }
}
