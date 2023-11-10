package com.psw.tetris.gameStates.stateTypes;

import static com.psw.tetris.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_WIDTH;

import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.gameStates.State;
import com.psw.tetris.main.Game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

public class GameOver extends State {

  public GameOver() {
    super(GameStatesEnum.GAME_OVER);
  }

  @Override
  public void render(Graphics g) {
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

    g.setColor(Color.WHITE);
    g.drawString("Game Over", GAME_WIDTH / 2 - 50, GAME_HEIGHT / 2);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    Game.exit();
  }
}