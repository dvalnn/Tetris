package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.GamePanel;

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
            case (KeyEvent.VK_W):
            case (KeyEvent.VK_UP):
                //
                break;
            case (KeyEvent.VK_A):
            case (KeyEvent.VK_LEFT):
                gamePanel.getBoard().getTetromino().setLeft(true);
                break;
            case (KeyEvent.VK_S):
            case (KeyEvent.VK_DOWN):
                gamePanel.getBoard().getTetromino().setDown(true);

                break;
            case (KeyEvent.VK_D):
            case (KeyEvent.VK_RIGHT):
                gamePanel.getBoard().getTetromino().setRight(true);
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
        }
    }
}