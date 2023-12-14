package com.apontadores.ui;

import static com.apontadores.utils.Constants.RESOURCES_PATH;
import static com.apontadores.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.apontadores.utils.Constants.GameConstants.GAME_WIDTH;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import com.apontadores.utils.LoadSave;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Frame {

  private static final HashMap<String, Class<? extends FrameElement>> assetTypes;
  static {
    assetTypes = new HashMap<>();
    // Initialize assetTypes here
    assetTypes.put("image", ImageElement.class);
    assetTypes.put("text", TextElement.class);
  }

  // TODO: add better error handling, refactor this
  public static Frame loadFromJson(final String jsonPath) {
    final Gson gson = new Gson();
    final Frame frame = new Frame();

    final JsonObject json = LoadSave.loadJson(
        jsonPath,
        JsonObject.class);

    frame.name = json.get("name").getAsString();

    frame.imagePath = json.get("imagePath").getAsString();
    if (frame.imagePath != null && !frame.imagePath.isEmpty())
      frame.backgroundImg = LoadSave.loadImage(RESOURCES_PATH + frame.imagePath);

    frame.color = gson.fromJson(json.get("color"), int[].class);
    if (frame.color != null)
      frame.backgroundColor = new Color(
          frame.color[0],
          frame.color[1],
          frame.color[2]);

    final JsonArray assetArray = json.get("assets").getAsJsonArray();
    frame.assets = new ArrayList<>(assetArray.size());

    for (int i = 0; i < assetArray.size(); i++) {
      assetArray.get(i);
      final JsonObject asset = assetArray.get(i).getAsJsonObject();
      final String type = asset.get("type").getAsString();

      final Class<? extends FrameElement> classOfT = assetTypes.get(type);
      if (classOfT == null)
        continue;

      final FrameElement assetObj = gson.fromJson(asset, classOfT);
      assetObj.init();
      frame.assets.add(assetObj);
    }

    return frame;
  }

  private transient Color backgroundColor;
  private transient BufferedImage backgroundImg;

  private String name;
  private String imagePath;
  private int[] color;

  private ArrayList<FrameElement> assets;

  public Object getElement(final String name) {
    if (assets == null)
      return null;

    for (final FrameElement asset : assets) {
      if (asset.getName().equals(name))
        return asset;
    }
    return null;
  }

  public void render(final Graphics g) {
    if (backgroundImg != null) {
      g.drawImage(backgroundImg, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
    } else if (backgroundColor != null) {
      g.setColor(backgroundColor);
      g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
    }

    for (final FrameElement asset : assets) {
      asset.render(g);
    }
  }

  public void update() {
    for (final FrameElement asset : assets) {
      asset.update();
    }
  }

  public String getName() {
    return name;
  }

}
