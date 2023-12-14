package com.apontadores.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import com.apontadores.gameElements.shapes.JsonShape;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LoadSave {

  public static Set<String> listFiles(final String dir) {
    return Stream.of(Objects.requireNonNull(new File(dir).listFiles()))
        .filter(file -> !file.isDirectory())
        .map(File::getName)
        .collect(Collectors.toSet());
  }

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

  public static <T> T loadJson(final String path, final Type typeOfT) {
    T json = null;
    try {
      json = new Gson().fromJson(new FileReader(path), typeOfT);
    } catch (final Exception e) {
      System.out.println("Failed to load json: " + path);
      e.printStackTrace();
    }
    return json;
  }

  public static <T> void saveJson(final String string, final T object) {
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

  public static ArrayList<JsonShape> parseAllJsonShapes(final String dir) {

    final Set<String> files = listFiles(dir);
    final ArrayList<JsonShape> shapes = new ArrayList<>(files.size());

    final String[] filesToRead = new String[files.size()];
    files.toArray(filesToRead);
    Arrays.sort(filesToRead);

    for (final String filename : filesToRead) {
      final JsonShape shape = loadJson(dir + filename, JsonShape.class);
      shapes.add(shape);
    }
    return shapes;
  }
}
