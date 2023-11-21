package com.psw.tetris.utils;

import java.io.FileReader;

import com.google.gson.Gson;

public class Keybindings {
  public final int rotatesLeft = 0;
  public final int rotatesRight = 0;
  public final int movesLeft = 0;
  public final int movesRight = 0;
  public final int softDrop = 0;
  public final int hardDrop = 0;
  public final int hold = 0;
  public final int debug = 0;
  public final int restart = 0;
  public final int pause = 0;
  public final int toggleGrid = 0;
  public final int enterTitleScreen = 0;
  public final int debugIShape = 0;
  public final int debugJShape = 0;
  public final int debugLShape = 0;
  public final int debugOShape = 0;
  public final int debugSShape = 0;
  public final int debugTShape = 0;
  public final int debugZShape = 0;

  public static Keybindings loadFromFile(final String pathWithFilename) {
    try {
      final FileReader reader = new FileReader(pathWithFilename);
      final Keybindings keybindings = new Gson().fromJson(reader, Keybindings.class);
      reader.close();
      return keybindings;
    } catch (final Exception e) {
      return new Keybindings();
    }
  }

  public static void saveToFile() {

  }
}
