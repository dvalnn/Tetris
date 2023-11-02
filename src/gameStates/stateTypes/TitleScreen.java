package gameStates.stateTypes;

import static utils.Constants.GameConstants.*;
import static utils.Constants.UI.Buttons.BUTTON_TYPE.PRESS_ENTER;

import gameStates.GameStateHandler.GameStatesEnum;
import gameStates.State;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import ui.MenuButton;
import utils.LoadSave;

public class TitleScreen extends State {

  private BufferedImage titleScreen;

  // button should be placed in the bottom center of the screen
  private final int xPos = GAME_WIDTH / 2;
  private final int yPos = GAME_HEIGHT - 100;

  private MenuButton button =
      new MenuButton(new Point(xPos, yPos), PRESS_ENTER, 0.25, GameStatesEnum.MAIN_MENU);

  public TitleScreen() {
    super(GameStatesEnum.TITLE_SCREEN);
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
      button.applyGameState();
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    switch (e.getKeyCode()) {
      case KeyEvent.VK_ENTER:
        button.applyGameState();
        break;
    }
  }
}
