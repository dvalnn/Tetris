package gameElements;

import java.awt.Color;
import java.awt.geom.Point2D;

import static utils.Constants.GameConstants.*;

public class IShape extends Shape {

  private static final Point2D[] SHAPE = {
      new Point2D.Double(0, 0),
      new Point2D.Double(1, 0),
      new Point2D.Double(2, 0),
      new Point2D.Double(3, 0)
  };
  private static final Point2D CENTER = new Point2D.Double(1.5, 0.5);
  private static final Color COLOR = new Color(0, 255, 255);

  private boolean horizontal = true;

  public IShape(int renderSize, Point2D renderOrigin) {
    super(CENTER, SHAPE, COLOR, renderSize, renderOrigin);

    // go to the center top of the board
    move(BOARD_WIDTH / 2 - 2, 0);

    calculateMinMaxCoords();
  }

  @Override
  protected void calculateMinMaxCoords() {
    if (horizontal) {
      minY = maxY = (int) shape[0].getY();
      minX = (int) Math.min(shape[0].getX(), shape[3].getX());
      maxX = (int) Math.max(shape[0].getX(), shape[3].getX());
      return;
    }

    minX = maxX = (int) shape[0].getX();
    minY = (int) Math.min(shape[0].getY(), shape[3].getY());
    maxY = (int) Math.max(shape[0].getY(), shape[3].getY());

  }

  @Override
  public void rotate(double angle) {
    rotatePoints(angle);
    horizontal = !horizontal;
    calculateMinMaxCoords();
  }
}
