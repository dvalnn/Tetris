package com.apontadores.utils;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.apontadores.gameElements.shapes.JsonShape;

public class JsonShapeParser {

  public static Set<String> listFiles(final String dir) {
    return Stream.of(new File(dir).listFiles())
        .filter(file -> !file.isDirectory())
        .map(File::getName)
        .collect(Collectors.toSet());
  }

  public static ArrayList<JsonShape> parseAllJsonShapes(final String dir) {

    final Set<String> files = listFiles(dir);
    ArrayList<JsonShape> shapes = new ArrayList<JsonShape>(files.size());

    final String[] filesToRead = new String[files.size()];
    files.toArray(filesToRead);
    Arrays.sort(filesToRead);

    for (final String filename : filesToRead) {

      try {
        final FileReader reader = new FileReader(dir + filename);
        final JsonShape shape = new Gson().fromJson(reader, JsonShape.class);
        reader.close();

        shapes.add(shape);

      } catch (final Exception e) {
        e.printStackTrace();
        return null;
      }
    }
    return shapes;
  }
}
