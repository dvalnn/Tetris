package com.psw.tetris.gameStates.states.menus;

import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.main.Game;
import com.psw.tetris.ui.Button;
import com.psw.tetris.ui.ButtonAction;
import com.psw.tetris.ui.SwitchStateAction;
import com.psw.tetris.utils.LoadSave;

import com.psw.tetris.gameStates.GameState;
import com.psw.tetris.gameStates.GameStateHandler;

import static com.psw.tetris.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_WIDTH;
import static com.psw.tetris.utils.Constants.UI.Buttons.EXIT_BUTTON;
import static com.psw.tetris.utils.Constants.UI.Buttons.RESTART_BUTTON;
import static com.psw.tetris.utils.Constants.UI.Buttons.RESUME_BUTTON;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Pause extends GameState {

  private final Button resumeButton;
  private final Button restartButton;
  private final Button exitButton;

  private final BufferedImage resumeButtonImage = LoadSave.loadImage(RESUME_BUTTON);
  private final BufferedImage restartButtonImage = LoadSave.loadImage(RESTART_BUTTON);
  private final BufferedImage exitButtonImage = LoadSave.loadImage(EXIT_BUTTON);

  private final BufferedImage pauseBackground;

  private final double BUTTON_SCALE = 0.29;

  private final int ButtonX = 450;
  private final int ButtonY = 230;
  private final int BUTTON_SPACING = 25;
  private final int EXIT_BUTTON_SPACING = 75;

  private final SwitchStateAction switchGameStateAction = new SwitchStateAction();

  private final ButtonAction<Void, Void> quitGameAction = (Void) -> {
    Game.exit();
    return null;
  };

  public Pause() {
    super(GameStatesEnum.PAUSE);

    pauseBackground = LoadSave.loadBackground("gamePaused.png");

    resumeButton = new Button(
        ButtonX,
        ButtonY,
        resumeButtonImage,
        BUTTON_SCALE);

    final int secondButtonY = (int) (ButtonY + resumeButton.getBounds().getHeight() + BUTTON_SPACING);
    restartButton = new Button(
        ButtonX,
        secondButtonY,
        restartButtonImage,
        BUTTON_SCALE);

    final int thirdButtonY = (int) (secondButtonY + restartButton.getBounds().getHeight() + EXIT_BUTTON_SPACING);
    exitButton = new Button(
        ButtonX,
        thirdButtonY,
        exitButtonImage,
        BUTTON_SCALE);
  }

  @Override
  public void render(final Graphics g) {

    GameStateHandler.getState(GameStatesEnum.PLAYING).render(g);

    g.drawImage(pauseBackground, 420, GAME_HEIGHT / 4, GAME_WIDTH / 4 + 100,
        GAME_HEIGHT / 2 - 25, null);

    resumeButton.render(g);
    restartButton.render(g);
    exitButton.render(g);
  }

  SwitchStateAction switchStateAction = new SwitchStateAction();

  ButtonAction<GameStatesEnum, Void> reloadAndSwitch = (state) -> {
    GameStateHandler.reloadState(state);
    switchStateAction.exec(state);
    return null;
  };

  @Override
  public void mouseClicked(final MouseEvent e) {
    resumeButton.execIfClicked(
        e.getPoint(),
        switchGameStateAction,
        GameStatesEnum.PLAYING);

    // TODO: implement restart functon
    restartButton.execIfClicked(
        e.getPoint(),
        reloadAndSwitch,
        GameStatesEnum.PLAYING);

    exitButton.execIfClicked(
        e.getPoint(),
        quitGameAction,
        null);
  }

}
