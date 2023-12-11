package com.apontadores.gameStates.states.menus;

import static com.apontadores.utils.Constants.FRAMES_PATH;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.List;

import com.apontadores.gameStates.GameState;
import com.apontadores.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.ui.ImageElement;
import com.apontadores.ui.SwitchStateAction;
import com.apontadores.utils.LeaderBoard;
import com.apontadores.ui.Frame;
import com.apontadores.utils.TopScores;

public class Leaderboard extends GameState {

  private final Frame frame;
  private final SwitchStateAction action = new SwitchStateAction();

  private List<TopScores> topScores;
  private boolean isLeaderBoardLoaded = false;

  public Leaderboard() {
    super(GameStatesEnum.LEADERBOARD);
    frame = Frame.loadFromJson(FRAMES_PATH + "leaderboard.json");
  }

  @Override
  public void update() {
    if (!isLeaderBoardLoaded) {
       topScores = LeaderBoard.loadLeaderboard();
      isLeaderBoardLoaded = true;
    }
  }

  @Override
  public void render(final Graphics g) {
    frame.render(g);
    // FIXME: This is not the best way to render the leaderboard
    if (topScores != null)
      System.out.println(topScores);
  }

  @Override
  public void mouseClicked(final MouseEvent e) {
    ((ImageElement) frame.getElement("returnToMainMenu"))
        .execIfClicked(e.getX(), e.getY(), action, GameStatesEnum.MAIN_MENU);
  }

}
