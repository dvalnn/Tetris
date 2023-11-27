package com.psw.tetris.gameStates.states.singleP;

import static com.psw.tetris.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_WIDTH;
import static com.psw.tetris.utils.Constants.UI.Buttons.BUTTON_PATH;
import static com.psw.tetris.utils.Constants.UI.Buttons.RETURN_BUTTON;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import org.apache.commons.lang3.StringUtils;

import com.psw.tetris.gameStates.GameState;
import com.psw.tetris.gameStates.GameStateHandler;
import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.main.Game;
import com.psw.tetris.ui.Button;
import com.psw.tetris.ui.ButtonAction;
import com.psw.tetris.ui.SwitchStateAction;
import com.psw.tetris.utils.LoadSave;

public class Lobby extends GameState {

  private final BufferedImage background = LoadSave.loadBackground("multiWaiting.png");

  private final int returnButtonX = 40;
  private final int returnButtonY = 620;

  private final int inputFieldButtonCenterX = GAME_WIDTH / 2;
  private final int inputFieldButtonCenterY = GAME_HEIGHT / 2;

  private final int inputFieldX = 270;
  private final int inputFieldY = 42;

  private final int inputMaxLength = 10;
  private String inputText = "";

  private final Button returnButton = new Button(
      returnButtonX,
      returnButtonY,
      LoadSave.loadImage(RETURN_BUTTON),
      0.050);

  private final Button inputFieldButton = new Button(
      new Point(inputFieldButtonCenterX, inputFieldButtonCenterY),
      LoadSave.loadImage(BUTTON_PATH + "playerName.png"),
      0.050);

  public Lobby() {
    super(GameStatesEnum.LOBBY);
  }

  SwitchStateAction switchStateAction = new SwitchStateAction();

  ButtonAction<GameStatesEnum, Void> reloadAndSwitch = (state) -> {
    GameStateHandler.reloadState(state);
    switchStateAction.exec(state);
    return null;
  };

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
      inputFieldButton.exec(
          reloadAndSwitch,
          GameStatesEnum.PLAYING);
    }

    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
      returnButton.exec(
          reloadAndSwitch,
          GameStatesEnum.GAME_MODE_SELECT);
    }

    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
      inputText = StringUtils.chop(inputText);
      return;
    }

    char c = e.getKeyChar();
    if (inputText.length() < inputMaxLength && Character.isDefined(c))
      inputText += c;

    Game.setUsername(inputText);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    returnButton.execIfClicked(
        e.getPoint(),
        new SwitchStateAction(),
        GameStatesEnum.MAIN_MENU);
  }

  @Override
  public void render(Graphics g) {
    g.drawImage(background, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
    returnButton.render(g);
    inputFieldButton.render(g);

    g.setColor(Color.WHITE);
    g.setFont(g.getFont().deriveFont(30f));
    Graphics2D g2 = (Graphics2D) g;

    g2.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

    g2.drawString(
        inputText,
        inputFieldButton.getAnchorPoint().x + inputFieldX,
        inputFieldButton.getAnchorPoint().y + inputFieldY);
  }

}
