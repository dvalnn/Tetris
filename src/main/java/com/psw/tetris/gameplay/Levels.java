package com.psw.tetris.gameplay;

public class Levels {

  private static final int maxLevel = 29;
  private static int currentLevel = 0;

  private static int levelLinesCleared = 0;
  private static int totalLinesCleared = 0;

  private static int[] linesToNextLevel = {
      10, 20, 30, 40, 50, 60, 70, 80, 90, // 0-8
      100, 100, 100, 100, 100, 100, 100, // 9-15
      110, 120, 130, 140, 150, 160, 170, 180, 190, // 16-24
      200, 200, 200, 200, 200 // 25 + (max level)
  };

  private static final int[] levelSpeeds = {
      48, 43, 38, 33, 28, 23, 18, 13, 8, 6, // 0-9
      5, 5, 5, // 10-13
      4, 4, 4, // 13-15
      3, 3, 3, // 16-18
      2, 2, 2, 2, 2, 2, 2, 2, 2, 2, // 19-28
      1 // 29 + (max level)
  };

  public static int softDropMultiplier = 2;

  public static int hardDropMultiplier = 20;

  public static int getTotalLinesCleared() {
    return totalLinesCleared;
  }

  public static int getLevelSpeed() {
    return levelSpeeds[currentLevel];
  }

  public static int getCurrentLevel() {
    return currentLevel;
  }

  public static void incrementLinesCleared(int lines) {
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
