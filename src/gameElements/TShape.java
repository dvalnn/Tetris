package gameElements;

import java.awt.Color;
import java.awt.geom.Point2D;

import static utils.Constants.Directions.*;
import static utils.Constants.GameConstants.*;

public class TShape extends Shape {

  private static final Point2D[] SHAPE = {
      new Point2D.Double(0, 1),
      new Point2D.Double(1, 1),
      new Point2D.Double(2, 1),
      new Point2D.Double(1, 0)
  };
  private static final Point2D CENTER = new Point2D.Double(1, 1);
  private static final Color COLOR = new Color(128, 0, 128); // Purple

  private int rotation = UP;
  private boolean horizontal = true;

  public TShape(int renderSize, Point2D renderOrigin) {
    super(CENTER, SHAPE, COLOR, renderSize, renderOrigin);

    // go to the center top of the board
    move(BOARD_WIDTH / 2 - 1, 0);

    calculateMinMaxCoords();
  }

  @Override
  protected void calculateMinMaxCoords() {
    switch (rotation) {
      case UP:
        minX = (int) shape[0].getX();
        maxX = (int) shape[2].getX();
        minY = (int) shape[3].getY();
        maxY = (int) shape[1].getY();
        break;

      case RIGHT:
        minX = (int) shape[1].getX();
        maxX = (int) shape[3].getX();
        minY = (int) shape[0].getY();
        maxY = (int) shape[2].getY();
        break;

      case DOWN:
        minX = (int) shape[2].getX();
        maxX = (int) shape[0].getX();
        minY = (int) shape[1].getY();
        maxY = (int) shape[3].getY();
        break;

      case LEFT:
        minX = (int) shape[3].getX();
        maxX = (int) shape[1].getX();
        minY = (int) shape[0].getY();
        maxY = (int) shape[2].getY();
        break;
    }
  }

  @Override
  public void rotate(double angle) {
    rotatePoints(angle);

    int dir = angle > 0 ? 1 : -1;
    rotation = (rotation + dir) % 4;
    if (rotation < 0) {
      rotation += 4;
    }

    horizontal = !horizontal;
    calculateMinMaxCoords();
  }

}
