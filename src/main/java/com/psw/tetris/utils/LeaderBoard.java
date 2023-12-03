package com.psw.tetris.utils;

import java.io.*;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;

import com.psw.tetris.gameStates.GameStateHandler;
import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;

import static com.psw.tetris.gameElements.gameplay.GameTime.getTimeStr;
import static com.psw.tetris.gameElements.gameplay.Levels.getCurrentLevel;
import static com.psw.tetris.gameElements.gameplay.Score.getScore;
import static com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum.GAME_OVER;
import static com.psw.tetris.gameStates.GameStateHandler.getActiveState;
import static com.psw.tetris.utils.Constants.RESOURCES_PATH;
import static com.psw.tetris.utils.Constants.UI.Buttons.LEADERBOARD;

public class LeaderBoard implements Serializable {
  private static int index;

  private int TotalScore;
  private String TimePlayed;
  private int LastLevel;

  private static LeaderBoard[] TopScore = new LeaderBoard[10];

  static {
    for (int i = 0; i < 10; i++) {
      TopScore[i] = new LeaderBoard();
    }

    index = 0;
  }

  public static void update() throws IOException {

    if (GameStateHandler.getActiveStateID().equals(GameStatesEnum.GAME_OVER)) {

      TopScore[index].TotalScore = getScore();
      TopScore[index].LastLevel = getCurrentLevel();// in progress
      TopScore[index].TimePlayed = getTimeStr();// in progress

      try {
        FileOutputStream f1 = new FileOutputStream(RESOURCES_PATH + "/Scores.txt");
        ObjectOutputStream out = new ObjectOutputStream(f1);
        for (int i = 0; i < 10; i++) {
          out.writeObject(TopScore[i]);
        }
        out.close();
        index++;
      } catch (IOException e) {
        e.printStackTrace();
      }

      if (GameStateHandler.getActiveStateID().equals(GameStatesEnum.LEADERBOARD)) {

        try {
          FileInputStream f2 = new FileInputStream(RESOURCES_PATH + "/Scores.txt");
          ObjectInputStream in = new ObjectInputStream(f2);
          LeaderBoard topScores = (LeaderBoard) in.readObject();
          in.close();
          System.out.println(topScores.TotalScore);
          System.out.println(topScores.LastLevel);
          System.out.println(topScores.TimePlayed);
        } catch (ClassNotFoundException e) {
          throw new RuntimeException(e);
        }
      }

    }
  }
}
