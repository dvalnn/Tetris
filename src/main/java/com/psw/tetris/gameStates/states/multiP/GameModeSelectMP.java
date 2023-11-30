package com.psw.tetris.gameStates.states.multiP;

import static com.psw.tetris.utils.Constants.RESOURCES_PATH;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.psw.tetris.gameStates.GameState;
import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.ui.Frame;
import com.psw.tetris.ui.ImageElement;
import com.psw.tetris.ui.SwitchStateAction;

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

    ((ImageElement) frame.getElement("join"))
        .execIfClicked(e.getX(), e.getY(), switchState, GameStatesEnum.JOIN);

    ((ImageElement) frame.getElement("host"))
        .execIfClicked(e.getX(), e.getY(), switchState, GameStatesEnum.HOST);

    ((ImageElement) frame.getElement("return"))
        .execIfClicked(e.getX(), e.getY(), switchState, GameStatesEnum.MODE_SELECT);
  }

}
