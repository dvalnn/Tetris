package com.psw.tetris.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class KeybindsTest {

  @Test
  // verifies that the keybindings.json file is parsed correctly
  public void verifyKeybindingsJsonData() {
    final Gson gson = new GsonBuilder()
        .serializeNulls()
        .setPrettyPrinting()
        .create();

    String file = "src/main/resources/keybinds.json";
    Keybindings keybindings = Keybindings.loadFromFile(file);
    String json = gson.toJson(keybindings);

    // save to file
    try {
      final java.io.FileWriter writerI = new java.io.FileWriter("src/test/resources/keybinds/keybinds.json");
      writerI.write(json);
      writerI.close();

    } catch (final Exception e) {
      e.printStackTrace();
      assertTrue(false); // force fail
    }
  }

  // tests that u can save the keybindings to a file
  @Test
  public void saveKeybindingsToFile() {
    final Gson gson = new GsonBuilder()
        .serializeNulls()
        .setPrettyPrinting()
        .create();

    String file = "src/test/resources/keybinds/newKeybindings.json";
    Keybindings newKeybindings = new Keybindings();
    newKeybindings.movesLeft = 1;
    Keybindings.saveToFile(newKeybindings, file);
  }
}
