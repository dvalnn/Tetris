package com.psw.tetris.gameElements;

import static com.psw.tetris.utils.Constants.Directions.DOWN;
import static com.psw.tetris.utils.Constants.Directions.LEFT;
import static com.psw.tetris.utils.Constants.Directions.RIGHT;
import static com.psw.tetris.utils.Constants.Directions.UP;
import static com.psw.tetris.utils.Constants.GameConstants.BOARD_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.BOARD_WIDTH;
import static com.psw.tetris.utils.Constants.GameConstants.UPS_SET;
import static com.psw.tetris.utils.Constants.TetrominoIDs.I;
import static com.psw.tetris.utils.Constants.TetrominoIDs.J;
import static com.psw.tetris.utils.Constants.TetrominoIDs.L;
import static com.psw.tetris.utils.Constants.TetrominoIDs.O;
import static com.psw.tetris.utils.Constants.TetrominoIDs.S;
import static com.psw.tetris.utils.Constants.TetrominoIDs.T;
import static com.psw.tetris.utils.Constants.TetrominoIDs.Z;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.Random;

import com.psw.tetris.gameElements.boardTypes.PlayerBoard;
import com.psw.tetris.gameElements.shapeTypes.GhostShape;
import com.psw.tetris.gameElements.shapeTypes.IShape;
import com.psw.tetris.gameElements.shapeTypes.JShape;
import com.psw.tetris.gameElements.shapeTypes.LShape;
import com.psw.tetris.gameElements.shapeTypes.OShape;
import com.psw.tetris.gameElements.shapeTypes.SShape;
import com.psw.tetris.gameElements.shapeTypes.TShape;
import com.psw.tetris.gameElements.shapeTypes.ZShape;
import com.psw.tetris.utils.WallKickData;

public class Tetromino {

  private final Shape shape;
  private final GhostShape ghost;
  private final PlayerBoard board;

  private final int HORIZONTAL_SPEED = 20;
  private final int VERTICAL_SLOW = 1;
  private final int VERTICAL_FAST = 20;
  private final int VERTICAL_INSTANT = 20000;

  private int verticalMoveTick = 0;
  private int horizontalMoveTick = 0;
  private int verticalSpeed = VERTICAL_SLOW;
  private int rotationStatus = UP;
  private int shapeID;

  private boolean right, left, down, drop;
  private boolean active = true;
  private boolean updateGhost = true;

  private final Random rand = new Random();
  private boolean deactivating;
  private int deactivationTickCounter;
  private final int DEACTIVATION_TICKS = UPS_SET / 2; // 0.5 seconds

  public Tetromino(final int renderSize, final Point2D renderOrigin, final PlayerBoard board) {
    this.board = board;
    shape = shapeFactory(renderSize, renderOrigin, rand.nextInt(7));
    shape.initPosition();

    ghost = new GhostShape(shape);
    System.out.println("[Tetromino] New Shape: " + shape);
  }

  // NOTE: this constructor is only used for testing
  // TODO: remove this constructor

  public Tetromino(final int renderSize, final Point2D renderOrigin, final PlayerBoard board, final int shapeID) {
    this.board = board;
    shape = shapeFactory(renderSize, renderOrigin, shapeID);
    shape.initPosition();

    ghost = new GhostShape(shape);

    System.out.println("[Tetromino] New Shape: " + shape);
  }

  private Shape shapeFactory(final int renderSize, final Point2D spawnPoint, final int shapeID) {
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

  private boolean sideColides(final int dir) {
    final int delta = dir == LEFT ? -1 : 1;

    if (dir == LEFT && shape.getMinX() <= 0)
      return true;
    else if (dir == RIGHT && shape.getMaxX() + 1 >= BOARD_WIDTH)
      return true;

    for (final Point2D point : shape.getPoints()) {
      final int x = (int) point.getX() + delta;
      final int y = (int) point.getY();
      if (board.getBoard().get(y).getIndexRGB(x) != board.getBackgroundColor().getRGB()) {
        return true;
      }
    }

    return false;
  }

  private boolean bottomColides(final Shape shape) {
    if (shape.getMaxY() + 1 >= BOARD_HEIGHT)
      return true;

    for (final Point2D point : shape.getPoints()) {
      final int x = (int) point.getX();
      final int y = (int) point.getY() + 1;
      if (board.getBoard().get(y).getIndexRGB(x) != board.getBackgroundColor().getRGB()) {
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

    for (final Point2D point : shape.getPoints()) {
      final int x = (int) point.getX();
      final int y = (int) point.getY();
      if (board.getBoard().get(y).getIndexRGB(x) != board.getBackgroundColor().getRGB()) {
        return true;
      }
    }

    return false;
  }

  public void rotate(final int direction) {
    double angle = 0;

    final int rotationStatusDelta = direction == RIGHT ? 1 : -1;

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
      final Point2D kickData = WallKickData.getKickData(shapeID, rotationStatus, direction, kickIndex);
      shape.move((int) kickData.getX(), (int) kickData.getY());

      // check if the rotation is valid for the current kick data
      if (!rotationColides()) {
        // if the rotation is valid, update the ghost and rotation status
        ghost.goToMaster(shape.getCenter());
        ghost.rotate(angle);
        updateGhost = true;

        // succesfull rotation resets disable timer
        deactivationTickCounter = 0;

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

  private void handleMoveDown() {

    final boolean colision = bottomColides(shape);

    if (colision && verticalSpeed == VERTICAL_INSTANT) {
      active = false;
      return;
    }

    if (colision && !deactivating) {
      System.out.println("[Tetromino] Deactivating");
      deactivating = true;
      deactivationTickCounter = 0;
      return;
    }

    if (colision && deactivating) {
      deactivationTickCounter++;
      System.out.println("[Tetromino] Deactivation tick: " + deactivationTickCounter);
      if (deactivationTickCounter >= DEACTIVATION_TICKS) {
        active = false;
      }
      return;
    }

    deactivationTickCounter = 0;
    deactivating = false;

    verticalMoveTick++;
    if (verticalMoveTick * verticalSpeed >= UPS_SET) {
      shape.move(0, 1);
      verticalMoveTick = 0;
    }
  }

  private void move(final int direction) {
    switch (direction) {
      case LEFT:
        if (!sideColides(LEFT)) {
          shape.move(-1, 0);
          updateGhost = true;

          // sucessfull movement resets disable timer
          deactivationTickCounter = 0;
        }
        break;

      case RIGHT:
        if (!sideColides(RIGHT)) {
          shape.move(1, 0);
          updateGhost = true;
        }
        break;

      case DOWN:
        handleMoveDown();
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

    move(DOWN);

    // if the vertical speed is instant, the shape should not move horizontally
    if (verticalSpeed == VERTICAL_INSTANT)
      return;

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

  public void render(final Graphics g) {
    // render ghost only if it's not in the same position as the shape
    if (!ghost.getCenter().equals(shape.getCenter()))
      ghost.render(g);

    // render shape after ghost so it's on top
    shape.render(g);
  }

  public void setRight(final boolean right) {
    this.right = right;
  }

  public void setLeft(final boolean left) {
    this.left = left;
  }

  public void setDown(final boolean down) {
    this.down = down;
  }

  public void setDrop(final boolean drop) {
    this.drop = drop;
  }

  public void setActive(final boolean active) {
    this.active = active;
  }

  public boolean isActive() {
    return active;
  }

  public int getShapeID() {
    return shapeID;
  }

  public Shape getShape() {
    return shape;
  }
}
