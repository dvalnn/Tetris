package gameStates;

import static utils.Constants.GameConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import main.Game;

public class GameOver extends State implements StateMethods {

  public GameOver(Game game) {
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
    g.drawString("Game Over", GAME_WIDTH / 2 - 50, GAME_HEIGHT / 2);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    game.exit();
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
    game.exit();
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }

  @Override
  public void windowLostFocus() {
  }
}
