package com.psw.tetris.Networking.Packet;

import com.psw.tetris.networking.GameClient;
import com.psw.tetris.networking.GameServer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.psw.tetris.networking.packets.Packet00Login;

public class PacketTest {
  @Test
  public void testConstructor1() {
    String username = "tiago";
    String data = "00," + username;
    Packet00Login newP = new Packet00Login(data.getBytes());
    assertNotNull(data);
  }
}
