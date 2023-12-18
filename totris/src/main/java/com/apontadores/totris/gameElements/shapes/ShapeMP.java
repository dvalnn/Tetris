package com.apontadores.totris.gameElements.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public class ShapeMP {
  private Point2D[] points;
  private Color color;
  private final int renderSize;
  private final Point2D renderOffset;

  public ShapeMP(final int renderSize, final int xOffset, final int yOffset) {
    this.renderSize = renderSize;
    this.renderOffset = new Point2D.Double(xOffset, yOffset);
  }

  public void update(final Point2D[] points, final Color color) {
    this.points = points;
    this.color = color;
  }

  public void render(final Graphics g) {
    if (points == null)
      return;

    for (final Point2D point : points) {
      int x = (int) (point.getX() * renderSize - renderSize / 2);
      int y = (int) (point.getY() * renderSize - renderSize / 2);

      x += (int) renderOffset.getX();
      y += (int) renderOffset.getY();

      g.setColor(color);
      g.fillRect(x, y, renderSize, renderSize);
      g.setColor(color.darker().darker());
      g.drawRect(x, y, renderSize, renderSize);
    }
  }

  public void renderAt(final Graphics g, final int x, final int y) {
    if (points == null)
      return;

    for (final Point2D point : points) {
      final int xCord = (int) (point.getX() * renderSize - renderSize / 2) + x;
      final int yCord = (int) (point.getY() * renderSize - renderSize / 2) + y;

      g.setColor(color);
      g.fillRect(xCord, yCord, renderSize, renderSize);
      g.setColor(color.darker().darker());
      g.drawRect(xCord, yCord, renderSize, renderSize);
    }
  }

  public Point2D[] getPoints() {
    return points;
  }

  public Color getColor() {
    return color;
  }
}
