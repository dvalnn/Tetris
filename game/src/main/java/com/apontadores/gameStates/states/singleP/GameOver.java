package com.apontadores.gameStates.states.singleP;

import static com.apontadores.utils.Constants.FRAMES_PATH;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.apontadores.gameElements.gameplay.GameTime;
import com.apontadores.gameElements.gameplay.Levels;
import com.apontadores.gameElements.gameplay.Score;
import com.apontadores.gameStates.GameState;
import com.apontadores.gameStates.GameStateHandler;
import com.apontadores.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.gameStates.states.menus.Leaderboard;
import com.apontadores.main.Game;
import com.apontadores.ui.ButtonAction;
import com.apontadores.ui.Frame;
import com.apontadores.ui.ImageElement;
import com.apontadores.ui.SwitchStateAction;
import com.apontadores.ui.TextElement;

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
