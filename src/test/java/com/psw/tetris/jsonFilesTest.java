package com.psw.tetris;

import com.psw.tetris.utils.JsonParserUtil;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonParser;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.lang.reflect.Type;
import java.util.Set;


public class jsonFilesTest {

  public class JsonShape {
    Point2D.Double center = null;
    Point2D.Double[] points = null;
    int rgb[] = null;

    transient Color color = null;

    public void initColor() {
      color = new Color(rgb[0], rgb[1], rgb[2]);
    }

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
  JsonShape shape = new JsonShape();
  JsonShape shape2 = new JsonShape();

  public void assertShapeEquals(JsonShape expected, JsonShape actual) {
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

    Gson gson = new GsonBuilder()
        .serializeNulls()
        .setPrettyPrinting()
        .create();

        
    String json = gson.toJson(shape);
    // save to file
    try {
      java.io.FileWriter writerI = new java.io.FileWriter("src/test/resources/shapeI.json");
      writerI.write(json);
      writerI.close();

      java.io.FileWriter writerJ = new java.io.FileWriter("src/test/resources/shapeJ.json");
      writerJ.write(json);
      writerJ.close();

    } catch (Exception e) {
      e.printStackTrace();
      assertTrue(false); // force fail
    }

    JsonShape shape3;
    JsonShape shape4;
    // read from file

// não dar hard code ao nome do ficheiro nem a quantos ficheiros são
// ver quantos ficheiros do tipo json existem e ler todos
    try {
      java.io.FileReader readerI= new java.io.FileReader("src/test/resources/shapeI.json");
      shape3 = gson.fromJson(readerI, JsonShape.class);
      readerI.close();

      java.io.FileReader readerJ = new java.io.FileReader("src/test/resources/shapeJ.json");
      shape4 = gson.fromJson(readerJ, JsonShape.class);
      readerJ.close();

      
    } catch (Exception e) {
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
    JsonParserUtil parser = new JsonParserUtil();
    String filename = "shapeI.json";
    String extension = parser.getExtensions(filename);
    assertEquals("json", extension);
  }

  @Test 
  public void listFilesFindsFourJsonFiles() {
    String dir = "src/test/resources/";
    JsonParserUtil parser = new JsonParserUtil();
    Set<String> files = parser.listFiles(dir);
    assertEquals(4, files.size());
  }
}