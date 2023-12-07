package com.apontadores.utils;

import java.io.*;
import java.io.Serializable;

import com.apontadores.gameStates.GameStateHandler;
import com.apontadores.gameStates.GameStateHandler.GameStatesEnum;

import static com.apontadores.gameElements.gameplay.GameTime.getTimeStr;
import static com.apontadores.gameElements.gameplay.Levels.getCurrentLevel;
import static com.apontadores.gameElements.gameplay.Score.getScore;
import static com.apontadores.utils.Constants.RESOURCES_PATH;
import java.util.ArrayList;
import java.util.Collections;
import com.apontadores.utils.LoadSave;

public class LeaderBoard implements Serializable {

  public static class TopScores extends LeaderBoard{

    private String username;
    private int score;
    private int level;

    private String time_in_string;
  }

  public static void saveScoreToLeaderBoard() {

    // saves new score to file and updates the leaderboard
    // if the score is better than the 10th best score
    // if not it does nothing
  }


  public static String loadLeaderboard() {

    return null;
  }

  public static void update()  {
    TopScores top =new TopScores();
    if (GameStateHandler.getActiveStateID().equals(GameStatesEnum.GAME_OVER)) {

      top.score = getScore();
      top.level = getCurrentLevel();
      top.time_in_string = getTimeStr();


      LoadSave.saveJson(RESOURCES_PATH + "/Scores.json", top);
    }
      LoadSave.loadJson(RESOURCES_PATH + "/Scores.json",TopScores.class);


    }
  }

