package networking.packets;

import static utils.Constants.GameConstants.BOARD_WIDTH;

import java.awt.Color;
import networking.GameClient;
import networking.GameServer;

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
