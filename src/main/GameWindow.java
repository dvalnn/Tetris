package main;

import javax.swing.JFrame;

// GameWindow is a JFrame -- a window for the game to be displayed in
// GameWindow does not handle any visual elements, but it does handle the window
// itself, including the size, title, and close operation
// Visual elements are handled by GamePanel, which represents the "picture"
public class GameWindow extends JFrame {
    private JFrame jFrame;

    public static final int WIDTH = 445, HEIGHT = 630;

    public GameWindow(GamePanel gamePanel) {
        jFrame = new JFrame("Tetris");
        jFrame.setSize(WIDTH, HEIGHT);
        jFrame.setResizable(false);
        // closes the window when the user clicks the close button
        // and terminates the program
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // adds the GamePanel to the GameWindow
        jFrame.add(gamePanel);

        // centers the GameWindow on the screen
        jFrame.setLocationRelativeTo(null);

        // makes the GameWindow visible -- must be the last line of code
        jFrame.setVisible(true);
    }
}