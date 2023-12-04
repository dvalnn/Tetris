package com.apontadores.gameStates.states.menus;

import static com.apontadores.utils.Constants.RESOURCES_PATH;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.apontadores.gameStates.GameState;
import com.apontadores.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.ui.ImageElement;
import com.apontadores.ui.SwitchStateAction;
import com.apontadores.ui.Frame;

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
    ((ImageElement) frame.getElement("enterButton"))
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
