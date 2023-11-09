package com.psw.tetris.gameStates.stateTypes;

import static com.psw.tetris.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_WIDTH;
import static com.psw.tetris.utils.Constants.UI.Buttons.BUTTON_TYPE.PRESS_ENTER;

import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.gameStates.State;
import com.psw.tetris.ui.MenuButton;
import com.psw.tetris.utils.LoadSave;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

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

  public void setTitleScreen(BufferedImage titleScreen) {
    this.titleScreen = titleScreen;
  }

  public void setButton(MenuButton button) {
    this.button = button;
  }
}
