package gameElements;

import static utils.Constants.GameConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Board {

  public class BoardLine {
    private List<Color> colors;
    private boolean recentlyChanged = false;

    public BoardLine() {
      colors = new ArrayList<Color>(BOARD_WIDTH);
      for (int i = 0; i < BOARD_WIDTH; i++) {
        colors.add(backgroundColor);
      }
      recentlyChanged = true;
    }

    public void setColor(int index, Color color) {
      setColor(index, color.getRGB());
    }

    public void setColor(int index, int rgb) {
      colors.set(index, new Color(rgb));
      recentlyChanged = true;
    }

    public int getIndexRGB(int index) {
      return colors.get(index).getRGB();
    }

    public Color getIndexColorCopy(int index) {
      return new Color(colors.get(index).getRGB());
    }

    public List<Color> getColorsCopyIfChanged() {
      if (recentlyChanged) {
        recentlyChanged = false;
        return getColorsCopy();
      }

      return null;
    }

    public List<Color> getColorsCopy() {
      List<Color> copy = new ArrayList<Color>(colors.size());
      for (Color color : colors) {
        copy.add(new Color(color.getRGB()));
      }
      return copy;
    }
  }

  protected List<BoardLine> board;

  protected Color backgroundColor;

  protected Color gridColor;

  protected Point2D renderOrigin;

  protected int renderSize;

  public Board(int size, int xOffset, int yOffset, Color color) {
    this.renderSize = size;
    renderOrigin = new Point2D.Double(xOffset, yOffset);

    backgroundColor = new Color(color.getRGB());
    gridColor = new Color(backgroundColor.brighter().getRGB());

    // init the board and board lines
    // board lines are created with
    // recentlyChanged = true
    // and with backgroundColor
    board = new ArrayList<BoardLine>(BOARD_HEIGHT);
    for (int i = 0; i < BOARD_HEIGHT; i++) {
      board.add(new BoardLine());
    }
  }

  public void render(Graphics g) {

    for (int row = 0; row < BOARD_HEIGHT; row++) {
      for (int col = 0; col < BOARD_WIDTH; col++) {
        g.setColor(board.get(row).getIndexColorCopy(col));
        g.fillRect(
            (int) (col * renderSize - renderSize / 2) + (int) renderOrigin.getX(),
            (int) (row * renderSize - renderSize / 2) + (int) renderOrigin.getY(),
            renderSize,
            renderSize);
        g.setColor(gridColor);
        g.drawRect(
            (int) (col * renderSize - renderSize / 2) + (int) renderOrigin.getX(),
            (int) (row * renderSize - renderSize / 2) + (int) renderOrigin.getY(),
            renderSize,
            renderSize);
      }
    }
  }

  public List<BoardLine> getBoard() {
    return board;
  }

  public Color getBackgroundColor() {
    return backgroundColor;
  }
}
