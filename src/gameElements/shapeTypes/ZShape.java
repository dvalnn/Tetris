package gameElements.shapeTypes;

// import static utils.Constants.Directions.*;
import static utils.Constants.GameConstants.*;

import gameElements.Shape;
import java.awt.Color;
import java.awt.geom.Point2D;

public class ZShape extends Shape {

  private static final Point2D[] SHAPE = {
      new Point2D.Double(0, 0),
      new Point2D.Double(1, 0),
      new Point2D.Double(1, 1),
      new Point2D.Double(2, 1),
  };
  private static final Point2D CENTER = new Point2D.Double(1, 1);
  private static final Color COLOR = Color.RED;

  // private int rotation = UP;

  public ZShape(int renderSize, Point2D renderOrigin) {
    super(CENTER, SHAPE, COLOR, renderSize, renderOrigin);

    minX = 0;
    maxX = 2;
    minY = 0;
    maxY = 1;

    // move to center top of the board;
    move(BOARD_WIDTH / 2 - 1, 0);
  }

  @Override
  protected void calculateMinMaxCoords() {
    minX = (int) Math.min(shape[0].getX(), shape[3].getX());
    maxX = (int) Math.max(shape[0].getX(), shape[3].getX());
    minY = (int) Math.min(shape[0].getY(), shape[3].getY());
    maxY = (int) Math.max(shape[0].getY(), shape[3].getY());
  }

  @Override
  public void rotate(double angle) {
    rotatePoints(angle);
    calculateMinMaxCoords();
  }
}