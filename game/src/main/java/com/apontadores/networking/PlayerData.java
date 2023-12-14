package com.apontadores.networking;

import static com.apontadores.utils.Constants.GameConstants.BOARD_HEIGHT;
import static com.apontadores.utils.Constants.GameConstants.BOARD_SQUARE;
import static com.apontadores.utils.Constants.GameConstants.BOARD_WIDTH;
import static com.apontadores.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.apontadores.utils.Constants.GameConstants.GAME_WIDTH;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.StringJoiner;

import com.apontadores.gameElements.boards.MPBoard;
import com.apontadores.gameElements.boards.PlayerBoard;
import com.apontadores.gameElements.shapes.Shape;
import com.apontadores.gameElements.shapes.ShapeMP;
import com.apontadores.packets.Packet100Update;
import com.apontadores.settings.BoardSettings;

public class PlayerData {

  public static final int OPPONENT_X_OFFSET = 3 * GAME_WIDTH / 4 - BOARD_WIDTH * BOARD_SQUARE / 2 + 10;

  public static final int Y_OFFSET = GAME_HEIGHT / 2 - BOARD_HEIGHT * BOARD_SQUARE / 2 + 18;

  private String playerName;

  private String opponentName;

  // NOTE: MP elements have getters methods and are updated by the client
  // with packets received from the server
  // the getters are used by the PlayingMP class to display the
  // opponent's board, score, etc. on the screen

  MPBoard opponentBoard;

  private final ShapeMP opponentShape;

  private int opponentScore;

  private int opponentLevel;

  // private ShapeMP holdShapeMP;
  //
  // public ShapeMP getHoldShapeMP() {
  // return holdShapeMP;
  // }
  //
  // private ShapeMP[] nextShapesMP;
  //
  // public ShapeMP[] getNextShapesMP() {
  // return nextShapesMP;
  // }

  PlayerBoard playerBoard;

  private Shape currentShape;

  private int score;

  private int level;

  // NOTE: Player elements have setters and are updated by the local game logic
  // the client then sends packets to the server with the local data to be
  // relayed to the opponent's cient and displayed on the screen

  private int linesCleared;

  private boolean syncShape;

  private boolean syncScore;
  // private boolean syncElements; // TODO: Implement this

  public PlayerData() {

    final Color boardColor = new Color(20, 20, 20);

    // Initializing the opponent player board and shapes
    opponentBoard = new MPBoard(
        new BoardSettings(
            BOARD_SQUARE,
            OPPONENT_X_OFFSET,
            Y_OFFSET,
            boardColor,
            boardColor.brighter()));

    opponentShape = new ShapeMP(BOARD_SQUARE, OPPONENT_X_OFFSET, Y_OFFSET);
    // holdShapeMP = new ShapeMP(BOARD_SQUARE, OPPONENT_X_OFFSET, Y_OFFSET);
    //
    // nextShapesMP = new ShapeMP[6];
    // nextShapesMP[0] = new ShapeMP(BOARD_SQUARE, OPPONENT_X_OFFSET, Y_OFFSET);
    // nextShapesMP[1] = new ShapeMP(BOARD_SQUARE, OPPONENT_X_OFFSET, Y_OFFSET);
    // nextShapesMP[2] = new ShapeMP(BOARD_SQUARE, OPPONENT_X_OFFSET, Y_OFFSET);
    // nextShapesMP[3] = new ShapeMP(BOARD_SQUARE, OPPONENT_X_OFFSET, Y_OFFSET);
    // nextShapesMP[4] = new ShapeMP(BOARD_SQUARE, OPPONENT_X_OFFSET, Y_OFFSET);
    // nextShapesMP[5] = new ShapeMP(BOARD_SQUARE, OPPONENT_X_OFFSET, Y_OFFSET);
  }

  public String getOpponentName() {
    return opponentName;
  }

  // TODO: Implement this

  // private Shape holdShape;
  //
  // public PlayerData setHoldShape(Shape holdShape) {
  // this.holdShape = holdShape;
  // syncElements = true;
  // return this;
  // }

  // TODO: Implement this
  //
  // private Shape[] nextShapes;

  // public PlayerData setNextShapes(Shape[] nextShapes) {
  // this.nextShapes = nextShapes;
  // syncElements = true;
  // return this;
  // }

  public PlayerData setOpponentName(final String opponentName) {
    this.opponentName = opponentName;
    return this;
  }

