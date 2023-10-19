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
    private int selectedShape;
    private int[][] shape;
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

    private int board_pixel_width;
    private int board_pixel_height;

    public Tetromino(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        selectedShape = rand.nextInt(SHAPES.length);
        shape = SHAPES[selectedShape];
        color = new Color(
                COLORS[selectedShape][0][0],
                COLORS[selectedShape][0][1],
                COLORS[selectedShape][0][2]);

        board_pixel_width = BOARD_WIDTH * size;
        board_pixel_height = BOARD_HEIGHT * size;
    }

    public void move(int dir) {
        switch (dir) {
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
                break;

            case (LEFT):
                if (!(x - size < 0))
                    x -= size;
                break;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void fastDrop() {
        moveSpeed = 100;
    }

    public void update() {
        moveTick++;
        if (moveTick * moveSpeed >= moveDelay) {
            moveTick = 0;
            if (y + size < board_pixel_height) {
                move(DOWN);
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
