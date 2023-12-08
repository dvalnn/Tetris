package com.apontadores.gameStates.states.multiP;

import static com.apontadores.utils.Constants.RESOURCES_PATH;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.apontadores.gameStates.GameState;
import com.apontadores.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.main.Game;
import com.apontadores.ui.Frame;
import com.apontadores.ui.ImageElement;
import com.apontadores.ui.SwitchStateAction;

public class Join extends GameState {
  private Frame frame;
  SwitchStateAction switchState = new SwitchStateAction();

  public Join() {
    super(GameStatesEnum.JOIN);
    frame = Frame.loadFromJson(RESOURCES_PATH + "/frames/joinMP.json");
  }

  @Override
  public void render(Graphics g) {
    frame.render(g);

    // TODO: switch banner and toggle start button
    // when a player connects to the server
  }

  @Override
  public void update() {
    // FIXME: implement user input for this field
    Game.setRoomName("defaultRoom");

    if (Game.getClient() == null)
      try {
        Game.initClient();
      } catch (Exception e) {
        e.printStackTrace();
        switchState.exec(GameStatesEnum.MAIN_MENU);
      }

    frame.update();
  }

  @Override
  public void mouseClicked(MouseEvent e) {

    int x = e.getX();
    int y = e.getY();

    // TODO: when join button is clicked, submit ip to client
    ((ImageElement) frame.getElement("join"))
        .execIfClicked(x, y,
            (state) -> {
              if (Game.getClient().connectToServer(
                  ((ImageElement) frame.getElement("IP"))
                      .getTextElement()
                      .getText()))
                switchState.exec(state);
              return null;
            },
            GameStatesEnum.CONNECTING);

    // TODO: when make localhost button transition submit ip to client
    ((ImageElement) frame.getElement("localHost"))
        .execIfClicked(x, y,
            (state) -> {
              if (Game.getClient().connectToServer("127.0.0.1"))
                switchState.exec(state);
              return null;
            },
            GameStatesEnum.CONNECTING);

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

    ImageElement inputRoomName = ((ImageElement) frame.getElement("roomName"));
    inputRoomName.getTextElement().removeFocus();
    inputRoomName.execIfClicked(x, y,
        (Void) -> {
          inputRoomName.getTextElement().giveFocus();
          return null;
        },
        null);

  }

  @Override
  public void keyPressed(KeyEvent e) {
    ((ImageElement) frame.getElement("IP"))
        .getTextElement().keyboardInput(e);
    ((ImageElement) frame.getElement("roomName"))
        .getTextElement().keyboardInput(e);
  }
}
