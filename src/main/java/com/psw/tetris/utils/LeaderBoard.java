package com.psw.tetris.utils;
import java.io.*;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;

import static com.psw.tetris.gameElements.gameplay.GameTime.getTimeStr;
import static com.psw.tetris.gameElements.gameplay.Levels.getCurrentLevel;
import static com.psw.tetris.gameElements.gameplay.Score.getScore;
import static com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum.GAME_OVER;
import static com.psw.tetris.gameStates.GameStateHandler.getActiveState;
import static com.psw.tetris.utils.Constants.RESOURCES_PATH;
import static com.psw.tetris.utils.Constants.UI.Buttons.LEADERBOARD;

public class LeaderBoard implements Serializable {
    private int TotalScore;

    private String TimePlayed;

    private int LastLevel;

    public static void update() throws IOException {

        LeaderBoard[] TopScore = new LeaderBoard[10];

        for(int i = 0; i < 10; i++) {

            TopScore[i] = new LeaderBoard();
        }
        int j = 0;
        if (getActiveState().equals(GAME_OVER)) {

            TopScore[j].TotalScore = getScore();
            TopScore[j].LastLevel = getCurrentLevel();//in progress
            TopScore[j].TimePlayed = getTimeStr();//in progress
            j = j + 1;

            try {
                FileOutputStream f1 = new FileOutputStream(RESOURCES_PATH+ "/Scores.txt");
                ObjectOutputStream out = new ObjectOutputStream(f1);
                for(int i = 0; i<10 ; i++) {
                    out.writeObject(TopScore[i]);
                }
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (getActiveState().equals(LEADERBOARD)){

                try {
                    FileInputStream f2 = new FileInputStream(RESOURCES_PATH+ "/Scores.txt");
                    ObjectInputStream in = new ObjectInputStream(f2);
                    LeaderBoard topScores = (LeaderBoard) in.readObject();
                    in.close();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }


        }
    }
}



