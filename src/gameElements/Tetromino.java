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
  private Board board;

  private int verticalMoveTick = 0;
  private int horizontalMoveTick = 0;

  private final int HORIZONTAL_SPEED = 20;
  private final int VERTICAL_SLOW = 1;
  private final int VERTICAL_FAST = 20;
  private final int VERTICAL_INSTANT = 10000;

  private int verticalSpeed;

  private boolean right, left, down, drop;
  private boolean canMove = true;

  public Tetromino(int size, int scale, Point2D spawnPoint, Board board) {
    this.verticalSpeed = VERTICAL_SLOW;
    this.shape = shapeFactory(size, spawnPoint);
    this.board = board;
  }

  private Shape shapeFactory(int renderSize, Point2D spawnPoint) {
    return new IShape(renderSize, spawnPoint);
  }

  private boolean checkHorizontalColision(int dir) {
    int minBodyX = BOARD_WIDTH;
    int maxBodyX = 0;
    int delta = dir == LEFT ? -1 : 1;

    for (Point2D point : shape.getBody()) {

      // ! crashed here -- index out of bounds
      if (board.getBoard()[(int) point.getY()][(int) point.getX() + delta] != null) {
        return true;
      }

      if (point.getX() < minBodyX) {
        minBodyX = (int) point.getX();
      }
      if (point.getX() > maxBodyX) {
        maxBodyX = (int) point.getX();
      }
    }

    if (dir == LEFT && minBodyX <= 0) {
      return true;
    } else if (dir == RIGHT && maxBodyX >= BOARD_WIDTH - 1) {
      return true;
    } else {
      return false;
    }
  }

  private boolean checkVerticalColision() {
    for (Point2D point : shape.getBody()) {
      if (point.getY() + 1 >= BOARD_HEIGHT) {
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
        shape.move(0, 1);
        break;
    }
  }

  public void update() {
    if (!canMove)
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
      canMove = !checkVerticalColision();
      if (canMove)
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

  public void setCanMove(boolean canMove) {
    this.canMove = canMove;
  }

  public Point2D[] getBody() {
    return shape.getBody();
  }

}
