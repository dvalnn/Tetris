package com.apontadores.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

import javax.imageio.ImageIO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LoadSave {

  public static BufferedImage loadImage(final String path) {
    BufferedImage image = null;

    try {
      image = ImageIO.read(new File(path));
    } catch (final IOException e) {
      System.out.println("Failed to load image: " + path);
      e.printStackTrace();
    }

    return image;
  }

  public static <T> T loadJson(final String path, final Class<T> classOfT) {
    T json = null;
    try {
      json = new Gson().fromJson(new FileReader(path), classOfT);
    } catch (final Exception e) {
      System.out.println("Failed to load json: " + path);
      e.printStackTrace();
    }
    return json;
  }

  public static <T> T loadJsonList(final String path, Type type) {
    T json = null;
    try {
      json = new Gson().fromJson(new FileReader(path), type);
    } catch (final Exception e) {
      System.out.println("Failed to load json: " + path);
      e.printStackTrace();
    }
    return json;
  }

  public static <T> void saveJson(String string, final T object) {
    final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    final String json = gson.toJson(object);
    try {
      final FileWriter fileWriter = new FileWriter(string);
      fileWriter.write(json);
      fileWriter.close();
    } catch (final IOException e) {
      System.out.println("Failed to save json: " + string);
      e.printStackTrace();
    }
  }

}
