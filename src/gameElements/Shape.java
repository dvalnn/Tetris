package gameElements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public abstract class Shape {

  protected Point2D center;
  protected Point2D[] shape;
  protected Color color;

  protected int minX, maxX, minY, maxY;

  private int renderSize;
  private Point2D renderOffset;

  protected abstract void calculateMinMaxCoords();

  // * Shape specific way of rotating
  // * can be used as rotatePoints() wrapper
  public abstract void rotate(double angle);

  public Shape(
      Point2D center,
      Point2D[] points,
      Color color,
      int renderSize,
      Point2D renderOrigin) {

    this.center = (Point2D) center.clone();
    this.shape = new Point2D[points.length];
    for (int i = 0; i < points.length; i++) {
      this.shape[i] = (Point2D) points[i].clone();
    }

    this.color = new Color(color.getRGB());
    this.renderSize = renderSize;
    this.renderOffset = renderOrigin;
  }

  public void move(int x, int y) {
    center.setLocation(center.getX() + x, center.getY() + y);
    for (Point2D point : shape) {
      point.setLocation(point.getX() + x, point.getY() + y);
    }

    minX += x;
    maxX += x;
    minY += y;
    maxY += y;
  }

  protected void rotatePoints(double angle) {
    for (Point2D point : shape) {
      double x = point.getX() - center.getX();
      double y = point.getY() - center.getY();
      double newX = x * Math.cos(angle) - y * Math.sin(angle);
      double newY = x * Math.sin(angle) + y * Math.cos(angle);
      point.setLocation(Math.round(newX + center.getX()), Math.round(newY + center.getY()));
    }
  }

  public void render(Graphics g) {
    for (Point2D point : shape) {
      g.setColor(color);
      g.fillRect(
          (int) (point.getX() * renderSize - renderSize / 2) + (int) renderOffset.getX(),
          (int) (point.getY() * renderSize - renderSize / 2) + (int) renderOffset.getY(),
          renderSize,
          renderSize);
      g.setColor(color.darker().darker());
      g.drawRect(
          (int) (point.getX() * renderSize - renderSize / 2) + (int) renderOffset.getX(),
          (int) (point.getY() * renderSize - renderSize / 2) + (int) renderOffset.getY(),
          renderSize,
          renderSize);
    }
  }

  public Point2D getCenter() {
    return center;
  }

  public Point2D[] getShape() {
    return shape;
  }

  public int getRenderSize() {
    return renderSize;
  }

  public Point2D getRenderOffset() {
    return renderOffset;
  }

  public Color getColor() {
    return color;
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
