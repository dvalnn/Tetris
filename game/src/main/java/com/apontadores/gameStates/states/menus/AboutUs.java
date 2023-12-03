package com.apontadores.gameStates.states.menus;

import static com.apontadores.utils.Constants.FRAMES_PATH;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.apontadores.gameStates.GameState;
import com.apontadores.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.ui.ImageElement;
import com.apontadores.ui.SwitchStateAction;
import com.apontadores.ui.Frame;

public class AboutUs extends GameState {

  private final Frame frame;
  private final SwitchStateAction stateAction = new SwitchStateAction();

  public AboutUs() {
    super(GameStatesEnum.ABOUT_US);
    frame = Frame.loadFromJson(FRAMES_PATH + "aboutUs.json");
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
