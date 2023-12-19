package com.apontadores.totris.gameElements.boards;

import static com.apontadores.totris.utils.Constants.RESOURCES_PATH;
import static com.apontadores.totris.utils.Constants.SYS_SEPARATOR;
import static com.apontadores.totris.utils.Constants.GameConstants.BOARD_HEIGHT;
import static com.apontadores.totris.utils.Constants.GameConstants.BOARD_WIDTH;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import com.apontadores.totris.gameElements.Board;
import com.apontadores.totris.gameElements.Tetromino;
import com.apontadores.totris.gameElements.gameplay.GameTime;
import com.apontadores.totris.gameElements.gameplay.Levels;
import com.apontadores.totris.gameElements.gameplay.Score;
import com.apontadores.totris.gameElements.shapes.JsonShape;
import com.apontadores.totris.gameElements.shapes.Shape;
import com.apontadores.totris.settings.BoardSettings;
import com.apontadores.totris.utils.LoadSave;

public class PlayerBoard extends Board {

  private boolean gameOver;

  private Tetromino activeTetro; // active
  private Tetromino nextTetro; // next
  private Tetromino nextTetro2; // next
  private Tetromino nextTetro3; // next
  private Tetromino nextTetro4; // next
  private Tetromino holdTetro; // hold

  private boolean debugMode = false;
  private boolean blockHoldTetromino = false;

  private final Random rand = new Random();

  private int[] shapeStocks;
  private final int numShapes;
  private final int maxRand;

  protected ArrayList<JsonShape> shapeData;

  private boolean holdChanged;
  private boolean nextShapesChanged;

  public PlayerBoard(final BoardSettings set) {
    super(set);

    Score.reset();
    Levels.reset();
    GameTime.reset();

    final String dirPath = RESOURCES_PATH
        + SYS_SEPARATOR
        + "shapes"
        + SYS_SEPARATOR;

    shapeData = LoadSave.parseAllJsonShapes(dirPath);

    numShapes = shapeData.size();
    shapeStocks = new int[numShapes];
    for (int i = 0; i < numShapes; i++) {
      shapeStocks[i] = (numShapes - 1) * 10;
    }
    maxRand = numShapes * (numShapes - 1) * 10;

    System.out.println("[Board] MaxRand: " + maxRand);

    activeTetro = tetrominoFactory();
    nextTetro = tetrominoFactory();
    nextTetro2 = tetrominoFactory();
    nextTetro3 = tetrominoFactory();
    nextTetro4 = tetrominoFactory();
  }

  public void holdTetromino() {
    if (blockHoldTetromino)
      return;

    holdChanged = true;

    if (holdTetro == null) {
      holdTetro = tetrominoFactory(activeTetro.getShapeID());
      activeTetro = nextTetro;
      nextTetro = nextTetro2;
      nextTetro2 = nextTetro3;
      nextTetro3 = nextTetro4;
      nextTetro4 = tetrominoFactory();

      nextShapesChanged = true;
      blockHoldTetromino = true;
      return;
    }

    final Tetromino aux = activeTetro;
    activeTetro = tetrominoFactory(holdTetro.getShapeID());
    holdTetro = tetrominoFactory(aux.getShapeID());
    blockHoldTetromino = true;
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

    activeTetro = tetrominoFactory();
    nextTetro = tetrominoFactory();
  }

  public void setPlayerName(final String name) {
    username = name;
  }

  public void update() {
    if (gameOver)
      return;

    // update the score. This is done in this class
    // and not direcly on the parent class so that
    // the player and the opponent can have different
    // score values, as both PlayerBoard and BoardMP
    // extend Board.
    playerScore = Score.getScore();
    playerLevel = Levels.getCurrentLevel() + 1;
    playerLines = Levels.getTotalLinesCleared();

    GameTime.tick();

    activeTetro.update();
    if (!activeTetro.isActive()) {

      addTetrominoToPile();
      checkLines();
      activeTetro = nextTetro;
      nextTetro = nextTetro2;
      nextTetro2 = nextTetro3;
      nextTetro3 = nextTetro4;
      nextTetro4 = tetrominoFactory();
      nextShapesChanged = true;
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
        set.next2RenderY);

    nextTetro3.getShape().renderAt(
        g,
        set.nextRenderX,
        set.next3RenderY);

    nextTetro4.getShape().renderAt(
        g,
        set.nextRenderX,
        set.next4RenderY);

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
  }

  // TODO: Obscure the debug mode activation
  // Maybe make it so that the user has
  // to press a certain key combination
  public void toggleDebugMode() {
    debugMode = !debugMode;
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

  public Shape getHoldIfChanged() {
    if (!holdChanged)
      return null;

    holdChanged = false;
    return holdTetro.getShape();
  }

  public Shape[] getNextIfChanged() {
    if (!nextShapesChanged)
      return null;

    nextShapesChanged = false;
    return new Shape[] {
        nextTetro.getShape(),
        nextTetro2.getShape(),
        nextTetro3.getShape(),
        nextTetro4.getShape() };
  }

  public boolean isGameOver() {
    return gameOver;
  }

  private Tetromino tetrominoFactory() {
    //System.out.println("[Board] Generating new tetromino");

    final int shapeID = generateShapeID();
    final JsonShape shape = shapeData.get(shapeID);

    return new Tetromino(
        set.squareSize,
        new Point2D.Double(set.xOffset, set.yOffset),
        this,
        shape,
        shapeID);
  }

  private int generateShapeID() {
    int shapeID = -1;
    int randValue = rand.nextInt(maxRand);

    // find shapeID
    if (randValue < shapeStocks[0]) {
      shapeID = 0;
    }

    int accumulator = shapeStocks[0];
    if (shapeID == -1)
      for (int i = 1; i < numShapes; i++) {
        accumulator += shapeStocks[i];

        if (randValue < accumulator) {
          shapeID = i;
          break;
        }
      }

    // update shape stocks
    final int stockIncrease = 3;
    final int stockReduction = (numShapes - 1) * stockIncrease;
    for (int i = 0; i < numShapes; i++) {
      if (i == shapeID) {
        shapeStocks[i] -= stockReduction;
      } else {
        shapeStocks[i] += stockIncrease;
      }
      //System.out.println("ShapeID: " + i + " stock: " + shapeStocks[i]);
    }

    //System.out.println("Selected shapeID: " + shapeID);
    //System.out.println("----");

    return shapeID;
  }

  private Tetromino tetrominoFactory(final int shapeID) {
    final JsonShape shape = shapeData.get(shapeID);
    return new Tetromino(
        set.squareSize,
        new Point2D.Double(set.xOffset, set.yOffset),
        this,
        shape,
        shapeID);
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
      gameOver = true;
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
  // it is not used in the actual game.
  // PERF: This method is very inefficient. It iterates over
  // the entire board every time the mouse is clicked.
  private void toggleBlockOnMousePosition(
      final int x,
      final int y,
      final boolean add) {

    // NOTE: for each grid square, expand it from board coordinates
    // to screen coordinates and check if the mouse is in it
    // if it is, toggle the block.
    // TODO: Make this more efficient somehow.
    // Performance is not an issue right now, but
    // it might be in the future
    for (int row = 0; row < BOARD_HEIGHT; row++) {
      for (int col = 0; col < BOARD_WIDTH; col++) {
        final int x1 = (col * set.squareSize - set.squareSize / 2) + set.xOffset;
        final int y1 = (row * set.squareSize - set.squareSize / 2) + set.yOffset;
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
}
