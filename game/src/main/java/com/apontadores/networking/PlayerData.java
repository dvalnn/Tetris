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
import com.apontadores.packets.Packet100Update.UpdateTypesEnum;
import com.apontadores.settings.BoardSettings;

public class PlayerData {

  public static final int OPPONENT_X_OFFSET = 3 * GAME_WIDTH / 4 - BOARD_WIDTH * BOARD_SQUARE / 2 + 10;
  public static final int Y_OFFSET = GAME_HEIGHT / 2 - BOARD_HEIGHT * BOARD_SQUARE / 2 + 18;

  // NOTE: MP elements have getters methods and are updated by the client
  // with packets received from the server
  // the getters are used by the PlayingMP class to display the
  // opponent's board, score, etc. on the screen

  MPBoard opponentBoard;
  private final ShapeMP opponentShape;
  private ShapeMP opponentHoldShape;
  private ShapeMP[] opponentNextShapes;

  // NOTE: Player elements have setters and are updated by the local game logic
  // the client then sends packets to the server with the local data to be
  // relayed to the opponent's cient and displayed on the screen
  PlayerBoard playerBoard;

  private Shape currentShape;
  private Shape holdShape;
  private Shape[] nextShapes;

  private int score;
  private int level;
  private int linesCleared;
  private boolean syncShape;
  private boolean syncScore;
  private boolean syncHold;
  private boolean syncNextShapes;

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
    opponentHoldShape = new ShapeMP(BOARD_SQUARE, OPPONENT_X_OFFSET, Y_OFFSET);

