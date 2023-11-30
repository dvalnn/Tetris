package com.psw.tetris.gameStates.states.menus;

import static com.psw.tetris.utils.Constants.RESOURCES_PATH;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.psw.tetris.gameStates.GameState;
import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.ui.ImageElement;
import com.psw.tetris.ui.SwitchStateAction;
import com.psw.tetris.ui.Frame;

public class AboutUs extends GameState {

  private final Frame frame;
  private final SwitchStateAction stateAction = new SwitchStateAction();

  public AboutUs() {
    super(GameStatesEnum.ABOUT_US);
    frame = Frame.loadFromJson(RESOURCES_PATH + "/frames/aboutUs.json");
  }

  @Override
  public void render(final Graphics g) {
    frame.render(g);
  }

  @Override
  public void mouseClicked(final MouseEvent e) {
    ((ImageElement) frame.getElement("returnToMainMenu"))
        .execIfClicked(e.getX(), e.getY(), stateAction, GameStatesEnum.MAIN_MENU);
  }
}
