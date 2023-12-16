package com.apontadores.gameElements;

import static com.apontadores.utils.Constants.GameConstants.BOARD_HEIGHT;
import static com.apontadores.utils.Constants.GameConstants.BOARD_WIDTH;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import com.apontadores.gameElements.gameplay.GameTime;
import com.apontadores.settings.BoardSettings;

public class Board {

  public class BoardLine {
    private final List<Color> colors;
    private boolean recentlyChanged;

    public BoardLine() {
      colors = new ArrayList<>(BOARD_WIDTH);
      for (int i = 0; i < BOARD_WIDTH; i++) {
        colors.add(set.backgroundColor);
      }
      recentlyChanged = true;
      getLineCounter = 0;
    }

    private int getLineCounter;
    private static final int GET_LINE_TRESHOLD = 1;

    public void setColor(final int index, final Color color) {
      setColor(index, color.getRGB());
    }

    public void setColor(final int index, final int rgb) {
      colors.set(index, new Color(rgb));
      recentlyChanged = true;
      getLineCounter = 0;
    }

    public int getIndexRGB(final int index) {
      return colors.get(index).getRGB();
    }

    public Color getIndexColorCopy(final int index) {
      return new Color(colors.get(index).getRGB());
    }

    public List<Color> getColorsCopyIfChanged() {
      if (recentlyChanged) {
        // recentlyChanged = false;
        getLineCounter++;
        if (getLineCounter > GET_LINE_TRESHOLD) {
          recentlyChanged = false;
          getLineCounter = 0;
        }

        return getColorsCopy();
      }

      return null;
    }

    public List<Color> getColorsCopy() {
      final List<Color> copy = new ArrayList<>(colors.size());
      for (final Color color : colors) {
        copy.add(new Color(color.getRGB()));
      }
      return copy;
    }

    public boolean isEmpty() {
      return colors.stream()
          .allMatch(color -> color.getRGB() == set.backgroundColor.getRGB());
    }
  }

  protected List<BoardLine> board;
  protected final BoardSettings set;

  protected int playerScore = 0;
  protected int playerLevel = 0;
  protected int playerLines = 0;
  protected String username;

  public Board(BoardSettings settings) {
    this.set = settings;

    // init the board and board lines
    // board lines are created with
    // recentlyChanged = true
    // and with backgroundColor
    board = new ArrayList<>(BOARD_HEIGHT);
    for (int i = 0; i < BOARD_HEIGHT; i++) {
      board.add(new BoardLine());
    }
  }

  public void render(final Graphics g) {
    for (int row = 0; row < BOARD_HEIGHT; row++) {
      for (int col = 0; col < BOARD_WIDTH; col++) {
        g.setColor(board.get(row).getIndexColorCopy(col));
        int x = (col * set.squareSize - set.squareSize / 2) + set.xOffset;
        int y = (row * set.squareSize - set.squareSize / 2) + set.yOffset;
        g.fillRect(x, y, set.squareSize, set.squareSize);
        g.setColor(set.gridColor);
        g.drawRect(x, y, set.squareSize, set.squareSize);
      }
    }

    // text elements rendering (score and level)
    Graphics2D g2 = (Graphics2D) g;
    g2.setColor(Color.WHITE);
    g2.setFont(g2.getFont().deriveFont(30f));

    g2.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

    // TODO: Center the text properly
    g2.drawString("" + playerScore, set.scoreRenderX, set.scoreRenderY);
    g2.drawString("" + playerLevel, set.levelRenderX, set.levelRenderY);
    g2.drawString("" + playerLines, set.linesRenderX, set.linesRenderY);

    g2.drawString(GameTime.getTimeStr(),
        set.timerRenderX,
        set.timerRenderY);

    if (username != null) {
      g2.setColor(Color.BLACK);
      g2.setFont(g2.getFont().deriveFont(20f));
      g2.drawString(username, set.nameRenderX, set.nameRenderY);
    }
  }

  public List<BoardLine> getBoard() {
    return board;
  }

  public Color getBackgroundColor() {
    return set.backgroundColor;
  }

}