  public MPBoard getOpponentBoard() {
    return opponentBoard;
  }

  public ShapeMP getOpponentShape() {
    return opponentShape;
  }

  public int getOpponentScore() {
    return opponentScore;
  }

  public int getOpponentLevel() {
    return opponentLevel;
  }

  public PlayerData setPlayerBoard(final PlayerBoard playerBoard) {
    this.playerBoard = playerBoard;
    return this;
  }

  // NOTE: the Playerboard has a getter because the client need to iterate
  // over its lines in order to feed their data into the getBoardUpdate method
  public PlayerBoard getPlayerBoard() {
    return playerBoard;
  }

  public PlayerData setCurrentShape(final Shape currentShape) {
    this.currentShape = currentShape;
    syncShape = true;
    return this;
  }

  public PlayerData setScore(final int score) {
    this.score = score;
    syncScore = true;
    return this;
  }

  public void setLevel(final int level) {
    this.level = level;
    syncScore = true;
  }

  public PlayerData setLinesCleared(final int linesCleared) {
    this.linesCleared = linesCleared;
    syncScore = true;
    return this;
  }

  @Override
  public String toString() {
    return "PlayerData {" +
        "username='" + playerName + '\'' +
        ", score=" + score +
        ", level=" + level +
        ", linesCleared=" + linesCleared +
        '}';
  }

  public void parsePacket(final Packet100Update inPacket) {
    switch (inPacket.getUpdateType()) {
      case "tetromino" -> parseTetrominoUpdate(inPacket.getUpdateData());
      case "board" -> parseBoardUpdate(inPacket.getUpdateData());
      case "score" -> parseScoreUpdate(inPacket.getUpdateData());
      default -> {
      }
    }
  }

  public void parseScoreUpdate(final String updateData) {
    final String[] tokens = updateData.split(";");
    opponentScore = Integer.parseInt(tokens[0]);
    final int opponentLinesCleared = Integer.parseInt(tokens[1]);
    opponentLevel = Integer.parseInt(tokens[2]);
  }

  public void parseBoardUpdate(final String updateData) {
    final String[] tokens = updateData.split(";");

    final int row = Integer.parseInt(tokens[0]);
    final int numColors = Integer.parseInt(tokens[1]);

    final Color[] colors = new Color[numColors];
    for (int i = 0; i < numColors; i++) {
      colors[i] = new Color(Integer.parseInt(tokens[i + 2]));
    }

    opponentBoard.update(row, colors);
  }

  public Packet100Update getShapeUpdate() {
    if (!syncShape || opponentShape == null) {
      return null;
    }

    syncShape = false;

    final StringJoiner joiner = new StringJoiner(";");
    final Point2D[] points = currentShape.getPoints();
    final Color color = currentShape.getColor();

    joiner.add(String.valueOf(color.getRGB()));
    joiner.add(String.valueOf(points.length));

    for (final Point2D point : points) {
      joiner.add(String.valueOf(point.getX()));
      joiner.add(String.valueOf(point.getY()));
    }

    return new Packet100Update("tetromino", joiner.toString());
  }

  public Packet100Update getScoreUpdate() {
    if (!syncScore) {
      return null;
    }

    syncScore = false;

    final StringJoiner joiner = new StringJoiner(";");
    joiner.add(String.valueOf(score))
        .add(String.valueOf(linesCleared))
        .add(String.valueOf(level));

    return new Packet100Update("score", joiner.toString());
  }

  public Packet100Update getBoardUpdate(final int row, final List<Color> colors) {
    final StringJoiner joiner = new StringJoiner(";");
    joiner.add(String.valueOf(row));
    joiner.add(String.valueOf(colors.size()));
    for (final Color color : colors) {
      joiner.add(String.valueOf(color.getRGB()));
    }

    return new Packet100Update("board", joiner.toString());
  }

  private void parseTetrominoUpdate(final String data) {
    final String[] tokens = data.split(";");
    final Color color = new Color(Integer.parseInt(tokens[0]));
    final int numPoints = Integer.parseInt(tokens[1]);
    final Point2D[] points = new Point2D[numPoints];

    for (int i = 0; i < numPoints; i++) {
      final double x = Double.parseDouble(tokens[i * 2 + 2]);
      final double y = Double.parseDouble(tokens[i * 2 + 3]);
      points[i] = new Point2D.Double(x, y);
    }

    opponentShape.update(points, color);
  }
}
