package gameElements;

import java.awt.Color;
import java.awt.geom.Point2D;

public class IShape extends Shape {

  private static final Point2D[] SHAPE = {
      new Point2D.Double(0, 0),
      new Point2D.Double(1, 0),
      new Point2D.Double(2, 0),
      new Point2D.Double(3, 0)
  };

  private static final Point2D ORIGIN = new Point2D.Double(1.5, 0.5);
  private static final Color COLOR = new Color(0, 255, 255);

  private boolean horizontal = true;

  public IShape(int renderSize, Point2D renderOffset) {
    super(ORIGIN, SHAPE, COLOR, renderSize, renderOffset);
    calculateMinMaxCoords();
  }

  @Override
  protected void calculateMinMaxCoords() {
    if (horizontal) {
      minY = maxY = (int) body[0].getY();
      minX = (int) Math.min(body[0].getX(), body[3].getX());
      maxX = (int) Math.max(body[0].getX(), body[3].getX());
      return;
    }

    minX = maxX = (int) body[0].getX();
    minY = (int) Math.min(body[0].getY(), body[3].getY());
    maxY = (int) Math.max(body[0].getY(), body[3].getY());

  }

  @Override
  public void rotate(double angle) {
    rotatePoints(angle);
    horizontal = !horizontal;
    calculateMinMaxCoords();
  }
}
