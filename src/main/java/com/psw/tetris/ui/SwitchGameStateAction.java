package com.psw.tetris.ui;

import com.psw.tetris.gameStates.GameStateHandler;

public class SwitchGameStateAction implements
    ButtonAction<GameStateHandler.GameStatesEnum, Void> {

  @Override
  public Void exec(GameStateHandler.GameStatesEnum state) {
    GameStateHandler.switchState(state);
    return null;
  }
}
