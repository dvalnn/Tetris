package com.psw.tetris.gameElements;

import static com.psw.tetris.utils.Constants.GameConstants.BOARD_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.BOARD_WIDTH;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.psw.tetris.settings.BoardSettings;

public class Board {

  public class BoardLine {
    private final List<Color> colors;
    private boolean recentlyChanged = false;

    public BoardLine() {
      colors = new ArrayList<Color>(BOARD_WIDTH);
      for (int i = 0; i < BOARD_WIDTH; i++) {
        colors.add(set.backgroundColor);
      }
      recentlyChanged = true;
    }

    public void setColor(final int index, final Color color) {
      setColor(index, color.getRGB());
    }

    public void setColor(final int index, final int rgb) {
      colors.set(index, new Color(rgb));
      recentlyChanged = true;
    }

    public int getIndexRGB(final int index) {
      return colors.get(index).getRGB();
    }

    public Color getIndexColorCopy(final int index) {
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
      final List<Color> copy = new ArrayList<Color>(colors.size());
      for (final Color color : colors) {
        copy.add(new Color(color.getRGB()));
      }
      return copy;
    }
  }

  protected List<BoardLine> board;

  protected final BoardSettings set;

  public Board(BoardSettings settings) {
    this.set = settings;

    // init the board and board lines
    // board lines are created with
    // recentlyChanged = true
    // and with backgroundColor
    board = new ArrayList<BoardLine>(BOARD_HEIGHT);
    for (int i = 0; i < BOARD_HEIGHT; i++) {
      board.add(new BoardLine());
    }
  }

  public void render(final Graphics g) {

    for (int row = 0; row < BOARD_HEIGHT; row++) {
      for (int col = 0; col < BOARD_WIDTH; col++) {
        g.setColor(board.get(row).getIndexColorCopy(col));
        g.fillRect(
            (int) (col * set.squareSize - set.squareSize / 2) + (int) set.xOffset,
            (int) (row * set.squareSize - set.squareSize / 2) + (int) set.yOffset,
            set.squareSize,
            set.squareSize);
        g.setColor(set.gridColor);
        g.drawRect(
            (int) (col * set.squareSize - set.squareSize / 2) + (int) set.xOffset,
            (int) (row * set.squareSize - set.squareSize / 2) + (int) set.yOffset,
            set.squareSize,
            set.squareSize);
      }
    }
  }

  public List<BoardLine> getBoard() {
    return board;
  }

  public Color getBackgroundColor() {
    return set.backgroundColor;
  }
}
