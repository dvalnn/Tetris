package networking.packets;

import java.awt.Color;
import networking.GameClient;
import networking.GameServer;

public class Packet03Update extends Packet {

  private String username;
  private int x, y;
  private Color color;
  private String[] data;

  public Packet03Update(byte[] data) {
    super(03);
    this.data = readData(data).split(",");

    // this.packetId = Integer.parseInt(this.data[0]);
    this.username = this.data[1];
    this.x = Integer.parseInt(this.data[2]);
    this.y = Integer.parseInt(this.data[3]);
    int r = Integer.parseInt(this.data[4]);
    int g = Integer.parseInt(this.data[5]);
    int b = Integer.parseInt(this.data[6]);

    this.color = new Color(r, g, b);
  }

  public Packet03Update(String username, int x, int y, Color color) {
    super(03);
    this.username = new String(username);
    this.x = x;
    this.y = y;
    this.color = new Color(color.getRGB());
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
    int r = color.getRed();
    int g = color.getGreen();
    int b = color.getBlue();
    return ("03"
            + ","
            + this.username
            + ","
            + this.x
            + ","
            + this.y
            + ","
            + r
            + ","
            + g
            + ","
            + b
            + ",")
        .getBytes();
  }

  public String getUsername() {
    return username;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public Color getColor() {
    return color;
  }
}
