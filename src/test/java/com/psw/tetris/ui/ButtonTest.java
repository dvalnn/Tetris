package com.psw.tetris.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.psw.tetris.gameStates.GameStateHandler;

public class ButtonTest {

  ButtonAction<Integer, Integer> add2 = (Integer value) -> {
    return value + 2;
  };

  Button<Integer, Integer> button = new Button<Integer, Integer>(0, 0, null, 0, add2);

  Button<GameStateHandler.GameStatesEnum, Void> stateButton = new Button<GameStateHandler.GameStatesEnum, Void>(
      0,
      0,
      null,
      0,
      new SwitchGameStateAction());

  @Test
  public void testInterface() {
    assertEquals(4, add2.exec(2));
  }

  @Test
  public void testButtonGenericAction() {
    assertEquals(4, button.execAction(2));
  }

  @Test
  public void testButtonStateAction() {
    // set current using the button
    stateButton.execAction(GameStateHandler.GameStatesEnum.PLAYING);
    // get current state
    GameStateHandler.GameStatesEnum state = GameStateHandler.getActiveStateID();
    assertEquals(state, GameStateHandler.GameStatesEnum.PLAYING);
  }
}
