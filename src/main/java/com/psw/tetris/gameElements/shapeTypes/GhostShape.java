package com.psw.tetris.gameElements.shapeTypes;

import com.psw.tetris.gameElements.Shape;
import java.awt.Color;
import java.awt.geom.Point2D;

public class GhostShape extends Shape {

  public GhostShape(Shape MasterShape) {

    super(
        MasterShape.getCenter(),
        MasterShape.getPoints(),
        MasterShape.getColor(),
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

  @Override
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

  @Override
  public void rotate(double angle) {
    rotatePoints(angle);
    calculateMinMaxCoords();
  }

  public void goToMaster(Point2D masterCenter) {
    double deltaX = masterCenter.getX() - center.getX();
    double deltaY = masterCenter.getY() - center.getY();
    move(deltaX, deltaY);
  }

  private void move(double x, double y) {
    for (Point2D point : points) {
      point.setLocation(point.getX() + x, point.getY() + y);
    }
    center.setLocation(center.getX() + x, center.getY() + y);

    minX += (int) x;
    maxX += (int) x;
    minY += (int) y;
    maxY += (int) y;
  }
}
