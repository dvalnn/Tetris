package com.apontadores.gameStates.states.multiP;

import static com.apontadores.utils.Constants.FRAMES_PATH;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import org.apache.commons.validator.routines.InetAddressValidator;

import com.apontadores.gameStates.GameState;
import com.apontadores.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.main.Game;
import com.apontadores.networking.NetworkControl.ClientStates;
import com.apontadores.ui.Frame;
import com.apontadores.ui.ImageElement;
import com.apontadores.ui.SwitchStateAction;

public class Join extends GameState {
  private final Frame frame;
  SwitchStateAction switchState = new SwitchStateAction();

  private final InetAddressValidator validator;

  public Join() {
    super(GameStatesEnum.JOIN);
    frame = Frame.loadFromJson(FRAMES_PATH + "joinMP.json");
    validator = new InetAddressValidator();
  }

  @Override
  public void render(Graphics g) {
    frame.render(g);
  }

  @Override
  public void update() {
    if (Game.getClient().getState() != ClientStates.RUNNING)
      Game.initClient();

    String roomName = ((ImageElement) frame.getElement("roomName"))
        .getTextElement().getText();

    String ip = ((ImageElement) frame.getElement("IP"))
        .getTextElement().getText();

    if (roomName.length() > 0 && ip.length() > 0
        && validator.isValidInet4Address(ip))
      ((ImageElement) frame.getElement("join")).enable();
    else
      ((ImageElement) frame.getElement("join")).disable();

    frame.update();
  }

  @Override
  public void mouseClicked(MouseEvent e) {

    int x = e.getX();
    int y = e.getY();

    Game.setRoomName(((ImageElement) frame.getElement("roomName"))
        .getTextElement()
        .getText());

    ((ImageElement) frame.getElement("join"))
        .execIfClicked(x, y,
            (state) -> {
              if (Game.getClient().connectToServer(
                  ((ImageElement) frame.getElement("IP"))
                      .getTextElement()
                      .getText(),
                  Game.getUsername(),
                  Game.getRoomName()))
                switchState.exec(state);
              return null;
            },
            GameStatesEnum.CONNECTING);

    ((ImageElement) frame.getElement("localHost"))
        .execIfClicked(x, y,
            (state) -> {
              if (Game.getClient().connectToServer(
                  "127.0.0.1",
                  Game.getUsername(),
                  Game.getRoomName()))
                switchState.exec(state);
              return null;
            },
            GameStatesEnum.CONNECTING);

    ((ImageElement) frame.getElement("return"))
        .execIfClicked(x, y, switchState, GameStatesEnum.MODE_SELECT_MP);

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
    if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
      switchState.exec(GameStatesEnum.MODE_SELECT_MP);

    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
      ((ImageElement) frame.getElement("IP"))
          .getTextElement()
          .removeFocus();

      ((ImageElement) frame.getElement("roomName"))
          .getTextElement()
          .removeFocus();
    }

    ((ImageElement) frame.getElement("IP"))
        .getTextElement().keyboardInput(e);
    ((ImageElement) frame.getElement("roomName"))
        .getTextElement().keyboardInput(e);
  }
}
