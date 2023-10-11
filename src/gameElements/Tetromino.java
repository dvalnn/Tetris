package gameElements;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Tetromino {
    private int x;
    private int y;
    private int maxX;
    private int maxY;
    private int width = 100;
    private int height = 50;

    private Random rand;

    public Tetromino(int x, int y, int maxX, int maxY) {
        this.x = x;
        this.y = y;
        this.maxX = maxX - width;
        this.maxY = maxY - height;
        rand = new Random();
    }

    public void moveX(int x) {
        this.x += x;
    }

    public void moveY(int y) {
        this.y += y;
    }

    public void update() {
        moveY(1);
        if (y >= maxY) {
            y = maxY;
        }
        if (x >= maxX) {
            x = maxX;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void paintTetromino(Graphics g) {
        g.setColor(getRandColor());
        g.fillRect(x, y, width, height);
    }

    private Color getRandColor() {
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);

        return new Color(r, g, b);
    }
}
