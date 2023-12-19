package com.apontadores.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.apontadores.totris.ui.TextElement;
import com.apontadores.totris.utils.LoadSave;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
        .x(10)
        .y(19)
        .build();

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String json = gson.toJson(mockText);
    TextElement deserialized = gson.fromJson(json, TextElement.class);

    assertNotNull(deserialized);
    assertEquals(deserialized.getName(), mockText.getName());

    try {
      LoadSave.saveJson("src/test/resources/TextAsset.json", mockText);
    } catch (Exception e) {
      e.printStackTrace();
      Assertions.fail();
    }

  }

}
