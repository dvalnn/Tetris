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
import java.net.InetAddress;
import java.util.List;

import com.apontadores.gameElements.Board;
import com.apontadores.gameElements.boards.MPBoard;
import com.apontadores.gameElements.boards.PlayerBoard;
import com.apontadores.gameElements.shapes.Shape;
import com.apontadores.gameElements.shapes.ShapeMP;
import com.apontadores.gameStates.GameState;
import com.apontadores.gameStates.GameStateHandler;
import com.apontadores.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.main.Game;
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
  private final int NETWORK_TICK_MAX = 2;
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

  public void addBoardMP(final String username, final InetAddress address, final int port) {
    System.out.println("[PlayingMP] Adding board for " + username);
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

  public void sendFullSync() {
    // send current player tetromino to opponent
    final Shape currentShape = playerBoard.getTetromino().getShape();
    Game.sendShapeUpdate(currentShape.getPoints(), currentShape.getColor());

    // send current player board to opponent
    for (int row = 0; row < BOARD_HEIGHT; row++) {
      final PlayerBoard.BoardLine line = playerBoard.getBoard().get(row);
      final List<Color> colors = line.getColorsCopy();
      Game.sendBoardUpdate(row, colors.toArray(new Color[colors.size()]));
    }
  }

  public void sendPlayerState() {
    // send current player tetromino to opponent
    final Shape currentShape = playerBoard.getTetromino().getShape();
    Game.sendShapeUpdate(currentShape.getPoints(), currentShape.getColor());

    // send current player board to opponent
    for (int row = 0; row < BOARD_HEIGHT; row++) {
      final PlayerBoard.BoardLine line = playerBoard.getBoard().get(row);
      final List<Color> colors = line.getColorsCopyIfChanged();
      if (colors != null) {
        Game.sendBoardUpdate(row, colors.toArray(new Color[colors.size()]));
      }
    }
  }

  @Override
  public void update() {
    frame.update();

    if (opponentBoard == null || opponentDisconnected) {
      return;
    }

    fullSyncTick++;
    networkTick++;
    if (fullSyncTick >= FULL_SYNC_TICK_MAX) {
      sendFullSync();
      fullSyncTick = 0;
    } else if (networkTick >= NETWORK_TICK_MAX) {
      sendPlayerState();
      networkTick = 0;
    }

    playerBoard.update();
  }

  @Override
  public void render(final Graphics g) {
    frame.render(g);

    if (opponentBoard == null) {
      // TODO: make this look nicer
      g.setColor(Color.WHITE);
      g.drawRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
      g.setColor(Color.BLACK);
      g.drawString("Waiting for opponent...", GAME_WIDTH / 2, GAME_HEIGHT / 2);
    } else if (opponentDisconnected) {
      g.setColor(Color.RED);
      // draw message centered above both boards
      // TODO: make this look nicer
      g.drawString("Opponent disconnected!", GAME_WIDTH / 2, GAME_HEIGHT / 2);
    } else {
      // draw line separating the two boards
      // g.setColor(Color.WHITE);
      // g.drawLine(GAME_WIDTH / 2, 0, GAME_WIDTH / 2, GAME_HEIGHT);

      // draw opponent board
      opponentBoard.render(g);
      // draw opponnent controled shape
      shapeMP.render(g);
    }

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