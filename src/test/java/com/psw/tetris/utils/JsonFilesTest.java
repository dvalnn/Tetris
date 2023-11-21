package com.psw.tetris.utils;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import com.google.gson.GsonBuilder;
import com.psw.tetris.gameElements.shapeTypes.JsonShape;

public class JsonFilesTest {

  JsonShape shape = new JsonShape();

  public void assertShapeEquals(final JsonShape expected, final JsonShape actual) {
    assertNotNull(expected);
    assertNotNull(actual);
    assertAll(
        () -> assertEquals(expected.center.getX(), actual.center.getX()),
        () -> assertEquals(expected.center.getY(), actual.center.getY()),
        () -> assertEquals(expected.points[0].getX(), actual.points[0].getX()),
        () -> assertEquals(expected.points[0].getY(), actual.points[0].getY()),
        () -> assertEquals(expected.points[1].getX(), actual.points[1].getX()),
        () -> assertEquals(expected.points[1].getY(), actual.points[1].getY()),
        () -> assertEquals(expected.points[2].getX(), actual.points[2].getX()),
        () -> assertEquals(expected.points[2].getY(), actual.points[2].getY()),
        () -> assertEquals(expected.points[3].getX(), actual.points[3].getX()),
        () -> assertEquals(expected.points[3].getY(), actual.points[3].getY()),
        () -> assertEquals(expected.rgb[0], actual.rgb[0]),
        () -> assertEquals(expected.rgb[1], actual.rgb[1]),
        () -> assertEquals(expected.rgb[2], actual.rgb[2]),
        () -> assertEquals(expected.id, actual.id),
        () -> assertEquals(expected.rotates, actual.rotates)
        );
  }

  @Test
  public void testShape() {
    final Gson gson = new GsonBuilder()
        .serializeNulls()
        .setPrettyPrinting()
        .create();

    // init shape as non null values
    shape.center = new java.awt.geom.Point2D.Double(1.0, 2.0);
    shape.points = new java.awt.geom.Point2D.Double[4];
    shape.points[0] = new java.awt.geom.Point2D.Double(3.0, 4.0);
    shape.points[1] = new java.awt.geom.Point2D.Double(5.0, 6.0);
    shape.points[2] = new java.awt.geom.Point2D.Double(7.0, 8.0);
    shape.points[3] = new java.awt.geom.Point2D.Double(9.0, 10.0);
    shape.rgb = new int[3];
    shape.rgb[0] = 11;
    shape.rgb[1] = 12;
    shape.rgb[2] = 13;
    shape.id = "shapeI";
    shape.rotates = true;

    final String json = gson.toJson(shape);
    // save to file
    try {
      final java.io.FileWriter writerI = new java.io.FileWriter("src/test/resources/shapes/shape2.json");
      writerI.write(json);
      writerI.close();

    } catch (final Exception e) {
      e.printStackTrace();
      assertTrue(false); // force fail
    }

    JsonShape shape2;
    // read from file

    try {
      final java.io.FileReader readerI = new java.io.FileReader("src/test/resources/shapes/shape2.json");
      shape2 = gson.fromJson(readerI, JsonShape.class);
      readerI.close();

    } catch (final Exception e) {
      shape2 = null; // here so that the lsp doesn't complain
      e.printStackTrace();
      assertTrue(false); // force fail
    }

    assertShapeEquals(shape, shape2);
  }

  @Test
  public void listFilesFindsFourJsonFiles() {
    final String dir = "src/test/resources/shapes/";
    final Set<String> files = JsonShapeParser.listFiles(dir);
    assertEquals(4, files.size());
  }

  
  @Test
  public void verifyJsonData() {
    final String dir = "src/test/resources/shapes/";
    final ArrayList<JsonShape> shapes = JsonShapeParser.parseAllJsonShapes(dir);
    assertNotNull(shapes);
    assertShapeEquals(shapes.get(0), shapes.get(2));
  }
  @Test
  //verifies that the keybindings.json file is parsed correctly
  public void verifyKeybindingsJsonData() {

    final Gson gson = new GsonBuilder()
        .serializeNulls()
        .setPrettyPrinting()
        .create();


    final String file = "src/main/resources/keybinds.json";
    final Keybindings keybindings = JsonKeybindingsParser.parseKeybindings(file);

    final String json = gson.toJson(keybindings);

    // save to file
    try {
      final java.io.FileWriter writerI = new java.io.FileWriter("src/test/resources/shapes/keybinds.json");
      writerI.write(json);
      writerI.close();

    } catch (final Exception e) {
      e.printStackTrace();
      assertTrue(false); // force fail
    }




  }

}
