package gameElements;

import java.awt.Color;
import java.awt.Graphics;

import static utils.Constants.GameConstants.*;

// GamePanel is a JPanel -- a container for all visual elements in the game
public class Board {
    private int squareSize;
    private int x0, y0;

    // private int[][] grid = new int[BOARD_WIDTH][BOARD_WIDTH];
    private Color bkgColor = Color.BLACK;

    private Tetromino tetromino;
    // private Tetromino nextTetromino;

    public Board(int squareSize, int offsetX, int offsetY, Color bkgColor) {
        this.squareSize = squareSize;

        this.x0 = offsetX;
        this.y0 = offsetY;
        this.bkgColor = bkgColor;

        int tetroX = (offsetX + BOARD_WIDTH) / 2 * squareSize;

        // squareSize is the size of each square in the tetromino
        // tetroX is the initial x position of the tetromino
        // offsetY is the initial y position of the tetromino
        // subtracting squareSize from tetroX centers the tetromino
        this.tetromino = new Tetromino(tetroX - squareSize, offsetY, squareSize);
    }

    public void update() {
        tetromino.update();
    }

    public void render(Graphics g) {
        // draws the background
        g.setColor(bkgColor);
        g.fillRect(x0, y0, BOARD_WIDTH * squareSize, BOARD_HEIGHT * squareSize);

        // draws the tetromino
        tetromino.render(g);

        // draws the grid
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            g.setColor(Color.WHITE);
            g.drawLine(x0, row * squareSize, BOARD_WIDTH * squareSize, row * squareSize);
        }
        for (int col = 0; col < BOARD_WIDTH; col++) {
            g.setColor(Color.WHITE);
            g.drawLine(col * squareSize, y0, col * squareSize, BOARD_HEIGHT * squareSize);
        }

    }
}