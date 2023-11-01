package gameElements;

import static utils.Constants.GameConstants.*;

import gameStates.GameState;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

// GamePanel is a JPanel -- a container for all visual elements in the game
public class PlayerBoard {

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

  private List<BoardLine> board;
  private Color backgroundColor;
  private Color gridColor;
  private Point2D renderOrigin;
  private int renderSize;

  private Tetromino tetro1;
  private Tetromino tetro2;

  private boolean debugMode = false;
  private boolean paused = false;

  public PlayerBoard(int size, int xOffset, int yOffset, Color color) {
    this.renderSize = size;
    renderOrigin = new Point(xOffset, yOffset);

    backgroundColor = new Color(color.getRGB());
    gridColor = new Color(backgroundColor.brighter().getRGB());

    tetro1 = new Tetromino(size, renderOrigin, this);
    tetro2 = new Tetromino(size, renderOrigin, this);

    // init the board and board lines
    // board lines are created with
    // recentlyChanged = true
    // and with backgroundColor
    board = new ArrayList<BoardLine>(BOARD_HEIGHT);
    for (int i = 0; i < BOARD_HEIGHT; i++) {
      board.add(new BoardLine());
    }
  }

  private void addTetrominoToPile() {
    for (Point2D point : tetro1.getShape().getPoints()) {
      int row = (int) point.getY();
      int col = (int) point.getX();
      board.get(row).setColor(col, tetro1.getShape().getColor());
    }
    if (tetro1.getShape().getMinY() <= 0) {
      System.out.println("[Board] Game Over!");
      GameState.state = GameState.GAME_OVER;
    }
  }

  private void clearRow(int row) {
    for (int col = 0; col < BOARD_WIDTH; col++) {
      board.get(row).setColor(col, backgroundColor);
    }
  }

  private void shiftRowsDown(int row) {
    for (int r = row; r > 0; r--) {
      for (int col = 0; col < BOARD_WIDTH; col++) {
        board.get(r).setColor(col, board.get(r - 1).getIndexRGB(col));
      }
    }
  }

  private void checkRows() {
    int rowsCleared = 0;

    for (int row = 0; row < BOARD_HEIGHT; row++) {
      boolean full = true;
      for (int col = 0; col < BOARD_WIDTH; col++) {
        if (board.get(row).getIndexRGB(col) == backgroundColor.getRGB()) {
          full = false;
          break;
        }
      }
      if (full) {
        rowsCleared++;
        clearRow(row);
        shiftRowsDown(row);
      }
    }

    System.out.println("[Board] Cleared " + rowsCleared + " rows!");
  }

  // NOTE: This method is only used for debugging purposes
  public void addBlockOnMousePosition(int x, int y) {
    if (!debugMode) return;

    toggleBlockOnMousePosition(x, y, true);
  }

  // NOTE: This method is only used for debugging purposes
  public void removeBlockOnMousePosition(int x, int y) {
    if (!debugMode) return;

    toggleBlockOnMousePosition(x, y, false);
  }

  // NOTE: This method is only used for debugging purposes
  // it is not used in the actual game.
  // PERF: This method is very inefficient. It iterates over
  // the entire board every time the mouse is clicked.
  private void toggleBlockOnMousePosition(int x, int y, boolean add) {

    // NOTE: for each grid square, expand it fmom board coordinates
    // to screen coordinates and check if the mouse is in it
    // if it is, toggle the block.
    // TODO: Make this more efficient somehow.
    // Performance is not an issue right now, but
    // it might be in the future
    for (int row = 0; row < BOARD_HEIGHT; row++) {
      for (int col = 0; col < BOARD_WIDTH; col++) {
        int x1 = (int) (col * renderSize - renderSize / 2) + (int) renderOrigin.getX();
        int y1 = (int) (row * renderSize - renderSize / 2) + (int) renderOrigin.getY();
        int x2 = x1 + renderSize;
        int y2 = y1 + renderSize;

        if (x >= x1 && x <= x2 && y >= y1 && y <= y2) {
          if (add) {
            board.get(row).setColor(col, Color.PINK);
          } else {
            board.get(row).setColor(col, backgroundColor);
          }
          return;
        }
      }
    }
  }

  // NOTE: This method is only used for debugging purposes
  public void setTetromino(int tetroID) {
    if (!debugMode) return;
    tetro1 = new Tetromino(renderSize, renderOrigin, this, tetroID);
  }

  // NOTE: This method is only used for debugging purposes
  public void reset() {
    if (!debugMode) return;

    for (int row = 0; row < BOARD_HEIGHT; row++) {
      for (int col = 0; col < BOARD_WIDTH; col++) {
        board.get(row).setColor(col, backgroundColor);
      }
    }
    tetro1 = new Tetromino(renderSize, renderOrigin, this);
    tetro2 = new Tetromino(renderSize, renderOrigin, this);
  }

  public void update() {
    // NOTE: This is only used for debugging purposes
    // TODO: Remove this
    if (paused) return;
    tetro1.update();
    if (!tetro1.isActive()) {
      addTetrominoToPile();
      checkRows();
      tetro1 = tetro2;
      tetro2 = new Tetromino(renderSize, renderOrigin, this);
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

    tetro1.render(g);

    // NOTE: This is only used for debugging purposes
    // TODO: Remove this
    if (debugMode) {
      g.setColor(Color.RED);
      g.drawString("Debug Mode", 10, 10);
    }
    if (paused) {
      g.setColor(Color.RED);
      g.drawString("Paused", 10, 30);
    }
  }

  // TODO: Obscure the debug mode activation
  // Maybe make it so that the user has
  // to press a certain key combination
  public void toggleDebugMode() {
    debugMode = !debugMode;
  }

  public void togglePause() {
    paused = !paused;
  }

  public Tetromino getTetromino() {
    return tetro1;
  }

  public List<BoardLine> getBoard() {
    return board;
  }

  public Color getBackgroundColor() {
    return backgroundColor;
  }
}
