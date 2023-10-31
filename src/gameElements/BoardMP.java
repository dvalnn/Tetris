package gameElements;

import static utils.Constants.GameConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public class BoardMP {
  public Color[][] board;
  private Color backgroundColor;
  private Color gridColor;
  private Point2D renderOrigin;
  private int renderSize;
  private String username;

  public BoardMP(int size, int xOffset, int yOffset, Color color, String username) {

    this.board = new Color[BOARD_HEIGHT][BOARD_WIDTH];
    this.renderSize = size;
    this.renderOrigin = new Point2D.Double(xOffset, yOffset);
    this.backgroundColor = color;
    this.gridColor = color.brighter();
    this.username = username;

    for (int row = 0; row < BOARD_HEIGHT; row++) {
      for (int col = 0; col < BOARD_WIDTH; col++) {
        board[row][col] = backgroundColor;
      }
    }
  }

  public void updateBoard(int row, int col, Color color) {
    board[row][col] = new Color(color.getRGB());
  }

  public void render(Graphics g) {
    // print opponent's username
    g.setColor(Color.BLACK);
    g.drawString(username, (int) renderOrigin.getX(), (int) renderOrigin.getY() - 10);

    // draw the board
    for (int row = 0; row < BOARD_HEIGHT; row++) {
      for (int col = 0; col < BOARD_WIDTH; col++) {
        g.setColor(board[row][col]);
        g.fillRect(
            (int) renderOrigin.getX() + col * renderSize,
            (int) renderOrigin.getY() + row * renderSize,
            renderSize,
            renderSize);
        g.setColor(gridColor);
        g.drawRect(
            (int) renderOrigin.getX() + col * renderSize,
            (int) renderOrigin.getY() + row * renderSize,
            renderSize,
            renderSize);
      }
    }
  }

  public String getUsername() {
    return username;
  }
}
