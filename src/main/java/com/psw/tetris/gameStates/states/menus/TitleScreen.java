package com.psw.tetris.gameStates.states.menus;

import static com.psw.tetris.utils.Constants.RESOURCES_PATH;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.psw.tetris.gameStates.GameState;
import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.ui.ImageElement;
import com.psw.tetris.ui.SwitchStateAction;
import com.psw.tetris.ui.Frame;

public class TitleScreen extends GameState {

  private final Frame frame;
  private final SwitchStateAction action = new SwitchStateAction();

  public TitleScreen() {
    super(GameStatesEnum.TITLE_SCREEN);
    frame = Frame.loadFromJson(RESOURCES_PATH + "/frames/titleScreen.json");
  }

  @Override
  public void render(final Graphics g) {
    frame.render(g);
  }

  @Override
  public void mouseClicked(final MouseEvent e) {
    ((ImageElement) frame.getAsset("enterButton"))
        .execIfClicked(e.getX(), e.getY(), action, GameStatesEnum.USERNAME);
  }

  @Override
  public void keyPressed(final KeyEvent e) {
    switch (e.getKeyCode()) {
      case KeyEvent.VK_ENTER:
        action.exec(GameStatesEnum.USERNAME);
        break;
    }
  }

}
