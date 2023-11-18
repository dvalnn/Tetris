package com.psw.tetris.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
}
