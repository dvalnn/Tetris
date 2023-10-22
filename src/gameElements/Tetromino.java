package gameElements;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import static utils.Constants.GameConstants.*;
import static utils.Constants.TetrominoConstants.*;
import static utils.Constants.Directions.*;

public class Tetromino {
  private int x;
  private int y;

  private int xSpawn;
  private int ySpawn;

  private int size;

  private Random rand = new Random();

  // 2D array representing the shape of the tetromino
  private int[][] shape;
  private Color color;
  private int shapeIndex;

  private int verticalMoveTick = 0;
  private int verticalMoveSpeed = 1;
  private int horizontalMoveTick = 0;
  private int horizontalMoveSpeed = 20;
  private final int moveDelay = UPS_SET;

  private final int DEFAULT_MOVE_SPEED = 1;
  private final int FAST_MOVE_SPEED = 10;
  private final int INTANT_DROP_SPEED = 5000;

  private int board_pixel_width;
  private int board_pixel_height;
  private boolean colision = false;

  private boolean right, left, down, drop;

  private Board gameBoard;

  public Tetromino(Board gameBoard, int x, int y, int size) {
    this.xSpawn = x;
    this.ySpawn = y;
    this.size = size;

    initTetromino();

    board_pixel_width = BOARD_WIDTH * size;
    board_pixel_height = BOARD_HEIGHT * size;

    this.gameBoard = gameBoard;
  }

  private void initTetromino() {
    x = xSpawn;
    y = ySpawn;

    verticalMoveSpeed = DEFAULT_MOVE_SPEED;

    shapeIndex = rand.nextInt(SHAPES.length);
    shape = SHAPES[shapeIndex];
    color = new Color(
        COLORS[shapeIndex][0][0],
        COLORS[shapeIndex][0][1],
        COLORS[shapeIndex][0][2]);

    drop = false;
    colision = false;
  }

  private boolean checkVerticalColision() {
    if ((y + size * (shape.length + 1) > board_pixel_height))
      return true;

    for (int row = 0; row < shape.length; row++) {
      for (int col = 0; col < shape[row].length; col++) {
        if (shape[row][col] == 1
            && gameBoard.getBoard()[y / size + row + 1][x / size + col] != Color.BLACK)
          return true;
      }
    }
    return false;
  }

  private boolean checkHorizontalColision(int dir) {
    if (dir == RIGHT && x + size * (shape[0].length + 1) > board_pixel_width)
      return true;

    if (dir == LEFT && x - size < 0)
      return true;

    int xDelta = dir == RIGHT ? 1 : -1;

    for (int row = 0; row < shape.length; row++) {
      for (int col = 0; col < shape[row].length; col++) {
        if (gameBoard.getBoard()[y / size + row][x / size + col + xDelta] != Color.BLACK)
          return true;
      }
    }

    return false;
  }

  // transposeMatrix
  private int[][] transposeMatrix(int[][] Matrix) {
    int[][] temp = new int[Matrix[0].length][Matrix.length];
    for (int row = 0; row < Matrix.length; row++) {
      for (int col = 0; col < Matrix[0].length; col++) {
        temp[col][row] = Matrix[row][col];
      }
    }
    return temp;
  }

  // reverseRows
  private void reverseRows(int[][] matrix) {
    int middle = matrix.length / 2;
    for (int row = 0; row < middle; row++) {
      int[] temp = matrix[row];
      matrix[row] = matrix[matrix.length - row - 1];
      matrix[matrix.length - row - 1] = temp;
    }
  }

  // reverseColumns
  private void reverseColumns(int[][] Matrix) {
    for (int i = 0; i < Matrix[0].length; i++) {
      int low = 0;
      int high = Matrix.length - 1;
      while (low < high) {
        int temp = Matrix[low][i];
        Matrix[low][i] = Matrix[high][i];
        Matrix[high][i] = temp;
        low++;
        high--;
      }
    }
  }

  public void rotate() {
    int[][] rotatedShape = transposeMatrix(shape);
    reverseRows(rotatedShape);

    int xCord = x / size;
    int yCord = y / size;
    // check if rotation is possible
    if ((xCord + rotatedShape[0].length > BOARD_WIDTH) || (yCord + rotatedShape.length > BOARD_HEIGHT)) {
      return;
    }

    // check if rotation colides with other pieces
    for (int row = 0; row < rotatedShape.length; row++) {
      for (int col = 0; col < rotatedShape[row].length; col++) {
        if ((rotatedShape[row][col] != 0) && (gameBoard.getBoard()[yCord + row][xCord + col] != Color.black)) {
            return;
        }
      }
    }

    shape = rotatedShape;
  }

  private void move(int dir) {
    switch (dir) {

      case (NONE):
        break;

      case (RIGHT):
        if (!checkHorizontalColision(RIGHT))
          x += size;
        break;

      case (DOWN):
        colision = checkVerticalColision();
        if (!colision)
          y += size;
        break;

      case (LEFT):
        if (!checkHorizontalColision(LEFT))
          x -= size;
        break;

      default:
        break;
    }
  }

  // * move tick, move delay and move speed are used to control the speed of
  // * the tetromino move tick will be incremented every time the update method
  // * is called, this means that the moveTick will count at the same rate as the
  // * UPS. move delay is the number of ticks that must pass before the tetromino
  // * moves down one square. This is set to the UPS so that the tetromino moves
  // * down one square every second, by default. By varying the move speed we
  // * can change the number of ticks that must pass before the tetromino moves.
  public void update() {
    if (colision) {
      gameBoard.freezePieceOnBoard(this);
      initTetromino();
      return;
    }

    verticalMoveTick++;
    horizontalMoveTick++;

    if (horizontalMoveTick * horizontalMoveSpeed >= moveDelay) {
      horizontalMoveTick = 0;

      if (left && !right) {
        move(LEFT);
      } else if (right && !left) {
        move(RIGHT);
      }
    }

    if (drop)
      verticalMoveSpeed = INTANT_DROP_SPEED;
    else if (down && verticalMoveSpeed != INTANT_DROP_SPEED)
      verticalMoveSpeed = FAST_MOVE_SPEED;
    else if (verticalMoveSpeed != INTANT_DROP_SPEED)
      verticalMoveSpeed = DEFAULT_MOVE_SPEED;

    if (verticalMoveTick * verticalMoveSpeed >= moveDelay) {
      verticalMoveTick = 0;
      move(DOWN);
    }
  }

  public void render(Graphics g) {
    for (int row = 0; row < shape.length; row++) {
      for (int col = 0; col < shape[row].length; col++) {
        if (shape[row][col] == 1) {
          // draw the tetromino colors
          g.setColor(color);
          g.fillRect(x + col * size, y + row * size, size, size);
          // draw the tetromino outline
          g.setColor(Color.GRAY);
          g.drawRect(x + col * size, y + row * size, size, size);
        }
      }
    }
  }

  // * GETTERS AND SETTERS
  public int getX() {
    return x;
  }

  public int getY() {
    return y;
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

  public Color getColor() {
    return color;
  }

  public int[][] getShape() {
    return shape;
  }

}
