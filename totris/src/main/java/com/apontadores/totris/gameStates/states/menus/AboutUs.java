package com.apontadores.totris.gameStates.states.menus;

import static com.apontadores.totris.utils.Constants.FRAMES_PATH;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.apontadores.totris.gameStates.GameState;
import com.apontadores.totris.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.totris.ui.ImageElement;
import com.apontadores.totris.ui.SwitchStateAction;
import com.apontadores.totris.ui.Frame;

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
