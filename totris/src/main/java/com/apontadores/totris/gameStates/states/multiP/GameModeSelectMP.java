package com.apontadores.totris.gameStates.states.multiP;

import static com.apontadores.totris.utils.Constants.FRAMES_PATH;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.apontadores.totris.gameStates.GameState;
import com.apontadores.totris.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.totris.main.Game;
import com.apontadores.totris.ui.Frame;
import com.apontadores.totris.ui.ImageElement;
import com.apontadores.totris.ui.SwitchStateAction;

public class GameModeSelectMP extends GameState {

  private final Frame frame;
  SwitchStateAction switchState = new SwitchStateAction();

  public GameModeSelectMP() {
    super(GameStatesEnum.MODE_SELECT_MP);
    frame = Frame.loadFromJson(FRAMES_PATH + "modeSelectMP.json");
  }

  @Override
  public void render(final Graphics g) {
    frame.render(g);
  }

  @Override
  public void update() {
    frame.update();
  }

  @Override
  public void mouseClicked(final MouseEvent e) {

    final int x = e.getX();
    final int y = e.getY();

    ((ImageElement) frame.getElement("host"))
        .execIfClicked(x, y,
            (state) -> {
              try {
                Game.initClient();
                switchState.exec(state);
              } catch (final Exception e1) {
                e1.printStackTrace();
                switchState.exec(GameStatesEnum.MAIN_MENU);
              }
              return null;
            },
            GameStatesEnum.HOST);

    ((ImageElement) frame.getElement("join"))
        .execIfClicked(x, y,
            (state) -> {
              try {
                Game.initClient();
                switchState.exec(state);
              } catch (final Exception e1) {
                e1.printStackTrace();
                switchState.exec(GameStatesEnum.MAIN_MENU);
              }
              return null;
            },
            GameStatesEnum.JOIN);

    ((ImageElement) frame.getElement("return"))
        .execIfClicked(x, y, switchState, GameStatesEnum.MODE_SELECT);
  }
}
