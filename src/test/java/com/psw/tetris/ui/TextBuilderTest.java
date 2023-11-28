package com.psw.tetris.ui;

import static com.psw.tetris.utils.Constants.RESOURCES_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.psw.tetris.utils.LoadSave;

public class TextBuilderTest {

  public TextAsset mockText;

  @Test
  public void test() {

    mockText = new TextAsset.Builder()
        .name("mockText")
        .text("mockText")
        .font("mockFont")
        .fontType("bold")
        .size("mockSize")
        .alignment("mockAlignment")
        .renderPriority(0)
        .x(10)
        .y(19)
        .visible("mockVisible")
        .build();

    // serialize mockText
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String json = gson.toJson(mockText);
    TextAsset desirealized = gson.fromJson(json, TextAsset.class);

    assertNotNull(desirealized);
    assertEquals(desirealized.getName(), mockText.getName());

    try {
      LoadSave.saveJson("src/test/resources/TextAsset.json", mockText);
    } catch (Exception e) {
      e.printStackTrace();
      Assertions.fail();
    }

  }

}
