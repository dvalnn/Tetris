package com.psw.tetris.gameStates.stateTypes;

import static com.psw.tetris.utils.Constants.Directions.LEFT;
import static com.psw.tetris.utils.Constants.Directions.RIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.BOARD_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.BOARD_SQUARE;
import static com.psw.tetris.utils.Constants.GameConstants.BOARD_WIDTH;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_WIDTH;
import static com.psw.tetris.utils.Constants.GameConstants.UPS_SET;

import com.psw.tetris.gameElements.Shape;
import com.psw.tetris.gameElements.ShapeMP;
import com.psw.tetris.gameElements.boardTypes.MPBoard;
import com.psw.tetris.gameElements.boardTypes.PlayerBoard;
import com.psw.tetris.gameStates.GameStateHandler;
import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.gameStates.State;
import com.psw.tetris.main.Game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.util.List;

public class PlayingMP extends State {

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
  private final int PLAYER_X_OFFSET = GAME_WIDTH / 4 - BOARD_WIDTH * BOARD_SQUARE / 2;
  private final int OPPONENT_X_OFFSET = 3 * GAME_WIDTH / 4 - BOARD_WIDTH * BOARD_SQUARE / 2;
  private final int Y_OFFSET = GAME_HEIGHT / 2 - BOARD_HEIGHT * BOARD_SQUARE / 2;

  public PlayingMP() {
    super(GameStatesEnum.PLAYING_MP);
    playerBoard = new PlayerBoard(BOARD_SQUARE, PLAYER_X_OFFSET, Y_OFFSET, boardColor);
  }

  public void addBoardMP(String username, InetAddress address, int port) {
    System.out.println("[PlayingMP] Adding board for " + username);
    opponentBoard = new MPBoard(BOARD_SQUARE, OPPONENT_X_OFFSET, Y_OFFSET, boardColor, username);
    shapeMP = new ShapeMP(BOARD_SQUARE, OPPONENT_X_OFFSET, Y_OFFSET);
  }

  public void removeBoardMP(String username) {
    System.out.println("[PlayingMP] " + username + " disconnected!");
    opponentDisconnected = true;
    matchOver = true;
  }

  public void sendFullSync() {
    // send current player tetromino to opponent
    Shape currentShape = playerBoard.getTetromino().getShape();
    Game.sendShapeUpdate(currentShape.getPoints(), currentShape.getColor());

    // send current player board to opponent
    for (int row = 0; row < BOARD_HEIGHT; row++) {
      PlayerBoard.BoardLine line = playerBoard.getBoard().get(row);
      List<Color> colors = line.getColorsCopy();
      Game.sendBoardUpdate(row, colors.toArray(new Color[colors.size()]));
    }
  }

  public void sendPlayerState() {
    // send current player tetromino to opponent
    Shape currentShape = playerBoard.getTetromino().getShape();
    Game.sendShapeUpdate(currentShape.getPoints(), currentShape.getColor());

    // send current player board to opponent
    for (int row = 0; row < BOARD_HEIGHT; row++) {
      PlayerBoard.BoardLine line = playerBoard.getBoard().get(row);
      List<Color> colors = line.getColorsCopyIfChanged();
      if (colors != null) {
        Game.sendBoardUpdate(row, colors.toArray(new Color[colors.size()]));
      }
    }
  }

  @Override
  public void update() {
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
  public void render(Graphics g) {
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
      g.setColor(Color.WHITE);
      g.drawLine(GAME_WIDTH / 2, 0, GAME_WIDTH / 2, GAME_HEIGHT);

      // draw opponent board
      opponentBoard.render(g);
      // draw opponnent controled shape
      shapeMP.render(g);
    }

    playerBoard.render(g);
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (matchOver) {
      GameStateHandler.setActiveState(GameStatesEnum.GAME_OVER);
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
  public void keyReleased(KeyEvent e) {
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
  public void windowLostFocus() {}

  public MPBoard getBoardMP() {
    return opponentBoard;
  }

  public ShapeMP getShapeMP() {
    return shapeMP;
  }
}
