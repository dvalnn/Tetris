package gameElements;

import java.awt.Color;
import java.awt.geom.Point2D;

import static utils.Constants.Directions.*;
import static utils.Constants.GameConstants.*;

public class JShape extends Shape {

  private static final Point2D[] SHAPE = {
      new Point2D.Double(0, 0),
      new Point2D.Double(0, 1),
      new Point2D.Double(1, 1),
      new Point2D.Double(2, 1),
  };
  private static final Point2D CENTER = new Point2D.Double(1, 1);
  private static final Color COLOR = Color.BLUE;

  private int rotation = UP;
  private boolean horizontal = true;

  public JShape(int renderSize, Point2D renderOrigin) {
    super(CENTER, SHAPE, COLOR, renderSize, renderOrigin);

    // move to center top of the board;
    move(BOARD_WIDTH / 2 - 1, 0);
  }

  @Override
  protected void calculateMinMaxCoords() {
    if (horizontal) {
      maxX = (int) Math.max(shape[0].getX(), shape[3].getX());
      minX = (int) Math.min(shape[0].getX(), shape[3].getX());
      if (rotation == UP) {
        maxY = (int) shape[1].getY();
        minY = (int) shape[0].getY();
      } else {
        maxY = (int) shape[0].getY();
        minY = (int) shape[1].getY();
      }
      return;
    }

    maxY = (int) Math.max(shape[0].getY(), shape[3].getY());
    minY = (int) Math.min(shape[0].getY(), shape[3].getY());
    if (rotation == RIGHT) {
      maxX = (int) shape[0].getX();
      minX = (int) shape[1].getX();
    } else {
      maxX = (int) shape[1].getX();
      minX = (int) shape[0].getX();
    }
  }

  @Override
  public void rotate(double angle) {
    rotatePoints(angle);

    horizontal = !horizontal;
    int dir = angle > 0 ? 1 : -1;
    rotation = (rotation + dir) % 4;
    if (rotation < 0) {
      rotation += 4;
    }

    calculateMinMaxCoords();
  }
}
