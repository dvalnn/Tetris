package gameElements;

import java.awt.Color;
import java.awt.Graphics;

// GamePanel is a JPanel -- a container for all visual elements in the game
public class Board {
    private int squareSize;
    private int width, height;

    private int x0, y0;

    private int[][] grid = new int[height][width];
    private Color bkgColor = Color.BLACK;

    private Tetromino tetromino;
    // private Tetromino nextTetromino;

    public Board(int squareSize, int width, int height, int offsetX, int offsetY, Color bkgColor) {
        this.squareSize = squareSize;
        this.width = width;
        this.height = height;
        this.x0 = offsetX;
        this.y0 = offsetY;
        this.bkgColor = bkgColor;

        int tetroX = (offsetX + width) / 2 * squareSize;

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
        g.fillRect(x0, y0, width * squareSize, height * squareSize);

        // draws the tetromino
        tetromino.render(g);

        // draws the grid
        for (int row = 0; row < height; row++) {
            g.setColor(Color.WHITE);
            g.drawLine(x0, row * squareSize, width * squareSize, row * squareSize);
        }
        for (int col = 0; col < width; col++) {
            g.setColor(Color.WHITE);
            g.drawLine(col * squareSize, y0, col * squareSize, height * squareSize);
        }

    }
}