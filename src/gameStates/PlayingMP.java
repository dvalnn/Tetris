package gameStates;

import static utils.Constants.Directions.*;
import static utils.Constants.GameConstants.*;

import gameElements.Board;
import gameElements.BoardMP;
import gameElements.Shape;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.net.InetAddress;
import main.Game;

public class PlayingMP extends State implements StateMethods {

  Color boardColor = new Color(20, 20, 20);
  Board playerBoard;
  BoardMP opponentBoard;
  private boolean matchOver = false;
  private boolean opponentDisconnected = false;

  private int networkTick = 0;
  private final int NETWORK_TICK_MAX = 5;

  // calculate the offsets so that the boards are centered
  // and the player's board is on the left and the opponent's
  private final int PLAYER_X_OFFSET = GAME_WIDTH / 4 - BOARD_WIDTH * BOARD_SQUARE / 2;
  private final int OPPONENT_X_OFFSET = 3 * GAME_WIDTH / 4 - BOARD_WIDTH * BOARD_SQUARE / 2;
  private final int Y_OFFSET = GAME_HEIGHT / 2 - BOARD_HEIGHT * BOARD_SQUARE / 2;

  public PlayingMP(Game game) {
    super(game);
    playerBoard = new Board(BOARD_SQUARE, PLAYER_X_OFFSET, Y_OFFSET, boardColor);
  }

  public void addBoardMP(String username, InetAddress address, int port) {
    System.out.println("[PlayingMP] Adding board for " + username);
    opponentBoard = new BoardMP(BOARD_SQUARE, OPPONENT_X_OFFSET, Y_OFFSET, boardColor, username);
  }

  public void removeBoardMP(String username) {
    System.out.println("[PlayingMP] " + username + " disconnected!");
    opponentDisconnected = true;
    matchOver = true;
  }

  public void sendPlayerState() {
    // send current player board to opponent
    for (int row = 0; row < BOARD_HEIGHT; row++) {
      for (int col = 0; col < BOARD_WIDTH; col++) {
        game.sendUpdate(row, col, playerBoard.getBoard()[row][col]);
      }
    }

    // send current player tetromino to opponent
    Shape shape = playerBoard.getTetromino().getShape();
    for (Point2D point : shape.getShape()) {
      int row = (int) point.getY();
      int col = (int) point.getX();
      game.sendUpdate(row, col, shape.getColor());
    }
  }

  @Override
  public void update() {
    if (opponentBoard == null || opponentDisconnected) {
      return;
    }

    networkTick++;
    if (networkTick >= NETWORK_TICK_MAX) {
      sendPlayerState();
      networkTick = 0;
    }

    playerBoard.update();
  }

  @Override
  public void render(Graphics g) {
    if (opponentBoard == null) {
      // TODO: make this look nicer
      g.setColor(Color.WHITE);
      g.drawRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
      g.setColor(Color.BLACK);
      g.drawString("Waiting for opponent...", GAME_WIDTH / 2, GAME_HEIGHT / 2);
    } else if (opponentDisconnected) {
      g.setColor(Color.RED);
      // draw message centered above both boards
      // TODO: make this look nicer
      g.drawString("Opponent disconnected!", GAME_WIDTH / 2, GAME_HEIGHT / 2);
    } else opponentBoard.render(g);

    playerBoard.render(g);
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
    if (matchOver) {
      GameState.state = GameState.GAME_OVER;
      return;
    }
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

  public BoardMP getOpponentBoard() {
    return opponentBoard;
  }
}
