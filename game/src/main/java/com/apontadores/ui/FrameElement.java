package com.apontadores.ui;

import java.awt.Graphics;
import java.awt.Point;

public interface FrameElement {

  public void render(Graphics g);

  public void update();

  public void enable();

  public void disable();

  public String getName();

  public String getType();

  public void init();

  public Point getAnchorPoint();

  public Point getDimensions();

  public double getRotation();

}
