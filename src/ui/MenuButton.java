package ui;

import static utils.Constants.UI.Buttons.*;

import gameStates.GameState;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import utils.LoadSave;

public class MenuButton {

  private int xPos;
  private int yPos;
  private double scale;
  private GameState state;

  private BufferedImage image;
  private int index;
  private Rectangle2D bounds;

  public MenuButton(Point center, int index, double scale, GameState state) {
    this.state = state;
    this.index = index;
    this.scale = scale;

    loadImg();

    this.xPos = (int) (center.getX() - image.getWidth() * scale / 2);
    this.yPos = (int) (center.getY() - image.getHeight() * scale / 2);

    initBounds();
  }

  public MenuButton(int xPos, int yPos, int index, double scale, GameState state) {
    this.state = state;
    this.index = index;
    this.scale = scale;
    this.xPos = xPos;
    this.yPos = yPos;

    loadImg();
    initBounds();
  }

  private void initBounds() {
    bounds =
        new Rectangle2D.Double(xPos, yPos, image.getWidth() * scale, image.getHeight() * scale);
  }

  public Rectangle2D getBounds() {
    return bounds;
  }

  private void loadImg() {
    image = LoadSave.loadButton(BUTTONS[index]);
  }

  public void render(Graphics g) {
    if (image == null) loadImg();

    // g.drawImage(image, (int) 0.5 * yPos, (int) 0.5 * yPos, null);
    // draw image centered on xPos and yPos and scaled to half size
    g.drawImage(
        image,
        xPos,
        yPos,
        (int) (image.getWidth() * scale),
        (int) (image.getHeight() * scale),
        null);
  }

  public void applyGameState() {
    GameState.state = state;
  }
}
