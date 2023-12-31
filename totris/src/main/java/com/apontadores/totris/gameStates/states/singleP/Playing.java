package com.apontadores.totris.gameStates.states.singleP;

import static com.apontadores.totris.utils.Constants.FRAMES_PATH;
import static com.apontadores.totris.utils.Constants.Directions.LEFT;
import static com.apontadores.totris.utils.Constants.Directions.RIGHT;
import static com.apontadores.totris.utils.Constants.GameConstants.BOARD_HEIGHT;
import static com.apontadores.totris.utils.Constants.GameConstants.BOARD_SQUARE;
import static com.apontadores.totris.utils.Constants.GameConstants.BOARD_WIDTH;
import static com.apontadores.totris.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.apontadores.totris.utils.Constants.GameConstants.GAME_WIDTH;
import static com.apontadores.totris.utils.Constants.TetrominoIDs.I;
import static com.apontadores.totris.utils.Constants.TetrominoIDs.J;
import static com.apontadores.totris.utils.Constants.TetrominoIDs.L;
import static com.apontadores.totris.utils.Constants.TetrominoIDs.O;
import static com.apontadores.totris.utils.Constants.TetrominoIDs.S;
import static com.apontadores.totris.utils.Constants.TetrominoIDs.T;
import static com.apontadores.totris.utils.Constants.TetrominoIDs.Z;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.apontadores.totris.gameElements.boards.PlayerBoard;
import com.apontadores.totris.gameStates.GameState;
import com.apontadores.totris.gameStates.GameStateHandler;
import com.apontadores.totris.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.totris.main.Game;
import com.apontadores.totris.settings.BoardSettings;
import com.apontadores.totris.ui.Frame;

public class Playing extends GameState {

  private final PlayerBoard board;

  private boolean mouseButton1Pressed;
  private boolean mouseButton3Pressed;
  private boolean usernameSet;

  private final Frame frame;

  public Playing() {
    super(GameStatesEnum.PLAYING);

    final Color boardColor = new Color(20, 20, 20);
    final int x_OFFSET = (GAME_WIDTH - BOARD_WIDTH * BOARD_SQUARE) / 2 + 10;
    final int y_OFFSET = (GAME_HEIGHT - BOARD_HEIGHT * BOARD_SQUARE) / 2 + 18;
    final BoardSettings set = new BoardSettings(
        BOARD_SQUARE,
        x_OFFSET,
        y_OFFSET,
        boardColor,
        boardColor.brighter());

    board = new PlayerBoard(set);
    frame = Frame.loadFromJson(FRAMES_PATH + "playing.json");
  }

  @Override
  public void update() {
    frame.update();
    board.update();

    if (!usernameSet) {
      board.setPlayerName(Game.getUsername());
    }

    if (board.isGameOver()) {
      GameStateHandler.switchState(GameStatesEnum.GAME_OVER);
    }
  }

  @Override
  public void render(final Graphics g) {
    frame.render(g);
    board.render(g);
  }

  @Override
  public void mouseClicked(final MouseEvent e) {
    if (e.getButton() == MouseEvent.BUTTON1)
      board.addBlockOnMousePosition(e.getX(), e.getY());
    else if (e.getButton() == MouseEvent.BUTTON3)
      board.removeBlockOnMousePosition(e.getX(), e.getY());
  }

  @Override
  public void mousePressed(final MouseEvent e) {
    if (e.getButton() == MouseEvent.BUTTON1)
      mouseButton1Pressed = true;
    else if (e.getButton() == MouseEvent.BUTTON3)
      mouseButton3Pressed = true;
  }

  @Override
  public void mouseReleased(final MouseEvent e) {
    if (e.getButton() == MouseEvent.BUTTON1)
      mouseButton1Pressed = false;
    else if (e.getButton() == MouseEvent.BUTTON3)
      mouseButton3Pressed = false;
  }

  @Override
  public void mouseDragged(final MouseEvent e) {
    if (mouseButton1Pressed && !mouseButton3Pressed)
      board.addBlockOnMousePosition(e.getX(), e.getY());
    else if (!mouseButton1Pressed && mouseButton3Pressed)
      board.removeBlockOnMousePosition(e.getX(), e.getY());
  }

  @Override
  public void keyPressed(final KeyEvent e) {

    if (e.getKeyCode() == Game.getKeybindings().rotatesLeft) {
      board.getTetromino().rotate(LEFT);
      return;
    }
    if (e.getKeyCode() == Game.getKeybindings().rotatesRight) {
      board.getTetromino().rotate(RIGHT);
      return;
    }
    if (e.getKeyCode() == Game.getKeybindings().movesLeft) {
      board.getTetromino().setLeft(true);
      return;
    }
    if (e.getKeyCode() == Game.getKeybindings().movesRight) {
      board.getTetromino().setRight(true);
      return;
    }
    if (e.getKeyCode() == Game.getKeybindings().softDrop) {
      board.getTetromino().setDown(true);
      return;
    }
    if (e.getKeyCode() == Game.getKeybindings().hardDrop) {
      board.getTetromino().setDrop(true);
      return;
    }
    if (e.getKeyCode() == Game.getKeybindings().hold) {
      board.holdTetromino();
      return;
    }
    if (e.getKeyCode() == Game.getKeybindings().debug) {
      board.toggleDebugMode();
      return;
    }
    if (e.getKeyCode() == Game.getKeybindings().restart) {
      board.reset();
      return;
    }
    if (e.getKeyCode() == Game.getKeybindings().pause) {
      // changes the game state to pause
      GameStateHandler.switchState(GameStatesEnum.PAUSE);
      return;
    }
    if (e.getKeyCode() == Game.getKeybindings().toggleGrid) {
      // board.toggleGrid();
      return;
    }
    if (e.getKeyCode() == Game.getKeybindings().debugIShape) {
      board.setTetromino(I);
      return;
    }
    if (e.getKeyCode() == Game.getKeybindings().debugJShape) {
      board.setTetromino(J);
      return;
    }
    if (e.getKeyCode() == Game.getKeybindings().debugLShape) {
      board.setTetromino(L);
      return;
    }
    if (e.getKeyCode() == Game.getKeybindings().debugOShape) {
      board.setTetromino(O);
      return;
    }
    if (e.getKeyCode() == Game.getKeybindings().debugSShape) {
      board.setTetromino(S);
      return;
    }
    if (e.getKeyCode() == Game.getKeybindings().debugTShape) {
      board.setTetromino(T);
      return;
    }
    if (e.getKeyCode() == Game.getKeybindings().debugZShape) {
      board.setTetromino(Z);
    }
  }

  @Override
  public void keyReleased(final KeyEvent e) {
    if (e.getKeyCode() == Game.getKeybindings().movesLeft) {
      board.getTetromino().setLeft(false);
      return;
    }
    if (e.getKeyCode() == Game.getKeybindings().softDrop) {
      board.getTetromino().setDown(false);
      return;
    }
    if (e.getKeyCode() == Game.getKeybindings().movesRight) {
      board.getTetromino().setRight(false);
      return;
    }
    if (e.getKeyCode() == Game.getKeybindings().hardDrop) {
      board.getTetromino().setDrop(false);
    }
  }

  @Override
  public void windowLostFocus() {
  }

}
