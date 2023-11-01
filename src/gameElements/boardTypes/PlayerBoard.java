package gameElements.boardTypes;

import static utils.Constants.GameConstants.*;

import gameElements.Board;
import gameElements.Tetromino;
import gameStates.GameState;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

// GamePanel is a JPanel -- a container for all visual elements in the game
public class PlayerBoard extends Board {

  private Tetromino tetro1;
  private Tetromino tetro2;

  private boolean debugMode = false;
  private boolean paused = false;

  public PlayerBoard(int size, int xOffset, int yOffset, Color color) {
    super(size, xOffset, yOffset, color);

    tetro1 = new Tetromino(size, renderOrigin, this);
    tetro2 = new Tetromino(size, renderOrigin, this);
  }

  private void addTetrominoToPile() {
    for (Point2D point : tetro1.getShape().getPoints()) {
      int row = (int) point.getY();
      int col = (int) point.getX();
      board.get(row).setColor(col, tetro1.getShape().getColor());
    }
    if (tetro1.getShape().getMinY() <= 0) {
      System.out.println("[Board] Game Over!");
      GameState.state = GameState.GAME_OVER;
    }
  }

  private void clearRow(int row) {
    for (int col = 0; col < BOARD_WIDTH; col++) {
      board.get(row).setColor(col, backgroundColor);
    }
  }

  private void shiftRowsDown(int row) {
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
  public void addBlockOnMousePosition(int x, int y) {
    if (!debugMode) return;

    toggleBlockOnMousePosition(x, y, true);
  }

  // NOTE: This method is only used for debugging purposes
  public void removeBlockOnMousePosition(int x, int y) {
    if (!debugMode) return;

    toggleBlockOnMousePosition(x, y, false);
  }

  // NOTE: This method is only used for debugging purposes
  // it is not used in the actual game.
  // PERF: This method is very inefficient. It iterates over
  // the entire board every time the mouse is clicked.
  private void toggleBlockOnMousePosition(int x, int y, boolean add) {

    // NOTE: for each grid square, expand it fmom board coordinates
    // to screen coordinates and check if the mouse is in it
    // if it is, toggle the block.
    // TODO: Make this more efficient somehow.
    // Performance is not an issue right now, but
    // it might be in the future
    for (int row = 0; row < BOARD_HEIGHT; row++) {
      for (int col = 0; col < BOARD_WIDTH; col++) {
        int x1 = (int) (col * renderSize - renderSize / 2) + (int) renderOrigin.getX();
        int y1 = (int) (row * renderSize - renderSize / 2) + (int) renderOrigin.getY();
        int x2 = x1 + renderSize;
        int y2 = y1 + renderSize;
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
  public void setTetromino(int tetroID) {
    if (!debugMode) return;
    tetro1 = new Tetromino(renderSize, renderOrigin, this, tetroID);
  }

  // NOTE: This method is only used for debugging purposes
  public void reset() {
    if (!debugMode) return;

    for (int row = 0; row < BOARD_HEIGHT; row++) {
      for (int col = 0; col < BOARD_WIDTH; col++) {
        board.get(row).setColor(col, backgroundColor);
      }
    }
    tetro1 = new Tetromino(renderSize, renderOrigin, this);
    tetro2 = new Tetromino(renderSize, renderOrigin, this);
  }

  public void update() {
    // NOTE: This is only used for debugging purposes
    // TODO: Remove this
    if (paused) return;
    tetro1.update();
    if (!tetro1.isActive()) {
      addTetrominoToPile();
      checkRows();
      tetro1 = tetro2;
      tetro2 = new Tetromino(renderSize, renderOrigin, this);
    }
  }

  public void render(Graphics g) {
    super.render(g);

    tetro1.render(g);

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
    return tetro1;
  }
}
