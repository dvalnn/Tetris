package com.psw.tetris.gameElements.shapeTypes;

import static com.psw.tetris.utils.Constants.GameConstants.BOARD_WIDTH;

import com.psw.tetris.gameElements.Shape;
import java.awt.Color;
import java.awt.geom.Point2D;

public class OShape extends Shape {

  private static final Point2D[] SHAPE = {
    new Point2D.Double(0, 0), new Point2D.Double(0, 1),
    new Point2D.Double(1, 0), new Point2D.Double(1, 1)
  };
  private static final Point2D CENTER = new Point2D.Double(0.5, 0.5);
  private static final Color COLOR = Color.YELLOW;

  public OShape(int renderSize, Point2D renderOrigin) {
    super(CENTER, SHAPE, COLOR, renderSize, renderOrigin);

    // set the min and max calculateMinMaxCords
    minX = 0;
    maxX = 1;
    minY = 0;
    maxY = 1;

    // move to the center top of the board;
    this.move(BOARD_WIDTH / 2, 0);
  }

  @Override
  public void rotate(double angle) {
    // do nothing
  }

  @Override
  protected void calculateMinMaxCoords() {
    // do nothing
  }
}
