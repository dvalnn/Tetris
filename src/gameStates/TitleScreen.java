package gameStates;

import static utils.Constants.GameConstants.*;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import main.Game;

public class TitleScreen extends State implements StateMethods {

  private BufferedImage titleScreen;

  public TitleScreen(Game game) {
    super(game);
    importImage();
  }

  private void importImage() {
    String path = System.getProperty("user.dir") + "/assets/titleScreen.png";
    try {
      titleScreen = ImageIO.read(new File(path));
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error importing image " + path);
      System.exit(1);
    }
  }

  @Override
  public void update() {}

  @Override
  public void render(Graphics g) {
    g.drawImage(titleScreen, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    GameState.state = GameState.PLAYING;
  }

  @Override
  public void mousePressed(MouseEvent e) {}

  @Override
  public void mouseReleased(MouseEvent e) {}

  @Override
  public void mouseMoved(MouseEvent e) {}

  @Override
  public void mouseDragged(MouseEvent e) {}

  @Override
  public void keyPressed(KeyEvent e) {
    GameState.state = GameState.PLAYING;
  }

  @Override
  public void keyReleased(KeyEvent e) {}

  @Override
  public void windowLostFocus() {}
}
