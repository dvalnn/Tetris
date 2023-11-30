package com.psw.tetris.gameStates.states.menus;

import static com.psw.tetris.utils.Constants.RESOURCES_PATH;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.psw.tetris.gameStates.GameState;
import com.psw.tetris.gameStates.GameStateHandler;
import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.ui.ImageElement;
import com.psw.tetris.ui.SwitchStateAction;
import com.psw.tetris.ui.ButtonAction;
import com.psw.tetris.ui.Frame;

public class Pause extends GameState {

  private final Frame frame;

  SwitchStateAction switchState = new SwitchStateAction();

  ButtonAction<GameStatesEnum, Void> reloadAndSwitch = (state) -> {
    GameStateHandler.reloadState(state);
    switchState.exec(state);
    return null;
  };

  public Pause() {
    super(GameStatesEnum.PAUSE);
    frame = Frame.loadFromJson(RESOURCES_PATH + "/frames/pause.json");
  }

  @Override
  public void render(final Graphics g) {
    GameStateHandler.getState(GameStatesEnum.PLAYING).render(g);
    frame.render(g);
  }

  @Override
  public void mouseClicked(final MouseEvent e) {

    ((ImageElement) frame.getElement("resume"))
        .execIfClicked(e.getX(), e.getY(), switchState, GameStatesEnum.PLAYING);

    ((ImageElement) frame.getElement("restart"))
        .execIfClicked(e.getX(), e.getY(), reloadAndSwitch, GameStatesEnum.PLAYING);

    ((ImageElement) frame.getElement("returnToMainMenu"))
        .execIfClicked(e.getX(), e.getY(), switchState, GameStatesEnum.CHANGE_KEYBINDS);
  }

}
