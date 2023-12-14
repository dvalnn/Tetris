package com.apontadores.gameStates.states.menus;

import static com.apontadores.utils.Constants.FRAMES_PATH;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.apontadores.gameStates.GameState;
import com.apontadores.gameStates.GameStateHandler;
import com.apontadores.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.ui.ImageElement;
import com.apontadores.ui.SwitchStateAction;
import com.apontadores.ui.ButtonAction;
import com.apontadores.ui.Frame;

public class GameModeSelect extends GameState {

  private final Frame frame;
  private final SwitchStateAction switchState = new SwitchStateAction();

  ButtonAction<GameStatesEnum, Void> reloadAndSwitch = (state) -> {
    GameStateHandler.reloadState(state);
    switchState.exec(state);
    return null;
  };

  public GameModeSelect() {
    super(GameStatesEnum.MODE_SELECT);
    frame = Frame.loadFromJson(FRAMES_PATH + "gameModeSelect.json");
  }

  @Override
  public void render(final Graphics g) {
    frame.render(g);
  }

  @Override
  public void mouseClicked(final MouseEvent e) {

    ((ImageElement) frame.getElement("singleplayer"))
        .execIfClicked(e.getX(), e.getY(),
            reloadAndSwitch, GameStatesEnum.PLAYING);

    ((ImageElement) frame.getElement("multiplayer"))
        .execIfClicked(e.getX(), e.getY(),
            switchState, GameStatesEnum.MODE_SELECT_MP);

    ((ImageElement) frame.getElement("returnToMainMenu"))
        .execIfClicked(e.getX(), e.getY(),
            switchState, GameStatesEnum.MAIN_MENU);
  }
}
