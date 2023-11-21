package com.psw.tetris.utils;

import java.io.FileReader;
import java.io.FileWriter;

import com.google.gson.Gson;

public class Keybindings {
  public int rotatesLeft = 90;
  public int rotatesRight = 88;
  public int movesLeft = 37;
  public int movesRight = 39;
  public int softDrop = 40;
  public int hardDrop = 32;
  public int hold = 67;
  public int debug = 68;
  public int restart = 82;
  public int pause = 80;
  public int toggleGrid = 71;
  public int enterTitleScreen = 13;
  public int debugIShape = 49;
  public int debugJShape = 50;
  public int debugLShape = 51;
  public int debugOShape = 52;
  public int debugSShape = 53;
  public int debugTShape = 54;
  public int debugZShape = 55;

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

  // saves the currnet keybindings to a file
  public static void saveToFile(Keybindings newKeybindings, final String pathWithFilename) {
    try {
      final String json = new Gson().toJson(newKeybindings);
      final FileWriter writer = new FileWriter(pathWithFilename);
      writer.write(json);
      writer.close();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }
}
