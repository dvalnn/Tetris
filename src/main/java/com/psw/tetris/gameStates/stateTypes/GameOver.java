package com.psw.tetris.gameStates.stateTypes;

import static com.psw.tetris.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_WIDTH;
import static com.psw.tetris.utils.Constants.UI.Buttons.NEW_GAME;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.gameStates.GameState;
import com.psw.tetris.ui.Button;
import com.psw.tetris.ui.SwitchGameStateAction;
import com.psw.tetris.utils.LoadSave;

public class GameOver extends GameState {

  private final Button returnMenuButton;
  private final Button retryButton;

  private final BufferedImage returnButtonImage = LoadSave.loadImage(NEW_GAME);
  private final BufferedImage retryButtonImage = LoadSave.loadImage(NEW_GAME);

  private final BufferedImage gameOverBackground;

  private final double SCALE = 0.25;
  private final int FIRST_BUTTON_X = 100;
  private final int FIRST_BUTTON_Y = 200;
  private final int FIFTH_BUTTON_X = 700;

  private final SwitchGameStateAction switchGameStateAction = new SwitchGameStateAction();

  public GameOver() {
    super(GameStatesEnum.GAME_OVER);

    gameOverBackground = LoadSave.loadBackground("gameOver.png");

    returnMenuButton = new Button(
        FIRST_BUTTON_X,
        FIRST_BUTTON_Y,
        returnButtonImage,
        SCALE);

    retryButton = new Button(
        FIFTH_BUTTON_X,
        FIRST_BUTTON_Y,
        retryButtonImage,
        SCALE);
  }

  @Override
  public void render(final Graphics g) {
    g.drawImage(gameOverBackground, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);

    returnMenuButton.render(g);
    retryButton.render(g);

    /*
     * g.setColor(Color.BLACK);
     * g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
     * 
     * g.setColor(Color.WHITE);
     * g.drawString("Game Over", GAME_WIDTH / 2 - 50, GAME_HEIGHT / 2);
     */
  }

  @Override
  public void mouseClicked(final MouseEvent e) {
    returnMenuButton.execIfClicked(
        e.getPoint(),
        switchGameStateAction,
        GameStatesEnum.MAIN_MENU);

    retryButton.execIfClicked(
        e.getPoint(),
        switchGameStateAction,
        GameStatesEnum.PLAYING);
  }
}
