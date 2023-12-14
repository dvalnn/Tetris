package com.apontadores.gameElements.gameplay;

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
      80, 72, 63, 55, 47, 38, 30, 22, 13, 10, // 0-9
      8, 8, 8, // 10-13
      7, 7, 7, // 13-15
      5, 5, 5, // 16-18
      3, 3, 3, 3, 3, 3, 3, 3, 3, 3, // 19-28
      2 // 29 + (max level)
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
