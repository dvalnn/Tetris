package com.psw.tetris.utils;

public class Constants {

  public static final String USER_DIR = System.getProperty("user.dir");
  public static final String RESOURCES_PATH = USER_DIR + "/src/main/resources";

  public static class UI {
    public static final String BACKGROUND_PATH = RESOURCES_PATH + "/backgrounds/";

    public static class Buttons {
      private static final String BUTTON_PATH = RESOURCES_PATH + "/buttons/";

      public static final String ABOUT_US = BUTTON_PATH + "aboutUs.png";
      public static final String EXIT_GAME = BUTTON_PATH + "exitGame.png";
      public static final String HOST_GAME = BUTTON_PATH + "hostGame.png";
      public static final String JOIN_GAME = BUTTON_PATH + "joinGame.png";
      public static final String LEADERBOARD = BUTTON_PATH + "leaderboard.png";
      public static final String MINUS_V1 = BUTTON_PATH + "minusv1.png";
      public static final String MINUS_V2 = BUTTON_PATH + "minusv2.png";
      public static final String MULTIPLAYER = BUTTON_PATH + "multiplayer.png";
      public static final String NEW_GAME = BUTTON_PATH + "newGame.png";
      public static final String PLUS_V1 = BUTTON_PATH + "plusv1.png";
      public static final String PLUS_V2 = BUTTON_PATH + "plusv2.png";
      public static final String PRESS_ENTER = BUTTON_PATH + "pressEnter.png";
      public static final String RETURN_BUTTON = BUTTON_PATH + "returnButton.png";
      public static final String SETTINGS = BUTTON_PATH + "settings.png";
      public static final String SINGLE_PLAYER = BUTTON_PATH + "singlePlayer.png";
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
