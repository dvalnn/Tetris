package com.psw.tetris.gameStates.stateTypes;

import static com.psw.tetris.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_WIDTH;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.gameStates.GameState;
import com.psw.tetris.main.Game;

public class GameOver extends GameState {

  public GameOver() {
    super(GameStatesEnum.GAME_OVER);
  }

  @Override
  public void render(final Graphics g) {
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

    g.setColor(Color.WHITE);
    g.drawString("Game Over", GAME_WIDTH / 2 - 50, GAME_HEIGHT / 2);
  }

  @Override
  public void mouseClicked(final MouseEvent e) {
    Game.exit();
  }
}
