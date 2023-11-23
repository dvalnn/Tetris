package com.psw.tetris.gameElements;

import static com.psw.tetris.utils.Constants.Directions.DOWN;
import static com.psw.tetris.utils.Constants.Directions.LEFT;
import static com.psw.tetris.utils.Constants.Directions.RIGHT;
import static com.psw.tetris.utils.Constants.Directions.UP;
import static com.psw.tetris.utils.Constants.GameConstants.BOARD_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.BOARD_WIDTH;
import static com.psw.tetris.utils.Constants.GameConstants.UPS_SET;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

import com.psw.tetris.gameElements.boardTypes.PlayerBoard;
import com.psw.tetris.gameElements.shapeTypes.GhostShape;
import com.psw.tetris.gameElements.shapeTypes.JsonShape;
import com.psw.tetris.gameElements.shapeTypes.Shape;
import com.psw.tetris.gameplay.Levels;
import com.psw.tetris.gameplay.Score;
import com.psw.tetris.utils.WallKickData;

public class Tetromino {
  private final Shape shape;
  private final GhostShape ghost;
  private final PlayerBoard board;

  private final int HORIZONTAL_SPEED = 20;

  private int verticalMoveTick = 0;
  private int horizontalMoveTick = 0;

  private int verticalSpeed = 0;
  private int speedModifier = 1;

  private int rotationStatus = UP;
  private int shapeID = 0;

  private boolean softDrop = false;
  private boolean hardDrop = false;

  private boolean right, left, down, drop;
  private boolean active = true;
  private boolean updateGhost = true;

  private boolean deactivating;
  private int deactivationTickCounter;
  private final int DEACTIVATION_TICKS = UPS_SET / 2; // 0.5 seconds

  public Tetromino(
      final int renderSize,
      final Point2D renderOrigin,
      final PlayerBoard board,
      final JsonShape shapeData,
      final int shapeID) {

    this.board = board;
    this.shapeID = shapeID;

    Color shapeColor = new Color(
        shapeData.rgb[0],
        shapeData.rgb[1],
        shapeData.rgb[2]);

    shape = new Shape(
        shapeData.center,
        shapeData.points,
        shapeColor,
        renderSize,
        renderOrigin);

    shape.initPosition();

    ghost = new GhostShape(shape);
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
      final Point2D kickData = WallKickData.getKickData(
          shapeID,
          rotationStatus,
          direction,
          kickIndex);

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

    if (colision && hardDrop) {
      active = false;
      return;
    }

    if (colision && !deactivating) {
      // System.out.println("[Tetromino] Deactivating");
      deactivating = true;
      deactivationTickCounter = 0;
      return;
    }

    if (colision && deactivating) {
      deactivationTickCounter++;
      // System.out.println("[Tetromino] Deactivation tick: " +
      // deactivationTickCounter);
      if (deactivationTickCounter >= DEACTIVATION_TICKS) {
        active = false;
      }
      return;
    }

    deactivationTickCounter = 0;
    deactivating = false;

    verticalMoveTick++;
    if (verticalMoveTick * speedModifier >= verticalSpeed) {
      shape.move(0, 1);
      verticalMoveTick = 0;
      if (hardDrop)
        Score.scoreHardDrop();
      else if (softDrop)
        Score.scoreSoftDrop();
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

    verticalSpeed = Levels.getLevelSpeed();

    if (drop) {
      hardDrop = true;
      speedModifier = Levels.hardDropMultiplier;
    } else if (down && !hardDrop) {
      softDrop = true;
      speedModifier = Levels.softDropMultiplier;
    } else if (!hardDrop) {
      speedModifier = 1;
    }

    move(DOWN);

    // if the vertical speed is instant, the shape should not move horizontally
    if (hardDrop)
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
