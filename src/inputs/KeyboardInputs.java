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
                //
                break;
            case (KeyEvent.VK_A):
                //
                break;
            case (KeyEvent.VK_S):
                //
                break;
            case (KeyEvent.VK_D):
                //
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}