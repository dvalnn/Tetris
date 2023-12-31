package com.apontadores.totris.gameStates.states.multiP;

import static com.apontadores.totris.utils.Constants.FRAMES_PATH;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.concurrent.ArrayBlockingQueue;

import com.apontadores.packets.Packet;
import com.apontadores.packets.Packet.PacketTypesEnum;
import com.apontadores.packets.Packet101GameOver;
import com.apontadores.packets.PacketException;
import com.apontadores.totris.gameElements.gameplay.GameTime;
import com.apontadores.totris.gameElements.gameplay.Levels;
import com.apontadores.totris.gameElements.gameplay.Score;
import com.apontadores.totris.gameStates.GameState;
import com.apontadores.totris.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.totris.main.Game;
import com.apontadores.totris.ui.Frame;
import com.apontadores.totris.ui.ImageElement;
import com.apontadores.totris.ui.SwitchStateAction;
import com.apontadores.totris.ui.TextElement;

public class GameOverMP extends GameState {

  private static final GameStatesEnum stateID = GameStatesEnum.GAME_OVER_MP;
  private final Frame frame;

  private String scorePlayer2, linesPlayer2;
  private String winner;

  SwitchStateAction switchState = new SwitchStateAction();

  private int networkTick = 0;

  public GameOverMP() {
    super(stateID);
    frame = Frame.loadFromJson(FRAMES_PATH + "gameOverMP.json");
  }

  @Override
  public void render(final Graphics g) {
    frame.render(g);
  }

  @Override
  public void update() {
    frame.update();

    ((TextElement) frame.getElement("p1Name")).setText(Game.getUsername());
    ((TextElement) frame.getElement("p2Name")).setText(Game.getOpponentName());

    String winner = getWinner();
    if (winner != null)
      ((TextElement) frame.getElement("winner")).setText(winner);

    ((TextElement) frame.getElement("scorePlayer1"))
        .setText(String.valueOf(Score.getScore()));

    ((TextElement) frame.getElement("linesClearedPlayer1"))
        .setText(String.valueOf(String.valueOf(Levels.getTotalLinesCleared())));

    ((TextElement) frame.getElement("scoreTimePlayer1"))
        .setText(String.valueOf(GameTime.getTimeStr()));

    ((TextElement) frame.getElement("scorePlayer2"))
        .setText(String.valueOf(scorePlayer2));

    ((TextElement) frame.getElement("linesClearedPlayer2"))
        .setText(String.valueOf(linesPlayer2));

    ((TextElement) frame.getElement("scoreTimePlayer2"))
        .setText(String.valueOf(GameTime.getTimeStr()));

    networkTick++;
    final int NETWORK_TICK_MAX = 2;
    if (networkTick >= NETWORK_TICK_MAX) {
      networkTick = 0;
      getUpdates();
      sendPlayerUpdates();
    }

  }

  private String getWinner() {
    if (winner != null)
      return winner;

    if (scorePlayer2 == null)
      return null;

    int score2 = Integer.parseInt(scorePlayer2);
    if (Score.getScore() > score2)
      winner = Game.getUsername() + " wins!";
    else if (score2 > Score.getScore())
      winner = Game.getOpponentName() + " wins!";
    else
      winner = "Draw";

    return winner;
  }

  @Override
  public void mouseClicked(final MouseEvent e) {

    final int x = e.getX();
    final int y = e.getY();

    ((ImageElement) frame.getElement("exit"))
        .execIfClicked(
            x, y,
            (state) -> {
              Game.getClient().finishConnection();
              switchState.exec(state);
              return null;
            },
            GameStatesEnum.MAIN_MENU);

  }

  private void getUpdates() {
    while (!Game.getClient().receivedUpdates.isEmpty()) {
      final Packet packet = Game.getClient().receivedUpdates.poll();
      if (packet == null)
        return;

      final String tokens[] = packet.asTokens();
      final PacketTypesEnum packetType = Packet.lookupPacket(tokens);
      switch (packetType) {
        case GAME_OVER -> {
          try {
            final Packet101GameOver packet101;
            packet101 = (Packet101GameOver) new Packet101GameOver().fromTokens(tokens);
            scorePlayer2 = packet101.getScore();
            linesPlayer2 = packet101.getLines();
          } catch (final PacketException e) {
            System.err.println("Invalid packet: " + e.getMessage());
            return;
          }
        }
        default -> {
        }
      }
    }
  }

  private void sendPlayerUpdates() {
    final Packet101GameOver packet = new Packet101GameOver(
        Score.getScore(),
        Levels.getTotalLinesCleared(),
        Levels.getCurrentLevel());

    final ArrayBlockingQueue<Packet> outQueue = Game.getClient().outgoingUpdates;
    if (outQueue.remainingCapacity() == 0)
      outQueue.poll();
    outQueue.add(packet);

  }

}
