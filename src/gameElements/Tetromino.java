package gameElements;

import static utils.Constants.Directions.*;
import static utils.Constants.GameConstants.*;
import static utils.Constants.TetrominoIDs.*;

import gameElements.shapeTypes.*;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.Random;
import utils.WallKickData;

public class Tetromino {

  private Shape shape;
  private GhostShape ghost;
  private Board board;

  private final int HORIZONTAL_SPEED = 20;
  private final int VERTICAL_SLOW = 1;
  private final int VERTICAL_FAST = 20;
  private final int VERTICAL_INSTANT = 10000;

  private int verticalMoveTick = 0;
  private int horizontalMoveTick = 0;
  private int verticalSpeed = VERTICAL_SLOW;
  private int rotationStatus = UP;
  private int shapeID;

  private boolean right, left, down, drop;
  private boolean active = true;
  private boolean updateGhost = true;

  private Random rand = new Random();

  public Tetromino(int renderSize, Point2D renderOrigin, Board board) {
    this.board = board;
    shape = shapeFactory(renderSize, renderOrigin, rand.nextInt(7));
    ghost = new GhostShape(shape);

    System.out.println("[Tetromino] Hello!");
    System.out.println("[Tetromino] Shape: " + shape);
  }

  // NOTE: this constructor is only used for testing
  // TODO: remove this constructor
  public Tetromino(int renderSize, Point2D renderOrigin, Board board,
      int shapeID) {
    this.board = board;
    shape = shapeFactory(renderSize, renderOrigin, shapeID);
    ghost = new GhostShape(shape);

    System.out.println("[Tetromino] Hello!");
    System.out.println("[Tetromino] Shape: " + shape);
  }

  private Shape shapeFactory(int renderSize, Point2D spawnPoint, int shapeID) {
    this.shapeID = shapeID;
    switch (shapeID) {
      case I:
        return new IShape(renderSize, spawnPoint);
      case T:
        return new TShape(renderSize, spawnPoint);
      case O:
        return new OShape(renderSize, spawnPoint);
      case J:
        return new JShape(renderSize, spawnPoint);
      case L:
        return new LShape(renderSize, spawnPoint);
      case S:
        return new SShape(renderSize, spawnPoint);
      case Z:
        return new ZShape(renderSize, spawnPoint);
      default:
        return null;
    }
  }

  private boolean sideColides(int dir) {
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

  private boolean bottomColides(Shape shape) {
    if (shape.getMaxY() + 1 >= BOARD_HEIGHT)
      return true;

    for (Point2D point : shape.getShape()) {
      int x = (int) point.getX();
      int y = (int) point.getY() + 1;
      if (board.getBoard()[y][x] != board.getBackgroundColor()) {
        return true;
      }
    }

    return false;
  }

  private boolean rotationColides() {
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

    int rotationStatusDelta = direction == RIGHT ? 1 : -1;

    switch (direction) {
      case LEFT:
        angle = -Math.PI / 2;
        break;
      case RIGHT:
        angle = Math.PI / 2;
        break;
    }

    shape.rotate(angle);

    for (int kickIndex = 0; kickIndex < 5; kickIndex++) {

      // get kick data for current rotation status, direction and kick index
      // kick data is a point with x and y coordinates
      // x and y are the amount of pixels the shape needs to move to avoid
      // collision with other shapes. The amount of pixels is relative to the
      // current rotation status and direction. Note that the rotation status is
      // only updated if the rotation succeeds.
      Point2D kickData = WallKickData.getKickData(shapeID, rotationStatus,
          direction, kickIndex);
      shape.move((int) kickData.getX(), (int) kickData.getY());

      // check if the rotation is valid for the current kick data
      if (!rotationColides()) {
        System.out.println("[Tetromino] Wall kicked on variant number " +
            kickIndex + " with rotation status " +
            rotationStatus + " and direction " + direction);
        // if the rotation is valid, update the ghost and rotation status
        ghost.goToMaster(shape.getCenter());
        ghost.rotate(angle);
        updateGhost = true;

        rotationStatus = (rotationStatus + rotationStatusDelta) % 4;
        if (rotationStatus < 0)
          rotationStatus = 3;
        return;
      }

      // if the rotation is not valid, move the shape back to its original
      // position
      shape.move(-(int) kickData.getX(), -(int) kickData.getY());
    }

    // if all kick variations fail, reset the shape to its original state
    shape.rotate(-angle);
  }

  public void move(int direction) {
    switch (direction) {
      case LEFT:
        if (!sideColides(LEFT)) {
          shape.move(-1, 0);
          updateGhost = true;
        }
        break;

      case RIGHT:
        if (!sideColides(RIGHT)) {
          shape.move(1, 0);
          updateGhost = true;
        }
        break;

      case DOWN:
        if (bottomColides(shape))
          active = false;
        else
          shape.move(0, 1);

        break;
    }
  }

  public void dropGhost() {
    while (!bottomColides(ghost))
      ghost.move(0, 1);
  }

  public void update() {
    if (!active)
      return;

    if (updateGhost) {
      ghost.goToMaster(shape.getCenter());
      dropGhost();
      updateGhost = false;
    }

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
    // render ghost only if it's not in the same position as the shape
    if (!ghost.getCenter().equals(shape.getCenter()))
      ghost.render(g);

    // render shape after ghost so it's on top
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
