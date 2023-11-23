package com.psw.tetris.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Button {

  private final int xPos;
  private final int yPos;
  private final double imgScale;

  private final BufferedImage image;
  private Rectangle bounds;

  public Button(
      final Point center,
      final BufferedImage image,
      final double imgScale) {

    xPos = (int) (center.getX() - image.getWidth() * imgScale / 2);
    yPos = (int) (center.getY() - image.getHeight() * imgScale / 2);

    this.image = image;
    this.imgScale = imgScale;

    initBounds();
  }

  public Button(
      final int xPos,
      final int yPos,
      final BufferedImage image,
      final double imgScale) {

    this.xPos = xPos;
    this.yPos = yPos;
    this.image = image;
    this.imgScale = imgScale;

    initBounds();
  }

  private void initBounds() {
    if (image == null) {
      return;
    }
    bounds = new Rectangle(
        xPos, yPos, (int) (image.getWidth() * imgScale), (int) (image.getHeight() * imgScale));
  }

  public Rectangle getBounds() {
    return bounds;
  }

  public boolean contains(final Point p) {
    if (bounds == null) {
      return false;
    }
    return bounds.contains(p);
  }

  public boolean isClicked(final Point p) {
    if (bounds == null) {
      return false;
    }
    return bounds.contains(p);
  }

  public <T, V> V exec(ButtonAction<T, V> act, final T args) {
    return act.exec(args);
  }

  public <T, V> V execIf(boolean Cond, ButtonAction<T, V> act, final T args) {
    if (Cond) {
      return act.exec(args);
    }
    return null;
  }

  public <T, V> V execIfClicked(final Point p, ButtonAction<T, V> act, final T args) {
    if (isClicked(p)) {
      return act.exec(args);
    }

    return null;
  }

  public void render(final Graphics g) {
    g.drawImage(
        image,
        xPos,
        yPos,
        (int) (image.getWidth() * imgScale),
        (int) (image.getHeight() * imgScale),
        null);

    // NOTE: debugging only
    // TODO: remove this
    // draw the collision box
    g.setColor(Color.RED);
    g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
  }

}
