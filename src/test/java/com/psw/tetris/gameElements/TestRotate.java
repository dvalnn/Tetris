package com.psw.tetris.gameElements;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.geom.Point2D;

import org.junit.jupiter.api.Test;

import com.psw.tetris.gameElements.shapeTypes.Shape;

public class TestRotate {
  Shape shape;

  // @Test
  // public void shapeIRotatesRightCorrectly() {
  // shape = new Shape(0, new Point2D.Double(0, 0));
  // Point2D[] points = shape.getPoints();
  // Point2D[] rotationPoints = {
  // new Point2D.Double(2, -1),
  // new Point2D.Double(2, 0),
  // new Point2D.Double(2, 1),
  // new Point2D.Double(2, 2) };
  // shape.rotate(Math.PI / 2);
  // for (int i = 0; i < points.length; i++) {
  // assertEquals(rotationPoints[i], points[i]);
  // }
  // }
  //
  // @Test
  // public void shapeIRotatesLeftCorrectly() {
  // shape = new Shape(0, new Point2D.Double(0, 0));
  // Point2D[] points = shape.getPoints();
  // Point2D[] rotationPoints = {
  // new Point2D.Double(1, 2),
  // new Point2D.Double(1, 1),
  // new Point2D.Double(1, 0),
  // new Point2D.Double(1, -1) };
  // shape.rotate(-Math.PI / 2);
  //
  // for (int i = 0; i < points.length; i++) {
  // assertEquals(rotationPoints[i], points[i]);
  // }
  // }
}
