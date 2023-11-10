package com.psw.tetris.utils;

public class Constants {

  public static class UI {
    public static final String BUTTON_PATH =
        System.getProperty("user.dir") + "/src/main/resources/buttons/";
    public static final String BACKGROUND_PATH =
        System.getProperty("user.dir") + "/src/main/resources/backgrounds/";

    public static class Buttons {

      public static class BUTTON_TYPE {
        public static final int ABOUT_US = 0;
        public static final int CHANGE_GAME_INPUTS = 1;
        public static final int EXIT_GAME = 2;
        public static final int HOST_GAME = 3;
        public static final int JOIN_GAME = 4;
        public static final int LEADERBOARD = 5;
        public static final int MINUS_V1 = 6;
        public static final int MINUS_V2 = 7;
        public static final int MULTIPLAYER = 8;
        public static final int NEW_GAME = 9;
        public static final int PLUS_V1 = 10;
        public static final int PLUS_V2 = 11;
        public static final int PRESS_ENTER = 12;
        public static final int RETURN_BUTTON = 13;
        public static final int SETTINGS = 14;
        public static final int SINGLE_PLAYER = 15;
      }

      public static final String ABOUT_US = "aboutUs.png";
      public static final String CHANGE_GAME_INPUTS = "changeGameInputs.png";
      public static final String EXIT_GAME = "exitGame.png";
      public static final String HOST_GAME = "hostGame.png";
      public static final String JOIN_GAME = "joinGame.png";
      public static final String LEADERBOARD = "leaderboard.png";
      public static final String MINUS_V1 = "minusv1.png";
      public static final String MINUS_V2 = "minusv2.png";
      public static final String MULTIPLAYER = "multiplayer.png";
      public static final String NEW_GAME = "newGame.png";
      public static final String PLUS_V1 = "plusv1.png";
      public static final String PLUS_V2 = "plusv2.png";
      public static final String PRESS_ENTER = "pressEnter.png";
      public static final String RETURN_BUTTON = "returnButton.png";
      public static final String SETTINGS = "settings.png";
      public static final String SINGLE_PLAYER = "singlePlayer.png";

      public static final String[] BUTTONS = {
        ABOUT_US,
        CHANGE_GAME_INPUTS,
        EXIT_GAME,
        HOST_GAME,
        JOIN_GAME,
        LEADERBOARD,
        MINUS_V1,
        MINUS_V2,
        MULTIPLAYER,
        NEW_GAME,
        PLUS_V1,
        PLUS_V2,
        PRESS_ENTER,
        RETURN_BUTTON,
        SETTINGS,
        SINGLE_PLAYER
      };

      public static final int BUTTON_COUNT = 16;
    }
  }

  public static class GameConstants {
    public static final int FPS_SET = 60;
    public static final int UPS_SET = 100;
    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = 20;
    public static final int BOARD_SQUARE = 30;
    public static final int GAME_WIDTH = 1280;
    public static final int GAME_HEIGHT = 720;
  }

  public static class Directions {
    public static final int NONE = -1;
    public static final int UP = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
  }

  public static class TetrominoIDs {
    public static final int I = 0;
    public static final int T = 1;
    public static final int O = 2;
    public static final int J = 3;
    public static final int L = 4;
    public static final int S = 5;
    public static final int Z = 6;
  }
}
