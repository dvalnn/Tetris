package com.apontadores.totris.gameStates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface StateMethods {
  void update();

  void render(Graphics g);

  void mouseClicked(MouseEvent e);

  void mousePressed(MouseEvent e);

  void mouseReleased(MouseEvent e);

  void mouseMoved(MouseEvent e);

  void mouseEntered(MouseEvent e);

  void mouseExited(MouseEvent e);

  void mouseDragged(MouseEvent e);

  void keyPressed(KeyEvent e);

  void keyReleased(KeyEvent e);

   void windowLostFocus();
}
