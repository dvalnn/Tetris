package com.psw.tetris.gameStates.stateTypes;

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

import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.gameplay.Levels;
import com.psw.tetris.gameplay.Score;
import com.google.gson.Gson;
import com.psw.tetris.gameElements.Board;
import com.psw.tetris.gameStates.GameState;
import com.psw.tetris.ui.Button;
import com.psw.tetris.ui.SwitchGameStateAction;
import com.psw.tetris.utils.LoadSave;

//TODO reset the game when the user clicks on the retry button

public class GameOver extends GameState {

  private final Button returnMenuButton;
  private final Button retryButton;

  private final BufferedImage returnButtonImage = LoadSave.loadImage(GAME_OVER_MAIN_MENU);
  private final BufferedImage retryButtonImage = LoadSave.loadImage(GAME_OVER_RETRY);

  private final BufferedImage gameOverBackground;
  private final BufferedImage background;
  private final BufferedImage foreground;

  private String username;
  private int score;
  private int linesCleared;
  private int minutes;
  private int seconds;

  private final double SCALE = 0.25;
  private final int FIRST_BUTTON_X = 290;
  private final int FIRST_BUTTON_Y = 450;
  private final int FIFTH_BUTTON_X = 420;

  private final SwitchGameStateAction switchGameStateAction = new SwitchGameStateAction();

  public GameOver() {
    super(GameStatesEnum.GAME_OVER);

    gameOverBackground = LoadSave.loadBackground("gameOverSingle.png");
    background = LoadSave.loadBackground("singlePlayerGame.png");
    foreground = LoadSave.loadBackground("singleEssentials2.png");

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

    minutes = Board.getTimeMinutes();
    seconds = Board.getTimeSeconds();

    g.drawImage(foreground, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
    g.drawImage(background, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
    g.drawImage(foreground, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);

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

    g2.drawString(String.format("%02d:%02d", minutes, seconds), GAME_WIDTH / 2 - 200, GAME_HEIGHT / 2 + 55);

    // jsonWrite("src/test/resources/test.json", username, score);
    // TODO: missing the leaderboard field

  }

  // write a function that writes all the data : score and name , to a json file
  // write a function that reads the data from the json file and displays it on
  // the leaderboard

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
