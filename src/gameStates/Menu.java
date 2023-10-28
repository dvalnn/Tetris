package gameStates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Color;

import main.Game;

import static utils.Constants.GameConstants.*;

public class Menu extends State implements StateMethods {

  public Menu(Game game) {
    super(game);
  }

  @Override
  public void update() {
  }

  @Override
  public void render(Graphics g) {
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

    g.setColor(Color.WHITE);
    g.drawString("Press any key to start", 100, 100);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    GameState.state = GameState.PLAYING;

  }

  @Override
  public void mousePressed(MouseEvent e) {

  }

  @Override
  public void mouseReleased(MouseEvent e) {

  }

  @Override
  public void mouseMoved(MouseEvent e) {

  }

  @Override
  public void mouseDragged(MouseEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    GameState.state = GameState.PLAYING;
  }

  @Override
  public void keyReleased(KeyEvent e) {

  }

  @Override
  public void windowLostFocus() {

  }

}
