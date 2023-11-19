package com.psw.tetris.utils;

import com.psw.tetris.utils.JsonShape;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonParser;

//import org.junit.jupiter.api.Test;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

public class JsonParserUtil {

  public String getExtensions(String filename) {
    return com.google.common.io.Files.getFileExtension(filename);
  }

  public Set<String> listFiles(String dir) {
    return Stream.of(new File(dir).listFiles())
        .filter(file -> !file.isDirectory())
        .map(File::getName)
        .collect(Collectors.toSet());
  }

  public JsonShape[] readAllJsonData(String dir) {
    
    int filesSaved = 0;

    Set<String> files = listFiles(dir);
    JsonParserUtil parser = new JsonParserUtil();
    JsonShape[] shapes = new JsonShape[files.size()];

    String[] filesToRead = new String[files.size()];
    files.toArray(filesToRead);
    Arrays.sort(filesToRead);

    for (String filename : filesToRead) {

      String extension = parser.getExtensions(filename);
      try {
        FileReader reader = new FileReader(dir + filename);
        JsonShape shape = new Gson().fromJson(reader, JsonShape.class);
        reader.close();

        shapes[filesSaved] = shape;
        filesSaved++;
          
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return shapes;
  } 
}
