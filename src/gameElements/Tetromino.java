package gameElements;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.Random;

import static utils.Constants.GameConstants.*;
import static utils.Constants.Directions.*;

import gameStates.GameState;

public class Tetromino {

  private Shape shape;
  private Board board;

  private int verticalMoveTick = 0;
  private int horizontalMoveTick = 0;

  private final int HORIZONTAL_SPEED = 20;
  private final int VERTICAL_SLOW = 1;
  private final int VERTICAL_FAST = 20;
  private final int VERTICAL_INSTANT = 10000;

  private int verticalSpeed = VERTICAL_SLOW;

  private boolean right, left, down, drop;
  private boolean active = true;

  private Random rand = new Random();

  public Tetromino(int renderSize, Point2D renderOrigin, Board board) {
    this.board = board;
    shape = shapeFactory(renderSize, renderOrigin);

    System.out.println("[Tetromino] Hello!");
    System.out.println("[Tetromino] Shape: " + shape);
  }

  private Shape shapeFactory(int renderSize, Point2D spawnPoint) {
    int shapeType = rand.nextInt(2);

    switch (shapeType) {
      case 0:
        return new IShape(renderSize, spawnPoint);
      case 1:
        return new TShape(renderSize, spawnPoint);
      default:
        return null;
    }
  }

  private boolean checkHorizontalColision(int dir) {
    int delta = dir == LEFT ? -1 : 1;

    if (dir == LEFT && shape.getMinX() <= 0)
      return true;
    else if (dir == RIGHT && shape.getMaxX() + 1 >= BOARD_WIDTH)
      return true;

    for (Point2D point : shape.getShape()) {
      int x = (int) point.getX() + delta;
      int y = (int) point.getY();
      if (board.getBoard()[y][x] != board.getBackgroundColor()) {
        return true;
      }
    }

    return false;
  }

  private boolean checkVerticalColision() {
    active = false;

    if (shape.getMaxY() + 1 >= BOARD_HEIGHT)
      return true;

    for (Point2D point : shape.getShape()) {
      int x = (int) point.getX();
      int y = (int) point.getY() + 1;
      if (board.getBoard()[y][x] != board.getBackgroundColor()) {
        return true;
      }
    }

    active = true;
    return false;
  }

  private boolean checkRotationColision() {
    if (shape.getMinY() < 0 || shape.getMaxY() >= BOARD_HEIGHT)
      return true;

    if (shape.getMinX() < 0 || shape.getMaxX() >= BOARD_WIDTH)
      return true;

    for (Point2D point : shape.getShape()) {
      int x = (int) point.getX();
      int y = (int) point.getY();
      if (board.getBoard()[y][x] != board.getBackgroundColor()) {
        return true;
      }
    }

    return false;
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

    boolean rotationBlocked = checkRotationColision();
    System.out.println("Rotation blocked: " + rotationBlocked);
    if (rotationBlocked)
      shape.rotate(-angle);
  }

  public void move(int direction) {
    switch (direction) {
      case LEFT:
        if (!checkHorizontalColision(LEFT))
          shape.move(-1, 0);
        break;

      case RIGHT:
        if (!checkHorizontalColision(RIGHT))
          shape.move(1, 0);
        break;

      case DOWN:
        if (!checkVerticalColision())
          shape.move(0, 1);
        break;
    }
  }

  public void update() {
    if (!active)
      return;

    if (drop) {
      verticalSpeed = VERTICAL_INSTANT;
    } else if (down && verticalSpeed != VERTICAL_INSTANT) {
      verticalSpeed = VERTICAL_FAST;
    } else if (verticalSpeed != VERTICAL_INSTANT) {
      verticalSpeed = VERTICAL_SLOW;
    }

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

  public void setDown(boolean down) {
    this.down = down;
  }

  public void setDrop(boolean drop) {
    this.drop = drop;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public boolean isActive() {
    return active;
  }

  public Shape getShape() {
    return shape;
  }

}
