package com.apontadores.totris.gameStates.states.menus;

import static com.apontadores.totris.utils.Constants.FRAMES_PATH;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.apontadores.totris.gameStates.GameState;
import com.apontadores.totris.gameStates.GameStateHandler;
import com.apontadores.totris.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.totris.ui.ImageElement;
import com.apontadores.totris.ui.SwitchStateAction;
import com.apontadores.totris.ui.ButtonAction;
import com.apontadores.totris.ui.Frame;

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
    frame = Frame.loadFromJson(FRAMES_PATH + "pause.json");
  }

  @Override
  public void render(final Graphics g) {
    GameStateHandler.getState(GameStatesEnum.PLAYING).render(g);
    frame.render(g);
  }

  @Override
  public void mouseClicked(final MouseEvent e) {

    ((ImageElement) frame.getElement("resume"))
        .execIfClicked(e.getX(), e.getY(),
            switchState, GameStatesEnum.PLAYING);

    ((ImageElement) frame.getElement("restart"))
        .execIfClicked(e.getX(), e.getY(),
            reloadAndSwitch, GameStatesEnum.PLAYING);

    ((ImageElement) frame.getElement("returnToMainMenu"))
        .execIfClicked(e.getX(), e.getY(),
            switchState, GameStatesEnum.MAIN_MENU);
  }

}
