package gameElements;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Tetromino {
    private int x;
    private int y;
    private int size;

    private int[][] shape = {
            { 0, 1, 0 },
            { 1, 1, 1 },
            { 0, 0, 0 }
    };

    private Color color;

    private Random rand;

    public Tetromino(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.rand = new Random();
        this.color = getRandColor();
    }

    public void moveX(int x) {
        this.x += x;
    }

    public void moveY(int y) {
        this.y += y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void update() {
        // this.y += size;
    }

    public void render(Graphics g) {
        g.setColor(color);
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] == 1) {
                    g.fillRect(x + col * size, y + row * size, size, size);
                }
            }
        }
    }

    private Color getRandColor() {
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);

        return new Color(r, g, b);
    }
}
