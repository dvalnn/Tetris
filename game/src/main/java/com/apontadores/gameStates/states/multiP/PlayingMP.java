package com.apontadores.gameStates.states.multiP;

import static com.apontadores.utils.Constants.RESOURCES_PATH;
import static com.apontadores.utils.Constants.Directions.LEFT;
import static com.apontadores.utils.Constants.Directions.RIGHT;
import static com.apontadores.utils.Constants.GameConstants.BOARD_HEIGHT;
import static com.apontadores.utils.Constants.GameConstants.BOARD_SQUARE;
import static com.apontadores.utils.Constants.GameConstants.BOARD_WIDTH;
import static com.apontadores.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.apontadores.utils.Constants.GameConstants.GAME_WIDTH;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

import com.apontadores.gameElements.boards.PlayerBoard;
import com.apontadores.gameElements.gameplay.Levels;
import com.apontadores.gameElements.gameplay.Score;
import com.apontadores.gameStates.GameState;
import com.apontadores.gameStates.GameStateHandler;
import com.apontadores.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.main.Game;
import com.apontadores.main.TimerBasedService;
import com.apontadores.networking.PlayerData;
import com.apontadores.packets.Packet100Update;
import com.apontadores.settings.BoardSettings;
import com.apontadores.ui.Frame;

public class PlayingMP extends GameState {

  Color boardColor = new Color(20, 20, 20);
  PlayerBoard playerBoard;
  private boolean matchOver = false;

  private int networkTick = 0;
  private final int NETWORK_TICK_MAX = 2;

  private PlayerData playerData;

  // calculate the offsets so that the boards are centered
  // and the player's board is on the left and the opponent's
  private final int PLAYER_X_OFFSET = GAME_WIDTH / 4 - BOARD_WIDTH * BOARD_SQUARE / 2 + 13;
  private final int Y_OFFSET = GAME_HEIGHT / 2 - BOARD_HEIGHT * BOARD_SQUARE / 2 + 18;

  private Frame frame;

  TimerBasedService updateProcessor;

  public PlayingMP() {
    super(GameStatesEnum.PLAYING_MP);

    playerBoard = new PlayerBoard(
        new BoardSettings(
            BOARD_SQUARE,
            PLAYER_X_OFFSET,
            Y_OFFSET,
            boardColor,
            boardColor.brighter()));

    playerData = new PlayerData();
    setPlayerUpdates();

    frame = Frame.loadFromJson(RESOURCES_PATH + "/frames/multiplayer.json");

    updateProcessor = new TimerBasedService(new TimerTask() {
      @Override
      public void run() {
        getUpdates();
        setPlayerUpdates();
      }
    }, 0, 5);
  }

  @Override
  public void update() {
    frame.update();

    updateProcessor.start();
    networkTick++;
    if (networkTick >= NETWORK_TICK_MAX) {
      networkTick = 0;
      sendPlayerUpdates();
    }

    playerBoard.update();
  }

  private void getUpdates() {
    Packet100Update updatePacket = Game.getClient().receivedUpdates.poll();
    if (updatePacket == null)
      return;

    playerData.parsePacket(updatePacket);
  }

  private void sendPlayerUpdates() {
    Packet100Update updatePacket = null;
    updatePacket = playerData.getScoreUpdate();
    if (updatePacket != null)
      sendPacket(updatePacket);

    updatePacket = playerData.getShapeUpdate();
    if (updatePacket != null)
      sendPacket(updatePacket);

    final PlayerBoard board = playerData.getPlayerBoard();
    if (board != null) {

      final List<PlayerBoard.BoardLine> boardLines = board.getBoard();
      for (int row = 0; row < BOARD_HEIGHT; row++) {
        final List<Color> line = boardLines.get(row).getColorsCopyIfChanged();
        if (line == null)
          continue;

        updatePacket = playerData.getBoardUpdate(row, line);
        if (updatePacket != null)
          sendPacket(updatePacket);
      }
    }
  }

  private void sendPacket(final Packet100Update packet) {
    ArrayBlockingQueue<Packet100Update> outQueue = Game.getClient().outgoingUpdates;
    if (outQueue.remainingCapacity() == 0)
      outQueue.poll();
    outQueue.add(packet);
  }

  private void setPlayerUpdates() {
    playerData
        .setPlayerBoard(playerBoard)
        .setCurrentShape(playerBoard.getTetromino().getShape())
        .setScore(Score.getScore())
        .setLinesCleared(Levels.getTotalLinesCleared())
        .setLevel(Levels.getCurrentLevel());
    // .setNextShapes(playerBoard.getNextShapes())
    // .setHoldShape(playerBoard.getHoldShape())
  }

  @Override
  public void render(final Graphics g) {
    frame.render(g);

    // draw opponent board
    playerData.getOpponentBoard().render(g);
    // draw opponnent controled shape
    playerData.getOpponentShape().render(g);

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

  public PlayerBoard getPlayerBoard() {
    return playerBoard;
  }

}
