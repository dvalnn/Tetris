package com.psw.tetris.ui;

import static com.psw.tetris.utils.Constants.RESOURCES_PATH;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.psw.tetris.utils.LoadSave;

public class ButtonElement implements UiElement {

  public static class Builder {
    private ButtonElement button;

    public Builder() {
      button = new ButtonElement();
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

    public Builder visible(boolean visible) {
      button.visible = visible;
      return this;
    }

    public Builder renderPriority(int renderPriority) {
      button.renderPriority = renderPriority;
      return this;
    }

    public Builder textElement(TextElement textElement) {
      button.textElement = textElement;
      return this;
    }

    public ButtonElement build() {
      return button;
    }

  }

  private String name;
  private String imagePath;

  private int x;
  private int y;
  private double imageScale;
  private boolean visible;
  private int renderPriority;
  private TextElement textElement;

  private transient Rectangle bounds;
  private transient BufferedImage image;
  private transient boolean enabled = true;

  public <T, R> R execIfClicked(
      final int x,
      final int y,
      ButtonAction<T, R> lambda,
      T args) {

    if (bounds.contains(x, y)) {
      return lambda.exec(args);
    }
    return null;
  }

  public void init() {
    if (enabled && image == null) {
      image = LoadSave.loadImage(RESOURCES_PATH + imagePath);
      enabled = image != null;
    }

    if (image == null) {
      System.out.println("Failed to load image: " + RESOURCES_PATH + imagePath);
      return;
    }

    bounds = new Rectangle(
        x,
        y,
        (int) (image.getWidth() * imageScale),
        (int) (image.getHeight() * imageScale));

  }

  @Override
  public void render(final Graphics g) {
    if (!visible || !enabled)
      return;

    g.drawImage(
        image,
        x,
        y,
        (int) (image.getWidth() * imageScale),
        (int) (image.getHeight() * imageScale),
        null);

    // draw the collision box
    // NOTE: debugging only
    g.setColor(Color.RED);
    g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

    if (textElement != null) {
      textElement.render(g);
    }
  }

  @Override
  public void update() {
  }

  @Override
  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  @Override
  public int getRenderPriority() {
    return renderPriority;
  }

  @Override
  public void setRenderPriority(int priority) {
    renderPriority = priority;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getType() {
    return "button";
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

}