package ui;

import static utils.Constants.UI.Buttons.*;

import gameStates.GameStateHandler;
import gameStates.GameStateHandler.GameStatesEnum;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import utils.LoadSave;

public class MenuButton {

  private int xPos;
  private int yPos;
  private double scale;
  private GameStatesEnum state;

  private BufferedImage image;
  private int index;
  private Rectangle bounds;

  public MenuButton(Point center, int index, double scale, GameStatesEnum state) {
    this.state = state;
    this.index = index;
    this.scale = scale;

    loadImg();

    this.xPos = (int) (center.getX() - image.getWidth() * scale / 2);
    this.yPos = (int) (center.getY() - image.getHeight() * scale / 2);

    initBounds();
  }

  public MenuButton(int xPos, int yPos, int index, double scale, GameStatesEnum state) {
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
        new Rectangle(
            xPos, yPos, (int) (image.getWidth() * scale), (int) (image.getHeight() * scale));
  }

  public Rectangle getBounds() {
    return bounds;
  }

  private void loadImg() {
    image = LoadSave.loadButton(BUTTONS[index]);
  }

  public void render(Graphics g) {
    if (image == null) loadImg();

    g.drawImage(
        image,
        xPos,
        yPos,
        (int) (image.getWidth() * scale),
        (int) (image.getHeight() * scale),
        null);

    // draw the collision box
    g.setColor(Color.RED);
    g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
  }

  public void applyGameState() {
    if (state != null) GameStateHandler.setActiveState(state);
  }
}
