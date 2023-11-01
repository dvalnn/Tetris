package gameStates;

import static utils.Constants.Directions.*;
import static utils.Constants.GameConstants.*;

import gameElements.Board;
import gameElements.BoardMP;
import gameElements.Shape;
import gameElements.ShapeMP;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import main.Game;

public class PlayingMP extends State implements StateMethods {

  Color boardColor = new Color(20, 20, 20);
  Board playerBoard;
  BoardMP opponentBoard;
  private ShapeMP shapeMP;
  private boolean matchOver = false;
  private boolean opponentDisconnected = false;

  private int networkTick = 0;
  private final int NETWORK_TICK_MAX = 2;

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
    shapeMP = new ShapeMP(BOARD_SQUARE, OPPONENT_X_OFFSET, Y_OFFSET);
  }

  public void removeBoardMP(String username) {
    System.out.println("[PlayingMP] " + username + " disconnected!");
    opponentDisconnected = true;
    matchOver = true;
  }

  public void sendPlayerState() {
    // send current player tetromino to opponent
    Shape currentShape = playerBoard.getTetromino().getShape();
    game.sendShapeUpdate(currentShape.getPoints(), currentShape.getColor());

    // send current player board to opponent
    for (int row = 0; row < BOARD_HEIGHT; row++) {
      for (int col = 0; col < BOARD_WIDTH; col++) {
        game.sendBoardUpdate(row, col, playerBoard.getBoard()[row][col]);
      }
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
    } else {
      // draw line separating the two boards
      g.setColor(Color.WHITE);
      g.drawLine(GAME_WIDTH / 2, 0, GAME_WIDTH / 2, GAME_HEIGHT);

      // draw opponent board
      opponentBoard.render(g);
      // draw opponnent controled shape
      shapeMP.render(g);
    }

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

  public ShapeMP getShapeMP() {
    return shapeMP;
  }
}
