package com.psw.tetris.gameStates.states.singleP;

import static com.psw.tetris.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_WIDTH;
import static com.psw.tetris.utils.Constants.UI.Buttons.GAME_OVER_MAIN_MENU;
import static com.psw.tetris.utils.Constants.UI.Buttons.GAME_OVER_RETRY;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.FileWriter;

import com.google.gson.Gson;
import com.psw.tetris.gameElements.gameplay.GameTime;
import com.psw.tetris.gameElements.gameplay.Levels;
import com.psw.tetris.gameElements.gameplay.Score;
import com.psw.tetris.gameStates.GameState;
import com.psw.tetris.gameStates.GameStateHandler;
import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.ui.Button;
import com.psw.tetris.ui.ButtonAction;
import com.psw.tetris.ui.SwitchStateAction;
import com.psw.tetris.utils.LoadSave;

public class GameOver extends GameState {

  private final Button returnMenuButton;
  private final Button retryButton;

  private final BufferedImage returnButtonImage = LoadSave.loadImage(GAME_OVER_MAIN_MENU);
  private final BufferedImage retryButtonImage = LoadSave.loadImage(GAME_OVER_RETRY);

  private final BufferedImage gameOverBackground;

  private String username;
  private int score;
  private int linesCleared;

  private final double SCALE = 0.050;
  private final int FIRST_BUTTON_X = 290;
  private final int FIRST_BUTTON_Y = 450;
  private final int FIFTH_BUTTON_X = 420;

  private final SwitchStateAction switchGameStateAction = new SwitchStateAction();

  public GameOver() {
    super(GameStatesEnum.GAME_OVER);

    gameOverBackground = LoadSave.loadBackground("gameOverSingle.png");

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

  public void setUsername(final String username) {
    this.username = username;
  }

  @Override
  public void render(final Graphics g) {

    score = Score.getScore();
    linesCleared = Levels.getTotalLinesCleared();

    GameStateHandler.getState(GameStatesEnum.PLAYING).render(g);

    g.drawImage(gameOverBackground, GAME_WIDTH / 4 - 50, GAME_HEIGHT / 4 - 50, GAME_WIDTH / 2 + 100,
        GAME_HEIGHT / 2 + 100, null);

    returnMenuButton.render(g);
    retryButton.render(g);

    g.setColor(Color.WHITE);
    g.setFont(g.getFont().deriveFont(25f));
    Graphics2D g2 = (Graphics2D) g;

    g2.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

    g2.drawString(username, GAME_WIDTH / 2 - 200, GAME_HEIGHT / 2 - 130);
    g2.drawString("" + score, GAME_WIDTH / 2 - 200, GAME_HEIGHT / 2 - 68);
    g2.drawString("" + linesCleared, GAME_WIDTH / 2 - 150, GAME_HEIGHT / 2 - 7);
    g2.drawString(GameTime.getTimeStr(), GAME_WIDTH / 2 - 200, GAME_HEIGHT / 2 + 55);

    // jsonWrite("src/test/resources/test.json", username, score);
    // TODO: missing the leaderboard field

  }

  public void jsonWrite(final String pathWithFilename, final String name, final int score) {
    // writes the name and score to a json file
    try {
      final String json = new Gson().toJson(score);
      final String json2 = new Gson().toJson(name);
      final FileWriter writer = new FileWriter(pathWithFilename);
      writer.write(json);
      writer.write(json2);
      writer.close();
    } catch (final Exception e) {
      e.printStackTrace();
    }

  }

  SwitchStateAction switchStateAction = new SwitchStateAction();

  ButtonAction<GameStatesEnum, Void> reloadAndSwitch = (state) -> {
    GameStateHandler.reloadState(state);
    switchStateAction.exec(state);
    return null;
  };

  @Override
  public void mouseClicked(final MouseEvent e) {
    returnMenuButton.execIfClicked(
        e.getPoint(),
        switchGameStateAction,
        GameStatesEnum.MAIN_MENU);

    retryButton.execIfClicked(
        e.getPoint(),
        reloadAndSwitch,
        GameStatesEnum.PLAYING);
  }

}