    opponentNextShapes = new ShapeMP[4];
    opponentNextShapes[0] = new ShapeMP(BOARD_SQUARE, OPPONENT_X_OFFSET, Y_OFFSET);
    opponentNextShapes[1] = new ShapeMP(BOARD_SQUARE, OPPONENT_X_OFFSET, Y_OFFSET);
    opponentNextShapes[2] = new ShapeMP(BOARD_SQUARE, OPPONENT_X_OFFSET, Y_OFFSET);
    opponentNextShapes[3] = new ShapeMP(BOARD_SQUARE, OPPONENT_X_OFFSET, Y_OFFSET);
  }

  public ShapeMP getOpponentHoldShape() {
    return opponentHoldShape;
  }

  public ShapeMP[] getOpponentNextShapes() {
    return opponentNextShapes;
  }

  public MPBoard getOpponentBoard() {
    return opponentBoard;
  }

  public ShapeMP getOpponentShape() {
    return opponentShape;
  }

  public PlayerData setOpponentName(final String opponentName) {
    opponentBoard.setPlayerName(opponentName);
    return this;
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

  public PlayerData setHoldShape(Shape holdShape) {
    if (holdShape == null) {
      return this;
    }

    this.holdShape = holdShape;
    syncHold = true;
    return this;
  }

  public PlayerData setNextShapes(Shape[] nextShapes) {
    if (nextShapes == null) {
      return this;
    }

    this.nextShapes = nextShapes;
    syncNextShapes = true;
    return this;
  }

  public PlayerData setScore(final int score) {
    this.score = score;
    syncScore = true;
    return this;
  }

  public PlayerData setLevel(final int level) {
    this.level = level;
    syncScore = true;
    return this;
  }

  public PlayerData setLinesCleared(final int linesCleared) {
    this.linesCleared = linesCleared;
    syncScore = true;
    return this;
  }

  public void parsePacket(final Packet100Update inPacket) {
    switch (inPacket.getUpdateType()) {
      case TETROMINO -> parseShapeUpdate(
          inPacket.getUpdateData(),
          opponentShape, ";");
      case HOLD -> parseShapeUpdate(
          inPacket.getUpdateData(),
          opponentHoldShape, ";");
      case NEXT -> parseNextShapesUpdate(inPacket.getUpdateData());
      case BOARD -> parseBoardUpdate(inPacket.getUpdateData());
      case SCORE -> parseScoreUpdate(inPacket.getUpdateData());
      default -> {
      }
    }
  }

  public Packet100Update getShapeUpdate() {
    if (!syncShape || opponentShape == null) {
      return null;
    }

    syncShape = false;

    return new Packet100Update(
        UpdateTypesEnum.TETROMINO,
        getShapeAsString(
            currentShape.getPoints(),
            currentShape.getColor(),
            ";"));
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

    return new Packet100Update(UpdateTypesEnum.SCORE, joiner.toString());
  }

  public Packet100Update getBoardUpdate(final int row, final List<Color> colors) {
    final StringJoiner joiner = new StringJoiner(";");
    joiner.add(String.valueOf(row));
    joiner.add(String.valueOf(colors.size()));
    for (final Color color : colors) {
      joiner.add(String.valueOf(color.getRGB()));
    }

    return new Packet100Update(UpdateTypesEnum.BOARD, joiner.toString());
  }

  public Packet100Update getHoldUpdate() {
    if (!syncHold) {
      return null;
    }
    syncHold = false;
    return new Packet100Update(
        UpdateTypesEnum.HOLD,
        getShapeAsString(
            holdShape.getPoints(),
            holdShape.getColor(),
            ";"));
  }

  public Packet100Update getNextShapesUpdate() {
    if (!syncNextShapes) {
      return null;
    }

    syncNextShapes = false;
    StringJoiner joiner = new StringJoiner(";");
    joiner.add(String.valueOf(nextShapes.length));
    for (final Shape shape : nextShapes) {
      joiner.add(getShapeAsString(
          shape.getPoints(),
          shape.getColor(),
          ":"));
    }
    return new Packet100Update(UpdateTypesEnum.NEXT, joiner.toString());
  }

  private void parseScoreUpdate(final String updateData) {
    final String[] tokens = updateData.split(";");

    try {
      final int opponentScore = Integer.parseInt(tokens[0]);
      final int opponentLinesCleared = Integer.parseInt(tokens[1]);
      final int opponentLevel = Integer.parseInt(tokens[2]);

      opponentBoard.updateInfo(
          opponentLinesCleared,
          opponentLevel,
          opponentScore);
    } catch (final NumberFormatException e) {
      System.err.println("Error parsing score update packet");
      return;
    }
  }

  private void parseBoardUpdate(final String updateData) {
    final String[] tokens = updateData.split(";");

    final int row = Integer.parseInt(tokens[0]);
    final int numColors = Integer.parseInt(tokens[1]);

    final Color[] colors = new Color[numColors];
    for (int i = 0; i < numColors; i++) {
      colors[i] = new Color(Integer.parseInt(tokens[i + 2]));
    }

    opponentBoard.updateState(row, colors);
  }

  private String getShapeAsString(
      final Point2D[] points,
      final Color color,
      final String separator) {

    final StringJoiner joiner = new StringJoiner(separator);
    joiner.add(String.valueOf(color.getRGB()));
    joiner.add(String.valueOf(points.length));
    for (final Point2D point : points) {
      joiner.add(String.valueOf(point.getX()));
      joiner.add(String.valueOf(point.getY()));
    }
    return joiner.toString();
  }

  private void parseShapeUpdate(
      final String data,
      final ShapeMP shapeMP,
      final String separator) {
    final String[] tokens = data.split(separator);
    final Color color = new Color(Integer.parseInt(tokens[0]));
    final int numPoints = Integer.parseInt(tokens[1]);
    final Point2D[] points = new Point2D[numPoints];

    for (int i = 0; i < numPoints; i++) {
      final double x = Double.parseDouble(tokens[i * 2 + 2]);
      final double y = Double.parseDouble(tokens[i * 2 + 3]);
      points[i] = new Point2D.Double(x, y);
    }

    shapeMP.update(points, color);
  }

  private void parseNextShapesUpdate(final String data) {
    final String[] shapes = data.split(";");
    int numShapes = Integer.parseInt(shapes[0]);
    for (int i = 0; i < numShapes; i++) {
      parseShapeUpdate(shapes[i + 1], opponentNextShapes[i], ":");
    }
  }

}
