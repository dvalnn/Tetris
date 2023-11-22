package com.psw.tetris.settings;

import java.awt.Color;

public class BoardSettings {

  public final int squareSize;
  public final int xOffset;
  public final int yOffset;
  public final int scoreRenderX;
  public final int scoreRenderY;
  public final Color backgroundColor;
  public final Color gridColor;

  public BoardSettings(
      final int squareSize,
      final int xOffset,
      final int yOffset,
      final Color backgroundColor,
      final Color gridColor) {

    this.squareSize = squareSize;
    this.xOffset = xOffset;
    this.yOffset = yOffset;
    this.backgroundColor = backgroundColor;
    this.gridColor = gridColor;

    // default magic numbers
    this.scoreRenderX = xOffset + 255;
    this.scoreRenderY = yOffset + 472;

  }
}
