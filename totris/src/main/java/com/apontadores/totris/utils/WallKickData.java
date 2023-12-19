package com.apontadores.totris.utils;

import static com.apontadores.totris.utils.Constants.Directions.RIGHT;
import static com.apontadores.totris.utils.Constants.TetrominoIDs.I;

import java.awt.geom.Point2D;

public class WallKickData {

  // NOTE: Notation used:
  // UP - tetromino default orientation
  // RIGHT - tetromino rotated 90 degrees from default orientation
  // DOWN - tetromino rotated 180 degrees from default orientation
  // LEFT - tetromino rotated -90 degrees from default orientation
  // see: https://tetris.wiki/Super_Rotation_System

  // NOTE: UP to RIGHT rotation
  private static final int[][] DEFAULT_UR_KICK = { { 0, 0 }, { -1, 0 }, { -1, 1 }, { 0, -2 }, { -1, -2 } };

  // NOTE: RIGHT to UP rotation
  private static final int[][] DEFAULT_RU_KICK = { { 0, 0 }, { 1, 0 }, { 1, -1 }, { 0, 2 }, { 1, 2 } };

  // NOTE: RIGHT to DOWN rotation
  private static final int[][] DEFAULT_RD_KICK = { { 0, 0 }, { 1, 0 }, { 1, -1 }, { 0, 2 }, { 1, 2 } };

  // NOTE: DOWN to RIGHT rotation
  private static final int[][] DEFAULT_DR_KICK = { { 0, 0 }, { -1, 0 }, { -1, 1 }, { 0, -2 }, { -1, -2 } };

  // NOTE: DOWN to LEFT rotation
  private static final int[][] DEFAULT_DL_KICK = { { 0, 0 }, { 1, 0 }, { 1, 1 }, { 0, -2 }, { 1, -2 } };

  // NOTE: LEFT to DOWN rotation
  private static final int[][] DEFAULT_LD_KICK = { { 0, 0 }, { -1, 0 }, { -1, -1 }, { 0, 2 }, { -1, 2 } };

  // NOTE: LEFT to UP rotation
  private static final int[][] DEFAULT_LU_KICK = { { 0, 0 }, { -1, 0 }, { -1, -1 }, { 0, 2 }, { -1, 2 } };

  // NOTE: UP to LEFT rotation
  private static final int[][] DEFAULT_UL_KICK = { { 0, 0 }, { 1, 0 }, { 1, 1 }, { 0, -2 }, { 1, -2 } };

  // NOTE: Wall kick data for J, L, S, T, Z shape tetrominos
  // First index is the rotation state (UP, RIGHT, DOWN, LEFT)
  // second index is the diretion of the rotation (RIGHT, LEFT)
  // third index is the kick index (0-4)
  private static final int[][][][] DEFAULT_KICKS = {
      { DEFAULT_UR_KICK, DEFAULT_UL_KICK },
      { DEFAULT_RU_KICK, DEFAULT_RD_KICK },
      { DEFAULT_DR_KICK, DEFAULT_DL_KICK },
      { DEFAULT_LU_KICK, DEFAULT_LD_KICK }
  };

  // NOTE: I Piece UP to RIGHT rotation
  private static final int[][] ARIKA_I_UR_KICK = { { 0, 0 }, { -2, 0 }, { 1, 0 }, { 1, 2 }, { -2, -1 } };

  // NOTE: I Piece RIGHT to UP rotation
  private static final int[][] ARIKA_I_RU_KICK = { { 0, 0 }, { 2, 0 }, { -1, 0 }, { 2, 1 }, { -1, -2 } };

  // NOTE: I Piece RIGHT to DOWN rotation
  private static final int[][] ARIKA_I_RD_KICK = { { 0, 0 }, { -1, 0 }, { 2, 0 }, { -1, 2 }, { 2, -1 } };

  // NOTE: I Piece DOWN to RIGHT rotation
  private static final int[][] ARIKA_I_DR_KICK = { { 0, 0 }, { -2, 0 }, { 1, 0 }, { -2, 1 }, { 1, -1 } };

  // NOTE: I Piece DOWN to LEFT rotation
  private static final int[][] ARIKA_I_DL_KICK = { { 0, 0 }, { 2, 0 }, { -1, 0 }, { 2, 1 }, { -1, -1 } };

  // NOTE: I Piece LEFT to DOWN rotation
  private static final int[][] ARIKA_I_LD_KICK = { { 0, 0 }, { 1, 0 }, { -2, 0 }, { 1, 2 }, { -2, -1 } };

  // NOTE: I Piece LEFT to UP rotation
  private static final int[][] ARIKA_I_LU_KICK = { { 0, 0 }, { -2, 0 }, { 1, 0 }, { -2, 1 }, { 1, -2 } };

  // NOTE: I Piece UP to LEFT rotation
  private static final int[][] ARIKA_I_UL_KICK = { { 0, 0 }, { 2, 0 }, { -1, 0 }, { -1, 2 }, { 2, -1 } };

  // NOTE: Wall kick data for I shape tetrominos
  // Notation and indexs are the same as above
  // The data for these kicks follow the Arika SRS
  // standard as it provides the best wall kick
  // behavior for I shape tetrominos
  private static final int[][][][] ARIKA_KICKS = {
      { ARIKA_I_UR_KICK, ARIKA_I_UL_KICK },
      { ARIKA_I_RU_KICK, ARIKA_I_RD_KICK },
      { ARIKA_I_DR_KICK, ARIKA_I_DL_KICK },
      { ARIKA_I_LU_KICK, ARIKA_I_LD_KICK }
  };

  public static Point2D getKickData(
      final int shapeIndex, final int rotationState, final int direction, final int kickIndex) {
    if (shapeIndex == I) {
      return getArikaKickData(rotationState, direction, kickIndex);
    } else {
      return getDefaultKickData(rotationState, direction, kickIndex);
    }
  }

  private static Point2D getDefaultKickData(final int rotationState, final int direction, final int kickIndex) {
    final int directionIndex = direction == RIGHT ? 0 : 1;
    return new Point2D.Double(
        DEFAULT_KICKS[rotationState][directionIndex][kickIndex][0],
        DEFAULT_KICKS[rotationState][directionIndex][kickIndex][1]);
  }

  private static Point2D getArikaKickData(final int rotationState, final int direction, final int kickIndex) {
    final int directionIndex = direction == RIGHT ? 0 : 1;
    return new Point2D.Double(
        ARIKA_KICKS[rotationState][directionIndex][kickIndex][0],
        ARIKA_KICKS[rotationState][directionIndex][kickIndex][1]);
  }
}
