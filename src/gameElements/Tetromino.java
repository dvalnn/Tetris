package gameElements;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.Random;

import static utils.Constants.GameConstants.*;
import static utils.Constants.TetrominoConstants.*;
import static utils.Constants.Directions.*;

import gameStates.GameState;

public class Tetromino {

  private Shape shape;

  private int horizontalSpeed;
  private int verticalSpeed;

  public Tetromino(int size, int scale, Point2D spawnPoint) {
    this.horizontalSpeed = 0;
    this.verticalSpeed = 0;
    this.shape = shapeFactory(size, scale);
    this.shape.move((int) spawnPoint.getX(), (int) spawnPoint.getY());
  }

  private Shape shapeFactory(int size, int scale) {
    return new IShape(size, scale);
  }

  public void rotate(int direction) {
    double angle = 0;

    switch (direction) {
      case LEFT:
        angle = -Math.PI / 2;
        break;
      case RIGHT:
        angle = Math.PI / 2;
        break;
    }

    shape.rotate(angle);
  }

  public void render(Graphics g) {
    shape.render(g);
  }

}
