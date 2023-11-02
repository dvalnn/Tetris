package gameStates;

import static utils.Constants.GameConstants.*;
import static utils.Constants.UI.Buttons.BUTTON_TYPE.PRESS_ENTER;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import main.Game;
import ui.MenuButton;
import utils.LoadSave;

public class TitleScreen extends State implements StateMethods {

  private BufferedImage titleScreen;

  // button should be placed in the bottom center of the screen
  private final int xPos = GAME_WIDTH / 2;
  private final int yPos = GAME_HEIGHT - 100;

  private MenuButton button =
      new MenuButton(new Point(xPos, yPos), PRESS_ENTER, 0.25, GameState.PLAYING);

  public TitleScreen(Game game) {
    super(game);
    titleScreen = LoadSave.loadBackground("titlescreen.png");
  }

  @Override
  public void update() {}

  @Override
  public void render(Graphics g) {
    g.drawImage(titleScreen, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
    button.render(g);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    if (button.getBounds().contains(e.getPoint())) {
      GameState.state = GameState.PLAYING;
    }
  }

  @Override
  public void mousePressed(MouseEvent e) {}

  @Override
  public void mouseReleased(MouseEvent e) {}

  @Override
  public void mouseMoved(MouseEvent e) {}

  @Override
  public void mouseDragged(MouseEvent e) {}

  @Override
  public void keyPressed(KeyEvent e) {
    switch (e.getKeyCode()) {
      case KeyEvent.VK_ENTER:
        GameState.state = GameState.PLAYING;
        break;
      case KeyEvent.VK_M:
        game.initNetworking();
        GameState.state = GameState.PLAYING_MP;
        break;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {}

  public void windowLostFocus() {}
}
