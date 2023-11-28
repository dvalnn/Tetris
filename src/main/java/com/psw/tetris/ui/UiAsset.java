package com.psw.tetris.ui;

import java.awt.Graphics;

public interface UiAsset {

  public void render(Graphics g);

  public void update();

  public void setVisible(boolean visible);

  public int getRenderPriority();

  public void setRenderPriority(int priority);

  public String getName();

  public String getType();

}
