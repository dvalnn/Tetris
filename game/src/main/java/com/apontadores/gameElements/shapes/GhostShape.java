package com.apontadores.gameElements.shapes;

import java.awt.Color;
import java.awt.geom.Point2D;

public class GhostShape extends Shape {

  public GhostShape(final Shape MasterShape) {

    super(
        MasterShape.getCenter(),
        MasterShape.getPoints(),
        MasterShape.getColor(),
        MasterShape.getId(),
        true,
        MasterShape.getRenderSize(),
        MasterShape.getRenderOffset());

    color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 100);

    minX = MasterShape.getMinX();
    maxX = MasterShape.getMaxX();
    minY = MasterShape.getMinY();
    maxY = MasterShape.getMaxY();
  }

  // move to center top of the board
  @Override
  public void initPosition() {
  }

  public void goToMaster(final Point2D masterCenter) {
    final double deltaX = masterCenter.getX() - center.getX();
    final double deltaY = masterCenter.getY() - center.getY();
    move(deltaX, deltaY);
  }

  private void move(final double x, final double y) {
    for (final Point2D point : points) {
      point.setLocation(point.getX() + x, point.getY() + y);
    }
    center.setLocation(center.getX() + x, center.getY() + y);

    minX += (int) x;
    maxX += (int) x;
    minY += (int) y;
    maxY += (int) y;
  }
}
