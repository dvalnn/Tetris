package com.apontadores.totris.utils;

public class Constants {

  public static class GameConstants {
    public static final int FPS_SET = 60;
    public static final int UPS_SET = 100;
    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = 20;
    public static final int BOARD_SQUARE = 25;
    public static final int GAME_WIDTH = 1280;
    public static final int GAME_HEIGHT = 720;
  }

  public static class Directions {
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

  public static final String USER_DIR = System.getProperty("user.dir");
  public static final String SYS_SEPARATOR = System.getProperty("file.separator");

  // FIXME:
  public static String RESOURCES_PATH;

  public static final String KEYBINDINGS_PATH;

  public static final String FRAMES_PATH;

  static {
    RESOURCES_PATH = USER_DIR;

    //if (!RESOURCES_PATH.contains("game")) {
    //  RESOURCES_PATH += SYS_SEPARATOR + "game";
    //}

    //totris/src/main/resources/config/keybinds.json

    RESOURCES_PATH += SYS_SEPARATOR +
        "src" +
        SYS_SEPARATOR +
        "main" +
        SYS_SEPARATOR +
        "resources" +
        SYS_SEPARATOR;

    KEYBINDINGS_PATH = RESOURCES_PATH + "config"+ SYS_SEPARATOR;
    FRAMES_PATH = RESOURCES_PATH + "frames" + SYS_SEPARATOR;

    System.out.println("USER_DIR: " + USER_DIR);
    System.out.println("SYS_SEPARATOR: " + SYS_SEPARATOR);
    System.out.println("RESOURCES_PATH: " + RESOURCES_PATH);
    System.out.println("KEYBINDINGS_PATH: " + KEYBINDINGS_PATH);
    System.out.println("FRAMES_PATH: " + FRAMES_PATH);
  }

}
