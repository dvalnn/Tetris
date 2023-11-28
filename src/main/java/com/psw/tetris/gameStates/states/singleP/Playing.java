package com.psw.tetris.gameStates.states.singleP;

import static com.psw.tetris.utils.Constants.Directions.LEFT;
import static com.psw.tetris.utils.Constants.Directions.RIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.BOARD_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.BOARD_SQUARE;
import static com.psw.tetris.utils.Constants.GameConstants.BOARD_WIDTH;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_WIDTH;
import static com.psw.tetris.utils.Constants.TetrominoIDs.I;
import static com.psw.tetris.utils.Constants.TetrominoIDs.J;
import static com.psw.tetris.utils.Constants.TetrominoIDs.L;
import static com.psw.tetris.utils.Constants.TetrominoIDs.O;
import static com.psw.tetris.utils.Constants.TetrominoIDs.S;
import static com.psw.tetris.utils.Constants.TetrominoIDs.T;
import static com.psw.tetris.utils.Constants.TetrominoIDs.Z;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import com.psw.tetris.gameElements.Board;
import com.psw.tetris.gameElements.boards.PlayerBoard;
import com.psw.tetris.gameStates.GameState;
import com.psw.tetris.gameStates.GameStateHandler;
import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.main.Game;
import com.psw.tetris.settings.BoardSettings;
import com.psw.tetris.utils.LoadSave;


public class Playing extends GameState {

  private final PlayerBoard board;
  private final Color boardColor = new Color(20, 20, 20);

  private final BufferedImage background;
  private final BufferedImage foreground;

  private final int X_OFFSET = (GAME_WIDTH - BOARD_WIDTH * BOARD_SQUARE) / 2 + 10;
  private final int Y_OFFSET = (GAME_HEIGHT - BOARD_HEIGHT * BOARD_SQUARE) / 2 + 18;

  private boolean mouseButton1Pressed = false;
  private boolean mouseButton3Pressed = false;

  public Playing() {
    super(GameStatesEnum.PLAYING);

    BoardSettings set = new BoardSettings(
        BOARD_SQUARE,
        X_OFFSET,
        Y_OFFSET,
        boardColor,
        boardColor.brighter());

    board = new PlayerBoard(set);
    background = LoadSave.loadBackground("singlePlayerGame.png");
    foreground = LoadSave.loadBackground("singleEssentials2.png");
  }

  @Override
  public void update() {
    board.update();
  }

  @Override
  public void render(final Graphics g) {
    g.drawImage(background, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
    g.drawImage(foreground, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
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

    if (e.getKeyCode() == Game.getKeybind().rotatesLeft) {
      board.getTetromino().rotate(LEFT);
      return;

    }
    if (e.getKeyCode() == Game.getKeybind().rotatesRight) {
      board.getTetromino().rotate(RIGHT);
      return;

    }
    if (e.getKeyCode() == Game.getKeybind().movesLeft) {
      board.getTetromino().setLeft(true);
      return;

    }
    if (e.getKeyCode() == Game.getKeybind().movesRight) {
      board.getTetromino().setRight(true);
      return;

    }
    if (e.getKeyCode() == Game.getKeybind().softDrop) {
      board.getTetromino().setDown(true);
      return;

    }

    if (e.getKeyCode() == Game.getKeybind().hardDrop) {
      board.getTetromino().setDrop(true);
      return;

    }
    if (e.getKeyCode() == Game.getKeybind().hold) {
      board.holdTetromino();
      return;

    }
    if (e.getKeyCode() == Game.getKeybind().debug) {
      board.toggleDebugMode();
      return;

    }
    if (e.getKeyCode() == Game.getKeybind().restart) {
      board.reset();
      return;

    }
    if (e.getKeyCode() == Game.getKeybind().pause) {
      // changes the game state to pause
      GameStateHandler.switchState(GameStatesEnum.PAUSE);
      return;
    }
    if (e.getKeyCode() == Game.getKeybind().toggleGrid) {
      //board.toggleGrid();
      return;

    }
    if (e.getKeyCode() == Game.getKeybind().debugIShape) {
      board.setTetromino(I);
      return;

    }
    if (e.getKeyCode() == Game.getKeybind().debugJShape) {
      board.setTetromino(J);
      return;

    }
    if (e.getKeyCode() == Game.getKeybind().debugLShape) {
      board.setTetromino(L);
      return;

    }
    if (e.getKeyCode() == Game.getKeybind().debugOShape) {
      board.setTetromino(O);
      return;

    }
    if (e.getKeyCode() == Game.getKeybind().debugSShape) {
      board.setTetromino(S);
      return;

    }
    if (e.getKeyCode() == Game.getKeybind().debugTShape) {
      board.setTetromino(T);
      return;

    }
    if (e.getKeyCode() == Game.getKeybind().debugZShape) {
      board.setTetromino(Z);
      return;
    }
    
    return;
  }

  @Override
  public void keyReleased(final KeyEvent e) {

    if (e.getKeyCode() == Game.getKeybind().movesLeft) {
        board.getTetromino().setLeft(false);
      return;
    }

    if (e.getKeyCode() == Game.getKeybind().softDrop) {
        board.getTetromino().setDown(false);
      return;
    }

    if (e.getKeyCode() == Game.getKeybind().movesRight) {
        board.getTetromino().setRight(false);
      return;
    }

    if (e.getKeyCode() == Game.getKeybind().hardDrop) {
        board.getTetromino().setDrop(false);
      return;
    }
    return;
  }

  @Override
  public void windowLostFocus() {
  }

  public Board getBoard() {
    return board;
  }
}
