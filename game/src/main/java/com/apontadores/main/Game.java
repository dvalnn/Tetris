package com.apontadores.main;

import static com.apontadores.utils.Constants.RESOURCES_PATH;
import static com.apontadores.utils.Constants.GameConstants.FPS_SET;
import static com.apontadores.utils.Constants.GameConstants.UPS_SET;

import java.awt.Graphics;

import com.apontadores.gameElements.Sound;
import com.apontadores.gameStates.GameStateHandler;
import com.apontadores.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.gameStates.states.multiP.PlayingMP;
import com.apontadores.networking.GameClient;
import com.apontadores.utils.Keybindings;
import com.apontadores.utils.LoadSave;

public class Game implements Runnable {

  private static Keybindings keybinds;

  private final GameWindow gameWindow;
  private final GamePanel gamePanel;
  private Thread gameThread;

  private static GameClient client;

  private static boolean exit = false;

  private static String username = null;

  static {
    keybinds = LoadSave.loadJson(RESOURCES_PATH + "/config/keybinds.json", Keybindings.class);
  }

  public Game() {
    GameStateHandler.init();

    gamePanel = new GamePanel(this);
    gameWindow = new GameWindow(gamePanel);
    // requests focus so that the GamePanel can receive keyboard inputs
    gamePanel.requestFocus();

    startGameLoop();
  }

  private void startGameLoop() {
    gameThread = new Thread(this);
    gameThread.start();
    // new Thread(new Sound()).start();
  }

  public void update() {
    GameStateHandler.getActiveState().update();
    // System.out.println(GameStateHandler.getActiveStateID().name());
  }

  public void render(final Graphics g) {
    GameStateHandler.getActiveState().render(g);
  }

  public void windowLostFocus() {
    GameStateHandler.getActiveState().windowLostFocus();
  }

  @Override
  public void run() {
    // time per frame and time per update in nanoseconds
    final double timePerFrame = 1000000000 / FPS_SET;
    final double timePerUpdate = 1000000000 / UPS_SET;

    double deltaFrame = 0;
    double deltaUpdate = 0;

    int frames = 0;
    int updates = 0;

    long previousTime = System.nanoTime();
    long lastCheck = System.currentTimeMillis();

    // Game Loop
    while (true) {
      final long currentTime = System.nanoTime();

      deltaFrame += (currentTime - previousTime) / timePerFrame;
      deltaUpdate += (currentTime - previousTime) / timePerUpdate;
      previousTime = currentTime;

      // ensures that the game logic is updated at a constant rate
      if (deltaUpdate >= 1) {
        update();
        updates++;
        deltaUpdate--;
      }

      // ensures that the game is rendered at a constant rate
      if (deltaFrame >= 1) {
        gamePanel.repaint();
        frames++;
        deltaFrame--;
      }

      // prints the FPS and UPS every second
      if (System.currentTimeMillis() - lastCheck >= 1000) {
        lastCheck = System.currentTimeMillis();
        System.out.println("FPS " + frames + " | UPS " + updates);
        frames = 0;
        updates = 0;
      }

      Thread.yield();

      if (exit)
        break;
    }

    gameWindow.dispose();
    System.exit(0);
  }

  public static void exit() {
    exit = true;
  }

  public static void setUsername(final String username) {

    Game.username = username;

    // TODO: remove this
    ((PlayingMP) (GameStateHandler.getState(GameStatesEnum.PLAYING_MP)))
        .getPlayerBoard().setUsername(username);
  }

  public static final String getUsername() {
    return username;
  }

  public static void initClient() throws Exception {
    client = new GameClient();
    client.setUsername(username);
    client.setRoomName("default");
  }

  public static GameClient getClient() {
    return client;
  }

  public static Keybindings getKeybinds() {
    return keybinds;
  }

  public static void setKeybinds(Keybindings keybind) {
    Game.keybinds = keybind;
  }

  public static void resetKeybind() {
    Game.keybinds = new Keybindings();
  }
}
