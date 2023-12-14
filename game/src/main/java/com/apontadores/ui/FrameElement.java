package com.apontadores.ui;

import java.awt.Graphics;
import java.awt.Point;

public interface FrameElement {

  void render(Graphics g);

  void update();

  void enable();

  void disable();

  String getName();

  String getType();

  void init();

  Point getAnchorPoint();

  Point getDimensions();

  double getRotation();

}
