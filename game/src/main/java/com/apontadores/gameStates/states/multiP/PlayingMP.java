package com.apontadores.gameStates.states.multiP;

import static com.apontadores.utils.Constants.RESOURCES_PATH;
import static com.apontadores.utils.Constants.Directions.LEFT;
import static com.apontadores.utils.Constants.Directions.RIGHT;
import static com.apontadores.utils.Constants.GameConstants.BOARD_HEIGHT;
import static com.apontadores.utils.Constants.GameConstants.BOARD_SQUARE;
import static com.apontadores.utils.Constants.GameConstants.BOARD_WIDTH;
import static com.apontadores.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.apontadores.utils.Constants.GameConstants.GAME_WIDTH;
import static com.apontadores.utils.Constants.GameConstants.UPS_SET;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.StringJoiner;

import com.apontadores.gameElements.Board;
import com.apontadores.gameElements.boards.MPBoard;
import com.apontadores.gameElements.boards.PlayerBoard;
import com.apontadores.gameElements.gameplay.Levels;
import com.apontadores.gameElements.gameplay.Score;
import com.apontadores.gameElements.shapes.Shape;
import com.apontadores.gameElements.shapes.ShapeMP;
import com.apontadores.gameStates.GameState;
import com.apontadores.gameStates.GameStateHandler;
import com.apontadores.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.main.Game;
import com.apontadores.packets.PlayerUpdatePacket;
import com.apontadores.settings.BoardSettings;
import com.apontadores.ui.Frame;

public class PlayingMP extends GameState {

  Color boardColor = new Color(20, 20, 20);
  PlayerBoard playerBoard;
  MPBoard opponentBoard;
  private ShapeMP shapeMP;
  private boolean matchOver = false;
  private boolean opponentDisconnected = false;

  private int networkTick = 0;
  private final int NETWORK_TICK_MAX = 5;
  private int fullSyncTick = 0;
  private final int FULL_SYNC_TICK_MAX = 2 * UPS_SET;

  // calculate the offsets so that the boards are centered
  // and the player's board is on the left and the opponent's
  private final int PLAYER_X_OFFSET = GAME_WIDTH / 4 - BOARD_WIDTH * BOARD_SQUARE / 2 + 13;
  private final int OPPONENT_X_OFFSET = 3 * GAME_WIDTH / 4 - BOARD_WIDTH * BOARD_SQUARE / 2 + 10;
  private final int Y_OFFSET = GAME_HEIGHT / 2 - BOARD_HEIGHT * BOARD_SQUARE / 2 + 18;

  private Frame frame;

  public PlayingMP() {
    super(GameStatesEnum.PLAYING_MP);

    playerBoard = new PlayerBoard(
        new BoardSettings(
            BOARD_SQUARE,
            PLAYER_X_OFFSET,
            Y_OFFSET,
            boardColor,
            boardColor.brighter()));

    frame = Frame.loadFromJson(RESOURCES_PATH + "/frames/multiplayer.json");
  }

  private void addBoardMP() {
    opponentBoard = new MPBoard(
        new BoardSettings(
            BOARD_SQUARE,
            OPPONENT_X_OFFSET,
            Y_OFFSET,
            boardColor,
            boardColor.brighter()));

    shapeMP = new ShapeMP(BOARD_SQUARE, OPPONENT_X_OFFSET, Y_OFFSET);
  }

  public void removeBoardMP(final String username) {
    System.out.println("[PlayingMP] " + username + " disconnected!");
    opponentDisconnected = true;
    matchOver = true;
  }

  @Override
  public void update() {
    frame.update();

    if (opponentBoard == null) {
      addBoardMP();
    }

    PlayerUpdatePacket inPacket = null;
    while ((inPacket = Game.getClient().getUpdate()) != null) {
      parseUpdate(inPacket);
    }

    // if (opponentDisconnected) {
    // matchOver = true;
    // }

    // fullSyncTick++;
    // if (fullSyncTick >= FULL_SYNC_TICK_MAX) {
    // sendFullSync();
    // fullSyncTick = 0;
    // } else if (networkTick >= NETWORK_TICK_MAX) {
    // sendPlayerState();
    // networkTick = 0;
    // }

    networkTick++;
    if (networkTick >= NETWORK_TICK_MAX) {
      networkTick = 0;
      sendPlayerState();
    }

    playerBoard.update();
  }

  private void sendPlayerState() {
    PlayerUpdatePacket tetrominoUpdate = getTetrominoUpdate();
    Game.getClient().sendUpdate(tetrominoUpdate);

    for (int row = 0; row < BOARD_HEIGHT; row++) {
      final PlayerBoard.BoardLine line = playerBoard.getBoard().get(row);
      final List<Color> colors = line.getColorsCopyIfChanged();
      if (colors != null) {
        PlayerUpdatePacket boardUpdate = getBoardUpdate(row, colors);
        Game.getClient().sendUpdate(boardUpdate);
      }
    }

    PlayerUpdatePacket scoreUpdate = getScoreUpdate();
    Game.getClient().sendUpdate(scoreUpdate);
  }

