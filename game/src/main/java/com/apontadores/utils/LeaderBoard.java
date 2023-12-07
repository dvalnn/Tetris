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

public class LeaderBoard implements Serializable {

  public static class TopScores extends LeaderBoard{

    private String username;
    private int score;
    private int level;

    private String time_in_string;
  }

  public static void saveScoreToLeaderBoard(String username, int score, int level) {

    ArrayList <Integer> scoreList = new ArrayList<Integer>();
    scoreList.add(score);


    // saves new score to file and updates the leaderboard
    // if the score is better than the 10th best score
    // if not it does nothing




  }

  public static String loadLeaderboard() {
    // loads the leaderboard from file
    // and returns a string with the leaderboard

    return null;
  }

  public static void update() throws IOException {
    TopScores top =new TopScores();
    if (GameStateHandler.getActiveStateID().equals(GameStatesEnum.GAME_OVER)) {

      top.score = getScore();
      top.level = getCurrentLevel();// in progress
      top.time_in_string = getTimeStr();
    }
      try {
        FileOutputStream f1 = new FileOutputStream(RESOURCES_PATH + "/Scores.txt");
        ObjectOutputStream out = new ObjectOutputStream(f1);
          out.writeObject(top);
          out.close();
      } catch (IOException e) {
        e.printStackTrace();
      }

      if (GameStateHandler.getActiveStateID().equals(GameStatesEnum.LEADERBOARD)) {

        try {
          FileInputStream f2 = new FileInputStream(RESOURCES_PATH + "/Scores.txt");
          ObjectInputStream in = new ObjectInputStream(f2);
          in.close();
          LeaderBoard topScores = (LeaderBoard) in.readObject();
          System.out.println(top.score);
          System.out.println(top.level);
          System.out.println(top.time_in_string);
        } catch (ClassNotFoundException | IOException e) {
          e.printStackTrace();
        }
      }

    }
  }

