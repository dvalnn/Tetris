package gameElements;

import java.awt.Color;

public class BoardMP extends Board {
  private String username;

  public BoardMP(int size, int xOffset, int yOffset, Color color, String username) {
    super(size, xOffset, yOffset, color);
    this.username = username;
  }

  public void update(int row, Color[] color) {}

  public String getUsername() {
    return username;
  }
}
