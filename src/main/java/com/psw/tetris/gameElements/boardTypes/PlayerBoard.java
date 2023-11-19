package com.psw.tetris.gameElements.boardTypes;

import static com.psw.tetris.utils.Constants.GameConstants.BOARD_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.BOARD_WIDTH;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

import com.psw.tetris.gameElements.Board;
import com.psw.tetris.gameElements.Tetromino;
import com.psw.tetris.gameStates.GameStateHandler;
import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;

// GamePanel is a JPanel -- a container for all visual elements in the game

public class PlayerBoard extends Board {

  // TODO: implement 6 Tetrominos

  private Tetromino activeTetro; // active
  private Tetromino nextTetro; // next
  private Tetromino holdTetro; // hold

  private boolean debugMode = false;
  private boolean paused = false;
  private boolean blockHoldTetromino = false;

  public PlayerBoard(final int size, final int xOffset, final int yOffset, final Color color) {
    super(size, xOffset, yOffset, color);

    activeTetro = new Tetromino(size, renderOrigin, this);
    nextTetro = new Tetromino(size, renderOrigin, this);
  }

  public void holdTetromino() {
    if (blockHoldTetromino)
      return;

    if (holdTetro == null) {
      holdTetro = activeTetro;
      activeTetro = new Tetromino(renderSize, renderOrigin, this, nextTetro.getShapeID());
      nextTetro = new Tetromino(renderSize, renderOrigin, this);
      blockHoldTetromino = true;
      return;
    }

    final Tetromino aux = activeTetro;
    activeTetro = new Tetromino(renderSize, renderOrigin, this, holdTetro.getShapeID());
    holdTetro = aux;
    blockHoldTetromino = true;
  }

  private void addTetrominoToPile() {
    blockHoldTetromino = false;
    for (final Point2D point : activeTetro.getShape().getPoints()) {
      final int row = (int) point.getY();
      final int col = (int) point.getX();
      board.get(row).setColor(col, activeTetro.getShape().getColor());
    }
    if (activeTetro.getShape().getMinY() <= 0) {
      System.out.println("[Board] Game Over!");
      GameStateHandler.setActiveState(GameStatesEnum.GAME_OVER);
    }

  }

  private void clearRow(final int row) {
    for (int col = 0; col < BOARD_WIDTH; col++) {
      board.get(row).setColor(col, backgroundColor);
    }
  }

  private void shiftRowsDown(final int row) {
    for (int r = row; r > 0; r--) {
      for (int col = 0; col < BOARD_WIDTH; col++) {
        board.get(r).setColor(col, board.get(r - 1).getIndexRGB(col));
      }
    }
  }

  private void checkRows() {
    int rowsCleared = 0;

    for (int row = 0; row < BOARD_HEIGHT; row++) {
      boolean full = true;
      for (int col = 0; col < BOARD_WIDTH; col++) {
        if (board.get(row).getIndexRGB(col) == backgroundColor.getRGB()) {
          full = false;
          break;
        }
      }
      if (full) {
        rowsCleared++;
        clearRow(row);
        shiftRowsDown(row);
      }
    }

    System.out.println("[Board] Cleared " + rowsCleared + " rows!");
  }

  // NOTE: This method is only used for debugging purposes
  public void addBlockOnMousePosition(final int x, final int y) {
    if (!debugMode)
      return;

    toggleBlockOnMousePosition(x, y, true);
  }

  // NOTE: This method is only used for debugging purposes
  public void removeBlockOnMousePosition(final int x, final int y) {
    if (!debugMode)
      return;

    toggleBlockOnMousePosition(x, y, false);
  }

  // NOTE: This method is only used for debugging purposes
  // it is not used in the actual game.
  // PERF: This method is very inefficient. It iterates over
  // the entire board every time the mouse is clicked.
  private void toggleBlockOnMousePosition(final int x, final int y, final boolean add) {

    // NOTE: for each grid square, expand it fmom board coordinates
    // to screen coordinates and check if the mouse is in it
    // if it is, toggle the block.
    // TODO: Make this more efficient somehow.
    // Performance is not an issue right now, but
    // it might be in the future
    for (int row = 0; row < BOARD_HEIGHT; row++) {
      for (int col = 0; col < BOARD_WIDTH; col++) {
        final int x1 = (int) (col * renderSize - renderSize / 2) + (int) renderOrigin.getX();
        final int y1 = (int) (row * renderSize - renderSize / 2) + (int) renderOrigin.getY();
        final int x2 = x1 + renderSize;
        final int y2 = y1 + renderSize;
        if (x >= x1 && x <= x2 && y >= y1 && y <= y2) {
          if (add) {
            board.get(row).setColor(col, Color.PINK);
          } else {
            board.get(row).setColor(col, backgroundColor);
          }
          return;
        }
      }
    }
  }

  // NOTE: This method is only used for debugging purposes
  public void setTetromino(final int tetroID) {
    if (!debugMode)
      return;
    activeTetro = new Tetromino(renderSize, renderOrigin, this, tetroID);
  }

  // NOTE: This method is only used for debugging purposes
  public void reset() {
    if (!debugMode)
      return;

    for (int row = 0; row < BOARD_HEIGHT; row++) {
      for (int col = 0; col < BOARD_WIDTH; col++) {
        board.get(row).setColor(col, backgroundColor);
      }
    }
    activeTetro = new Tetromino(renderSize, renderOrigin, this);
    nextTetro = new Tetromino(renderSize, renderOrigin, this);
  }

  public void update() {
    // NOTE: This is only used for debugging purposes
    // TODO: Remove this

    if (paused)
      return;
    activeTetro.update();
    if (!activeTetro.isActive()) {

      addTetrominoToPile();
      checkRows();
      activeTetro = nextTetro;
      nextTetro = new Tetromino(renderSize, renderOrigin, this);
    }
  }

  public void render(final Graphics g) {
    super.render(g);

    activeTetro.render(g);

    // NOTE: This is only used for debugging purposes
    // TODO: Remove this
    if (debugMode) {
      g.setColor(Color.RED);
      g.drawString("Debug Mode", 10, 10);
    }
    if (paused) {
      g.setColor(Color.RED);
      g.drawString("Paused", 10, 30);
    }
  }

  // TODO: Obscure the debug mode activation
  // Maybe make it so that the user has
  // to press a certain key combination
  public void toggleDebugMode() {
    debugMode = !debugMode;
  }

  public void togglePause() {
    paused = !paused;
  }

  public Tetromino getTetromino() {
    return activeTetro;
  }

  public Tetromino getNextTetromino() {
    return nextTetro;
  }

  public Tetromino getHoldTetromino() {
    return holdTetro;
  }
}
