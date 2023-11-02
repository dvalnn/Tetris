package inputs;

import gameStates.GameStateHandler;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInputs implements KeyListener {

  public KeyboardInputs() {}

  @Override
  public void keyTyped(KeyEvent e) {}

  @Override
  public void keyPressed(KeyEvent e) {
    GameStateHandler.getActiveState().keyPressed(e);
  }

  @Override
  public void keyReleased(KeyEvent e) {
    GameStateHandler.getActiveState().keyReleased(e);
  }
}
