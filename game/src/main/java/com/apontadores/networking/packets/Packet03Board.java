package com.apontadores.networking.packets;

import static com.apontadores.utils.Constants.GameConstants.BOARD_WIDTH;

import java.awt.Color;

import com.apontadores.networking.GameClient;
import com.apontadores.networking.GameServer;

public class Packet03Board extends Packet {

  private final String username;
  private final int row;
  private final Color[] lineColors;
  private String[] data;

  public Packet03Board(final byte[] data) {
    super(03);
    this.data = readData(data).split(",");

    this.username = this.data[1];
    this.row = Integer.parseInt(this.data[2]);
    this.lineColors = new Color[BOARD_WIDTH];
    for (int i = 0; i < BOARD_WIDTH; i++) {
      this.lineColors[i] = new Color(Integer.parseInt(this.data[3 + i]));
    }
  }

  public Packet03Board(final String username, final int row, final Color[] lineColors) {
    super(03);
    this.username = new String(username);
    this.row = row;
    this.lineColors = lineColors;
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
    String retStr = "03" + "," + username + "," + row;
    for (final Color color : lineColors) {
      retStr += "," + color.getRGB();
    }
    return (retStr + ",").getBytes();
  }

  public String getUsername() {
    return username;
  }

  public int getRow() {
    return row;
  }

  public Color[] getLineColors() {
    return lineColors;
  }
}
