package com.psw.tetris.gameStates.stateTypes;

import static com.psw.tetris.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_WIDTH;
import static com.psw.tetris.utils.Constants.UI.Buttons.MULTIPLAYER;
import static com.psw.tetris.utils.Constants.UI.Buttons.RETURN_BUTTON;
import static com.psw.tetris.utils.Constants.UI.Buttons.SINGLE_PLAYER;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import com.psw.tetris.gameStates.GameState;
import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.ui.Button;
import com.psw.tetris.ui.SwitchGameStateAction;
import com.psw.tetris.utils.LoadSave;

public class GameModeSelect extends GameState {

  private final BufferedImage background;

  private final double buttonScale = 0.35;

  private final int pButtonX = 316;
  private final int pButtonY = 334;

  private final int mpButtonX = 706;
  private final int mpButtonY = 334;

  private final int returnButtonX = 40;
  private final int returnButtonY = 620;

  private SwitchGameStateAction switchStateAction = new SwitchGameStateAction();

  private final Button playButton = new Button(
      pButtonX,
      pButtonY,
      LoadSave.loadImage(SINGLE_PLAYER),
      buttonScale);

  private final Button onlineButton = new Button(
      mpButtonX,
      mpButtonY,
      LoadSave.loadImage(MULTIPLAYER),
      buttonScale);

  private final Button returnButton = new Button(
      returnButtonX,
      returnButtonY,
      LoadSave.loadImage(RETURN_BUTTON),
      buttonScale);

  public GameModeSelect() {
    super(GameStatesEnum.GAME_MODE_SELECT);
    background = LoadSave.loadBackground("newGame.png");
  }

  @Override
  public void render(final Graphics g) {
    g.drawImage(background, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
    playButton.render(g);
    onlineButton.render(g);
    returnButton.render(g);
  }

  @Override
  public void mouseClicked(final MouseEvent e) {

    playButton.execIfClicked(
        e.getPoint(),
        switchStateAction,
        GameStatesEnum.LOBBY);

    onlineButton.execIfClicked(
        e.getPoint(),
        switchStateAction,
        GameStatesEnum.LOBBY); // TODO: add lobby mp

    returnButton.execIfClicked(
        e.getPoint(),
        switchStateAction,
        GameStatesEnum.MAIN_MENU);
  }

}
