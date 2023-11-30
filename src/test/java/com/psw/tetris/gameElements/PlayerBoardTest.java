package com.psw.tetris.gameElements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.awt.Color;

import org.junit.jupiter.api.Test;

import com.psw.tetris.gameElements.boards.PlayerBoard;
import com.psw.tetris.settings.BoardSettings;

public class PlayerBoardTest {
  private PlayerBoard board;
  private BoardSettings set;

  @Test
  public void holdTetrominoTest() {

    set = new BoardSettings(0, 0, 0, Color.BLACK, Color.BLACK);
    board = new PlayerBoard(set);

    // NOTE: before the first hold, holdTetromino() should return null
    Tetromino active = board.getTetromino(); // peça valida
    Tetromino next = board.getNextTetromino(); // peça valida
    Tetromino hold = board.getHoldTetromino();

    assertNotNull(active);
    assertNotNull(next);
    assertNull(hold);// antes de dar hold esta variavel tem que ser nula

    board.holdTetromino();

    // NOTE: after the first hold, holdTetromino() should be the previous
    // active tetromino. The active tetromino should be the next tetromino.
    Tetromino newActive = board.getTetromino();
    Tetromino newNext = board.getNextTetromino();
    Tetromino newHold = board.getHoldTetromino();

    assertNotNull(newNext);
    assertEquals(newHold.getShapeID(), active.getShapeID());
    assertEquals(newActive.getShapeID(), next.getShapeID());

    // NOTE: trying to hold again should not change the board.
    // holding is blocked until the active tetromino is placed.
    board.holdTetromino();
    Tetromino newActive2 = board.getTetromino();
    Tetromino newNext2 = board.getNextTetromino();
    Tetromino newHold2 = board.getHoldTetromino();

    assertEquals(newHold, newHold2);
    assertEquals(newActive, newActive2);
    assertEquals(newNext, newNext2);

  }
}
