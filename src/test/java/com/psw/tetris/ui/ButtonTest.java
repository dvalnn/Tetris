package com.psw.tetris.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.psw.tetris.utils.LoadSave;

public class ButtonTest {

  @Test
  public void test() {
    ImageElement mockButton = new ImageElement.Builder()
        .name("testButton")
        .imagePath("/buttonsV2/newGame.png")
        .x(0)
        .y(0)
        .imageScale(1)
        .renderPriority(1)
        .build();

    assertNotNull(mockButton);

    TextElement mockText = new TextElement.Builder()
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

    mockButton.setTextElement(mockText);
    assertNotNull(mockButton.getTextElement());

    // serialize mockButton
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String json = gson.toJson(mockButton);
    TextElement desirealized = gson.fromJson(json, TextElement.class);

    assertNotNull(desirealized);
    assertEquals(desirealized.getName(), mockButton.getName());

    try {
      LoadSave.saveJson("src/test/resources/ButtonElement.json", mockButton);
    } catch (Exception e) {
      e.printStackTrace();
      Assertions.fail();
    }
  }

}
