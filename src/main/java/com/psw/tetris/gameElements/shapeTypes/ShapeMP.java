package com.psw.tetris.gameElements.shapeTypes;

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
