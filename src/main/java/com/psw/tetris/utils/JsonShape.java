package com.psw.tetris.utils;

import java.awt.Color;
import java.awt.geom.Point2D;

public class JsonShape {

  Point2D.Double center = null;
  Point2D.Double[] points = null;
  int rgb[] = null;

  transient Color color = null;

  public void initColor() {
    color = new Color(rgb[0], rgb[1], rgb[2]);
  }

  // !USED FOR TESTING REMOVE LATER

  public void initDefault() {
    center = new Point2D.Double(0, 0);
    points = new Point2D.Double[4];
    points[0] = new Point2D.Double(0, 0);
    points[1] = new Point2D.Double(0, 0);
    points[2] = new Point2D.Double(0, 0);
    points[3] = new Point2D.Double(0, 0);
    rgb = new int[3];
    rgb[0] = 0;
    rgb[1] = 0;
    rgb[2] = 0;
  }
}
