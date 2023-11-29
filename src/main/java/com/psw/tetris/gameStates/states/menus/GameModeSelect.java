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

public class GameModeSelect extends GameState {

  private final Frame frame;
  private final SwitchStateAction switchState = new SwitchStateAction();


  ButtonAction<GameStatesEnum, Void> reloadAndSwitch = (state) -> {
    GameStateHandler.reloadState(state);
    switchState.exec(state);
    return null;
  };

  public GameModeSelect() {
    super(GameStatesEnum.GAME_MODE_SELECT);
    frame = Frame.loadFromJson(RESOURCES_PATH + "/frames/gameModeSelect.json");
  }

  @Override
  public void render(final Graphics g) {
    frame.render(g);
  }

  @Override
  public void mouseClicked(final MouseEvent e) {

    ((ImageElement) frame.getElement("singleplayer"))
        .execIfClicked(e.getX(), e.getY(), reloadAndSwitch, GameStatesEnum.PLAYING);

    ((ImageElement) frame.getElement("multiplayer"))
        .execIfClicked(e.getX(), e.getY(), switchState, GameStatesEnum.GAME_MODE_SELECT_MP);

    ((ImageElement) frame.getElement("returnToMainMenu"))
        .execIfClicked(e.getX(), e.getY(), switchState, GameStatesEnum.MAIN_MENU);
  }
}
