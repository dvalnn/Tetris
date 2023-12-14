package com.apontadores.gameElements.shapes;

import static com.apontadores.utils.Constants.GameConstants.BOARD_WIDTH;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public class Shape {

  protected Point2D center;
  protected Point2D[] points;
  protected Color color;
  protected int minX, maxX, minY, maxY;

  private final String id;
  private final boolean rotates;
  private final int renderSize;
  private final Point2D renderOffset;

  public Shape(
      final Point2D center,
      final Point2D[] points,
      final Color color,
      final String name,
      final boolean rotates,
      final int renderSize,
      final Point2D renderOrigin) {

    this.center = (Point2D) center.clone();
    this.points = new Point2D[points.length];
    for (int i = 0; i < points.length; i++) {
      this.points[i] = (Point2D) points[i].clone();
    }

    this.color = new Color(color.getRGB());
    this.id = name;
    this.rotates = rotates;

    this.renderSize = renderSize;
    this.renderOffset = renderOrigin;

    calculateMinMaxCoords();
  }

  public Shape(
      final JsonShape shapeData,
      final int renderSize,
      final Point2D renderOrigin) {

    this.center = (Point2D) shapeData.center.clone();
    this.points = new Point2D[shapeData.points.length];
    for (int i = 0; i < shapeData.points.length; i++) {
      this.points[i] = (Point2D) shapeData.points[i].clone();
    }

    this.color = new Color(
        shapeData.rgb[0],
        shapeData.rgb[1],
        shapeData.rgb[2]);

    this.id = shapeData.name;
    this.rotates = shapeData.rotates;

    this.renderSize = renderSize;
    this.renderOffset = renderOrigin;

    calculateMinMaxCoords();
  }

  public void move(final int x, final int y) {
    center.setLocation(center.getX() + x, center.getY() + y);
    for (final Point2D point : points) {
      point.setLocation(point.getX() + x, point.getY() + y);
    }

    minX += x;
    maxX += x;
    minY += y;
    maxY += y;
  }

  public void initPosition() {
    move(BOARD_WIDTH / 2 - 1, 0);
  }

  public void rotate(final double angle) {
    if (!rotates)
      return;

    for (final Point2D point : points) {
      final double x = point.getX() - center.getX();
      final double y = point.getY() - center.getY();
      final double rotatedX = x * Math.cos(angle) - y * Math.sin(angle);
      final double rotatedY = x * Math.sin(angle) + y * Math.cos(angle);
      final double newX = Math.round(rotatedX + center.getX());
      final double newY = Math.round(rotatedY + center.getY());

      point.setLocation(newX, newY);
    }

    calculateMinMaxCoords();
  }

  public void render(final Graphics g) {
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
    for (final Point2D point : points) {
      final int xCord = (int) (point.getX() * renderSize - renderSize / 2) + x;
      final int yCord = (int) (point.getY() * renderSize - renderSize / 2) + y;

      g.setColor(color);
      g.fillRect(xCord, yCord, renderSize, renderSize);
      g.setColor(color.darker().darker());
      g.drawRect(xCord, yCord, renderSize, renderSize);
    }
  }

  public Point2D getCenter() {
    return center;
  }

  public Point2D[] getPoints() {
    return points;
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

  public String getId() {
    return id;
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

  protected void calculateMinMaxCoords() {
    minX = Integer.MAX_VALUE;
    maxX = Integer.MIN_VALUE;
    minY = Integer.MAX_VALUE;
    maxY = Integer.MIN_VALUE;

    for (final Point2D point : points) {
      minX = (int) Math.min(point.getX(), minX);
      maxX = (int) Math.max(point.getX(), maxX);
      minY = (int) Math.min(point.getY(), minY);
      maxY = (int) Math.max(point.getY(), maxY);
    }
  }
}
