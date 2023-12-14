package com.apontadores.main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

// GameWindow is a JFrame -- a window for the game to be displayed in
// GameWindow does not handle any visual elements, but it does handle the window
// itself, including the size, title, and close operation
// Visual elements are handled by GamePanel, which represents the "picture"
public class GameWindow extends JFrame {
  protected GamePanel gamePanel;

  public GameWindow(final GamePanel gamePanel) {
    final JFrame jFrame = new JFrame("Tetris");
    jFrame.setResizable(false);

    // closes the window when the user clicks the close button
    // and terminates the program
    jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    this.gamePanel = gamePanel;

    // adds the GamePanel to the GameWindow
    jFrame.add(gamePanel);
    jFrame.pack();

    // centers the GameWindow on the screen
    jFrame.setLocationRelativeTo(null);

    jFrame.addWindowFocusListener(
        new WindowFocusListener() {
          @Override
          public void windowGainedFocus(final WindowEvent arg0) {
          }

          @Override
          public void windowLostFocus(final WindowEvent arg0) {
            gamePanel.getGame().windowLostFocus();
          }
        });

    jFrame.addWindowListener(
        new WindowListener() {

          @Override
          public void windowActivated(final WindowEvent arg0) {
          }

          @Override
          public void windowClosed(final WindowEvent arg0) {
          }

          @Override
          public void windowClosing(final WindowEvent arg0) {
            // gamePanel.getGame().terminateConnection();
          }

          @Override
          public void windowDeactivated(final WindowEvent arg0) {
          }

          @Override
          public void windowDeiconified(final WindowEvent arg0) {
          }

          @Override
          public void windowIconified(final WindowEvent arg0) {
          }

          @Override
          public void windowOpened(final WindowEvent arg0) {
          }
        });

    // makes the GameWindow visible -- must be the last line of code
    jFrame.setVisible(true);
  }
}
