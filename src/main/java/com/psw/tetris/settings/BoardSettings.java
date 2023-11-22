package com.psw.tetris.settings;

import java.awt.Color;

public class BoardSettings {

  public final int squareSize;

  // render offsets
  public final int xOffset;
  public final int yOffset;

  // all of these are calculated based on the previous offsets
  public final int levelRenderX;
  public final int levelRenderY;
  public final int linesRenderX;
  public final int linesRenderY;
  public final int timerRenderX;
  public final int timerRenderY;
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

    this.linesRenderX = xOffset - 120;
    this.linesRenderY = yOffset + 310;
    this.levelRenderX = xOffset - 120;
    this.levelRenderY = yOffset + 390;
    this.timerRenderX = xOffset - 120;
    this.timerRenderY = yOffset + 472;

    this.scoreRenderX = xOffset + 255;
    this.scoreRenderY = yOffset + 472;
  }
}
