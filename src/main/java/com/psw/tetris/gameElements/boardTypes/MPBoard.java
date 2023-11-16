package com.psw.tetris.gameElements.boardTypes;

import com.psw.tetris.gameElements.Board;
import java.awt.Color;

// Class to represent the board of a multiplayer opponent
public class MPBoard extends Board {
  private String username;

  public MPBoard(int size, int xOffset, int yOffset, Color color, String username) {
    super(size, xOffset, yOffset, color);
    this.username = username;
  }

  public void update(int row, Color[] colorLine) {
    for (int x = 0; x < colorLine.length; x++) {
      board.get(row).setColor(x, colorLine[x]);
    }
  }

  public String getUsername() {
    return username;
  }
}
