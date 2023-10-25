package gameElements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;

import static utils.Constants.GameConstants.*;
import static utils.Constants.TetrominoConstants.*;
import static utils.Constants.Directions.*;

import gameStates.GameState;

public class Tetromino {

  private Point position;
  private Point ghostPos;
  private Point boardOrigin;

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
  private final int INSTANT_DROP_SPEED = 5000;

  private int board_pixel_width;
  private int board_pixel_height;
  private boolean colision = false;

  private boolean right, left, down, drop;

  private Board board;

  public Tetromino(Board gameBoard, Point boardOrigin, int size) {
    this.boardOrigin = new Point(boardOrigin);

    this.size = size;
    this.board = gameBoard;

    initTetromino();

    board_pixel_width = BOARD_WIDTH * size;
    board_pixel_height = BOARD_HEIGHT * size;
  }

  private void initTetromino() {
    position = new Point(BOARD_WIDTH / 2 - 1, 0);
    ghostPos = new Point(position);

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

    // System.out.println("Tetromino initialized on position: " + position.x + ", " + position.y);
  }

  private boolean checkVerticalColision(Point point) {
    if ((size * (point.y + shape.length + 1) > board_pixel_height))
      return true;

    for (int row = 0; row < shape.length; row++) {
      for (int col = 0; col < shape[row].length; col++) {
        if (shape[row][col] == 1
            && board.getBoard()[point.y + row + 1][point.x + col] != board.getBackgroundColor())
          return true;
      }
    }
    return false;
  }

  private boolean checkHorizontalColision(Point point, int dir) {
    if (dir == RIGHT && size * (point.x + shape[0].length + 1) > board_pixel_width)
      return true;

    if (dir == LEFT && size * (point.x - 1) < 0)
      return true;

    int xDelta = dir == RIGHT ? 1 : -1;

    for (int row = 0; row < shape.length; row++) {
      for (int col = 0; col < shape[row].length; col++) {
        if (shape[row][col] == 1
            && board.getBoard()[point.y + row][point.x + col + xDelta] != board.getBackgroundColor())
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

    if ((position.x + rotated[0].length > BOARD_WIDTH) || (position.y +
        rotated.length > BOARD_HEIGHT)) {
      return;
    }

    // check if rotation colides with other pieces
    for (int row = 0; row < rotated.length; row++) {
      for (int col = 0; col < rotated[row].length; col++) {
        if ((rotated[row][col] != 0) && (board.getBoard()[position.y + row][position.x
            + col] != board.getBackgroundColor())) {
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
        if (!checkHorizontalColision(position, RIGHT))
          position.x += 1;
        break;

      case (DOWN):
        colision = checkVerticalColision(position);
        if (!colision)
          position.y += 1;
        break;

      case (LEFT):
        if (!checkHorizontalColision(position, LEFT))
          position.x -= 1;
        break;

      default:
        break;
    }
  }

  // calculate the ghost piece position
  public void calculateGhostPiece() {
    ghostPos = new Point(position);

    while (!checkVerticalColision(ghostPos)) {
      ghostPos.y += 1;
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

      // System.out.println("Tetromino colided on position: " + position.x + ", " + position.y);

      if (position.y <= 0) {
        GameState.state = GameState.GAME_OVER;
        return;
      }

      board.freezePieceOnBoard(this);
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
      verticalMoveSpeed = INSTANT_DROP_SPEED;
    else if (down && verticalMoveSpeed != INSTANT_DROP_SPEED)
      verticalMoveSpeed = FAST_MOVE_SPEED;
    else if (verticalMoveSpeed != INSTANT_DROP_SPEED)
      verticalMoveSpeed = DEFAULT_MOVE_SPEED;

    if (verticalMoveTick * verticalMoveSpeed >= moveDelay) {
      verticalMoveTick = 0;
      move(DOWN);
    }

    calculateGhostPiece();
  }

  public void render(Graphics g) {
    if (GameState.state == GameState.GAME_OVER)
      return;

    for (int row = 0; row < shape.length; row++) {
      for (int col = 0; col < shape[row].length; col++) {
        if (shape[row][col] == 1) {
          // draw the tetromino block
          g.setColor(color);
          g.fillRect((position.x + col) * size + boardOrigin.x, (position.y + row) * size + boardOrigin.y, size, size);
          // draw the tetromino block border
          g.setColor(Color.GRAY);
          g.drawRect((position.x + col) * size + boardOrigin.x, (position.y + row) * size + boardOrigin.y, size, size);

          if (ghostPos.equals(position))
            continue;

          // draw the ghost piece
          g.setColor(ghostColor);
          g.fillRect((ghostPos.x + col) * size + boardOrigin.x, (ghostPos.y + row) * size + boardOrigin.y, size, size);
          // draw the ghost piece border
          // g.setColor(Color.GRAY);
          // g.drawRect((ghostPos.x + col) * size + boardOrigin.x, (ghostPos.y + row) * size + boardOrigin.x, size, size);
        }
      }
    }
  }

  // * GETTERS AND SETTERS
  public int getX() {
    return position.x;
  }

  public int getY() {
    return position.y;
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
