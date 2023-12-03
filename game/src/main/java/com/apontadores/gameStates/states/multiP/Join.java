package com.apontadores.gameStates.states.multiP;

import static com.apontadores.utils.Constants.FRAMES_PATH;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.apontadores.gameStates.GameState;
import com.apontadores.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.ui.Frame;
import com.apontadores.ui.ImageElement;
import com.apontadores.ui.SwitchStateAction;

public class Join extends GameState {
  private Frame frame;
  SwitchStateAction switchState = new SwitchStateAction();

  public Join() {
    super(GameStatesEnum.JOIN);
    frame = Frame.loadFromJson(FRAMES_PATH + "joinMP.json");
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

    // TODO: when join button is clicked, submit ip to client
    ((ImageElement) frame.getElement("join"))
        .execIfClicked(x, y, switchState, GameStatesEnum.CONNECTING);

    // TODO: when make localhost button transition submit ip to client
    ((ImageElement) frame.getElement("localHost"))
        .execIfClicked(x, y, switchState, GameStatesEnum.CONNECTING);

    ((ImageElement) frame.getElement("return"))
        .execIfClicked(x, y, switchState, GameStatesEnum.MODE_SELECT_MP);

    // TODO: check if ip is valid and toggle join button
    ImageElement ipField = ((ImageElement) frame.getElement("IP"));
    ipField.getTextElement().removeFocus();
    ipField.execIfClicked(
        x, y,
        (Void) -> {
          ipField.getTextElement().giveFocus();
          return null;
        },
        null);

  }

  @Override
  public void keyPressed(KeyEvent e) {
    ((ImageElement) frame.getElement("IP"))
        .getTextElement().keyboardInput(e);
  }
}
