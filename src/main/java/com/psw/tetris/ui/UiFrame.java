package com.psw.tetris.ui;

import static com.psw.tetris.utils.Constants.RESOURCES_PATH;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_WIDTH;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.psw.tetris.utils.LoadSave;

public class UiFrame {

  private String name;
  private String imagePath;
  private int[] color;
  private ArrayList<UiElement> assets;

  private transient Color backgroundColor;
  private transient BufferedImage backgroundImg;

  // TODO: add better error handling
  public static UiFrame loadFromJson(String jsonPath) {
    Gson gson = new Gson();
    UiFrame frame = new UiFrame();

    JsonObject json = LoadSave.loadJson(
        jsonPath,
        JsonObject.class);

    frame.name = json.get("name").getAsString();

    frame.imagePath = json.get("imagePath").getAsString();
    if (frame.imagePath != null && !frame.imagePath.isEmpty())
      frame.backgroundImg = LoadSave.loadImage(RESOURCES_PATH + frame.imagePath);

    frame.color = gson.fromJson(json.get("color"), int[].class);
    frame.backgroundColor = new Color(
        frame.color[0],
        frame.color[1],
        frame.color[2]);

    JsonArray assetArray = json.get("assets").getAsJsonArray();
    frame.assets = new ArrayList<UiElement>(assetArray.size());

    for (int i = 0; i < assetArray.size(); i++) {
      assetArray.get(i);
      JsonObject asset = assetArray.get(i).getAsJsonObject();

      if (asset.get("type").getAsString().equals("image")) {
        ImageElement button = gson.fromJson(asset, ImageElement.class);
        button.init();
        frame.addAsset(button);
      }
    }

    return frame;
  }

  public <T> T getAsset(String name, Class<T> classOfT) {
    for (UiElement asset : assets) {
      if (asset.getName().equals(name))
        return classOfT.cast(asset);
    }
    return null;
  }

  // TODO: look into optimizing this
  public void addAsset(final UiElement asset) {
    if (assets == null)
      assets = new ArrayList<UiElement>();

    assets.add(asset);
    // sort assets by render priority
    for (int i = 0; i < assets.size(); i++) {
      for (int j = i + 1; j < assets.size(); j++) {
        if (assets.get(i).getRenderPriority() > assets.get(j).getRenderPriority()) {
          final UiElement temp = assets.get(i);
          assets.set(i, assets.get(j));
          assets.set(j, temp);
        }
      }
    }
  }

  // TODO: look into optimizing this
  public void removeAsset(final String name) {
    for (int i = 0; i < assets.size(); i++) {
      if (assets.get(i).getName().equals(name)) {
        assets.remove(i);
        return;
      }
    }
  }

  public void render(Graphics g) {
    if (backgroundImg != null) {
      g.drawImage(backgroundImg, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
    } else if (backgroundColor != null) {
      g.setColor(backgroundColor);
      g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
    }

    for (final UiElement asset : assets) {
      asset.render(g);
    }
  }

  public void update() {
    for (final UiElement asset : assets) {
      asset.update();
    }
  }

  public String getName() {
    return name;
  }

  public Color getBackgroundColor() {
    return backgroundColor;
  }

  public BufferedImage getBackgroundImg() {
    return backgroundImg;
  }

}
