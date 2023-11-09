package com.psw.tetris.networking.packets;

import com.psw.tetris.networking.GameClient;
import com.psw.tetris.networking.GameServer;
import java.awt.Color;
import java.awt.geom.Point2D;

public class Packet04Shape extends Packet {
  private final int POINTS_LEN = 4;

  private String username;
  Point2D[] points;
  Color color;
  private String[] data;

  public Packet04Shape(byte[] data) {
    super(04);
    this.data = readData(data).split(",");

    // this.packetId = Integer.parseInt(this.data[0]);
    this.username = this.data[1];
    points = new Point2D[POINTS_LEN];
    for (int i = 0; i < POINTS_LEN; i++) {
      this.points[i] =
          new Point2D.Double(
              Double.parseDouble(this.data[2 + i * 2]), Double.parseDouble(this.data[3 + i * 2]));
    }

    int r = Integer.parseInt(this.data[10]);
    int g = Integer.parseInt(this.data[11]);
    int b = Integer.parseInt(this.data[12]);

    this.color = new Color(r, g, b);
  }

  public Packet04Shape(String username, Point2D[] points, Color color) {
    super(04);

    this.username = new String(username);
    this.points = new Point2D[POINTS_LEN];
    for (int i = 0; i < POINTS_LEN; i++) {
      this.points[i] = new Point2D.Double(points[i].getX(), points[i].getY());
    }
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
    String retMsg = "04" + "," + this.username;

    for (int i = 0; i < POINTS_LEN; i++) {
      retMsg += "," + this.points[i].getX() + "," + this.points[i].getY();
    }
    retMsg += "," + r + "," + g + "," + b + ",";

    return retMsg.getBytes();
  }

  public String getUsername() {
    return username;
  }

  public Point2D[] getPoints() {
    return points;
  }

  public Color getColor() {
    return color;
  }
}
