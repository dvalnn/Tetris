package com.apontadores.totris.gameStates.states.singleP;

import static com.apontadores.totris.utils.Constants.FRAMES_PATH;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.apontadores.totris.gameElements.gameplay.GameTime;
import com.apontadores.totris.gameElements.gameplay.Levels;
import com.apontadores.totris.gameElements.gameplay.Score;
import com.apontadores.totris.gameStates.GameState;
import com.apontadores.totris.gameStates.GameStateHandler;
import com.apontadores.totris.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.totris.gameStates.states.menus.Leaderboard;
import com.apontadores.totris.main.Game;
import com.apontadores.totris.ui.ButtonAction;
import com.apontadores.totris.ui.Frame;
import com.apontadores.totris.ui.ImageElement;
import com.apontadores.totris.ui.SwitchStateAction;
import com.apontadores.totris.ui.TextElement;

public class GameOver extends GameState {

  private final Frame frame;
  SwitchStateAction switchState = new SwitchStateAction();
  private boolean isLeaderBoardUpdated = false;

  ButtonAction<GameStatesEnum, Void> reloadAndSwitch = (state) -> {
    GameStateHandler.reloadState(state);
    switchState.exec(state);
    return null;
  };

  public GameOver() {
    super(GameStatesEnum.GAME_OVER);
    frame = Frame.loadFromJson(FRAMES_PATH + "gameOverSingle.json");
  }

  @Override
  public void render(final Graphics g) {
    GameStateHandler.getState(GameStatesEnum.PLAYING).render(g);
    frame.render(g);

  }

  @Override
  public void mouseClicked(final MouseEvent e) {
    ((ImageElement) frame.getElement("restart"))
        .execIfClicked(e.getX(), e.getY(),
            reloadAndSwitch, GameStatesEnum.PLAYING);

    ((ImageElement) frame.getElement("returnToMainMenu"))
        .execIfClicked(e.getX(), e.getY(),
            switchState, GameStatesEnum.MAIN_MENU);
  }

  @Override
  public void update() {
    if (!isLeaderBoardUpdated) {
      Leaderboard.saveNewScore(
          Game.getUsername(),
          Score.getScore(),
          Levels.getCurrentLevel() + 1, // levels start at 0, so we add 1 to save
          Levels.getTotalLinesCleared(),
          GameTime.getTimeStr());

      isLeaderBoardUpdated = true;
    }

    // ((TextElement) frame.getElement("level"))
    // .setText(String.valueOf(Levels.getCurrentLevel()));

    ((TextElement) frame.getElement("totalScore"))
        .setText(String.valueOf(Score.getScore()));

    ((TextElement) frame.getElement("linesCleared"))
        .setText(String.valueOf(Levels.getTotalLinesCleared()));

    ((TextElement) frame.getElement("timePlayed"))
        .setText(GameTime.getTimeStr());

  }
}
