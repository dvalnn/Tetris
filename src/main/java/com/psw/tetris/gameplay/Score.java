package com.psw.tetris.gameplay;

import java.util.HashMap;

public class Score {

  private static int score = 0;

  private static boolean combo = false;
  private static Action prevAction = Action.NONE;

  public static enum Action {
    NONE, SINGLE, DOUBLE, TRIPLE, TETRIS, B2B_TETRIS, MINI_T_SPIN,
    MINI_T_SPIN_SINGLE, MINI_T_SPIN_DOUBLE, B2B_MINI_T_SPIN_DOUBLE, T_SPIN,
    T_SPIN_SINGLE, T_SPIN_DOUBLE, T_SPIN_TRIPLE, B2B_T_SPIN_SINGLE,
    B2B_T_SPIN_DOUBLE, B2B_T_SPIN_TRIPLE;

  }

  private static HashMap<Action, Integer> scoreMap = new HashMap<Action, Integer>();

  // TODO: implement action detection into the game for all actions
  static {
    scoreMap.put(Action.NONE, 0);

    scoreMap.put(Action.SINGLE, 100); // TODO: implement
    scoreMap.put(Action.DOUBLE, 300); // TODO: implement
    scoreMap.put(Action.TRIPLE, 500); // TODO: implement
    scoreMap.put(Action.TETRIS, 800); // TODO: implement
    scoreMap.put(Action.B2B_TETRIS, 1200); // TODO: implement

    scoreMap.put(Action.MINI_T_SPIN, 100); // TODO: implement
    scoreMap.put(Action.MINI_T_SPIN_SINGLE, 200); // TODO: implement
    scoreMap.put(Action.MINI_T_SPIN_DOUBLE, 400); // TODO: implement
    scoreMap.put(Action.B2B_MINI_T_SPIN_DOUBLE, 600); // TODO: implement

    scoreMap.put(Action.T_SPIN, 400); // TODO: implement
    scoreMap.put(Action.T_SPIN_SINGLE, 800); // TODO: implement
    scoreMap.put(Action.T_SPIN_DOUBLE, 1200); // TODO: implement
    scoreMap.put(Action.T_SPIN_TRIPLE, 1600); // TODO: implement
    scoreMap.put(Action.B2B_T_SPIN_SINGLE, 1200); // TODO: implement
    scoreMap.put(Action.B2B_T_SPIN_DOUBLE, 1800); // TODO: implement
    scoreMap.put(Action.B2B_T_SPIN_TRIPLE, 2400); // TODO: implement
  }

  public static void registerLinesCleared(int lines) {

    Score.Action action = Score.Action.NONE;

    switch (lines) {
      case 0:
        break;

      case 1:
        action = Score.scoreAction(Score.Action.SINGLE);
        break;

      case 2:
        action = Score.scoreAction(Score.Action.DOUBLE);
        break;

      case 3:
        action = Score.scoreAction(Score.Action.TRIPLE);
        break;

      case 4:
        action = Score.scoreAction(Score.Action.TETRIS);
        break;

    }

    System.out.println("[SCORE] Score: " + score);
    System.out.println("[SCORE] Action: " + action);
    System.out.println("[SCORE] Combo: " + combo);
  }

  public static Action scoreAction(Action action) {
    if (action == Action.NONE) {
      combo = false;
      prevAction = Action.NONE;
      return Action.NONE;
    }

    boolean updatePreviousAction = true;

    if (action == Action.TETRIS && prevAction == Action.TETRIS) {
      action = Action.B2B_TETRIS;
      combo = true;
      updatePreviousAction = false;
    } else if (action == Action.MINI_T_SPIN_DOUBLE && prevAction == Action.MINI_T_SPIN_DOUBLE) {
      action = Action.B2B_MINI_T_SPIN_DOUBLE;
      combo = true;
      updatePreviousAction = false;
    } else if (action == Action.T_SPIN_SINGLE && prevAction == Action.T_SPIN_SINGLE) {
      action = Action.B2B_T_SPIN_SINGLE;
      combo = true;
      updatePreviousAction = false;
    } else if (action == Action.T_SPIN_DOUBLE && prevAction == Action.T_SPIN_DOUBLE) {
      action = Action.B2B_T_SPIN_DOUBLE;
      combo = true;
      updatePreviousAction = false;
    } else if (action == Action.T_SPIN_TRIPLE && prevAction == Action.T_SPIN_TRIPLE) {
      action = Action.B2B_T_SPIN_TRIPLE;
      combo = true;
      updatePreviousAction = false;
    }

    int levelMultiplier = Levels.getCurrentLevel() + 1; // levels start at 0
    int actionModifier = scoreMap.get(action);
    int previousModifier = scoreMap.get(prevAction);

    if (2 * actionModifier < previousModifier) {
      combo = false;
    }

    int comboScore = 50;
    if (!combo) {
      comboScore = 0;
    }

    score += (actionModifier + comboScore) * levelMultiplier;

    if (updatePreviousAction)
      prevAction = action;

    return action;
  }

  public static void scoreSoftDrop() {
    score++;
  };

  public static void scoreHardDrop() {
    score += 2;
  }

  public static void resetScore() {
    score = 0;
  }

  public static int getScore() {
    return score;
  }

}
