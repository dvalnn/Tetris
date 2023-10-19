package gameElements;

import java.awt.Color;
import java.awt.Graphics;

import static utils.Constants.GameConstants.*;
import static utils.Constants.TetrominoConstants.*;

public class Tetromino {
    private int x;
    private int y;
    private int size;

    // 2D array representing the shape of the tetromino
    private int[][] shape = {
            { 0, 0, 0 },
            { 0, 1, 0 },
            { 1, 1, 1 }
    };

    private Color color;

    // * move tick, move delay and move speed are used to control the speed of
    // * the tetromino move tick will be incremented every time the update method
    // * is called, this means that the moveTick will count at the same rate as the
    // * UPS. move delay is the number of ticks that must pass before the tetromino
    // * moves down one square. This is set to the UPS so that the tetromino moves
    // * down one square every second, by default. By varying the move speed we
    // * can change the number of ticks that must pass before the tetromino moves.
    private int moveTick = 0;
    private int moveSpeed = 1;
    private final int moveDelay = UPS_SET;

    public Tetromino(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = new Color(T_COLOR[0][0], T_COLOR[0][1], T_COLOR[0][2]);
    }

    public void moveX(int x) {
        this.x += x;
    }

    public void moveY(int y) {
        this.y += y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void update() {
        moveTick++;
        if (moveTick >= moveDelay * moveSpeed) {
            moveTick = 0;
            if (y + 3 * size < BOARD_HEIGHT * size) {
                moveY(size);
            }
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

}
