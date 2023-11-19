package com.psw.tetris.networking.packets;

import com.psw.tetris.networking.GameClient;
import com.psw.tetris.networking.GameServer;

public class Packet00Login extends Packet {

  private final String username;

  public Packet00Login(final byte[] data) {
    super(00);
    this.username = readData(data).split(",")[1];
  }

  public Packet00Login(final String username) {
    super(00);
    this.username = username;
  }

  @Override
  public void writeData(final GameClient client) {
    client.sendData(getData());
  }

  @Override
  public void writeData(final GameServer server) {
    server.sendData(getData());
  }

  @Override
  public byte[] getData() {
    return ("00" + "," + this.username).getBytes();
  }

  public String getUsername() {
    return username;
  }
}
