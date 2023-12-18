package com.apontadores.totris.gameStates.states.menus;

import static com.apontadores.totris.utils.Constants.FRAMES_PATH;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.apontadores.totris.gameStates.GameState;
import com.apontadores.totris.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.totris.ui.ImageElement;
import com.apontadores.totris.ui.SwitchStateAction;
import com.apontadores.totris.ui.Frame;

public class TitleScreen extends GameState {

  private final Frame frame;
  private final SwitchStateAction action = new SwitchStateAction();

  public TitleScreen() {
    super(GameStatesEnum.TITLE_SCREEN);
    frame = Frame.loadFromJson(FRAMES_PATH + "titleScreen.json");
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
    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
      action.exec(GameStatesEnum.USERNAME);
    }
  }

}
