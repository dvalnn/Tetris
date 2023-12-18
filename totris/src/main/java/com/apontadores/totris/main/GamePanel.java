package com.apontadores.totris.main;

import static com.apontadores.totris.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.apontadores.totris.utils.Constants.GameConstants.GAME_WIDTH;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JPanel;

import com.apontadores.totris.inputs.KeyboardInputs;
import com.apontadores.totris.inputs.MouseInputs;

// GamePanel is a JPanel -- a container for all visual elements in the game
public class GamePanel extends JPanel {

  private final Game game;

  public GamePanel(final Game game) {
    this.game = game;
    setPanelSize();

    addKeyListener(new KeyboardInputs());

    final MouseInputs mouseInputs = new MouseInputs();
    addMouseListener(mouseInputs);
    addMouseMotionListener(mouseInputs);
  }

  // paintComponent is called whenever the JPanel needs to be redrawn
  public void paintComponent(final Graphics g) {
    // calls JPanel's paintComponent method
    super.paintComponent(g);
    game.render(g);
    // c syncs the graphics state
    // to avoid weird graphical glitches
    Toolkit.getDefaultToolkit().sync();
  }

  public Game getGame() {
    return game;
  }

  private void setPanelSize() {
    final Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    setMinimumSize(size);
    setMaximumSize(size);
    setPreferredSize(size);
  }
}
