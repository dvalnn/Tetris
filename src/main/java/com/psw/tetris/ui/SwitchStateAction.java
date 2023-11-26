package com.psw.tetris.ui;

import com.psw.tetris.gameStates.GameStateHandler;
import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;

public class SwitchStateAction implements
    ButtonAction<GameStatesEnum, Void> {

  @Override
  public Void exec(GameStatesEnum state) {
    GameStateHandler.switchState(state);
    return null;
  }
}
