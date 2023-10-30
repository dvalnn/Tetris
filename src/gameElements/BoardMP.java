package gameElements;

import static utils.Constants.GameConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.net.InetAddress;

public class BoardMP {
  public Color[][] board;
  private Color backgroundColor;
  private Color gridColor;
  private Point2D renderOrigin;
  private int renderSize;
  public InetAddress ipAddress;
  public int port;
  private String username;

  public BoardMP(int size, int xOffset, int yOffset, Color color,
                 InetAddress ipAddress, int port, String username) {

    this.board = new Color[BOARD_HEIGHT][BOARD_WIDTH];
    this.renderSize = size;
    this.renderOrigin = new Point2D.Double(xOffset, yOffset);
    this.backgroundColor = color;
    this.gridColor = color.brighter();
    this.ipAddress = ipAddress;
    this.port = port;

    for (int row = 0; row < BOARD_HEIGHT; row++) {
      for (int col = 0; col < BOARD_WIDTH; col++) {
        board[row][col] = backgroundColor;
      }
    }
  }

  public void update() {}

  public void render(Graphics g) {
    for (int row = 0; row < BOARD_HEIGHT; row++) {
      for (int col = 0; col < BOARD_WIDTH; col++) {
        g.setColor(board[row][col]);
        g.fillRect((int)renderOrigin.getX() + col * renderSize,
                   (int)renderOrigin.getY() + row * renderSize, renderSize,
                   renderSize);
        g.setColor(gridColor);
        g.drawRect((int)renderOrigin.getX() + col * renderSize,
                   (int)renderOrigin.getY() + row * renderSize, renderSize,
                   renderSize);
      }
    }
  }

  public String getUsername() { return username; }
}
