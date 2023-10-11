package main;

import javax.swing.JPanel;

import gameElements.Tetromino;
import inputs.KeyboardInputs;
import inputs.MouseInputs;

import java.awt.Graphics;
import java.awt.Toolkit;

// GamePanel is a JPanel -- a container for all visual elements in the game
public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private Tetromino tetromino;

    public GamePanel() {
        addKeyListener(new KeyboardInputs(this));

        mouseInputs = new MouseInputs(this);
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);

        tetromino = new Tetromino(400, 0, 800, 600);
    }

    public void updatePanel() {
        tetromino.update();
    }

    // paintComponent is called whenever the JPanel needs to be redrawn
    public void paintComponent(Graphics g) {
        // calls JPanel's paintComponent method
        super.paintComponent(g);
        tetromino.paintTetromino(g);
        // c syncs the graphics state
        // to avoid weird graphical glitches
        Toolkit.getDefaultToolkit().sync();
    }
}