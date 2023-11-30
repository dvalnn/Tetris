package com.psw.tetris.ui;

import static com.psw.tetris.utils.Constants.RESOURCES_PATH;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_WIDTH;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.sound.sampled.Clip;

import com.psw.tetris.gameElements.Sound;
import com.psw.tetris.utils.LoadSave;

public class ImageElement implements FrameElement {

  public static class Builder {
    private ImageElement button;

    public Builder() {
      button = new ImageElement();
    }

    public Builder name(String name) {
      button.name = name;
      return this;
    }

    public Builder imagePath(String imagePath) {
      button.imagePath = imagePath;
      return this;
    }

    public Builder x(int x) {
      button.x = x;
      return this;
    }

    public Builder y(int y) {
      button.y = y;
      return this;
    }

    public Builder imageScale(int imageScale) {
      button.imageScale = imageScale;
      return this;
    }

    public Builder textElement(TextElement textElement) {
      button.textElement = textElement;
      return this;
    }

    public ImageElement build() {
      return button;
    }

  }

  // NOTE: json fields
  private String name;
  private String imagePath;
  private double x;
  private double y;
  private double imageScale;
  private double angle;
  private boolean enabled = true;
  private TextElement textElement;

  // NOTE: not serialized
  private transient int xAbs;
  private transient int yAbs;
  private transient Rectangle bounds;
  private transient BufferedImage image;
  private transient Clip clipEffect = Sound.setFileMusic(RESOURCES_PATH + "/sounds/clickSound.wav");;
  private transient Image scaledImage;

  public <T, R> R execIfClicked(
      final int x,
      final int y,
      ButtonAction<T, R> lambda,
      T args) {
    if (!enabled)
      return null;


    
    if (bounds.contains(x, y)) {
        Sound.playEffect(clipEffect);
        ///Sound.setMusicVolume(3.0f); //TODO fix this : implement a volume calculator
      return lambda.exec(args);
    }

    return null;
  }

  @Override
  public void init() {
    if (image == null) {
      image = LoadSave.loadImage(RESOURCES_PATH + imagePath);
    }

    if (image == null) {
      System.out.println("Failed to load image: " + RESOURCES_PATH + imagePath);
      enabled = false;
      return;
    }

    // convert percentual coordinates to absolute coordinates
    xAbs = (int) (x / 100 * GAME_WIDTH);
    yAbs = (int) (y / 100 * GAME_HEIGHT);

    bounds = new Rectangle(
        xAbs,
        yAbs,
        (int) (image.getWidth() * imageScale),
        (int) (image.getHeight() * imageScale));

    if (textElement != null) {
      textElement.setParent(this);
      textElement.init();
    }

  }

  @Override
  public void render(final Graphics g) {
    if (!enabled)
      return;

    Graphics2D g2d = (Graphics2D) g;

    g2d.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

    AffineTransform orig = g2d.getTransform();

    if (angle != 0)
      g2d.rotate(Math.toRadians(angle));

    int scaledWidth = (int) (image.getWidth() * imageScale);
    int scaledHeight = (int) (image.getHeight() * imageScale);

    if (imageScale != 1.0 && scaledImage == null) {
      scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
    } else if (scaledImage == null) {
      scaledImage = image;
    }

    g2d.drawImage(
        scaledImage,
        xAbs,
        yAbs,
        scaledWidth,
        scaledHeight,
        null);

    // draw the collision box
    // NOTE: debugging only
    g.setColor(Color.RED);
    g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

    g2d.setTransform(orig);

    if (textElement != null) {
      textElement.render(g);
    }
  }

  @Override
  public void update() {
    if (!enabled)
      return;

    if (textElement != null) {
      textElement.update();
    }
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getType() {
    return "button";
  }

  @Override
  public void enable() {
    enabled = true;
  }

  @Override
  public void disable() {
    enabled = false;
  }

  public Rectangle getBounds() {
    return bounds;
  }

  public TextElement getTextElement() {
    return textElement;
  }

  public void setTextElement(TextElement textElement) {
    this.textElement = textElement;
  }

  @Override
  public Point getAnchorPoint() {
    return new Point(xAbs, yAbs);
  }

  @Override
  public Point getDimensions() {
    return new Point(
        (int) (image.getWidth() * imageScale),
        (int) (image.getHeight() * imageScale));
  }

  @Override
  public double getRotation() {
    return angle;
  }
}
