package gameElements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;

import static utils.Constants.GameConstants.*;

// GamePanel is a JPanel -- a container for all visual elements in the game
public class Board {
  private Tetromino tetromino;
  private Color[][] board;

  private Point2D origin;

  private int size;
  private Color backgroundColor;
  private Color gridColor;

  public Board(int size, int xOffset, int yOffset, Color color) {
    this.size = size;
    this.origin = new Point(xOffset, yOffset);
    this.board = new Color[size][size];
    this.backgroundColor = new Color(color.getRGB());
    this.gridColor = new Color(backgroundColor.brighter().getRGB());

    this.tetromino = new Tetromino(size, size, this.origin);
  }

  public void update() {
    tetromino.update();
  }

  public void render(Graphics g) {
    for (int row = 0; row < BOARD_HEIGHT; row++) {
      for (int col = 0; col < BOARD_WIDTH; col++) {
        g.setColor(backgroundColor);
        g.fillRect(
            (int) (col * size - size / 2) + (int) origin.getX(),
            (int) (row * size - size / 2) + (int) origin.getY(),
            size,
            size);
        g.setColor(gridColor);
        g.drawRect(
            (int) (col * size - size / 2) + (int) origin.getX(),
            (int) (row * size - size / 2) + (int) origin.getY(),
            size,
            size);
      }
    }

    tetromino.render(g);
  }

  public Tetromino getTetromino() {
    return tetromino;
  }

}
