package gameElements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public class ShapeMP {
  private Point2D[] points;
  private Color color;
  private int renderSize;
  private Point2D renderOffset;

  public ShapeMP(int renderSize, int xOffset, int yOffset) {
    this.renderSize = renderSize;
    this.renderOffset = new Point2D.Double(xOffset, yOffset);
  }

  public void update(Point2D[] points, Color color) {
    this.points = points;
    this.color = color;
  }

  public void render(Graphics g) {
    if (points == null) return;

    for (Point2D point : points) {
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

  public Point2D[] getPoints() {
    return points;
  }
}
