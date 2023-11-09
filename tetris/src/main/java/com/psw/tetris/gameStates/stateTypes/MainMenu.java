package com.psw.tetris.gameStates.stateTypes;

import static com.psw.tetris.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_WIDTH;
import static com.psw.tetris.utils.Constants.UI.Buttons.BUTTON_TYPE.ABOUT_US;
import static com.psw.tetris.utils.Constants.UI.Buttons.BUTTON_TYPE.EXIT_GAME;
import static com.psw.tetris.utils.Constants.UI.Buttons.BUTTON_TYPE.LEADERBOARD;
import static com.psw.tetris.utils.Constants.UI.Buttons.BUTTON_TYPE.NEW_GAME;
import static com.psw.tetris.utils.Constants.UI.Buttons.BUTTON_TYPE.SETTINGS;

import com.psw.tetris.gameStates.GameStateHandler;
import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.gameStates.State;
import com.psw.tetris.main.Game;
import com.psw.tetris.ui.MenuButton;
import com.psw.tetris.utils.LoadSave;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class MainMenu extends State {

  private MenuButton newGameButton;
  private MenuButton leaderboardButton;
  private MenuButton settingsButton;
  private MenuButton aboutUsButton;
  private MenuButton exitButton;

  private MenuButton[] Buttons = {
    newGameButton, leaderboardButton, settingsButton, aboutUsButton, exitButton
  };

  private BufferedImage menuBackground;

  private final double SCALE = 0.25;
  private final int FIRST_BUTTON_X = 100;
  private final int FIRST_BUTTON_Y = 300;
  private final int BUTTON_SPACING = 25;

  public MainMenu() {
    super(GameStatesEnum.MAIN_MENU);

    menuBackground = LoadSave.loadBackground("mainMenu.png");
    newGameButton =
        new MenuButton(FIRST_BUTTON_X, FIRST_BUTTON_Y, NEW_GAME, SCALE, GameStatesEnum.PLAYING);

    int secondButtonY =
        (int) (FIRST_BUTTON_Y + newGameButton.getBounds().getHeight() + BUTTON_SPACING);
    leaderboardButton = new MenuButton(FIRST_BUTTON_X, secondButtonY, LEADERBOARD, SCALE, null);

    int thirdButtonY =
        (int) (secondButtonY + leaderboardButton.getBounds().getHeight() + BUTTON_SPACING);
    settingsButton = new MenuButton(FIRST_BUTTON_X, thirdButtonY, SETTINGS, SCALE, null);

    int fourthButtonY =
        (int) (thirdButtonY + settingsButton.getBounds().getHeight() + BUTTON_SPACING);
    aboutUsButton = new MenuButton(FIRST_BUTTON_X, fourthButtonY, ABOUT_US, SCALE, null);

    int fifthButtonY =
        (int) (fourthButtonY + aboutUsButton.getBounds().getHeight() + BUTTON_SPACING);
    exitButton = new MenuButton(FIRST_BUTTON_X, fifthButtonY, EXIT_GAME, SCALE, null);

    Buttons[0] = newGameButton;
    Buttons[1] = leaderboardButton;
    Buttons[2] = settingsButton;
    Buttons[3] = aboutUsButton;
    Buttons[4] = exitButton;
  }

  @Override
  public void render(Graphics g) {
    g.drawImage(menuBackground, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);

    for (MenuButton button : Buttons) {
      button.render(g);
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    for (MenuButton button : Buttons) {
      if (button.getBounds().contains(e.getPoint())) {
        button.applyGameState();
      }
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {

    switch (e.getKeyCode()) {
        // TODO: remove this
      case KeyEvent.VK_M:
        Game.initNetworking();
        GameStateHandler.setActiveState(GameStatesEnum.MAIN_MENU);
        break;
    }
  }
}
