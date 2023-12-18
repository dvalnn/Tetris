package com.apontadores.totris.gameElements.gameplay;

public class Levels {

  private static final int maxLevel = 29;
  private static int currentLevel = 0;

  private static int levelLinesCleared = 0;
  private static int totalLinesCleared = 0;

  public static int softDropMultiplier = 20;
  public static int hardDropMultiplier = 100;

  private static final int[] linesToNextLevel = {
      10, 20, 30, 40, 50, 60, 70, 80, 90, // 0-8
      100, 100, 100, 100, 100, 100, 100, // 9-15
      110, 120, 130, 140, 150, 160, 170, 180, 190, // 16-24
      200, 200, 200, 200, 200 // 25 + (max level)
  };

  private static final int[] levelSpeed = {
      78, 70, 61, 53, 45, 36, 28, 20, 11, 8, // 0-9
      6, 6, 6, // 10-13
      5, 5, 5, // 13-15
      4, 4, 4, // 16-18
      3, 3, 3, 3, 3, // 19-23
      2, 2, 2, 2, 2, // 24-28
      1 // 29 + (max level)
  };

  public static void reset() {
    Levels.currentLevel = 0;
    Levels.levelLinesCleared = 0;
    Levels.totalLinesCleared = 0;
  }

  public static int getTotalLinesCleared() {
    return totalLinesCleared;
  }

  public static int getLevelSpeed() {
    return levelSpeed[currentLevel];
  }

  public static int getCurrentLevel() {
    return currentLevel;
  }

  public static void registerLinesCleared(final int lines) {
    levelLinesCleared += lines;
    totalLinesCleared += lines;

    if (levelLinesCleared >= linesToNextLevel[currentLevel]) {
      levelLinesCleared = 0;
      currentLevel++;
    }

    if (currentLevel > maxLevel)
      currentLevel = maxLevel;
  }
}
