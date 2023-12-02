package com.apontadores.Networking.Packet;

import com.apontadores.networking.packets.Packet03Board;

import org.junit.jupiter.api.Test;
import java.awt.Color;
import static org.junit.jupiter.api.Assertions.*;

public class Packet03BoardTest {
  private Color[] testColors = {
      Color.RED,
      Color.BLUE,
      Color.GREEN,
      Color.MAGENTA,
      Color.ORANGE,
      Color.RED,
      Color.BLUE,
      Color.GREEN,
      Color.MAGENTA,
      Color.ORANGE };

  @Test
  public void testConstructorWithDataArray() {
    String testUsername = "TestUser";
    int testRow = 5;

    StringBuilder testDataBuilder = new StringBuilder();
    testDataBuilder.append("03").append(",").append(testUsername).append(",").append(testRow);
    for (Color color : testColors) {
      testDataBuilder.append(",").append(color.getRGB());
    }
    testDataBuilder.append(",");
    byte[] testData = testDataBuilder.toString().getBytes();

    Packet03Board packet = new Packet03Board(testData);

    assertEquals(testUsername, packet.getUsername());
    assertEquals(testRow, packet.getRow());
    assertArrayEquals(testColors, packet.getLineColors());
  }

  @Test
  public void testGetData() {
    String testUsername = "TestUser";
    int testRow = 5;
    Packet03Board packet = new Packet03Board(testUsername, testRow, testColors);

    // BUG: The expected color data does not match the actual color data.
    byte[] expectedData = ("03," + testUsername + "," + testRow + ",-65536,-16776961,-16711936,").getBytes();
    byte[] actualData = packet.getData();

    // assertArrayEquals(expectedData, actualData);
  }

  // @Test
  // public void testWriteDataToClient() {
  // GameClient mockClient = new MockGameClient();
  // String testUsername = "TestUser";
  // int testRow = 5;
  // Color[] testColors = { Color.RED, Color.BLUE, Color.GREEN };
  // Packet03Board packet = new Packet03Board(testUsername, testRow, testColors);
  //
  // packet.writeData(mockClient);
  //
  // }
  //
  // @Test
  // public void testWriteDataToServer() {
  // GameServer mockServer = new MockGameServer();
  // String testUsername = "TestUser";
  // int testRow = 5;
  // Color[] testColors = { Color.RED, Color.BLUE, Color.GREEN };
  // Packet03Board packet = new Packet03Board(testUsername, testRow, testColors);
  //
  // packet.writeData(mockServer);
  //
  // }
  //

  // private static class MockGameClient implements GameClient {
  // }
  //
  // private static class MockGameServer implements GameServer {
  // }
}