package com.psw.tetris.gameElements.shapeTypes;

import static com.psw.tetris.utils.Constants.GameConstants.BOARD_WIDTH;

import com.psw.tetris.gameElements.Shape;
import java.awt.Color;
import java.awt.geom.Point2D;

public class JShape extends Shape {

  private static final Point2D[] SHAPE = {
    new Point2D.Double(0, 0),
    new Point2D.Double(0, 1),
    new Point2D.Double(1, 1),
    new Point2D.Double(2, 1),
  };
  private static final Point2D CENTER = new Point2D.Double(1, 1);
  private static final Color COLOR = Color.BLUE;

  public JShape(int renderSize, Point2D renderOrigin) {
    super(CENTER, SHAPE, COLOR, renderSize, renderOrigin);

    minX = 0;
    maxX = 2;
    minY = 0;
    maxY = 1;

  }
  // move to center top of the board
  @Override 
  public void initPosition()
  {
    move(BOARD_WIDTH / 2 - 1, 0);
  }

  @Override
  protected void calculateMinMaxCoords() {
    minX = (int) Math.min(points[0].getX(), points[3].getX());
    maxX = (int) Math.max(points[0].getX(), points[3].getX());
    minY = (int) Math.min(points[0].getY(), points[3].getY());
    maxY = (int) Math.max(points[0].getY(), points[3].getY());
  }

  @Override
  public void rotate(double angle) {
    rotatePoints(angle);
    calculateMinMaxCoords();
  }
}
