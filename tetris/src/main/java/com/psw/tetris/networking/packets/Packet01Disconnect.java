package com.psw.tetris.networking.packets;

import com.psw.tetris.networking.GameClient;
import com.psw.tetris.networking.GameServer;

public class Packet01Disconnect extends Packet {

  private String username;

  public Packet01Disconnect(byte[] data) {
    super(01);
    this.username = readData(data).split(",")[1];
  }

  public Packet01Disconnect(String username) {
    super(01);
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
    return ("01" + "," + this.username).getBytes();
  }

  public String getUsername() {
    return username;
  }
}
