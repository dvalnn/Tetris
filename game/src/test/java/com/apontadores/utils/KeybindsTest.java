package com.apontadores.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class KeybindsTest {

  @Test
  public void verifyKeybindingsJsonData() {
    final Gson gson = new GsonBuilder()
        .serializeNulls()
        .setPrettyPrinting()
        .create();

    String file = "src/main/resources/keybinds.json";
    Keybindings keybindings = LoadSave.loadJson(file, Keybindings.class);
    String json = gson.toJson(keybindings);

    try {
      final java.io.FileWriter writerI = new java.io.FileWriter("src/test/resources/keybinds/keybinds.json");
      writerI.write(json);
      writerI.close();
    } catch (final Exception e) {
      e.printStackTrace();
      Assertions.fail("Failed to save keybindings to file");
    }
  }

  // tests that u can save the keybindings to a file
  @Test
  public void saveKeybindingsToFile() {
    String file = "src/test/resources/keybinds/newKeybindings.json";
    Keybindings newKeybindings = new Keybindings();
    newKeybindings.movesLeft = 1;
    LoadSave.saveJson(file, newKeybindings);
  }
}
