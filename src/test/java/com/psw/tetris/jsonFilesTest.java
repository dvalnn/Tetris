package com.psw.tetris;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class jsonFilesTest {
  
  @Test
  public void readDahyunCorretly(){
    String filePath = System.getProperty("user.dir") + "/src/test/resources/dahyun.json";
    FileReader reader = null;
    JSONArray centro = null;
    JSONObject jsonData = null;
    JSONArray pontos = null;
    
    try{      
      reader = new FileReader(filePath);
      jsonData = new JSONObject(reader);
      
      int data = jsonData.getInt("noGuieFixe");
      assertEquals(data, 1);

      centro = jsonData.getJSONArray("centro");
      assertEquals(centro.length(), 2);
      assertEquals(centro.getInt(0), 1);
      assertEquals(centro.getInt(1), 2);
      
      pontos = jsonData.getJSONArray("pontos");

      Point2D[] points = new Point2D[pontos.length()];

      for(int i = 0; i < pontos.length(); i++){
        Double X = pontos.getJSONArray(i).getDouble(0);
        Double Y = pontos.getJSONArray(i).getDouble(1);
        Point2D point = new Point2D.Double(X, Y);
        points[i] = point;
      } 
      assertEquals(points.length, 4);
      assertEquals(points[0].getX(), 0);
      assertEquals(points[0].getY(), 0);
      assertEquals(points[1].getX(), 1);
      assertEquals(points[1].getY(), 0);
      assertEquals(points[2].getX(), 2);
      assertEquals(points[2].getY(), 0);
      assertEquals(points[3].getX(), 3);
      assertEquals(points[3].getY(), 1);

    } catch (IOException e){

      assertNotNull(reader);

      //e.printStackTrace();
    } catch (JSONException e){
      // assertNotNull(arrayPoints);
      assertNotNull(jsonData);
     // e.printStackTrace();
    }
  }
}
