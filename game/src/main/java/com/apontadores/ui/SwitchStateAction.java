package com.apontadores.ui;

import com.apontadores.gameStates.GameStateHandler;
import com.apontadores.gameStates.GameStateHandler.GameStatesEnum;

public class SwitchStateAction implements
    ButtonAction<GameStatesEnum, Void> {

  @Override
  public Void exec(GameStatesEnum state) {
    GameStateHandler.switchState(state);
    return null;
  }
}
