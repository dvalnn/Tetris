package com.psw.tetris.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.psw.tetris.utils.LoadSave;

public class TextBuilderTest {

  public TextElement mockText;

  @Test
  public void test() {

    mockText = new TextElement.Builder()
        .name("mockText")
        .text("mockText")
        .font("mockFont")
        .fontType("bold")
        .size("mockSize")
        .alignment("mockAlignment")
        .renderPriority(0)
        .x(10)
        .y(19)
        .build();

    // serialize mockText
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String json = gson.toJson(mockText);
    TextElement desirealized = gson.fromJson(json, TextElement.class);

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
