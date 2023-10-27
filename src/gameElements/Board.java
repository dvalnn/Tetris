package gameElements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;

import gameStates.GameState;

import static utils.Constants.GameConstants.*;

// GamePanel is a JPanel -- a container for all visual elements in the game
public class Board {
  private Color[][] board;
  private Color backgroundColor;
  private Color gridColor;
  private Point2D renderOrigin;
  private int renderSize;

  private Tetromino tetro1;
  private Tetromino tetro2;

  public Board(int size, int xOffset, int yOffset, Color color) {
    this.renderSize = size;
    renderOrigin = new Point(xOffset, yOffset);

    board = new Color[size][size];
    backgroundColor = new Color(color.getRGB());
    gridColor = new Color(backgroundColor.brighter().getRGB());

    tetro1 = new Tetromino(size, renderOrigin, this);
    tetro2 = new Tetromino(size, renderOrigin, this);

    for (int row = 0; row < BOARD_HEIGHT; row++) {
      for (int col = 0; col < BOARD_WIDTH; col++) {
        board[row][col] = backgroundColor;
      }
    }
  }

  private void addTetrominoToPile() {
    for (Point2D point : tetro1.getShape().getShape()) {
      int row = (int) point.getY();
      int col = (int) point.getX();
      board[row][col] = tetro1.getShape().getColor();
    }
    if (tetro1.getShape().getMinY() <= 0) {
      System.out.println("[Board] Game Over!");
      GameState.state = GameState.GAME_OVER;
    }
  }

  private void clearRow(int row) {
    for (int col = 0; col < BOARD_WIDTH; col++) {
      board[row][col] = backgroundColor;
    }
  }

  private void shiftRowsDown(int row) {
    for (int r = row; r > 0; r--) {
      for (int col = 0; col < BOARD_WIDTH; col++) {
        board[r][col] = board[r - 1][col];
      }
    }
  }

  private void checkRows() {
    int rowsCleared = 0;

    for (int row = 0; row < BOARD_HEIGHT; row++) {
      boolean full = true;
      for (int col = 0; col < BOARD_WIDTH; col++) {
        if (board[row][col] == backgroundColor) {
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

  public void update() {
    tetro1.update();
    if (!tetro1.isActive()) {
      addTetrominoToPile();
      checkRows();
      tetro1 = tetro2;
      tetro2 = new Tetromino(renderSize, renderOrigin, this);
    }
  }

  public void render(Graphics g) {
    for (int row = 0; row < BOARD_HEIGHT; row++) {
      for (int col = 0; col < BOARD_WIDTH; col++) {
        g.setColor(board[row][col]);
        g.fillRect(
            (int) (col * renderSize - renderSize / 2) + (int) renderOrigin.getX(),
            (int) (row * renderSize - renderSize / 2) + (int) renderOrigin.getY(),
            renderSize,
            renderSize);
        g.setColor(gridColor);
        g.drawRect(
            (int) (col * renderSize - renderSize / 2) + (int) renderOrigin.getX(),
            (int) (row * renderSize - renderSize / 2) + (int) renderOrigin.getY(),
            renderSize,
            renderSize);
      }
    }

    tetro1.render(g);
  }

  public Tetromino getTetromino() {
    return tetro1;
  }

  public Color[][] getBoard() {
    return board;
  }

  public Color getBackgroundColor() {
    return backgroundColor;
  }

}
