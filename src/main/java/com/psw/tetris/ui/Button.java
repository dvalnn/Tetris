package com.psw.tetris.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Button<T, V> {

  private ButtonAction<T, V> action;

  private int xPos;
  private int yPos;
  private double imgScale;

  private BufferedImage image;
  private Rectangle bounds;

  public Button(
      int xPos,
      int yPos,
      BufferedImage image,
      double imgScale,
      ButtonAction<T, V> action) {

    this.xPos = xPos;
    this.yPos = yPos;
    this.image = image;
    this.imgScale = imgScale;
    this.action = action;

    initBounds();
  }

  public Button(
      Point center,
      BufferedImage image,
      double imgScale,
      ButtonAction<T, V> action) {

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

  public V execAction(T args) {
    return action.exec(args);
  }

  public void render(Graphics g) {
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
