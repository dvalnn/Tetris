package com.psw.tetris.gameElements.boardTypes;

import java.awt.Color;

import com.psw.tetris.gameElements.Board;
import com.psw.tetris.settings.BoardSettings;

// Class to represent the board of a multiplayer opponent
public class MPBoard extends Board {

  public MPBoard(BoardSettings set) {
    super(set);
  }

  public void update(final int row, final Color[] colorLine) {
    for (int x = 0; x < colorLine.length; x++) {
      board.get(row).setColor(x, colorLine[x]);
    }
  }

}
