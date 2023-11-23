package com.psw.tetris.gameStates.stateTypes;

import static com.psw.tetris.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_WIDTH;
import static com.psw.tetris.utils.Constants.UI.Buttons.RETURN_BUTTON;

import com.psw.tetris.gameStates.GameState;
import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.ui.Button;
import com.psw.tetris.ui.SwitchGameStateAction;
import com.psw.tetris.utils.LoadSave;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Lobby extends GameState {

  private final BufferedImage background = LoadSave.loadBackground("multiWaiting.png");
  private static final GameStatesEnum stateID = GameStatesEnum.LOBBY;

  private static final double buttonScale = 0.25;
  private static final int returnButtonX = 40;
  private static final int returnButtonY = 620;

  private static final Button returnButton = new Button(
      returnButtonX,
      returnButtonY,
      LoadSave.loadImage(RETURN_BUTTON),
      buttonScale);

  public Lobby() {
    super(stateID);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    returnButton.execIfClicked(
        e.getPoint(),
        new SwitchGameStateAction(),
        GameStatesEnum.MAIN_MENU);

  }

  @Override
  public void render(Graphics g) {
    g.drawImage(background, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
    returnButton.render(g);
  }

}
