package com.psw.tetris.gameStates.stateTypes;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import com.psw.tetris.utils.LoadSave;

import static com.psw.tetris.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_WIDTH;
import static com.psw.tetris.utils.Constants.UI.Buttons.HOST_GAME;
import static com.psw.tetris.utils.Constants.UI.Buttons.JOIN_GAME;
import static com.psw.tetris.utils.Constants.UI.Buttons.RETURN_BUTTON;

import com.psw.tetris.gameStates.GameState;
import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;

import com.psw.tetris.ui.Button;
import com.psw.tetris.ui.SwitchStateAction;

public class GameModeSelectMP extends GameState {

  private final BufferedImage background;

  private final double buttonScale = 0.35;

  private final int pButtonX = 316;
  private final int pButtonY = 334;

  private final int mpButtonX = 706;
  private final int mpButtonY = 334;

  private final int returnButtonX = 40;
  private final int returnButtonY = 620;

  private SwitchStateAction switchStateAction = new SwitchStateAction();

  private final Button hostButton = new Button(
      pButtonX,
      pButtonY,
      LoadSave.loadImage(HOST_GAME),
      buttonScale);

  private final Button joinButton = new Button(
      mpButtonX,
      mpButtonY,
      LoadSave.loadImage(JOIN_GAME),
      buttonScale);

  private final Button returnButton = new Button(
      returnButtonX,
      returnButtonY,
      LoadSave.loadImage(RETURN_BUTTON),
      buttonScale);

  public GameModeSelectMP() {
    super(GameStatesEnum.GAME_MODE_SELECT_MP);
    background = LoadSave.loadBackground("multiplayer.png");
  }

  @Override
  public void render(final Graphics g) {
    g.drawImage(background, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
    hostButton.render(g);
    joinButton.render(g);
    returnButton.render(g);
  }

  @Override
  public void mouseClicked(final MouseEvent e) {

    hostButton.execIfClicked(
        e.getPoint(),
        switchStateAction,
        GameStatesEnum.HOST_GAME);

    joinButton.execIfClicked(
        e.getPoint(),
        switchStateAction,
        GameStatesEnum.JOIN_GAME); // TODO: add lobby mp

    returnButton.execIfClicked(
        e.getPoint(),
        switchStateAction,
        GameStatesEnum.GAME_MODE_SELECT);
  }

}
