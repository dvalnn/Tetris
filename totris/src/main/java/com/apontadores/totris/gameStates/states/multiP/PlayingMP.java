package com.apontadores.totris.gameStates.states.multiP;

import static com.apontadores.totris.utils.Constants.FRAMES_PATH;
import static com.apontadores.totris.utils.Constants.Directions.LEFT;
import static com.apontadores.totris.utils.Constants.Directions.RIGHT;
import static com.apontadores.totris.utils.Constants.GameConstants.BOARD_HEIGHT;
import static com.apontadores.totris.utils.Constants.GameConstants.BOARD_SQUARE;
import static com.apontadores.totris.utils.Constants.GameConstants.BOARD_WIDTH;
import static com.apontadores.totris.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.apontadores.totris.utils.Constants.GameConstants.GAME_WIDTH;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

import com.apontadores.main.TimerBasedService;
import com.apontadores.packets.Packet;
import com.apontadores.packets.Packet.PacketTypesEnum;
import com.apontadores.packets.Packet100Update;
import com.apontadores.packets.Packet101GameOver;
import com.apontadores.totris.gameElements.boards.PlayerBoard;
import com.apontadores.totris.gameElements.gameplay.Levels;
import com.apontadores.totris.gameElements.gameplay.Score;
import com.apontadores.totris.gameStates.GameState;
import com.apontadores.totris.gameStates.GameStateHandler;
import com.apontadores.totris.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.totris.main.Game;
import com.apontadores.totris.networking.PlayerData;
import com.apontadores.totris.settings.BoardSettings;
import com.apontadores.totris.ui.Frame;
import com.apontadores.totris.ui.ImageElement;

public class PlayingMP extends GameState {

  private final Frame frame;
  private final PlayerData playerData;

  Color boardColor = new Color(20, 20, 20);
  PlayerBoard playerBoard;

  private boolean usernameSet;
  private boolean resyncBoard;

  private boolean opponentGameOver;

  BoardSettings opponentBoardSettings;
  private int networkTick = 0;

  TimerBasedService updateProcessor;

  public PlayingMP() {
    super(GameStatesEnum.PLAYING_MP);
    opponentBoardSettings = new BoardSettings(
        BOARD_SQUARE,
        PlayerData.OPPONENT_X_OFFSET,
        PlayerData.Y_OFFSET,
        boardColor,
        boardColor.brighter());

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

    if (playerBoard.isGameOver() && opponentGameOver) {
      GameStateHandler.switchState(GameStatesEnum.GAME_OVER_MP);
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

    playerBoard.update();
  }

  @Override
  public void render(final Graphics g) {
    frame.render(g);

    playerData.getOpponentBoard().render(g);
    playerData.getOpponentShape().render(g);

    playerData.getOpponentHoldShape().renderAt(g,
        opponentBoardSettings.holdRenderX,
        opponentBoardSettings.holdRenderY);

    for (int i = 0; i < 4; i++) {
      playerData.getOpponentNextShapes()[i].renderAt(g,
          opponentBoardSettings.nextRenderX,
          opponentBoardSettings.nextRenderY + i * 100);
    }

    playerBoard.render(g);

    if (playerBoard.isGameOver()) {
      ImageElement gameOver;
      gameOver = (ImageElement) frame.getElement("playerGameOver");
      gameOver.enable();
      gameOver.render(g);
      gameOver.disable();
    }

    if (opponentGameOver) {
      ImageElement gameOver;
      gameOver = (ImageElement) frame.getElement("opponentGameOver");
      gameOver.enable();
      gameOver.render(g);
      gameOver.disable();
    }
  }

  @Override
  public void keyPressed(final KeyEvent e) {
    if (e.getKeyCode() == Game.getKeybindings().rotatesLeft) {
      playerBoard.getTetromino().rotate(LEFT);
      return;
    }
    if (e.getKeyCode() == Game.getKeybindings().rotatesRight) {
      playerBoard.getTetromino().rotate(RIGHT);
      return;
    }
    if (e.getKeyCode() == Game.getKeybindings().movesLeft) {
      playerBoard.getTetromino().setLeft(true);
      return;
    }
    if (e.getKeyCode() == Game.getKeybindings().movesRight) {
      playerBoard.getTetromino().setRight(true);
      return;
    }
    if (e.getKeyCode() == Game.getKeybindings().softDrop) {
      playerBoard.getTetromino().setDown(true);
      return;
    }
    if (e.getKeyCode() == Game.getKeybindings().hardDrop) {
      playerBoard.getTetromino().setDrop(true);
      return;
    }
    if (e.getKeyCode() == Game.getKeybindings().hold) {
      playerBoard.holdTetromino();
      return;
    }
  }

  @Override
  public void keyReleased(final KeyEvent e) {
    if (e.getKeyCode() == Game.getKeybindings().movesLeft) {
      playerBoard.getTetromino().setLeft(false);
      return;
    }
    if (e.getKeyCode() == Game.getKeybindings().softDrop) {
      playerBoard.getTetromino().setDown(false);
      return;
    }
    if (e.getKeyCode() == Game.getKeybindings().movesRight) {
      playerBoard.getTetromino().setRight(false);
      return;
    }
    if (e.getKeyCode() == Game.getKeybindings().hardDrop) {
      playerBoard.getTetromino().setDrop(false);
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
      case RESYNC -> resyncBoard = true;
      case GAME_OVER -> {
        opponentGameOver = true;
      }
      default -> {
      }
    }
  }

  private void sendPlayerUpdates() {
    if (playerBoard.isGameOver()) {
      sendPacket(new Packet101GameOver(
          Score.getScore(),
          Levels.getTotalLinesCleared(),
          Levels.getCurrentLevel()));
      return;
    }

    Packet100Update updatePacket;
    updatePacket = playerData.getScoreUpdate();
    if (updatePacket != null)
      sendPacket(updatePacket);

    updatePacket = playerData.getShapeUpdate();
    if (updatePacket != null)
      sendPacket(updatePacket);

    updatePacket = playerData.getHoldUpdate();
    if (updatePacket != null)
      sendPacket(updatePacket);

    updatePacket = playerData.getNextShapesUpdate();
    if (updatePacket != null)
      sendPacket(updatePacket);

    final PlayerBoard board = playerData.getPlayerBoard();
    if (board == null)
      return;

    final List<PlayerBoard.BoardLine> boardLines = board.getBoard();
    for (int row = 0; row < BOARD_HEIGHT; row++) {
      List<Color> line;
      if (resyncBoard && !boardLines.isEmpty()) {
        line = boardLines.get(row).getColorsCopy();
      } else {
        line = boardLines.get(row).getColorsCopyIfChanged();
      }

      if (line == null)
        continue;

      updatePacket = playerData.getBoardUpdate(row, line);
      if (updatePacket != null)
        sendPacket(updatePacket);
    }

    resyncBoard = false;
  }

  private void sendPacket(final Packet packet) {
    final ArrayBlockingQueue<Packet> outQueue = Game.getClient().outgoingUpdates;
    if (outQueue.remainingCapacity() == 0)
      outQueue.poll();
    outQueue.add(packet);
  }

  private void setPlayerUpdates() {
    playerData
        .setPlayerBoard(playerBoard)
        .setCurrentShape(playerBoard.getTetromino().getShape())
        .setHoldShape(playerBoard.getHoldIfChanged())
        .setNextShapes(playerBoard.getNextIfChanged())
        .setScore(Score.getScore())
        .setLinesCleared(Levels.getTotalLinesCleared())
        .setLevel(Levels.getCurrentLevel());
  }

}
