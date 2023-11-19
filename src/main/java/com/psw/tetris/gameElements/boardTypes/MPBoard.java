package com.psw.tetris.gameElements.boardTypes;

import java.awt.Color;

import com.psw.tetris.gameElements.Board;

// Class to represent the board of a multiplayer opponent
public class MPBoard extends Board {
  private final String username;

  public MPBoard(final int size, final int xOffset, final int yOffset, final Color color, final String username) {
    super(size, xOffset, yOffset, color);
    this.username = username;
  }

  public void update(final int row, final Color[] colorLine) {
    for (int x = 0; x < colorLine.length; x++) {
      board.get(row).setColor(x, colorLine[x]);
    }
  }

  public String getUsername() {
    return username;
  }
}
