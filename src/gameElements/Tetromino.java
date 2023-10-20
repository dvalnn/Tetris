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

    private int board_pixel_width;
    private int board_pixel_height;
    private boolean colision = false;

    private boolean up, right, left, down;

    public Tetromino(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        shapeIndex = rand.nextInt(SHAPES.length);
        shape = SHAPES[shapeIndex];
        color = new Color(
                COLORS[shapeIndex][0][0],
                COLORS[shapeIndex][0][1],
                COLORS[shapeIndex][0][2]);

        board_pixel_width = BOARD_WIDTH * size;
        board_pixel_height = BOARD_HEIGHT * size;
    }

    private void move(int dir) {
        switch (dir) {

            case (NONE):
                break;

            case (UP):
                break;

            case (RIGHT):
                if (!(x + size * (shape[0].length + 1) > board_pixel_width)) {
                    x += size;
                }
                break;

            case (DOWN):
                if (!(y + size * (shape.length + 1) > board_pixel_height))
                    y += size;
                else
                    colision = true;
                break;

            case (LEFT):
                if (!(x - size < 0))
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
        if (colision)
            return;

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

        if (verticalMoveTick * verticalMoveSpeed >= moveDelay) {
            verticalMoveTick = 0;
            if (down)
                verticalMoveSpeed = 100;
            move(DOWN);
        }
    }

    public void render(Graphics g) {
        g.setColor(color);
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] == 1) {
                    g.fillRect(x + col * size, y + row * size, size, size);
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

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

}
