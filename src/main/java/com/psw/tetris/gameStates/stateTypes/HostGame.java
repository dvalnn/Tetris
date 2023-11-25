package com.psw.tetris.gameStates.stateTypes;

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
import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.main.Game;
import com.psw.tetris.ui.Button;
import com.psw.tetris.ui.SwitchGameStateAction;
import com.psw.tetris.utils.LoadSave;

public class HostGame extends GameState {

  private final Button returnButton;
  private final Button nameInputFieldButton;

  private final BufferedImage returnButtonImage = LoadSave.loadImage(RETURN_BUTTON);
  private final BufferedImage nameInputFieldButtonImage = LoadSave.loadImage(BUTTON_PATH + "playerName.png");

  private final BufferedImage background;

  private final int returnButtonX = 40;
  private final int returnButtonY = 620;

  private final int inputFieldButtonCenterX = GAME_WIDTH / 2;
  private final int nameInputFieldButtonCenterY = GAME_HEIGHT / 2;

  private final int inputFieldX = 270;
  private final int inputFieldY = 42;

  private final int nameMaxLength = 10;

  private final SwitchGameStateAction switchGameStateAction = new SwitchGameStateAction();

  private String nameInputText = "";

  // TODO: Add connection with the multiplayer logic
  // TODO: Add a way to get the IP address

  public HostGame() {
    super(GameStatesEnum.HOST_GAME);

    background = LoadSave.loadBackground("multiWaiting.png");

    returnButton = new Button(
        returnButtonX,
        returnButtonY,
        returnButtonImage,
        0.29);

    nameInputFieldButton = new Button(
        new Point(inputFieldButtonCenterX, nameInputFieldButtonCenterY),
        nameInputFieldButtonImage,
        0.35);

  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_ENTER) {

        Game.initNetworking();

        nameInputFieldButton.exec(
            switchGameStateAction,
            GameStatesEnum.PLAYING_MP);
        return;
    }

    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
      returnButton.exec(
          switchGameStateAction,
          GameStatesEnum.GAME_MODE_SELECT);
    }

    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {

        nameInputText = StringUtils.chop(nameInputText);
        return;
    }

      char c = e.getKeyChar();
      if (nameInputText.length() < nameMaxLength && Character.isDefined(c))
        nameInputText += c;
      Game.setUsername(nameInputText);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    returnButton.execIfClicked(
        e.getPoint(),
        switchGameStateAction,
        GameStatesEnum.GAME_MODE_SELECT_MP);
  }

  @Override
  public void render(Graphics g) {
    g.drawImage(background, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
    returnButton.render(g);
    nameInputFieldButton.render(g);

    g.setColor(Color.WHITE);
    g.setFont(g.getFont().deriveFont(30f));
    Graphics2D g2 = (Graphics2D) g;

    g2.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

    g2.drawString(
        nameInputText,
        nameInputFieldButton.getAnchorPoint().x + inputFieldX,
        nameInputFieldButton.getAnchorPoint().y + inputFieldY);
  }
}