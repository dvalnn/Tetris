package com.apontadores.totris.ui;

import com.apontadores.totris.gameStates.GameStateHandler;
import com.apontadores.totris.gameStates.GameStateHandler.GameStatesEnum;

public class SwitchStateAction implements
    ButtonAction<GameStatesEnum, Void> {

  @Override
  public Void exec(final GameStatesEnum state) {
    GameStateHandler.switchState(state);
    return null;
  }
}
