package com.apontadores.gameStates.states.menus;

import static com.apontadores.utils.Constants.FRAMES_PATH;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.apontadores.gameStates.GameState;
import com.apontadores.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.main.Game;
import com.apontadores.ui.Frame;
import com.apontadores.ui.ImageElement;
import com.apontadores.ui.SwitchStateAction;

public class Username extends GameState {

  private final Frame frame;

  public Username() {
    super(GameStatesEnum.USERNAME);
    frame = Frame.loadFromJson(FRAMES_PATH + "username.json");
  }

  SwitchStateAction switchAction = new SwitchStateAction();

  @Override
  public void mouseClicked(MouseEvent e) {
    // HACK: to make sure the input field is only focused when the user clicks
    // we need to first remove focus from the input field, then check if the
    // user clicked on the input field, and if so, give focus back to the
    // input field. Maybe the input field should have a method that checks if
    // it was clicked, and if so, gives focus to itself. But this works too.
    ImageElement inputField = ((ImageElement) frame.getElement("inputField"));
    inputField.getTextElement().removeFocus();
    inputField.execIfClicked(
        e.getX(),
        e.getY(),
        (Void) -> {
          inputField.getTextElement().giveFocus();
          return null;
        },
        null);

    ((ImageElement) frame.getElement("startButton"))
        .execIfClicked(
            e.getX(),
            e.getY(),
            (state) -> {
              Game.setUsername(inputField.getTextElement().getText());
              switchAction.exec(state);
              return null;
            },
            GameStatesEnum.MAIN_MENU);

  }

  @Override
  public void keyPressed(KeyEvent e) {
    // NOTE: the input field will only update if it has focus
    ((ImageElement) frame.getElement("inputField"))
        .getTextElement().keyboardInput(e);
  }

  @Override
  public void render(Graphics g) {
    frame.render(g);
  }

  @Override
  public void update() {
    frame.update();
  }

}
