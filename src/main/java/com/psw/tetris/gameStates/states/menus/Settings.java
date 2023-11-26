package com.psw.tetris.gameStates.states.menus;

import static com.psw.tetris.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_WIDTH;
import static com.psw.tetris.utils.Constants.UI.Buttons.CHANGE_GAME_INPUTS;
import static com.psw.tetris.utils.Constants.UI.Buttons.MINUS_V1;
import static com.psw.tetris.utils.Constants.UI.Buttons.MINUS_V2;
import static com.psw.tetris.utils.Constants.UI.Buttons.PLUS_V1;
import static com.psw.tetris.utils.Constants.UI.Buttons.PLUS_V2;
import static com.psw.tetris.utils.Constants.UI.Buttons.RETURN_BUTTON;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;

import com.psw.tetris.gameStates.GameState;
import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.ui.Button;
import com.psw.tetris.ui.ButtonAction;
import com.psw.tetris.ui.SwitchStateAction;
import com.psw.tetris.utils.LoadSave;

public class Settings extends GameState {

  private final Button plusVolumeButton;
  private final Button minusVolumeButton;
  private final Button plusEffectsButton;
  private final Button minusEffectsButton;
  private final Button changeGameInputsButton;
  private final Button returnButton;

  private final BufferedImage plusVolumeButtonImage = LoadSave.loadImage(PLUS_V1);
  private final BufferedImage minusVolumeButtonImage = LoadSave.loadImage(MINUS_V1);
  private final BufferedImage plusEffectsButtonImage = LoadSave.loadImage(PLUS_V2);
  private final BufferedImage minusEffectsButtonImage = LoadSave.loadImage(MINUS_V2);
  private final BufferedImage changeGameInputsButtonImage = LoadSave.loadImage(CHANGE_GAME_INPUTS);
  private final BufferedImage returnButtonImage = LoadSave.loadImage(RETURN_BUTTON);

  private final BufferedImage settingsBackground;

  private final double SMALL_BUTTON_SCALE = 0.25;
  private final double CGI_BUTTON_SCALE = 0.29;
  private final double RETURN_BUTTON_SCALE = 0.29;// 0.35;

  private final int FIRST_SMALL_BUTTON_X = 420;
  private final int SECOND_SMALL_BUTTON_X = 500;
  private final int FIRST_BUTTON_Y = 202;

  private final int SMALLER_BUTTON_SPACING = 35;

  private final int cgiButtonX = 40;
  private final int cgiButtonY = 400;

  private final int returnButtonX = 40;
  private final int returnButtonY = 620;

  private final SwitchStateAction switchGameStateAction = new SwitchStateAction();

  private final ButtonAction<Void, Void> volumeManager = (Void) -> {
    return null;
  };
  private final ButtonAction<Void, Void> effectManager = (Void) -> {
    return null;
  };

  public Settings() {
    super(GameStatesEnum.SETTINGS);

    settingsBackground = LoadSave.loadBackground("settings.png");

    plusVolumeButton = new Button(
        FIRST_SMALL_BUTTON_X,
        FIRST_BUTTON_Y,
        plusVolumeButtonImage,
        SMALL_BUTTON_SCALE);

    minusVolumeButton = new Button(
        SECOND_SMALL_BUTTON_X,
        FIRST_BUTTON_Y,
        minusVolumeButtonImage,
        SMALL_BUTTON_SCALE);

    final int secondButtonY = (int) (FIRST_BUTTON_Y + plusVolumeButton.getBounds().getHeight()
        + SMALLER_BUTTON_SPACING);

    plusEffectsButton = new Button(
        FIRST_SMALL_BUTTON_X,
        secondButtonY,
        plusEffectsButtonImage,
        SMALL_BUTTON_SCALE);

    minusEffectsButton = new Button(
        SECOND_SMALL_BUTTON_X,
        secondButtonY,
        minusEffectsButtonImage,
        SMALL_BUTTON_SCALE);

    changeGameInputsButton = new Button(
        cgiButtonX,
        cgiButtonY,
        changeGameInputsButtonImage,
        CGI_BUTTON_SCALE);

    returnButton = new Button(
        returnButtonX,
        returnButtonY,
        returnButtonImage,
        RETURN_BUTTON_SCALE);

  }

  @Override
  public void render(final Graphics g) {

    g.drawImage(settingsBackground, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
    plusVolumeButton.render(g);
    minusVolumeButton.render(g);
    plusEffectsButton.render(g);
    minusEffectsButton.render(g);
    changeGameInputsButton.render(g);
    returnButton.render(g);
  }

  @Override
  public void mouseClicked(final MouseEvent e) {
    plusVolumeButton.execIfClicked(
        e.getPoint(),
        volumeManager,
        null);

    minusVolumeButton.execIfClicked(
        e.getPoint(),
        volumeManager,
        null);

    plusEffectsButton.execIfClicked(
        e.getPoint(),
        effectManager,
        null);

    minusEffectsButton.execIfClicked(
        e.getPoint(),
        effectManager,
        null);

    changeGameInputsButton.execIfClicked(
        e.getPoint(),
        switchGameStateAction,
        GameStatesEnum.CHANGE_KEYBINDS);

    returnButton.execIfClicked(
        e.getPoint(),
        switchGameStateAction,
        GameStatesEnum.MAIN_MENU);
  }

}
