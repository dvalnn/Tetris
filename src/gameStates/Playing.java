package gameStates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Color;

import main.Game;
import gameElements.Board;

import static utils.Constants.GameConstants.*;
import static utils.Constants.Directions.*;

public class Playing extends State implements StateMethods {

  private Board board;
  private Color boardColor = new Color(20, 20, 20);

  private final int X_OFFSET = GAME_WIDTH / 2 - BOARD_WIDTH * BOARD_SQUARE / 2;
  private final int Y_OFFSET = GAME_HEIGHT / 2 - BOARD_HEIGHT * BOARD_SQUARE / 2;

  public Playing(Game game) {
    super(game);
    board = new Board(BOARD_SQUARE, X_OFFSET, Y_OFFSET, boardColor);
  }

  @Override
  public void update() {
    board.update();
  }

  @Override
  public void render(Graphics g) {
    board.render(g);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
  }

  @Override
  public void mousePressed(MouseEvent e) {
  }

  @Override
  public void mouseReleased(MouseEvent e) {
  }

  @Override
  public void mouseMoved(MouseEvent e) {
  }

  @Override
  public void keyPressed(KeyEvent e) {
    switch (e.getKeyCode()) {
      case (KeyEvent.VK_Z):
        board.getTetromino().rotate(LEFT);
        break;

      case (KeyEvent.VK_X):
        board.getTetromino().rotate(RIGHT);
        break;

      case (KeyEvent.VK_LEFT):
        // board.getTetromino().setLeft(true);
        break;

      case (KeyEvent.VK_DOWN):
        // board.getTetromino().setDown(true);
        break;

      case (KeyEvent.VK_RIGHT):
        // board.getTetromino().setRight(true);
        break;

      case (KeyEvent.VK_SPACE):
        // board.getTetromino().setDrop(true);
        break;

      default:
        break;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    switch (e.getKeyCode()) {
      case (KeyEvent.VK_LEFT):
        // board.getTetromino().setLeft(false);
        break;

      case (KeyEvent.VK_DOWN):
        // board.getTetromino().setDown(false);
        break;

      case (KeyEvent.VK_RIGHT):
        // board.getTetromino().setRight(false);
        break;

      case (KeyEvent.VK_SPACE):
        // board.getTetromino().setDrop(false);
        break;

      default:
        break;
    }
  }

  @Override
  public void windowLostFocus() {
    // board.disableInputs();
  }

}
