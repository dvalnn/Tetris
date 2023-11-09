package com.psw.tetris.networking.packets;

import com.psw.tetris.networking.GameClient;
import com.psw.tetris.networking.GameServer;

public class Packet00Login extends Packet {

  private String username;

  public Packet00Login(byte[] data) {
    super(00);
    this.username = readData(data).split(",")[1];
  }

  public Packet00Login(String username) {
    super(00);
    this.username = username;
  }

  @Override
  public void writeData(GameClient client) {
    client.sendData(getData());
  }

  @Override
  public void writeData(GameServer server) {
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
