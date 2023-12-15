package com.apontadores.gameStates.states.multiP;

import static com.apontadores.utils.Constants.FRAMES_PATH;

import java.awt.Graphics;

import com.apontadores.gameStates.GameState;
import com.apontadores.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.ui.Frame;

public class GameOverMP extends GameState {

  private final Frame frame;

  public GameOverMP(GameStatesEnum stateID) {
    super(stateID);
    frame = Frame.loadFromJson(FRAMES_PATH + "gameOverMP.json");
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
