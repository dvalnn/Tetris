package com.psw.tetris.gameStates.states.menus;

import static com.psw.tetris.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_WIDTH;
import static com.psw.tetris.utils.Constants.UI.Buttons.ABOUT_US;
import static com.psw.tetris.utils.Constants.UI.Buttons.EXIT_GAME;
import static com.psw.tetris.utils.Constants.UI.Buttons.LEADERBOARD;
import static com.psw.tetris.utils.Constants.UI.Buttons.NEW_GAME;
import static com.psw.tetris.utils.Constants.UI.Buttons.SETTINGS;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.gameStates.GameState;
import com.psw.tetris.main.Game;
import com.psw.tetris.ui.Button;
import com.psw.tetris.ui.ButtonAction;
import com.psw.tetris.ui.SwitchStateAction;
import com.psw.tetris.utils.LoadSave;

public class MainMenu extends GameState {

  private final Button newGameButton;
  private final Button leaderboardButton;
  private final Button settingsButton;
  private final Button aboutUsButton;
  private final Button exitButton;

  private final BufferedImage newGameButtonImage = LoadSave.loadImage(NEW_GAME);
  private final BufferedImage leaderboardButtonImage = LoadSave.loadImage(LEADERBOARD);
  private final BufferedImage settingsButtonImage = LoadSave.loadImage(SETTINGS);
  private final BufferedImage aboutUsButtonImage = LoadSave.loadImage(ABOUT_US);
  private final BufferedImage exitButtonImage = LoadSave.loadImage(EXIT_GAME);

  private final SwitchStateAction switchStateAction = new SwitchStateAction();
  private final ButtonAction<Void, Void> quitGameAction = (Void) -> {
    Game.exit();
    return null;
  };

  private final BufferedImage menuBackground;

  private final double SCALE = 0.25;
  private final int FIRST_BUTTON_X = 100;
  private final int FIRST_BUTTON_Y = 300;
  private final int BUTTON_SPACING = 25;

  public MainMenu() {
    super(GameStatesEnum.MAIN_MENU);

    menuBackground = LoadSave.loadBackground("mainMenu.png");
    newGameButton = new Button(
        FIRST_BUTTON_X,
        FIRST_BUTTON_Y,
        newGameButtonImage,
        SCALE);

    final int secondButtonY = (int) (FIRST_BUTTON_Y + newGameButton.getBounds().getHeight() + BUTTON_SPACING);
    leaderboardButton = new Button(
        FIRST_BUTTON_X,
        secondButtonY,
        leaderboardButtonImage,
        SCALE);

    final int thirdButtonY = (int) (secondButtonY + leaderboardButton.getBounds().getHeight() + BUTTON_SPACING);
    settingsButton = new Button(
        FIRST_BUTTON_X,
        thirdButtonY,
        settingsButtonImage,
        SCALE);

    final int fourthButtonY = (int) (thirdButtonY + settingsButton.getBounds().getHeight() + BUTTON_SPACING);
    aboutUsButton = new Button(
        FIRST_BUTTON_X,
        fourthButtonY,
        aboutUsButtonImage,
        SCALE);

    final int fifthButtonY = (int) (fourthButtonY + aboutUsButton.getBounds().getHeight() + BUTTON_SPACING);
    exitButton = new Button(
        FIRST_BUTTON_X,
        fifthButtonY,
        exitButtonImage,
        SCALE);
  }

  @Override
  public void render(final Graphics g) {
    g.drawImage(menuBackground, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);

    newGameButton.render(g);
    leaderboardButton.render(g);
    settingsButton.render(g);
    aboutUsButton.render(g);
    exitButton.render(g);
  }

  @Override
  public void mouseClicked(final MouseEvent e) {
    newGameButton.execIfClicked(
        e.getPoint(),
        switchStateAction,
        GameStatesEnum.GAME_MODE_SELECT);

    leaderboardButton.execIfClicked(
        e.getPoint(),
        switchStateAction,
        GameStatesEnum.LEADERBOARD); // TODO: implement leaderboard;

    settingsButton.execIfClicked(
        e.getPoint(),
        switchStateAction,
        GameStatesEnum.SETTINGS);

    aboutUsButton.execIfClicked(
        e.getPoint(),
        switchStateAction,
        GameStatesEnum.ABOUT_US); // TODO: implement about us section;

    exitButton.execIfClicked(
        e.getPoint(),
        quitGameAction,
        null);
  }

}
