package com.psw.tetris.ui;

import java.awt.Graphics;

public interface UiElement {

  public void render(Graphics g);

  public void update();

  public void enable();

  public void disable();

  public int getRenderPriority();

  public void setRenderPriority(int priority);

  public String getName();

  public String getType();

}
