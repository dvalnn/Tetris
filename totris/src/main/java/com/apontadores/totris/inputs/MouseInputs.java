package com.apontadores.totris.inputs;

import com.apontadores.totris.gameStates.GameStateHandler;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInputs extends MouseAdapter {

  public MouseInputs() {
  }

  @Override
  public void mouseClicked(final MouseEvent e) {
    GameStateHandler.getActiveState().mouseClicked(e);
  }

  @Override
  public void mousePressed(final MouseEvent e) {
    GameStateHandler.getActiveState().mousePressed(e);
  }

  @Override
  public void mouseReleased(final MouseEvent e) {
    GameStateHandler.getActiveState().mouseReleased(e);
  }

  @Override
  public void mouseEntered(final MouseEvent e) {
    GameStateHandler.getActiveState().mouseEntered(e);
  }

  @Override
  public void mouseExited(final MouseEvent e) {
    GameStateHandler.getActiveState().mouseExited(e);
  }

  @Override
  public void mouseDragged(final MouseEvent e) {
    GameStateHandler.getActiveState().mouseDragged(e);
  }

  @Override
  public void mouseMoved(final MouseEvent e) {
    GameStateHandler.getActiveState().mouseMoved(e);
  }
}
