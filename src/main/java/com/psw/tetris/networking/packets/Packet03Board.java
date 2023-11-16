package com.psw.tetris.networking.packets;

import static com.psw.tetris.utils.Constants.GameConstants.BOARD_WIDTH;

import com.psw.tetris.networking.GameClient;
import com.psw.tetris.networking.GameServer;
import java.awt.Color;

public class Packet03Board extends Packet {

  private String username;
  private int row;
  private Color[] lineColors;
  private String[] data;

  public Packet03Board(byte[] data) {
    super(03);
    this.data = readData(data).split(",");

    this.username = this.data[1];
    this.row = Integer.parseInt(this.data[2]);
    this.lineColors = new Color[BOARD_WIDTH];
    for (int i = 0; i < BOARD_WIDTH; i++) {
      this.lineColors[i] = new Color(Integer.parseInt(this.data[3 + i]));
    }
  }

  public Packet03Board(String username, int row, Color[] lineColors) {
    super(03);
    this.username = new String(username);
    this.row = row;
    this.lineColors = lineColors;
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
    String retStr = "03" + "," + username + "," + row;
    for (Color color : lineColors) {
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
