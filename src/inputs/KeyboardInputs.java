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
            case (KeyEvent.VK_W):
            case (KeyEvent.VK_UP):
                //
                break;
            case (KeyEvent.VK_A):
            case (KeyEvent.VK_LEFT):
                gamePanel.getBoard().getTetromino().setDirX(LEFT);
                break;
            case (KeyEvent.VK_S):
            case (KeyEvent.VK_DOWN):
                gamePanel.getBoard().getTetromino().fastDrop();
                break;
            case (KeyEvent.VK_D):
            case (KeyEvent.VK_RIGHT):
                gamePanel.getBoard().getTetromino().setDirX(RIGHT);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}