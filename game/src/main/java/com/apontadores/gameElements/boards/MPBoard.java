package com.apontadores.gameElements.boards;

import java.awt.Color;

import com.apontadores.gameElements.Board;
import com.apontadores.settings.BoardSettings;

// Class to represent the board of a multiplayer opponent
public class MPBoard extends Board {

  public MPBoard(final BoardSettings set) {
    super(set);
  }

  public void update(final int row, final Color[] colorLine) {
    for (int x = 0; x < colorLine.length; x++) {
      board.get(row).setColor(x, colorLine[x]);
    }
  }

}
