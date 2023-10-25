package gameElements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import static utils.Constants.GameConstants.*;

// GamePanel is a JPanel -- a container for all visual elements in the game
public class Board {
  private int squareSize;
  private Point origin;

  private Color[][] board = new Color[BOARD_HEIGHT][BOARD_WIDTH];
  private Tetromino tetromino;

  public Board(int squareSize, int offsetX, int offsetY, Color bkgColor) {
    this.squareSize = squareSize;
    origin = new Point(offsetX, offsetY);

    // this.bkgColor = bkgColor;

    // squareSize is the size of each square in the tetromino
    // tetroX is the initial x position of the tetromino
    // offsetY is the initial y position of the tetromino
    // subtracting squareSize from tetroX centers the tetromino
    this.tetromino = new Tetromino(this, origin, squareSize);

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
        g.fillRect(origin.x + col * squareSize, origin.y + row * squareSize, squareSize, squareSize);

        // draw the cell border -- which is white -- to make the grid
        g.setColor(Color.WHITE);
        g.drawRect(origin.x + col * squareSize, origin.y + row * squareSize, squareSize,
            squareSize);
      }

      // render the tetromino
      tetromino.render(g);
    }
  }

  public Tetromino getTetromino() {
    return tetromino;
  }

  public void disableInputs() {
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
          board[tetromino.getY() + row][tetromino.getX() + col] = tetromino
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
