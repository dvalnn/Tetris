package gameElements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public class Shape {

  protected Point2D center;
  protected Point2D[] points;
  protected Color color;
  protected int size;
  protected int scale;

  public Shape(Point2D center, Point2D[] points, Color color, int size, int scale) {
    this.center = center;
    this.points = points;
    this.color = color;
    this.size = size;
    this.scale = scale;
  }

  public void move(int x, int y) {
    center.setLocation(center.getX() + x, center.getY() + y);
    for (Point2D point : points) {
      point.setLocation(point.getX() + x, point.getY() + y);
    }
  }

  public void rotate(double angle) {
    for (Point2D point : points) {
      double x = point.getX() - center.getX();
      double y = point.getY() - center.getY();
      double newX = x * Math.cos(angle) - y * Math.sin(angle);
      double newY = x * Math.sin(angle) + y * Math.cos(angle);
      point.setLocation(newX + center.getX(), newY + center.getY());
    }
  }

  public void render(Graphics g) {
    for (Point2D point : points) {
      g.setColor(color);
      g.fillRect(
          (int) (point.getX() - size / 2) * scale,
          (int) (point.getY() - size / 2) * scale,
          size,
          size);
      g.setColor(color.brighter());
      g.drawRect(
          (int) (point.getX() - size / 2) * scale,
          (int) (point.getY() - size / 2) * scale,
          size,
          size);
    }

  }

}
