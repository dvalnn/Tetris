package gameElements;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import utils.Constants.GameStates;

import static utils.Constants.GameConstants.*;
import static utils.Constants.TetrominoConstants.*;
import static utils.Constants.Directions.*;

public class Tetromino {
  private int x;
  private int y;

  private int ghostX;
  private int ghostY;

  private int xSpawn;
  private int ySpawn;

  private int size;

  private Random rand = new Random();

  // 2D array representing the shape of the tetromino
  private int[][] shape;
  private Color color;
  private Color ghostColor;
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

    this.gameBoard = gameBoard;

    initTetromino();

    board_pixel_width = BOARD_WIDTH * size;
    board_pixel_height = BOARD_HEIGHT * size;

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

    // same color as the tetromino but with 100 alpha
    // ghostColor = new Color(
    // COLORS[shapeIndex][0][0],
    // COLORS[shapeIndex][0][1],
    // COLORS[shapeIndex][0][2],
    // 50);

    ghostColor = new Color(127, 127, 127);

    drop = false;
    colision = false;

    if (shape != O_SHAPE && shape != I_SHAPE) {
      int rotations = rand.nextInt(4);
      for (int i = 0; i < rotations; i++) {
        rotate(RIGHT);
      }
    }
  }

  private boolean checkVerticalColision(int xCord, int yCord) {
    if ((yCord + size * (shape.length + 1) > board_pixel_height))
      return true;

    for (int row = 0; row < shape.length; row++) {
      for (int col = 0; col < shape[row].length; col++) {
        if (shape[row][col] == 1
            && gameBoard.getBoard()[yCord / size + row + 1][xCord / size + col] != Color.BLACK)
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
        if (shape[row][col] == 1 && gameBoard.getBoard()[y / size + row][x / size + col + xDelta] != Color.BLACK)
          return true;
      }
    }

    return false;
  }

  public void rotate(int dir) {
    int[][] rotated;
    switch (dir) {
      case LEFT:
        rotated = rotateCounterClockwise();
        break;

      case RIGHT:
        rotated = rotateClockwise();
        break;

      default:
        return;
    }

    int xCord = x / size;
    int yCord = y / size;
    // check if rotation is possible
    if ((xCord + rotated[0].length > BOARD_WIDTH) || (yCord +
        rotated.length > BOARD_HEIGHT)) {
      return;
    }

    // check if rotation colides with other pieces
    for (int row = 0; row < rotated.length; row++) {
      for (int col = 0; col < rotated[row].length; col++) {
        if ((rotated[row][col] != 0) && (gameBoard.getBoard()[yCord + row][xCord
            + col] != Color.black)) {
          return;
        }
      }
    }

    shape = rotated;
  }

  public int[][] rotateClockwise() {
    int rows = shape.length;
    int cols = shape[0].length;
    int[][] rotated = new int[cols][rows];

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        rotated[j][rows - 1 - i] = shape[i][j];
      }
    }

    return rotated;
  }

  public int[][] rotateCounterClockwise() {
    int rows = shape.length;
    int cols = shape[0].length;
    int[][] rotated = new int[cols][rows];

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        rotated[cols - 1 - j][i] = shape[i][j];
      }
    }

    return rotated;
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
        colision = checkVerticalColision(x, y);
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

  // calculate the ghost piece position
  public void calculateGhostPiece() {
    ghostX = x;
    ghostY = y;

    // first check on the current position
    boolean ghostColision = checkVerticalColision(ghostX, ghostY);

    while (!ghostColision) {
      ghostY += size;
      ghostColision = checkVerticalColision(ghostX, ghostY);
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

      if (y <= 0) {
        GameStates.gameState = GameStates.GAME_OVER;
        return;
      }

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

    calculateGhostPiece();
  }

  public void render(Graphics g) {
    if (GameStates.gameState == GameStates.GAME_OVER)
      return;

    for (int row = 0; row < shape.length; row++) {
      for (int col = 0; col < shape[row].length; col++) {
        if (shape[row][col] == 1) {
          // draw the tetromino block
          g.setColor(color);
          g.fillRect(x + col * size, y + row * size, size, size);
          // draw the tetromino block border
          g.setColor(Color.GRAY);
          g.drawRect(x + col * size, y + row * size, size, size);

          if (ghostX == x && ghostY == y)
            continue;

          // draw the ghost piece
          g.setColor(ghostColor);
          g.fillRect(ghostX + col * size, ghostY + row * size, size, size);
          // draw the ghost piece border
          g.setColor(Color.GRAY);
          g.drawRect(ghostX + col * size, ghostY + row * size, size, size);

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
