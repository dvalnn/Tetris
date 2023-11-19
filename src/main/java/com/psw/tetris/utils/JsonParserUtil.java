package com.psw.tetris.utils;

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.Gson;

public class JsonParserUtil {

  public String getExtensions(final String filename) {
    return com.google.common.io.Files.getFileExtension(filename);
  }

  public Set<String> listFiles(final String dir) {
    return Stream.of(new File(dir).listFiles())
        .filter(file -> !file.isDirectory())
        .map(File::getName)
        .collect(Collectors.toSet());
  }

  public JsonShape[] readAllJsonData(final String dir) {

    int filesSaved = 0;

    final Set<String> files = listFiles(dir);
    final JsonShape[] shapes = new JsonShape[files.size()];

    final String[] filesToRead = new String[files.size()];
    files.toArray(filesToRead);
    Arrays.sort(filesToRead);

    for (final String filename : filesToRead) {

      try {
        final FileReader reader = new FileReader(dir + filename);
        final JsonShape shape = new Gson().fromJson(reader, JsonShape.class);
        reader.close();

        shapes[filesSaved] = shape;
        filesSaved++;

      } catch (final Exception e) {
        e.printStackTrace();
      }
    }
    return shapes;
  }
}
