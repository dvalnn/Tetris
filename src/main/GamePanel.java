package main;

import javax.swing.JPanel;

import gameElements.Board;
import inputs.KeyboardInputs;
import inputs.MouseInputs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;

// GamePanel is a JPanel -- a container for all visual elements in the game
public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private Board gameBoard;

    private Color gameBakcgroundColor = Color.BLACK;
    private int gameSquareSize = 30;
    // Traditional Tetris is 10x20
    private int gameBoardWidth = 10, gameBoardHeight = 20;
    private int gameBoardOffsetX = 0, gameBoardOffsetY = 0;

    public GamePanel() {
        addKeyListener(new KeyboardInputs(this));

        mouseInputs = new MouseInputs(this);
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);

        gameBoard = new Board(gameSquareSize, gameBoardWidth, gameBoardHeight, gameBoardOffsetX, gameBoardOffsetY,
                gameBakcgroundColor);
    }

    public void updatePanel() {
        gameBoard.update();
    }

    // paintComponent is called whenever the JPanel needs to be redrawn
    public void paintComponent(Graphics g) {
        // calls JPanel's paintComponent method
        super.paintComponent(g);
        gameBoard.render(g);
        // c syncs the graphics state
        // to avoid weird graphical glitches
        Toolkit.getDefaultToolkit().sync();
    }
}