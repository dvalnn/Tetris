package com.apontadores.gameStates.states.multiP;

import static com.apontadores.utils.Constants.FRAMES_PATH;
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
import com.apontadores.packets.Packet;
import com.apontadores.packets.Packet100Update;
import com.apontadores.packets.Packet.PacketTypesEnum;
import com.apontadores.settings.BoardSettings;
import com.apontadores.ui.Frame;

public class PlayingMP extends GameState {

  private final Frame frame;
  private final PlayerData playerData;

  Color boardColor = new Color(20, 20, 20);
  PlayerBoard playerBoard;

  private boolean usernameSet = false;

  private int networkTick = 0;

  TimerBasedService updateProcessor;

  public PlayingMP() {
    super(GameStatesEnum.PLAYING_MP);

    // calculate the offsets so that the boards are centered
    // and the player's board is on the left and the opponent's
    final int PLAYER_X_OFFSET = GAME_WIDTH / 4 - BOARD_WIDTH * BOARD_SQUARE / 2 + 13;
    final int y_OFFSET = GAME_HEIGHT / 2 - BOARD_HEIGHT * BOARD_SQUARE / 2 + 18;
    playerBoard = new PlayerBoard(
        new BoardSettings(
            BOARD_SQUARE,
            PLAYER_X_OFFSET,
            y_OFFSET,
            boardColor,
            boardColor.brighter()));

    frame = Frame.loadFromJson(FRAMES_PATH + "multiplayer.json");

    playerData = new PlayerData();
    setPlayerUpdates();

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
    if (!usernameSet) {
      playerBoard.setPlayerName(Game.getUsername());
      playerData.setOpponentName(Game.getOpponentName());
      usernameSet = true;
    }

    // NOTE:
    // starting the packet processor in the update loop is ok
    // since the method only starts the timer if it is not already running
    updateProcessor.start();

    frame.update();

    networkTick++;
    final int NETWORK_TICK_MAX = 2;
    if (networkTick >= NETWORK_TICK_MAX) {
      networkTick = 0;
      sendPlayerUpdates();
    }

    if (playerBoard.isGameOver()) {
      GameStateHandler.switchState(GameStatesEnum.GAME_OVER_MP);
    }

    playerBoard.update();
  }

  @Override
  public void render(final Graphics g) {
    frame.render(g);

    playerData.getOpponentBoard().render(g);
    playerData.getOpponentShape().render(g);

    playerBoard.render(g);
  }

  @Override
  public void keyPressed(final KeyEvent e) {
    // same controls as playing.java
    switch (e.getKeyCode()) {
      case (KeyEvent.VK_Z) -> playerBoard.getTetromino().rotate(LEFT);
      case (KeyEvent.VK_X) -> playerBoard.getTetromino().rotate(RIGHT);
      case (KeyEvent.VK_LEFT) -> playerBoard.getTetromino().setLeft(true);
      case (KeyEvent.VK_DOWN) -> playerBoard.getTetromino().setDown(true);
      case (KeyEvent.VK_RIGHT) -> playerBoard.getTetromino().setRight(true);
      case (KeyEvent.VK_SPACE) -> playerBoard.getTetromino().setDrop(true);
    }
  }

  @Override
  public void keyReleased(final KeyEvent e) {
    switch (e.getKeyCode()) {
      case (KeyEvent.VK_LEFT) -> playerBoard.getTetromino().setLeft(false);
      case (KeyEvent.VK_DOWN) -> playerBoard.getTetromino().setDown(false);
      case (KeyEvent.VK_RIGHT) -> playerBoard.getTetromino().setRight(false);
      case (KeyEvent.VK_SPACE) -> playerBoard.getTetromino().setDrop(false);
      default -> {
      }
    }
  }

  @Override
  public void windowLostFocus() {
  }

  public PlayerBoard getPlayerBoard() {
    return playerBoard;
  }

  private void getUpdates() {
    final Packet packet = Game.getClient().receivedUpdates.poll();
    if (packet == null)
      return;

    final PacketTypesEnum packetType = Packet.lookupPacket(packet.asTokens());
    switch (packetType) {
      case UPDATE -> playerData.parsePacket((Packet100Update) packet);
      case GAME_OVER -> {
        GameStateHandler.switchState(GameStatesEnum.GAME_OVER_MP);
      }
      default -> {
      }
    }
  }

  private void sendPlayerUpdates() {
    Packet100Update updatePacket;
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
    final ArrayBlockingQueue<Packet> outQueue = Game.getClient().outgoingUpdates;
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

}
