package com.psw.tetris.gameElements.shapeTypes;

import static com.psw.tetris.utils.Constants.Directions.DOWN;
import static com.psw.tetris.utils.Constants.Directions.LEFT;
import static com.psw.tetris.utils.Constants.Directions.RIGHT;
import static com.psw.tetris.utils.Constants.Directions.UP;
import static com.psw.tetris.utils.Constants.GameConstants.BOARD_WIDTH;

import com.psw.tetris.gameElements.Shape;
import java.awt.Color;
import java.awt.geom.Point2D;

public class TShape extends Shape {

  private static final Point2D[] SHAPE = {
      new Point2D.Double(0, 1), new Point2D.Double(1, 1),
      new Point2D.Double(2, 1), new Point2D.Double(1, 0)
  };
  private static final Point2D CENTER = new Point2D.Double(1, 1);
  private static final Color COLOR = new Color(128, 0, 128); // Purple

  private int rotation = UP;
  private boolean horizontal = true;

  public TShape(int renderSize, Point2D renderOrigin) {
    super(CENTER, SHAPE, COLOR, renderSize, renderOrigin);

    minX = 0;
    maxX = 2;
    minY = 0;
    maxY = 1;
  }

  // move to center top of the board;
  @Override
  public void initPosition() {
    move(BOARD_WIDTH / 2 - 1, 0);
  }

  @Override
  protected void calculateMinMaxCoords() {
    switch (rotation) {
      case UP:
        minX = (int) points[0].getX();
        maxX = (int) points[2].getX();
        minY = (int) points[3].getY();
        maxY = (int) points[1].getY();
        break;

      case RIGHT:
        minX = (int) points[1].getX();
        maxX = (int) points[3].getX();
        minY = (int) points[0].getY();
        maxY = (int) points[2].getY();
        break;

      case DOWN:
        minX = (int) points[2].getX();
        maxX = (int) points[0].getX();
        minY = (int) points[1].getY();
        maxY = (int) points[3].getY();
        break;

      case LEFT:
        minX = (int) points[3].getX();
        maxX = (int) points[1].getX();
        minY = (int) points[2].getY();
        maxY = (int) points[0].getY();
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
