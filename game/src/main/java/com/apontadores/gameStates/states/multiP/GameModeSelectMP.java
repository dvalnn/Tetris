package com.apontadores.gameStates.states.multiP;

import static com.apontadores.utils.Constants.RESOURCES_PATH;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.apontadores.gameStates.GameState;
import com.apontadores.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.ui.Frame;
import com.apontadores.ui.ImageElement;
import com.apontadores.ui.SwitchStateAction;

public class GameModeSelectMP extends GameState {

  private Frame frame;
  SwitchStateAction switchState = new SwitchStateAction();

  public GameModeSelectMP() {
    super(GameStatesEnum.MODE_SELECT_MP);
    frame = Frame.loadFromJson(RESOURCES_PATH + "/frames/modeSelectMP.json");
  }

  @Override
  public void render(Graphics g) {
    frame.render(g);
  }

  @Override
  public void update() {
    frame.update();
  }

  @Override
  public void mouseClicked(MouseEvent e) {

    int x = e.getX();
    int y = e.getY();

    ((ImageElement) frame.getElement("host"))
        .execIfClicked(x, y, switchState, GameStatesEnum.HOST);

    ((ImageElement) frame.getElement("join"))
        .execIfClicked(x, y, switchState, GameStatesEnum.JOIN);

    ((ImageElement) frame.getElement("return"))
        .execIfClicked(x, y, switchState, GameStatesEnum.MODE_SELECT);
  }
}