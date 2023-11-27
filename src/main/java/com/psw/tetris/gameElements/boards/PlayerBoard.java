package com.psw.tetris.gameElements.boards;

import static com.psw.tetris.utils.Constants.RESOURCES_PATH;
import static com.psw.tetris.utils.Constants.GameConstants.BOARD_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.BOARD_WIDTH;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import com.psw.tetris.gameElements.Board;
import com.psw.tetris.gameElements.Tetromino;
import com.psw.tetris.gameElements.gameplay.GameTime;
import com.psw.tetris.gameElements.gameplay.Levels;
import com.psw.tetris.gameElements.gameplay.Score;
import com.psw.tetris.gameElements.shapes.JsonShape;
import com.psw.tetris.gameStates.GameStateHandler;
import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.settings.BoardSettings;
import com.psw.tetris.utils.JsonShapeParser;

// GamePanel is a JPanel -- a container for all visual elements in the game

public class PlayerBoard extends Board {

  // TODO: implement 6 Tetrominos
  private Tetromino activeTetro; // active
  private Tetromino nextTetro; // next
  private Tetromino nextTetro2; // next
  private Tetromino nextTetro3; // next
  private Tetromino nextTetro4; // next

  private Tetromino holdTetro; // hold

  private boolean debugMode = false;
  private boolean paused = false;
  private boolean blockHoldTetromino = false;

  protected ArrayList<JsonShape> shapeData;

  private final Random rand = new Random();

  public PlayerBoard(BoardSettings set) {
    super(set);

    Score.reset();
    Levels.reset();
    GameTime.reset();

    shapeData = JsonShapeParser.parseAllJsonShapes(RESOURCES_PATH + "/shapes/");

    activeTetro = tetrominoFactory(shapeData);
    nextTetro = tetrominoFactory(shapeData);
    nextTetro2 = tetrominoFactory(shapeData);
    nextTetro3 = tetrominoFactory(shapeData);
    nextTetro4 = tetrominoFactory(shapeData);
  }

  private Tetromino tetrominoFactory(ArrayList<JsonShape> shapeData) {

    int shapeID = rand.nextInt(shapeData.size());
    JsonShape shape = shapeData.get(shapeID);

    return new Tetromino(
        set.squareSize,
        new Point2D.Double(set.xOffset, set.yOffset),
        this,
        shape,
        shapeID);
  }

  private Tetromino tetrominoFactory(int shapeID) {
    JsonShape shape = shapeData.get(shapeID);
    return new Tetromino(
        set.squareSize,
        new Point2D.Double(set.xOffset, set.yOffset),
        this,
        shape,
        shapeID);
  }

  public void holdTetromino() {
    if (blockHoldTetromino)
      return;

    if (holdTetro == null) {
      holdTetro = tetrominoFactory(activeTetro.getShapeID());
      activeTetro = nextTetro;
      nextTetro = nextTetro2;
      nextTetro2 = nextTetro3;
      nextTetro3 = nextTetro4;
      nextTetro4 = tetrominoFactory(shapeData);

      blockHoldTetromino = true;
      return;
    }

    final Tetromino aux = activeTetro;
    activeTetro = tetrominoFactory(holdTetro.getShapeID());
    holdTetro = tetrominoFactory(aux.getShapeID());
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
      GameStateHandler.switchState(GameStatesEnum.GAME_OVER);
    }

  }

  private void clearLine(final int row) {
    for (int col = 0; col < BOARD_WIDTH; col++) {
      board.get(row).setColor(col, set.backgroundColor);
    }
  }

  private void shiftLineDown(final int row) {
    for (int r = row; r > 0; r--) {
      for (int col = 0; col < BOARD_WIDTH; col++) {
        board.get(r).setColor(col, board.get(r - 1).getIndexRGB(col));
      }
    }
  }

  private void checkLines() {
    int linesCleared = 0;

    for (int row = 0; row < BOARD_HEIGHT; row++) {
      boolean full = true;
      for (int col = 0; col < BOARD_WIDTH; col++) {
        if (board.get(row).getIndexRGB(col) == set.backgroundColor.getRGB()) {
          full = false;
          break;
        }
      }
      if (full) {
        linesCleared++;
        clearLine(row);
        shiftLineDown(row);
      }
    }

    Score.registerLinesCleared(linesCleared);
    Levels.registerLinesCleared(linesCleared);

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
  private void toggleBlockOnMousePosition(
      final int x,
      final int y,
      final boolean add) {

    // NOTE: for each grid square, expand it fmom board coordinates
    // to screen coordinates and check if the mouse is in it
    // if it is, toggle the block.
    // TODO: Make this more efficient somehow.
    // Performance is not an issue right now, but
    // it might be in the future
    for (int row = 0; row < BOARD_HEIGHT; row++) {
      for (int col = 0; col < BOARD_WIDTH; col++) {
        final int x1 = (int) (col * set.squareSize - set.squareSize / 2) + set.xOffset;
        final int y1 = (int) (row * set.squareSize - set.squareSize / 2) + set.yOffset;
        final int x2 = x1 + set.squareSize;
        final int y2 = y1 + set.squareSize;
        if (x >= x1 && x <= x2 && y >= y1 && y <= y2) {
          if (add) {
            board.get(row).setColor(col, Color.PINK);
          } else {
            board.get(row).setColor(col, set.backgroundColor);
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
    activeTetro = tetrominoFactory(tetroID);
  }

  // NOTE: This method is only used for debugging purposes
  public void reset() {
    if (!debugMode)
      return;

    for (int row = 0; row < BOARD_HEIGHT; row++) {
      for (int col = 0; col < BOARD_WIDTH; col++) {
        board.get(row).setColor(col, set.backgroundColor);
      }
    }

    activeTetro = tetrominoFactory(shapeData);
    nextTetro = tetrominoFactory(shapeData);
  }

  public void update() {
    // update the score. This is done in this class
    // and not direcly on the parent class so that
    // the player and the opponent can have different
    // score values, as both PlayerBoard and BoardMP
    // extend Board.
    playerScore = Score.getScore();
    playerLevel = Levels.getCurrentLevel() + 1;
    playerLines = Levels.getTotalLinesCleared();

    GameTime.tick();

    // NOTE: This is only used for debugging purposes
    // TODO: Remove this
    if (paused)
      return;

    activeTetro.update();
    if (!activeTetro.isActive()) {

      addTetrominoToPile();
      checkLines();
      activeTetro = nextTetro;
      nextTetro = nextTetro2;
      nextTetro2 = nextTetro3;
      nextTetro3 = nextTetro4;
      nextTetro4 = tetrominoFactory(shapeData);
    }
  }

  public void render(final Graphics g) {
    super.render(g);
    activeTetro.render(g);

    nextTetro.getShape().renderAt(
        g,
        set.nextRenderX,
        set.nextRenderY);

    nextTetro2.getShape().renderAt(
        g,
        set.nextRenderX,
        set.nextRenderY + 100);

    nextTetro3.getShape().renderAt(
        g,
        set.nextRenderX,
        set.nextRenderY + 200);

    nextTetro4.getShape().renderAt(
        g,
        set.nextRenderX,
        set.nextRenderY + 300);

    if (holdTetro != null)
      holdTetro.getShape().renderAt(
          g,
          set.holdRenderX,
          set.holdRenderY);

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
