package com.psw.tetris.gameStates.stateTypes;

import static com.psw.tetris.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_WIDTH;
import static com.psw.tetris.utils.Constants.UI.Buttons.ABOUT_US;
import static com.psw.tetris.utils.Constants.UI.Buttons.EXIT_GAME;
import static com.psw.tetris.utils.Constants.UI.Buttons.LEADERBOARD;
import static com.psw.tetris.utils.Constants.UI.Buttons.NEW_GAME;
import static com.psw.tetris.utils.Constants.UI.Buttons.SETTINGS;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import com.psw.tetris.gameStates.GameStateHandler;
import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.gameStates.State;
import com.psw.tetris.main.Game;
import com.psw.tetris.ui.Button;
import com.psw.tetris.ui.ButtonAction;
import com.psw.tetris.ui.SwitchGameStateAction;
import com.psw.tetris.utils.LoadSave;

public class MainMenu extends State {

  private Button<GameStatesEnum, Void> newGameButton;
  private Button<GameStatesEnum, Void> leaderboardButton;
  private Button<GameStatesEnum, Void> settingsButton;
  private Button<GameStatesEnum, Void> aboutUsButton;
  private Button<Void, Void> exitButton;

  private BufferedImage newGameButtonImage = LoadSave.loadImage(NEW_GAME);
  private BufferedImage leaderboardButtonImage = LoadSave.loadImage(LEADERBOARD);
  private BufferedImage settingsButtonImage = LoadSave.loadImage(SETTINGS);
  private BufferedImage aboutUsButtonImage = LoadSave.loadImage(ABOUT_US);
  private BufferedImage exitButtonImage = LoadSave.loadImage(EXIT_GAME);

  private SwitchGameStateAction switchGameStateAction = new SwitchGameStateAction();
  private ButtonAction<Void, Void> quitGameAction = (Void) -> {
    Game.exit();
    return null;
  };

  private BufferedImage menuBackground;

  private final double SCALE = 0.25;
  private final int FIRST_BUTTON_X = 100;
  private final int FIRST_BUTTON_Y = 300;
  private final int BUTTON_SPACING = 25;

  public MainMenu() {
    super(GameStatesEnum.MAIN_MENU);

    menuBackground = LoadSave.loadBackground("mainMenu.png");
    newGameButton = new Button<GameStatesEnum, Void>(
        FIRST_BUTTON_X,
        FIRST_BUTTON_Y,
        newGameButtonImage,
        SCALE,
        switchGameStateAction);

    int secondButtonY = (int) (FIRST_BUTTON_Y + newGameButton.getBounds().getHeight() + BUTTON_SPACING);
    leaderboardButton = new Button<GameStatesEnum, Void>(
        FIRST_BUTTON_X,
        secondButtonY,
        leaderboardButtonImage,
        SCALE,
        switchGameStateAction);

    int thirdButtonY = (int) (secondButtonY + leaderboardButton.getBounds().getHeight() + BUTTON_SPACING);
    settingsButton = new Button<GameStatesEnum, Void>(
        FIRST_BUTTON_X,
        thirdButtonY,
        settingsButtonImage,
        SCALE,
        switchGameStateAction);

    int fourthButtonY = (int) (thirdButtonY + settingsButton.getBounds().getHeight() + BUTTON_SPACING);
    aboutUsButton = new Button<GameStatesEnum, Void>(
        FIRST_BUTTON_X,
        fourthButtonY,
        aboutUsButtonImage,
        SCALE,
        switchGameStateAction);

    int fifthButtonY = (int) (fourthButtonY + aboutUsButton.getBounds().getHeight() + BUTTON_SPACING);
    exitButton = new Button<Void, Void>(
        FIRST_BUTTON_X,
        fifthButtonY,
        exitButtonImage,
        SCALE,
        quitGameAction);
  }

  @Override
  public void render(Graphics g) {
    g.drawImage(menuBackground, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);

    newGameButton.render(g);
    leaderboardButton.render(g);
    settingsButton.render(g);
    aboutUsButton.render(g);
    exitButton.render(g);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    if (newGameButton.getBounds().contains(e.getPoint())) {
      newGameButton.execAction(GameStatesEnum.PLAYING);
      return;
    }
    if (leaderboardButton.getBounds().contains(e.getPoint())) {
      // TODO: implement this feature
      // leaderboardButton.execAction(GameStatesEnum.LEADERBOARD);
      return;
    }
    if (settingsButton.getBounds().contains(e.getPoint())) {
      // TODO: implement this feature
      // settingsButton.execAction(GameStatesEnum.SETTINGS);
      return;
    }
    if (aboutUsButton.getBounds().contains(e.getPoint())) {
      // TODO: implement this feature
      // aboutUsButton.execAction(GameStatesEnum.ABOUT_US);
      return;
    }
    if (exitButton.getBounds().contains(e.getPoint())) {
      exitButton.execAction(null);
      return;
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {

    switch (e.getKeyCode()) {
      // TODO: remove this and add a multiplayer button
      case KeyEvent.VK_M:
        Game.initNetworking();
        GameStateHandler.setActiveState(GameStatesEnum.PLAYING_MP);
        break;
    }
  }
}
