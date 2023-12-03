
package com.apontadores.gameStates.states.multiP;

import static com.apontadores.utils.Constants.FRAMES_PATH;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.apontadores.gameStates.GameState;
import com.apontadores.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.ui.Frame;
import com.apontadores.ui.ImageElement;
import com.apontadores.ui.SwitchStateAction;

public class Connecting extends GameState {
  private Frame frame;
  SwitchStateAction switchState = new SwitchStateAction();

  public Connecting() {
    super(GameStatesEnum.CONNECTING);
    frame = Frame.loadFromJson(FRAMES_PATH + "connecting.json");
  }

  @Override
  public void render(Graphics g) {
    frame.render(g);

    // TODO: switch banner and toggle start button
    // when a player connects to the server
  }

  @Override
  public void update() {
    frame.update();
  }

  @Override
  public void mouseClicked(MouseEvent e) {

    int x = e.getX();
    int y = e.getY();

    // TODO: the return button should shutdonw the client
    ((ImageElement) frame.getElement("return"))
        .execIfClicked(x, y, switchState, GameStatesEnum.JOIN);
  }
}
