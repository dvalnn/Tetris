
package com.apontadores.totris.gameStates.states.multiP;

import static com.apontadores.totris.utils.Constants.FRAMES_PATH;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.apontadores.totris.gameStates.GameState;
import com.apontadores.totris.gameStates.GameStateHandler;
import com.apontadores.totris.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.totris.main.Game;
import com.apontadores.totris.networking.NetworkControl.ConnectionPhases;
import com.apontadores.totris.ui.Frame;
import com.apontadores.totris.ui.ImageElement;
import com.apontadores.totris.ui.SwitchStateAction;

public class Connecting extends GameState {
  private final Frame frame;
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

    ((ImageElement) frame.getElement("connectionInfoBanner"))
        .getTextElement()
        .setText("Room Name: " + Game.getRoomName());

    switch (Game.getClient().getState()) {
      case RUNNING -> {
        if (Game.getClient().getPhase() == ConnectionPhases.PLAYING) {
          GameStateHandler.reloadState(GameStatesEnum.PLAYING_MP);
          switchState.exec(GameStatesEnum.PLAYING_MP);
        }
      }

      case CONNECTION_TIMEOUT -> {
        System.err.println("Connection timeout");
        switchState.exec(GameStatesEnum.JOIN);
      }

      case SOCKET_ERROR -> {
        System.err.println("Socket error");
        switchState.exec(GameStatesEnum.JOIN);
      }

      case ROOM_FULL -> {
        System.err.println("Room full");
        switchState.exec(GameStatesEnum.JOIN);
      }

      case USERNAME_IN_USE -> {
        System.err.println("Username in use");
        switchState.exec(GameStatesEnum.JOIN);
      }

      case ERROR -> {
        System.err.println("Error");
        switchState.exec(GameStatesEnum.JOIN);
      }

      default -> {
      }
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {

    int x = e.getX();
    int y = e.getY();

    ((ImageElement) frame.getElement("return"))
        .execIfClicked(x, y,
            (state) -> {
              Game.getClient().abortConnection();
              switchState.exec(state);
              return null;
            },
            GameStatesEnum.JOIN);
  }
}
