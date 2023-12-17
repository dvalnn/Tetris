
package com.apontadores.gameStates.states.multiP;

import static com.apontadores.utils.Constants.FRAMES_PATH;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.apontadores.gameStates.GameState;
import com.apontadores.gameStates.GameStateHandler;
import com.apontadores.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.main.Game;
import com.apontadores.networking.TetrisClient;
import com.apontadores.networking.NetworkControl.ClientStates;
import com.apontadores.ui.Frame;
import com.apontadores.ui.ImageElement;
import com.apontadores.ui.SwitchStateAction;

public class Host extends GameState {
  private final Frame frame;
  private boolean displayIP = false;
  private boolean updateText = true;
  SwitchStateAction switchState = new SwitchStateAction();
  private String roomName = "";

  String ip = "XXX.XXX.XXX.XXX";

  public Host() {
    super(GameStatesEnum.HOST);
    frame = Frame.loadFromJson(FRAMES_PATH + "hostMP.json");
  }

  @Override
  public void render(final Graphics g) {
    frame.render(g);
  }

  @Override
  public void update() {
    frame.update();

    TetrisClient client = Game.getClient();
    if (client.getState() == ClientStates.INACTIVE) {
      Game.initClient();
    }

    roomName = ((ImageElement) frame.getElement("roomName"))
        .getTextElement().getText();

    switch (client.getPhase()) {
      case DISCONNECTED -> {
        if (roomName.length() > 0)
          ((ImageElement) frame.getElement("start"))
              .enable();
        else
          ((ImageElement) frame.getElement("start"))
              .disable();

      }
      case WAITING_FOR_OPPONENT -> {
        ((ImageElement) frame.getElement("bannerWait"))
            .enable();
        ((ImageElement) frame.getElement("start"))
            .disable();
      }
      case PLAYING -> {
        GameStateHandler.reloadState(GameStatesEnum.PLAYING_MP);
        switchState.exec(GameStatesEnum.PLAYING_MP);
      }
      default -> {
      }
    }

    if (!updateText)
      return;

    if (!displayIP)
      ip = "XXX.XXX.XXX.XXX";
    else
      try {
        ip = InetAddress.getLocalHost().getHostAddress();
      } catch (UnknownHostException e) {
        ip = "Error getting local host IP";
      }

    ((ImageElement) frame.getElement("IP"))
        .getTextElement()
        .setText(ip);

    updateText = false;
  }

  @Override
  public void mouseClicked(final MouseEvent e) {

    final int x = e.getX();
    final int y = e.getY();

    ((ImageElement) frame.getElement("start"))
        .execIfClicked(e.getX(), e.getY(),
            (Void) -> {
              Game.initServer();
              Game.getClient().connectToServer(
                  "127.0.0.1",
                  Game.getUsername(),
                  roomName);
              return null;
            }, null);

    ((ImageElement) frame.getElement("return"))
        .execIfClicked(e.getX(), e.getY(),
            (state) -> {
              Game.stopServer();
              Game.getClient().finishConnection();
              switchState.exec(state);
              return null;
            }, GameStatesEnum.MODE_SELECT_MP);

    final ImageElement showIP = (ImageElement) frame.getElement("showIP");
    final ImageElement hideIP = (ImageElement) frame.getElement("hideIP");

    if (!displayIP)
      showIP.execIfClicked(
          x, y,
          (Void) -> {
            displayIP = true;
            updateText = true;
            hideIP.enable();
            showIP.disable();
            return null;
          },
          null);
    else
      hideIP.execIfClicked(
          x, y,
          (Void) -> {
            displayIP = false;
            updateText = true;
            hideIP.disable();
            showIP.enable();
            return null;
          },
          null);

    final ImageElement inputRoomName = ((ImageElement) frame.getElement("roomName"));
    inputRoomName.getTextElement().removeFocus();
    inputRoomName.execIfClicked(x, y,
        (Void) -> {
          inputRoomName.getTextElement().giveFocus();
          return null;
        },
        null);
  }

  @Override
  public void keyPressed(final KeyEvent e) {
    ((ImageElement) frame.getElement("roomName"))
        .getTextElement().keyboardInput(e);
  }
}
