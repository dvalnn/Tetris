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

  private int size;
  private Color color = new Color(15, 18, 23);

  public Board(int size, int xOffset, int yOffset, Color color) {
    this.size = size;
    this.board = new Color[size][size];

    Point2D spawnPoint = new Point2D.Double(200, 200);

    this.tetromino = new Tetromino(size, size, spawnPoint);
  }

}
