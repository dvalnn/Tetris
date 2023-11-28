package com.psw.tetris.gameStates.states.multiP;

import static com.psw.tetris.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_WIDTH;
import static com.psw.tetris.utils.Constants.UI.Buttons.BUTTON_PATH;
import static com.psw.tetris.utils.Constants.UI.Buttons.HOST_GAME;
import static com.psw.tetris.utils.Constants.UI.Buttons.JOIN_GAME;
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
import org.apache.commons.validator.routines.InetAddressValidator;

import com.psw.tetris.gameStates.GameState;
import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.main.Game;
import com.psw.tetris.ui.Button;
import com.psw.tetris.ui.ButtonAction;
import com.psw.tetris.ui.SwitchStateAction;
import com.psw.tetris.utils.LoadSave;

public class GameModeSelectMP extends GameState {
  private final BufferedImage background;

  private final double buttonScale = 0.050;

  private final int pButtonX = 316;
  private final int pButtonY = 334;

  private final int mpButtonX = 706;
  private final int mpButtonY = 334;

  private final int returnButtonX = 40;
  private final int returnButtonY = 620;

  private final int inputFieldButtonCenterX = GAME_WIDTH / 2;
  private final int inputFieldButtonCenterY = GAME_HEIGHT / 2 + 200;
  private final int inputFieldX = 270;
  private final int inputFieldY = 42;

  private final int inputMaxLength = 15;
  private String inputText = "127.0.0.1";
  private Boolean defaultText = true;
  private Boolean validIP = true;

  private SwitchStateAction switchStateAction = new SwitchStateAction();
  private final Button inputFieldButton = new Button(
      new Point(inputFieldButtonCenterX, inputFieldButtonCenterY),
      LoadSave.loadImage(BUTTON_PATH + "playerName.png"), // change to IP Adress
      0.35);

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

    inputFieldButton.render(g);

    g.setColor(Color.WHITE);
    g.setFont(g.getFont().deriveFont(30f));
    Graphics2D g2 = (Graphics2D) g;

    g2.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

    g2.drawString(
        "Host IPV4: " + inputText,
        inputFieldButton.getAnchorPoint().x + inputFieldX,
        inputFieldButton.getAnchorPoint().y + inputFieldY);
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (defaultText) {
      inputText = "";
      defaultText = false;
    }

    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
      inputText = StringUtils.chop(inputText);
      return;
    }

    char c = e.getKeyChar();
    if (inputText.length() < inputMaxLength && Character.isDefined(c))
      inputText += c;

  }

  ButtonAction<Void, Void> initHostAndSwitch = (Void) -> {
    Game.hostMultiplayer();
    switchStateAction.exec(GameStatesEnum.PLAYING_MP);
    return null;
  };

  ButtonAction<String, Boolean> initClientAndSwitch = ipAddress -> {
    // verify ip address
    if (!InetAddressValidator.getInstance().isValid(ipAddress))
      return false;

    Game.connectMultiplayer(ipAddress);
    switchStateAction.exec(GameStatesEnum.PLAYING_MP);
    return true;
  };

  @Override
  public void mouseClicked(final MouseEvent e) {

    hostButton.execIfClicked(
        e.getPoint(),
        initHostAndSwitch,
        null);

    validIP = joinButton.execIfClicked(
        e.getPoint(),
        initClientAndSwitch,
        inputText);

    if (validIP != null && !validIP) {
      inputText = "";
      validIP = true;
    }

    returnButton.execIfClicked(
        e.getPoint(),
        switchStateAction,
        GameStatesEnum.GAME_MODE_SELECT);
  }

}
