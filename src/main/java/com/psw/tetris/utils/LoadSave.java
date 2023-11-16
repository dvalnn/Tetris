package com.psw.tetris.utils;

import static com.psw.tetris.utils.Constants.UI.BACKGROUND_PATH;
import static com.psw.tetris.utils.Constants.UI.BUTTON_PATH;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class LoadSave {

  public static BufferedImage loadButton(String path) {
    return loadImage(BUTTON_PATH + path);
  }

  public static BufferedImage loadBackground(String path) {
    return loadImage(BACKGROUND_PATH + path);
  }

  public static BufferedImage loadImage(String path) {
    BufferedImage image = null;

    try {
      image = ImageIO.read(new File(path));
    } catch (IOException e) {
      System.out.println("Failed to load image: " + path);
      e.printStackTrace();
    }

    return image;
  }
}
