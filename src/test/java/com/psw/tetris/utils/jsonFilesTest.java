package com.psw.tetris.utils;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class jsonFilesTest {

  JsonShape shape = new JsonShape();
  JsonShape shape2 = new JsonShape();

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
        () -> assertEquals(expected.color, actual.color));
  }

  @Test
  public void testShape() {
    shape.initDefault();
    shape2.initDefault();
    shape.initColor();
    shape2.initColor();

    assertShapeEquals(shape, shape2);

    final Gson gson = new GsonBuilder()
        .serializeNulls()
        .setPrettyPrinting()
        .create();

    final String json = gson.toJson(shape);
    // save to file
    try {
      final java.io.FileWriter writerI = new java.io.FileWriter("src/test/resources/shapeI.json");
      writerI.write(json);
      writerI.close();

      final java.io.FileWriter writerJ = new java.io.FileWriter("src/test/resources/shapeJ.json");
      writerJ.write(json);
      writerJ.close();

    } catch (final Exception e) {
      e.printStackTrace();
      assertTrue(false); // force fail
    }

    JsonShape shape3;
    JsonShape shape4;
    // read from file

    // não dar hard code ao nome do ficheiro nem a quantos ficheiros são
    // ver quantos ficheiros do tipo json existem e ler todos
    try {
      final java.io.FileReader readerI = new java.io.FileReader("src/test/resources/shapeI.json");
      shape3 = gson.fromJson(readerI, JsonShape.class);
      readerI.close();

      final java.io.FileReader readerJ = new java.io.FileReader("src/test/resources/shapeJ.json");
      shape4 = gson.fromJson(readerJ, JsonShape.class);
      readerJ.close();

    } catch (final Exception e) {
      shape3 = null; // here so that the lsp doesn't complain
      shape4 = null; // here so that the lsp doesn't complain
      e.printStackTrace();
      assertTrue(false); // force fail
    }
    shape3.initColor();
    assertShapeEquals(shape, shape3);

    shape4.initColor();
    assertShapeEquals(shape, shape4);
  }

  @Test
  public void shapeIisFoundByGetExtensions() {
    final JsonParserUtil parser = new JsonParserUtil();
    final String filename = "shapeI.json";
    final String extension = parser.getExtensions(filename);
    assertEquals("json", extension);
  }

  @Test
  public void listFilesFindsFourJsonFiles() {
    final String dir = "src/test/resources/";
    final JsonParserUtil parser = new JsonParserUtil();
    final Set<String> files = parser.listFiles(dir);
    assertEquals(4, files.size());
  }

  // TODO: Refactor this later heart
  // This test compares the read json files and checks if they are equal
  @Test
  public void verifyJsonData() {
    final String dir = "src/test/resources/";
    final JsonParserUtil parser = new JsonParserUtil();
    final JsonShape shapes[] = parser.readAllJsonData(dir);
    assertShapeEquals(shapes[0], shapes[1]);
  }
}
