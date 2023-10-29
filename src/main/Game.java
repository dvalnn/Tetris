package main;

import static utils.Constants.GameConstants.*;

import gameStates.GameOver;
import gameStates.GameState;
import gameStates.Menu;
import gameStates.Playing;
import java.awt.Graphics;

public class Game implements Runnable {

  private GameWindow gameWindow;
  private GamePanel gamePanel;
  private Thread gameThread;

  private Menu menu;
  private Playing playing;
  private GameOver gameOver;

  private boolean exit = false;

  public Game() {
    initClasses();

    gamePanel = new GamePanel(this);
    gameWindow = new GameWindow(gamePanel);
    // requests focus so that the GamePanel can receive keyboard inputs
    gamePanel.requestFocus();

    startGameLoop();
  }

  private void initClasses() {
    menu = new Menu(this);
    playing = new Playing(this);
    gameOver = new GameOver(this);
  }

  private void startGameLoop() {
    gameThread = new Thread(this);
    gameThread.start();
  }

  public void update() {
    switch (GameState.state) {
      case MENU:
        menu.update();
        break;
      case PLAYING:
        playing.update();
        break;
      case GAME_OVER:
        gameOver.update();
        break;
    }
  }

  public void render(Graphics g) {
    switch (GameState.state) {
      case MENU:
        menu.render(g);
        break;
      case PLAYING:
        playing.render(g);
        break;
      case GAME_OVER:
        gameOver.render(g);
        break;
    }
  }

  public void windowLostFocus() {
    System.out.println("Game.windowLostFocus()");
    switch (GameState.state) {
      case MENU:
        menu.windowLostFocus();
        break;
      case PLAYING:
        playing.windowLostFocus();
        break;
      case GAME_OVER:
        gameOver.windowLostFocus();
        break;
    }
  }

  @Override
  public void run() {
    // time per frame and time per update in nanoseconds
    double timePerFrame = 1000000000 / FPS_SET;
    double timePerUpdate = 1000000000 / UPS_SET;

    double deltaFrame = 0;
    double deltaUpdate = 0;

    // int frames = 0;
    // int updates = 0;

    long previousTime = System.nanoTime();

    // long lastCheck = System.currentTimeMillis();

    // Game Loop
    while (true) {
      long currentTime = System.nanoTime();

      deltaFrame += (currentTime - previousTime) / timePerFrame;
      deltaUpdate += (currentTime - previousTime) / timePerUpdate;
      previousTime = currentTime;

      // ensures that the game logic is updated at a constant rate
      if (deltaUpdate >= 1) {
        update();
        // updates++;
        deltaUpdate--;
      }

      // ensures that the game is rendered at a constant rate
      if (deltaFrame >= 1) {
        gamePanel.repaint();
        // frames++;
        deltaFrame--;
      }

      // prints the FPS and UPS every second
      // if (System.currentTimeMillis() - lastCheck >= 1000) {
      // lastCheck = System.currentTimeMillis();
      // System.out.println("FPS " + frames + " | UPS " + updates);
      // frames = 0;
      // updates = 0;
      // }

      if (exit)
        break;
    }

    gameWindow.dispose();
    System.exit(0);
  }

  public void exit() {
    exit = true;
  }

  public Playing getPlaying() {
    return playing;
  }

  public Menu getMenu() {
    return menu;
  }

  public GameOver getGameOver() {
    return gameOver;
  }
}
