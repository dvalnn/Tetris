package com.psw.tetris.Networking.Packet;

import com.psw.tetris.networking.packets.Packet03Board;

import org.junit.jupiter.api.Test;
import java.awt.Color;
import static org.junit.jupiter.api.Assertions.*;

public class Packet03BoardTest {

  @Test
  public void testConstructorWithDataArray() {
    String testUsername = "TestUser";
    int testRow = 5;
    Color[] testColors = { Color.RED, Color.BLUE, Color.GREEN };

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
    Color[] testColors = { Color.RED, Color.BLUE, Color.GREEN };
    Packet03Board packet = new Packet03Board(testUsername, testRow, testColors);

    byte[] expectedData = ("03," + testUsername + "," + testRow + ",-65536,-16776961,-16711936,").getBytes();
    byte[] actualData = packet.getData();

    assertArrayEquals(expectedData, actualData);
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
