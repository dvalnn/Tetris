package com.psw.tetris.gameStates;

import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class State implements StateMethods {

  protected GameStatesEnum stateID;

  public State(GameStatesEnum stateID) {
    this.stateID = stateID;
  }

  public GameStatesEnum getStateID() {
    return stateID;
  }

  @Override
  public void update() {
  }

  @Override
  public void render(Graphics g) {
  }

  @Override
  public void mouseClicked(MouseEvent e) {
  }

  @Override
  public void mousePressed(MouseEvent e) {
  }

  @Override
  public void mouseReleased(MouseEvent e) {
  }

  @Override
  public void mouseMoved(MouseEvent e) {
  }

  @Override
  public void mouseDragged(MouseEvent e) {
  }

  @Override
  public void keyPressed(KeyEvent e) {
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }

  @Override
  public void windowLostFocus() {
  }

  @Override
  public void mouseEntered(MouseEvent e) {
  }

  @Override
  public void mouseExited(MouseEvent e) {
  }
}
