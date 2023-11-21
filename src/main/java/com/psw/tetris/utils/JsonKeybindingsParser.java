package com.psw.tetris.utils;


import java.io.FileReader;
import com.google.gson.Gson;


public class JsonKeybindingsParser {
    
    public static Keybindings parseKeybindings(final String pathWithFilename) {
     try {
        final FileReader reader = new FileReader(pathWithFilename);
        final Keybindings keybindings = new Gson().fromJson(reader, Keybindings.class);
        reader.close();
        return keybindings;
     } catch (final Exception e) {
        e.printStackTrace();
        return null;
     }
      }    
}
