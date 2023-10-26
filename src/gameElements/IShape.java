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

  public IShape(int renderSize, Point2D renderOffset) {
    super(ORIGIN, SHAPE, COLOR, renderSize, renderOffset);
  }

}
