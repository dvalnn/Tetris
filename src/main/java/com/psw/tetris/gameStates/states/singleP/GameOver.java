package com.psw.tetris.gameStates.states.singleP;

import static com.psw.tetris.utils.Constants.RESOURCES_PATH;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.psw.tetris.gameElements.gameplay.GameTime;
import com.psw.tetris.gameElements.gameplay.Levels;
import com.psw.tetris.gameElements.gameplay.Score;
import com.psw.tetris.gameStates.GameState;
import com.psw.tetris.gameStates.GameStateHandler;
import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.ui.ImageElement;
import com.psw.tetris.ui.TextElement;
import com.psw.tetris.ui.SwitchStateAction;
import com.psw.tetris.ui.ButtonAction;
import com.psw.tetris.ui.Frame;

public class GameOver extends GameState {

  private final Frame frame;
  SwitchStateAction switchState = new SwitchStateAction();

  ButtonAction<GameStatesEnum, Void> reloadAndSwitch = (state) -> {
    GameStateHandler.reloadState(state);
    switchState.exec(state);
    return null;
  };

  public GameOver() {
    super(GameStatesEnum.GAME_OVER);
    frame = Frame.loadFromJson(RESOURCES_PATH + "/frames/gameOverSingle.json");
  }

  @Override
  public void render(final Graphics g) {
    GameStateHandler.getState(GameStatesEnum.PLAYING).render(g);
    frame.render(g);

  }

  @Override
  public void mouseClicked(final MouseEvent e) {
    ((ImageElement) frame.getElement("restart"))
        .execIfClicked(e.getX(), e.getY(), reloadAndSwitch, GameStatesEnum.PLAYING);

    ((ImageElement) frame.getElement("returnToMainMenu"))
        .execIfClicked(e.getX(), e.getY(), switchState, GameStatesEnum.MAIN_MENU);
  }

  @Override
  public void update() {

    ((TextElement) frame.getElement("totalScore"))
        .setText(String.valueOf(Score.getScore()));

    ((TextElement) frame.getElement("linesCleared"))
        .setText(String.valueOf(Levels.getTotalLinesCleared()));

    ((TextElement) frame.getElement("timePlayed"))
        .setText(String.valueOf(GameTime.getTimeStr()));

  }
}