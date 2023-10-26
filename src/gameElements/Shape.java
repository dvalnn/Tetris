package gameElements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public abstract class Shape {

  protected Point2D center;
  protected Point2D[] body;
  protected Color color;
  protected int size;

  protected int minX, maxX, minY, maxY;

  private Point2D renderOffset;

  public Shape(Point2D center, Point2D[] points, Color color, int size, Point2D renderOffset) {
    this.center = center;
    this.body = points;
    this.color = color;
    this.size = size;
    this.renderOffset = renderOffset;
  }

  protected abstract void calculateMinMaxCoords();

  public void move(int x, int y) {
    center.setLocation(center.getX() + x, center.getY() + y);
    for (Point2D point : body) {
      point.setLocation(point.getX() + x, point.getY() + y);
    }

    minX += x;
    maxX += x;
    minY += y;
    maxY += y;
  }

  public abstract void rotate(double angle);

  protected void rotatePoints(double angle) {
    for (Point2D point : body) {
      double x = point.getX() - center.getX();
      double y = point.getY() - center.getY();
      double newX = x * Math.cos(angle) - y * Math.sin(angle);
      double newY = x * Math.sin(angle) + y * Math.cos(angle);
      point.setLocation(newX + center.getX(), newY + center.getY());
    }
  }

  public void render(Graphics g) {
    // System.out.println("Rendering shape");
    // System.out.println("Center: " + center);

    for (Point2D point : body) {
      // System.out.println("Point x: " + point);

      g.setColor(color);
      g.fillRect(
          (int) (point.getX() * size - size / 2) + (int) renderOffset.getX(),
          (int) (point.getY() * size - size / 2) + (int) renderOffset.getY(),
          size,
          size);

      g.setColor(color.brighter());
      g.drawRect(
          (int) (point.getX() * size - size / 2) + (int) renderOffset.getX(),
          (int) (point.getY() * size - size / 2) + (int) renderOffset.getY(),
          size,
          size);
    }
    // System.out.println("Color: " + color);
    // System.out.println("Size: " + size);
    // System.out.println("End of shape");

  }

  public Point2D[] getBody() {
    return body;
  }

  public int getMinX() {
    return minX;
  }

  public int getMaxX() {
    return maxX;
  }

  public int getMinY() {
    return minY;
  }

  public int getMaxY() {
    return maxY;
  }

}
