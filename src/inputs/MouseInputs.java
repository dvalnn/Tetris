package inputs;

import gameStates.GameState;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import main.GamePanel;

public class MouseInputs extends MouseAdapter {
  private GamePanel gamePanel;

  public MouseInputs(GamePanel gamePanel) { this.gamePanel = gamePanel; }

  @Override
  public void mouseClicked(MouseEvent e) {
    switch (GameState.state) {
    case TITLE_SCREEN:
      gamePanel.getGame().getMenu().mouseClicked(e);
      break;

    case PLAYING:
      gamePanel.getGame().getPlaying().mouseClicked(e);
      break;

    case GAME_OVER:
      gamePanel.getGame().getGameOver().mouseClicked(e);
      break;
    }
  }

  @Override
  public void mousePressed(MouseEvent e) {
    switch (GameState.state) {
    case TITLE_SCREEN:
      gamePanel.getGame().getMenu().mousePressed(e);
      break;

    case PLAYING:
      gamePanel.getGame().getPlaying().mousePressed(e);
      break;

    case GAME_OVER:
      gamePanel.getGame().getGameOver().mousePressed(e);
      break;
    }
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    switch (GameState.state) {
    case TITLE_SCREEN:
      gamePanel.getGame().getMenu().mouseReleased(e);
      break;

    case PLAYING:
      gamePanel.getGame().getPlaying().mouseReleased(e);
      break;

    case GAME_OVER:
      gamePanel.getGame().getGameOver().mouseReleased(e);
      break;
    }
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    // System.out.println("Mouse Entered");
  }

  @Override
  public void mouseExited(MouseEvent e) {
    // System.out.println("Mouse Exited");
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    switch (GameState.state) {
    case TITLE_SCREEN:
      gamePanel.getGame().getMenu().mouseDragged(e);
      break;

    case PLAYING:
      gamePanel.getGame().getPlaying().mouseDragged(e);
      break;

    case GAME_OVER:
      gamePanel.getGame().getGameOver().mouseDragged(e);
      break;
    }
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    // System.out.println("Mouse Moved");
  }
}
