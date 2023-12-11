package com.apontadores.utils;

import java.io.*;
import java.io.Serializable;

import com.apontadores.gameStates.GameStateHandler;
import com.apontadores.gameStates.GameStateHandler.GameStatesEnum;

import static com.apontadores.gameElements.gameplay.GameTime.getTimeStr;
import static com.apontadores.gameElements.gameplay.Levels.getCurrentLevel;
import static com.apontadores.gameElements.gameplay.Score.getScore;
import static com.apontadores.utils.Constants.RESOURCES_PATH;

import java.lang.reflect.Type;
import java.util.*;

import com.apontadores.utils.LoadSave;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class LeaderBoard implements Serializable {


    public static void saveScoreToLeaderBoard() {

    }
    // saves new score to file and updates the leaderboard
    // if the score is better than the 10th best score
    // if not it does nothing


    public static List<TopScores> loadLeaderboard() {

        Type listType = new TypeToken<ArrayList<TopScores>>() {
        }.getType();
        return LoadSave.loadJsonList(RESOURCES_PATH + "/Scores.json", listType);

    }

    public static void update() {


        //////

        if (GameStateHandler.getActiveStateID().equals(GameStatesEnum.GAME_OVER)) {


            //LoadSave.loadJson(RESOURCES_PATH + "/Scores.json",TopScores.class);
            TopScores newScore = new TopScores();

            newScore.setScore(getScore());
            newScore.setLevel(getCurrentLevel());
            newScore.setTime_in_string(getTimeStr());

            List<TopScores> topScores = loadLeaderboard();
            for (TopScores topScore : topScores) {
                if (topScore.equals(newScore)){
                    return;
                }
            }
            topScores.add(newScore);

            LoadSave.saveJson(RESOURCES_PATH + "/Scores.json", topScores);

        }
    }
}


