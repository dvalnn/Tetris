package com.apontadores.gameStates.states.menus;

import static com.apontadores.utils.Constants.FRAMES_PATH;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.apontadores.gameStates.GameState;
import com.apontadores.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.ui.ImageElement;
import com.apontadores.ui.SwitchStateAction;
import com.apontadores.utils.LeaderBoard;
import com.apontadores.ui.Frame;

public class Leaderboard extends GameState {

  private final Frame frame;
  private final SwitchStateAction action = new SwitchStateAction();

  private String temporaryLeaderboard;
  private boolean isLeaderBoardLoaded = false;

  public Leaderboard() {
    super(GameStatesEnum.LEADERBOARD);
    frame = Frame.loadFromJson(FRAMES_PATH + "leaderboard.json");
  }

  @Override
  public void update() {
    if (!isLeaderBoardLoaded) {
      temporaryLeaderboard = LeaderBoard.loadLeaderboard();
      isLeaderBoardLoaded = true;
    }
  }

  @Override
  public void render(final Graphics g) {
    frame.render(g);
    // FIXME: This is not the best way to render the leaderboard
    if (temporaryLeaderboard != null)
      System.out.println(temporaryLeaderboard);
  }

  @Override
  public void mouseClicked(final MouseEvent e) {
    ((ImageElement) frame.getElement("returnToMainMenu"))
        .execIfClicked(e.getX(), e.getY(), action, GameStatesEnum.MAIN_MENU);
  }

}
