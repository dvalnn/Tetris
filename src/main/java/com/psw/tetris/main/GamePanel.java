package com.psw.tetris.main;

import static com.psw.tetris.utils.Constants.GameConstants.*;

import com.psw.tetris.inputs.KeyboardInputs;
import com.psw.tetris.inputs.MouseInputs;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import javax.swing.JPanel;

// GamePanel is a JPanel -- a container for all visual elements in the game
public class GamePanel extends JPanel {

  private MouseInputs mouseInputs;
  private Game game;

  public GamePanel(Game game) {
    this.game = game;
    setPanelSize();

    addKeyListener(new KeyboardInputs());

    mouseInputs = new MouseInputs();
    addMouseListener(mouseInputs);
    addMouseMotionListener(mouseInputs);
  }

  private void setPanelSize() {
    Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    setMinimumSize(size);
    setMaximumSize(size);
    setPreferredSize(size);
  }

  // paintComponent is called whenever the JPanel needs to be redrawn
  public void paintComponent(Graphics g) {
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
}