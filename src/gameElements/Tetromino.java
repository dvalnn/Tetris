package gameElements;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.Random;

import static utils.Constants.GameConstants.*;
import static utils.Constants.TetrominoConstants.*;
import static utils.Constants.Directions.*;

import gameStates.GameState;

public class Tetromino {

  private Shape shape;

  private int verticalMoveTick = 0;
  private int horizontalMoveTick = 0;

  private final int HORIZONTAL_SPEED = 20;
  private final int VERTICAL_SLOW = 1;
  private final int VERTICAL_FAST = 20;
  private final int VERTICAL_INSTANT = 10000;

  private int verticalSpeed;

  private boolean right, left, down, drop;

  public Tetromino(int size, int scale, Point2D spawnPoint) {
    this.verticalSpeed = VERTICAL_SLOW;
    this.shape = shapeFactory(size, spawnPoint);
  }

  private Shape shapeFactory(int renderSize, Point2D spawnPoint) {
    return new IShape(renderSize, spawnPoint);
  }

  public void rotate(int direction) {
    double angle = 0;

    switch (direction) {
      case LEFT:
        angle = -Math.PI / 2;
        break;
      case RIGHT:
        angle = Math.PI / 2;
        break;
    }

    shape.rotate(angle);
  }

  public void move(int direction) {
    switch (direction) {
      case LEFT:
        shape.move(-1, 0);
        break;
      case RIGHT:
        shape.move(1, 0);
        break;
      case DOWN:
        shape.move(0, 1);
        break;
    }
  }

  public void update() {
    verticalMoveTick++;
    if (verticalMoveTick * verticalSpeed >= UPS_SET) {
      verticalMoveTick = 0;
      move(DOWN);
    }

    horizontalMoveTick++;
    if (horizontalMoveTick * HORIZONTAL_SPEED >= UPS_SET) {
      horizontalMoveTick = 0;
      if (right && !left) {
        move(RIGHT);
      } else if (left && !right) {
        move(LEFT);
      }
    }
  }

  public void render(Graphics g) {
    shape.render(g);
  }

  public void setRight(boolean right) {
    this.right = right;
  }

  public void setLeft(boolean left) {
    this.left = left;
  }

}
