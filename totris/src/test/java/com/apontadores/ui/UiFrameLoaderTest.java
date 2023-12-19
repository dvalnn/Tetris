package com.apontadores.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.apontadores.totris.ui.ImageElement;
import com.apontadores.totris.utils.LoadSave;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class UiFrameLoaderTest {

  @Test
  public void test() {

    JsonObject json = LoadSave.loadJson(
        "src/test/resources/mainMenu.json",
        JsonObject.class);

    assertNotNull(json);

    String name = json.get("name").getAsString();
    assertNotNull(name);
    assertEquals("MAIN_MENU", name);

    JsonArray assetArray = json.get("assets").getAsJsonArray();
    assertNotNull(assetArray);
    assertEquals(assetArray.size(), 6);

    Gson gson = new Gson();

    for (int i = 0; i < assetArray.size(); i++) {
      assetArray.get(i);
      JsonObject asset = assetArray.get(i).getAsJsonObject();
      if (asset.get("type").getAsString().equals("image")) {
        ImageElement button = gson.fromJson(asset, ImageElement.class);
        assertNotNull(button);
        assertEquals(button.getName(), asset.get("name").getAsString());
      }
    }

  }

}
