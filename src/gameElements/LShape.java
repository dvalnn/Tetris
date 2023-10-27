package gameElements;

import java.awt.Color;
import java.awt.geom.Point2D;

import static utils.Constants.Directions.*;
import static utils.Constants.GameConstants.*;

public class LShape extends Shape {

  private static final Point2D[] SHAPE = {
      new Point2D.Double(0, 1),
      new Point2D.Double(1, 1),
      new Point2D.Double(2, 1),
      new Point2D.Double(2, 0),
  };
  private static final Point2D CENTER = new Point2D.Double(1, 1);
  private static final Color COLOR = Color.ORANGE;

  private int rotation = UP;
  private boolean horizontal = true;

  public LShape(int renderSize, Point2D renderOrigin) {
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
    if (horizontal) {
      maxX = (int) Math.max(shape[0].getX(), shape[3].getX());
      minX = (int) Math.min(shape[0].getX(), shape[3].getX());
      if (rotation == UP) {
        maxY = (int) shape[2].getY();
        minY = (int) shape[3].getY();
      } else {
        maxY = (int) shape[3].getY();
        minY = (int) shape[2].getY();
      }
      return;
    }

    maxY = (int) Math.max(shape[0].getY(), shape[3].getY());
    minY = (int) Math.min(shape[0].getY(), shape[3].getY());
    if (rotation == RIGHT) {
      maxX = (int) shape[3].getX();
      minX = (int) shape[2].getX();
    } else {
      maxX = (int) shape[2].getX();
      minX = (int) shape[3].getX();
    }
  }

  @Override
  public void rotate(double angle) {
    rotatePoints(angle);

    horizontal = !horizontal;
    int dir = angle > 0 ? 1 : -1;
    rotation = (rotation + dir) % 4;
    if (rotation < 0)
      rotation += 4;

    calculateMinMaxCoords();
  }

}
