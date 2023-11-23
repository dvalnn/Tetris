package com.psw.tetris.gameElements.shapeTypes;

import static com.psw.tetris.utils.Constants.GameConstants.BOARD_WIDTH;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public class Shape {

  protected Point2D center;
  protected Point2D[] points;
  protected Color color;

  protected int minX, maxX, minY, maxY;

  private final int renderSize;
  private final Point2D renderOffset;

  public Shape(
      final Point2D center,
      final Point2D[] points,
      final Color color,
      final int renderSize,
      final Point2D renderOrigin) {

    this.center = (Point2D) center.clone();
    this.points = new Point2D[points.length];
    for (int i = 0; i < points.length; i++) {
      this.points[i] = (Point2D) points[i].clone();
    }

    this.color = new Color(color.getRGB());
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

  protected void calculateMinMaxCoords() {
    minX = Integer.MAX_VALUE;
    maxX = Integer.MIN_VALUE;
    minY = Integer.MAX_VALUE;
    maxY = Integer.MIN_VALUE;

    for (Point2D point : points) {
      minX = (int) Math.min(point.getX(), minX);
      maxX = (int) Math.max(point.getX(), maxX);
      minY = (int) Math.min(point.getY(), minY);
      maxY = (int) Math.max(point.getY(), maxY);
    }
  }

  public void rotate(final double angle) {
    for (final Point2D point : points) {
      final double x = point.getX() - center.getX();
      final double y = point.getY() - center.getY();
      final double newX = x * Math.cos(angle) - y * Math.sin(angle);
      final double newY = x * Math.sin(angle) + y * Math.cos(angle);
      point.setLocation(Math.round(newX + center.getX()), Math.round(newY + center.getY()));
    }

    calculateMinMaxCoords();
  }

  public void render(final Graphics g) {
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

  public void renderAt(final Graphics g, final int x, final int y) {
    for (final Point2D point : points) {
      g.setColor(color);
      g.fillRect(
          (int) (point.getX() * renderSize - renderSize / 2) + x,
          (int) (point.getY() * renderSize - renderSize / 2) + y,
          renderSize,
          renderSize);
      g.setColor(color.darker().darker());
      g.drawRect(
          (int) (point.getX() * renderSize - renderSize / 2) + x,
          (int) (point.getY() * renderSize - renderSize / 2) + y,
          renderSize,
          renderSize);
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
