package com.psw.tetris.gameStates.states.menus;

import static com.psw.tetris.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_WIDTH;
import static com.psw.tetris.utils.Constants.UI.Buttons.PRESS_ENTER;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.gameStates.GameState;
import com.psw.tetris.ui.Button;
import com.psw.tetris.ui.SwitchStateAction;
import com.psw.tetris.utils.LoadSave;

public class TitleScreen extends GameState {

  private final BufferedImage titleScreen;

  private final int buttonX = GAME_WIDTH / 2;
  private final int buttonY = GAME_HEIGHT - 100;

  private final Button button = new Button(
      new Point(buttonX, buttonY),
      LoadSave.loadImage(PRESS_ENTER),
      0.25);

  private final SwitchStateAction btnAction = new SwitchStateAction();

  public TitleScreen() {
    super(GameStatesEnum.TITLE_SCREEN);
    titleScreen = LoadSave.loadBackground("titlescreen.png");
  }

  @Override
  public void render(final Graphics g) {
    g.drawImage(titleScreen, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
    button.render(g);
  }

  @Override
  public void mouseClicked(final MouseEvent e) {
    button.execIfClicked(
        e.getPoint(),
        btnAction,
        GameStatesEnum.MAIN_MENU);
  }

  @Override
  public void keyPressed(final KeyEvent e) {
    switch (e.getKeyCode()) {
      case KeyEvent.VK_ENTER:
        button.exec(btnAction, GameStatesEnum.MAIN_MENU);
        break;
    }
  }

}