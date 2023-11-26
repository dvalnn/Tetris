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

    Tetromino active = board.getTetromino(); // peça valida
    Tetromino next = board.getNextTetromino(); // peça valida
    Tetromino hold = board.getHoldTetromino(); // null

    assertNotNull(active);
    assertNotNull(next);
    assertNull(hold);// antes de dar hold esta variavel tem que ser nula

    board.holdTetromino();

    Tetromino newActive = board.getTetromino();
    Tetromino newNext = board.getNextTetromino();
    Tetromino newHold = board.getHoldTetromino();

    assertNotNull(newNext);
    assertEquals(newHold, active);
    assertEquals(newActive.getShapeID(), next.getShapeID());

  }
}
