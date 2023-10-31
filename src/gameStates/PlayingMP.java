package gameStates;

import static utils.Constants.Directions.*;
import static utils.Constants.GameConstants.*;

import gameElements.Board;
import gameElements.BoardMP;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import main.Game;

public class PlayingMP extends State implements StateMethods {

  Color boardColor = new Color(20, 20, 20);
  Board playerBoard;
  List<BoardMP> opponentBoards = new ArrayList<BoardMP>();

  // calculate the offsets so that the boards are centered
  // and the player's board is on the left and the opponent's
  private final int PLAYER_X_OFFSET = GAME_WIDTH / 4 - BOARD_WIDTH * BOARD_SQUARE / 2;
  private final int OPPONENT_X_OFFSET = 3 * GAME_WIDTH / 4 - BOARD_WIDTH * BOARD_SQUARE / 2;
  private final int Y_OFFSET = GAME_HEIGHT / 2 - BOARD_HEIGHT * BOARD_SQUARE / 2;

  public PlayingMP(Game game) {
    super(game);
    playerBoard = new Board(BOARD_SQUARE, PLAYER_X_OFFSET, Y_OFFSET, boardColor);
  }

  @Override
  public void update() {
    if (opponentBoards.size() == 0) {
      return;
    }

    playerBoard.update();
    for (BoardMP board : opponentBoards) {
      board.update();
    }
  }

  @Override
  public void render(Graphics g) {
    playerBoard.render(g);
    if (opponentBoards.size() == 0) {
      g.drawString("Waiting for opponent...", GAME_WIDTH / 2, GAME_HEIGHT / 2);
    } else
      for (BoardMP board : opponentBoards) {
        board.render(g);
      }
  }

  @Override
  public void mouseClicked(MouseEvent e) {}

  @Override
  public void mousePressed(MouseEvent e) {}

  @Override
  public void mouseReleased(MouseEvent e) {}

  @Override
  public void mouseMoved(MouseEvent e) {}

  @Override
  public void mouseDragged(MouseEvent e) {}

  @Override
  public void keyPressed(KeyEvent e) {
    // same controls as playing.java
    switch (e.getKeyCode()) {
      case (KeyEvent.VK_Z):
        playerBoard.getTetromino().rotate(LEFT);
        break;

      case (KeyEvent.VK_X):
        playerBoard.getTetromino().rotate(RIGHT);
        break;

      case (KeyEvent.VK_LEFT):
        playerBoard.getTetromino().setLeft(true);
        break;

      case (KeyEvent.VK_DOWN):
        playerBoard.getTetromino().setDown(true);
        break;

      case (KeyEvent.VK_RIGHT):
        playerBoard.getTetromino().setRight(true);
        break;

      case (KeyEvent.VK_SPACE):
        playerBoard.getTetromino().setDrop(true);
        break;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    switch (e.getKeyCode()) {
      case (KeyEvent.VK_LEFT):
        playerBoard.getTetromino().setLeft(false);
        break;

      case (KeyEvent.VK_DOWN):
        playerBoard.getTetromino().setDown(false);
        break;

      case (KeyEvent.VK_RIGHT):
        playerBoard.getTetromino().setRight(false);
        break;

      case (KeyEvent.VK_SPACE):
        playerBoard.getTetromino().setDrop(false);
        break;

      default:
        break;
    }
  }

  @Override
  public void windowLostFocus() {}

  public void addBoardMP(String username, InetAddress address, int port) {
    System.out.println("[PlayingMP] Adding board for " + username);
    opponentBoards.add(
        new BoardMP(
            BOARD_SQUARE, OPPONENT_X_OFFSET, Y_OFFSET, boardColor, address, port, username));
  }

  public List<BoardMP> getOpponentBoards() {
    return opponentBoards;
  }
}