  private PlayerUpdatePacket getScoreUpdate() {
    StringJoiner joiner = new StringJoiner(";");
    joiner.add(String.valueOf(Score.getScore()))
        .add(String.valueOf(Levels.getTotalLinesCleared()))
        .add(String.valueOf(Levels.getCurrentLevel()));

    return new PlayerUpdatePacket("score", joiner.toString());
  }

  private PlayerUpdatePacket getBoardUpdate(int row, List<Color> colors) {
    StringJoiner joiner = new StringJoiner(";");
    joiner.add(String.valueOf(row));
    joiner.add(String.valueOf(colors.size()));
    for (Color color : colors) {
      joiner.add(String.valueOf(color.getRGB()));
    }

    return new PlayerUpdatePacket("board", joiner.toString());
  }

  private PlayerUpdatePacket getTetrominoUpdate() {
    StringJoiner joiner = new StringJoiner(";");

    Shape shape = playerBoard.getTetromino().getShape();
    Point2D points[] = shape.getPoints();
    Color color = shape.getColor();

    joiner.add(String.valueOf(color.getRGB()));
    joiner.add(String.valueOf(points.length));
    for (Point2D point : points) {
      joiner.add(String.valueOf(point.getX()));
      joiner.add(String.valueOf(point.getY()));
    }

    return new PlayerUpdatePacket("tetromino", joiner.toString());
  }

  private void parseUpdate(PlayerUpdatePacket inPacket) {
    switch (inPacket.getUpdateType()) {
      case "tetromino":
        parseTetrominoUpdate(inPacket.getUpdateData());
        break;

      case "board":
        parseBoardUpdate(inPacket.getUpdateData());
        break;

      case "score":
        // parseScoreUpdate(inPacket.getUpdateData());
        break;

      default:
        break;
    }
  }

  private void parseScoreUpdate(String updateData) {
    String tokens = updateData.split(";")[0];
    int score = Integer.parseInt(tokens);
    int linesCleared = Integer.parseInt(tokens);
    int level = Integer.parseInt(tokens);

    // opponentBoard.setScore(score);
    // opponentBoard.setLinesCleared(linesCleared);
    // opponentBoard.setLevel(level);
  }

  private void parseBoardUpdate(String updateData) {
    String tokens[] = updateData.split(";");

    int row = Integer.parseInt(tokens[0]);
    int numColors = Integer.parseInt(tokens[1]);

    Color[] colors = new Color[numColors];
    for (int i = 0; i < numColors; i++) {
      colors[i] = new Color(Integer.parseInt(tokens[i + 2]));
    }

    opponentBoard.update(row, colors);
  }

  private void parseTetrominoUpdate(String string) {
    String tokens[] = string.split(";");
    Color color = new Color(Integer.parseInt(tokens[0]));
    int numPoints = Integer.parseInt(tokens[1]);
    Point2D points[] = new Point2D[numPoints];

    for (int i = 0; i < numPoints; i++) {
      double x = Double.parseDouble(tokens[i * 2 + 2]);
      double y = Double.parseDouble(tokens[i * 2 + 3]);
      points[i] = new Point2D.Double(x, y);
    }
    shapeMP.update(points, color);
  }

  @Override
  public void render(final Graphics g) {
    frame.render(g);
    // draw opponent board
    opponentBoard.render(g);
    // draw opponnent controled shape
    shapeMP.render(g);
    playerBoard.render(g);
  }

  @Override
  public void keyPressed(final KeyEvent e) {
    if (matchOver) {
      GameStateHandler.switchState(GameStatesEnum.GAME_OVER);
      return;
    }
    // same controls as playing.java
    switch (e.getKeyCode()) {
      case (KeyEvent.VK_Z):
        playerBoard.getTetromino().rotate(LEFT);
        break;

      case (KeyEvent.VK_X):
        playerBoard.getTetromino().rotate(RIGHT);
        break;

      case (KeyEvent.VK_LEFT):
        playerBoard.getTetromino().setLeft(true);
        break;

      case (KeyEvent.VK_DOWN):
        playerBoard.getTetromino().setDown(true);
        break;

      case (KeyEvent.VK_RIGHT):
        playerBoard.getTetromino().setRight(true);
        break;

      case (KeyEvent.VK_SPACE):
        playerBoard.getTetromino().setDrop(true);
        break;
    }
  }

  @Override
  public void keyReleased(final KeyEvent e) {
    switch (e.getKeyCode()) {
      case (KeyEvent.VK_LEFT):
        playerBoard.getTetromino().setLeft(false);
        break;

      case (KeyEvent.VK_DOWN):
        playerBoard.getTetromino().setDown(false);
        break;

      case (KeyEvent.VK_RIGHT):
        playerBoard.getTetromino().setRight(false);
        break;

      case (KeyEvent.VK_SPACE):
        playerBoard.getTetromino().setDrop(false);
        break;

      default:
        break;
    }
  }

  @Override
  public void windowLostFocus() {
  }

  public MPBoard getBoardMP() {
    return opponentBoard;
  }

  public ShapeMP getShapeMP() {
    return shapeMP;
  }

  public Board getPlayerBoard() {
    return playerBoard;
  }

  public Board getOpponentBoard() {
    return opponentBoard;
  }
}
