package main;

import javax.swing.JPanel;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

// GamePanel is a JPanel -- a container for all visual elements in the game
public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;

    private float xDelta = 100, yDelta = 100;
    private float xDir = 1f, yDir = 1f;

    private Color color = Color.CYAN;

    private Random rand;

    public GamePanel() {
        rand = new Random();

        addKeyListener(new KeyboardInputs(this));

        mouseInputs = new MouseInputs(this);
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    public void changeXDelta(int value) {
        this.xDelta += value;

    }

    public void changeYDelta(int value) {
        this.yDelta += value;

    }

    public void setRectPosition(int x, int y) {
        this.xDelta = x;
        this.yDelta = y;

    }

    public void updatePanel() {
        updateRectangle();
    }

    // paintComponent is called whenever the JPanel needs to be redrawn
    public void paintComponent(Graphics g) {
        // calls JPanel's paintComponent method
        super.paintComponent(g);

        g.setColor(color);
        g.fillRect((int) xDelta, (int) yDelta, 100, 100);
    }

    private void updateRectangle() {
        xDelta += xDir;
        yDelta += yDir;

        if (xDelta >= 800 || xDelta <= 0) {
            xDir *= -1;
            color = getRandColor();
        }

        if (yDelta >= 600 || yDelta <= 0) {
            yDir *= -1;
            color = getRandColor();
        }

    }

    private Color getRandColor() {
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);

        return new Color(r, g, b);
    }

}