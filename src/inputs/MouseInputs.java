package inputs;

import gameStates.GameStateHandler;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInputs extends MouseAdapter {

  public MouseInputs() {}

  @Override
  public void mouseClicked(MouseEvent e) {
    GameStateHandler.getActiveState().mouseClicked(e);
  }

  @Override
  public void mousePressed(MouseEvent e) {
    GameStateHandler.getActiveState().mousePressed(e);
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    GameStateHandler.getActiveState().mouseReleased(e);
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    GameStateHandler.getActiveState().mouseEntered(e);
  }

  @Override
  public void mouseExited(MouseEvent e) {
    GameStateHandler.getActiveState().mouseExited(e);
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    GameStateHandler.getActiveState().mouseDragged(e);
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    GameStateHandler.getActiveState().mouseMoved(e);
  }
}
