package gameElements;

import java.awt.Color;
import java.awt.Graphics;

import static utils.Constants.GameConstants.*;

// GamePanel is a JPanel -- a container for all visual elements in the game
public class Board {
  private int squareSize;
  private int x0, y0;

  private Color[][] board = new Color[BOARD_HEIGHT][BOARD_WIDTH];
  // private Color bkgColor = Color.BLACK;

  private Tetromino tetromino;
  // private Tetromino nextTetromino;

  public Board(int squareSize, int offsetX, int offsetY, Color bkgColor) {
    this.squareSize = squareSize;

    this.x0 = offsetX;
    this.y0 = offsetY;
    // this.bkgColor = bkgColor;

    int tetroX = (offsetX + BOARD_WIDTH) / 2 * squareSize;

    // squareSize is the size of each square in the tetromino
    // tetroX is the initial x position of the tetromino
    // offsetY is the initial y position of the tetromino
    // subtracting squareSize from tetroX centers the tetromino
    this.tetromino = new Tetromino(this, tetroX - squareSize, offsetY, squareSize);

    // init the board with a black background
    for (int row = 0; row < BOARD_HEIGHT; row++) {
      for (int col = 0; col < BOARD_WIDTH; col++) {
        board[row][col] = bkgColor;
      }
    }
  }

  public void update() {
    tetromino.update();
    checkLine();
  }

  public void render(Graphics g) {
    for (int row = 0; row < BOARD_HEIGHT; row++) {
      for (int col = 0; col < BOARD_WIDTH; col++) {
        // draw the cell color
        g.setColor(board[row][col]);
        g.fillRect(x0 + col * squareSize, y0 + row * squareSize, squareSize, squareSize);

        // draw the cell border -- which is white -- to make the grid
        g.setColor(Color.WHITE);
        g.drawRect(x0 + col * squareSize, y0 + row * squareSize, squareSize,
            squareSize);
      }

      // render the tetromino
      tetromino.render(g);
    }
  }

  public Tetromino getTetromino() {
    return tetromino;
  }

  public void setPaused() {
    tetromino.setDown(false);
    tetromino.setLeft(false);
    tetromino.setRight(false);
    tetromino.setDrop(false);
  }

  public void freezePieceOnBoard(Tetromino tetromino) {
    // set the backgound color of the grid to the color of the tetromino
    for (int row = 0; row < tetromino.getShape().length; row++) {
      for (int col = 0; col < tetromino.getShape()[row].length; col++) {
        if (tetromino.getShape()[row][col] == 1) {
          board[tetromino.getY() / squareSize + row][tetromino.getX() / squareSize + col] = tetromino
              .getColor();
        }
      }
    }
  }

  public void checkLine() {
    int bottomLine = board.length - 1;
    for (int topLine = bottomLine; topLine > 0; topLine--) {
      int count = 0;
      for (int col = 0; col < board[0].length; col++) {
        if (board[topLine][col] != Color.black)
          count++;
        board[bottomLine][col] = board[topLine][col];
      }
      if (count < board[0].length)
        bottomLine--;
    }
  }

  public Color[][] getBoard() {
    return board;
  }

}
