package com.psw.tetris.gameStates.states.menus;

import static com.psw.tetris.utils.Constants.RESOURCES_PATH;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.psw.tetris.gameStates.GameState;
import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.main.Game;
import com.psw.tetris.ui.ButtonElement;
import com.psw.tetris.ui.SwitchStateAction;
import com.psw.tetris.ui.UiFrame;

public class MainMenu extends GameState {

  private UiFrame frame;
  private final ButtonElement newGameButton;
  private final ButtonElement aboutButton;
  private final ButtonElement settingsButton;
  private final ButtonElement leaderboardButton;
  private final ButtonElement exitButton;

  private SwitchStateAction switchState = new SwitchStateAction();

  public MainMenu() {
    super(GameStatesEnum.MAIN_MENU);
    String path = RESOURCES_PATH + "/frames/mainMenu.json";
    frame = UiFrame.loadFromJson(path);

    newGameButton = frame.getAsset("newGame", ButtonElement.class);
    aboutButton = frame.getAsset("aboutUs", ButtonElement.class);
    settingsButton = frame.getAsset("settings", ButtonElement.class);
    leaderboardButton = frame.getAsset("leaderboard", ButtonElement.class);
    exitButton = frame.getAsset("exitGame", ButtonElement.class);
  }

  @Override
  public void render(Graphics g) {
    frame.render(g);
  }

  @Override
  public void update() {
    frame.update();
  }

  @Override
  public void mouseClicked(MouseEvent e) {

    int x = e.getX();
    int y = e.getY();

    newGameButton.execIfClicked(x, y, switchState, GameStatesEnum.GAME_MODE_SELECT);
    aboutButton.execIfClicked(x, y, switchState, GameStatesEnum.ABOUT_US);
    settingsButton.execIfClicked(x, y, switchState, GameStatesEnum.SETTINGS);
    leaderboardButton.execIfClicked(x, y, switchState, GameStatesEnum.LEADERBOARD);

    exitButton.execIfClicked(
        x, y,
        (Void) -> {
          Game.exit();
          return null;
        },
        null);
  }

}
