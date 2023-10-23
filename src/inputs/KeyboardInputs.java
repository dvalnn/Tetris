package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.GamePanel;

import static utils.Constants.Directions.*;

public class KeyboardInputs implements KeyListener {

    private GamePanel gamePanel;

    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case (KeyEvent.VK_Z):
                gamePanel.getBoard().getTetromino().rotate(LEFT);
                break;

            case (KeyEvent.VK_X):
                gamePanel.getBoard().getTetromino().rotate(RIGHT);
                break;

            case (KeyEvent.VK_LEFT):
                gamePanel.getBoard().getTetromino().setLeft(true);
                break;

            case (KeyEvent.VK_DOWN):
                gamePanel.getBoard().getTetromino().setDown(true);
                break;

            case (KeyEvent.VK_RIGHT):
                gamePanel.getBoard().getTetromino().setRight(true);
                break;

            case (KeyEvent.VK_SPACE):
                gamePanel.getBoard().getTetromino().setDrop(true);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case (KeyEvent.VK_W):
            case (KeyEvent.VK_UP):
                //
                break;
            case (KeyEvent.VK_A):
            case (KeyEvent.VK_LEFT):
                gamePanel.getBoard().getTetromino().setLeft(false);
                break;
            case (KeyEvent.VK_S):
            case (KeyEvent.VK_DOWN):
                gamePanel.getBoard().getTetromino().setDown(false);

                break;
            case (KeyEvent.VK_D):
            case (KeyEvent.VK_RIGHT):
                gamePanel.getBoard().getTetromino().setRight(false);
                break;

            case (KeyEvent.VK_SPACE):
                gamePanel.getBoard().getTetromino().setDrop(false);
                break;
        }
    }
}
