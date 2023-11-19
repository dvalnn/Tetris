package com.psw.tetris.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Button<T, V> {

  private final ButtonAction<T, V> action;

  private final int xPos;
  private final int yPos;
  private final double imgScale;

  private final BufferedImage image;
  private Rectangle bounds;

  public Button(
      final int xPos,
      final int yPos,
      final BufferedImage image,
      final double imgScale,
      final ButtonAction<T, V> action) {

    this.xPos = xPos;
    this.yPos = yPos;
    this.image = image;
    this.imgScale = imgScale;
    this.action = action;

    initBounds();
  }

  public Button(
      final Point center,
      final BufferedImage image,
      final double imgScale,
      final ButtonAction<T, V> action) {

    xPos = (int) (center.getX() - image.getWidth() * imgScale / 2);
    yPos = (int) (center.getY() - image.getHeight() * imgScale / 2);

    this.image = image;
    this.imgScale = imgScale;
    this.action = action;

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

  public V execAction(final T args) {
    return action.exec(args);
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
