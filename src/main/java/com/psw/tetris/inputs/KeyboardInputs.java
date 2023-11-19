package com.psw.tetris.inputs;

import com.psw.tetris.gameStates.GameStateHandler;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInputs implements KeyListener {

  public KeyboardInputs() {
  }

  @Override
  public void keyTyped(final KeyEvent e) {
  }

  @Override
  public void keyPressed(final KeyEvent e) {
    GameStateHandler.getActiveState().keyPressed(e);
  }

  @Override
  public void keyReleased(final KeyEvent e) {
    GameStateHandler.getActiveState().keyReleased(e);
  }
}
