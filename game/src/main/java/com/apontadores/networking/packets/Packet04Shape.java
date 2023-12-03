package com.apontadores.networking.packets;

import java.awt.Color;
import java.awt.geom.Point2D;

import com.apontadores.networking.GameClient;
import com.apontadores.networking.GameServer;

public class Packet04Shape extends Packet {
  private final int POINTS_LEN = 4;

  private final String username;
  Point2D[] points;
  Color color;
  private String[] data;

  public Packet04Shape(final byte[] data) {
    super(04);
    this.data = readData(data).split(",");

    // this.packetId = Integer.parseInt(this.data[0]);
    this.username = this.data[1];
    points = new Point2D[POINTS_LEN];
    for (int i = 0; i < POINTS_LEN; i++) {
      this.points[i] = new Point2D.Double(
          Double.parseDouble(this.data[2 + i * 2]), Double.parseDouble(this.data[3 + i * 2]));
    }

    final int r = Integer.parseInt(this.data[10]);
    final int g = Integer.parseInt(this.data[11]);
    final int b = Integer.parseInt(this.data[12]);

    this.color = new Color(r, g, b);
  }

  public Packet04Shape(final String username, final Point2D[] points, final Color color) {
    super(04);

    this.username = new String(username);
    this.points = new Point2D[POINTS_LEN];
    for (int i = 0; i < POINTS_LEN; i++) {
      this.points[i] = new Point2D.Double(points[i].getX(), points[i].getY());
    }
    this.color = new Color(color.getRGB());
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
    final int r = color.getRed();
    final int g = color.getGreen();
    final int b = color.getBlue();
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
