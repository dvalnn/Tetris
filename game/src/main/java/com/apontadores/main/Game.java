package com.apontadores.main;

import static com.apontadores.utils.Constants.KEYBINDINGS_PATH;
import static com.apontadores.utils.Constants.GameConstants.FPS_SET;
import static com.apontadores.utils.Constants.GameConstants.UPS_SET;

import java.awt.Graphics;

import com.apontadores.gameElements.Sound;
import com.apontadores.gameStates.GameStateHandler;
import com.apontadores.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.gameStates.states.multiP.PlayingMP;
import com.apontadores.networking.TetrisClient;
import com.apontadores.utils.Keybindings;
import com.apontadores.utils.LoadSave;

public class Game implements Runnable {

  private static Keybindings keybinds;

  private static TetrisClient client;
  private static boolean exit = false;
  private static String username = null;
  private static String roomName;
  private static String opponentName;

  static {
    keybinds = LoadSave.loadJson(KEYBINDINGS_PATH + "keybinds.json", Keybindings.class);
  }

  public static void exit() {
    exit = true;
  }

  public static void setUsername(final String username) {
    Game.username = username;

    // FIXME: fix this
    ((PlayingMP) (GameStateHandler.getState(GameStatesEnum.PLAYING_MP)))
        .getPlayerBoard().setUsername(username);
  }

  public static void setRoomName(final String roomName) {
    Game.roomName = roomName;
  }

  public static String getUsername() {
    return username;
  }

  public static String getRoomName() {
    return roomName;
  }

  public static void initClient() {
    client = new TetrisClient();
    client.start();
  }

  public static TetrisClient getClient() {
    return client;
  }

  public static Keybindings getKeybinds() {
    return keybinds;
  }

  public static void resetKeybind() {
    Game.keybinds = new Keybindings();
  }

  public static void setOpponentName(final String opponentName) {
    Game.opponentName = opponentName;
  }

  public static String getOpponentName() {
    return opponentName;
  }

  private final GameWindow gameWindow;

  private final GamePanel gamePanel;

  public Game() {
    GameStateHandler.init();

    gamePanel = new GamePanel(this);
    gameWindow = new GameWindow(gamePanel);
    // requests focus so that the GamePanel can receive keyboard inputs
    gamePanel.requestFocus();

    startGameLoop();
  }

  public void update() {

    if (!gamePanel.hasFocus())
      gamePanel.requestFocus();

    GameStateHandler.getActiveState().update();
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
    final double timePerFrame = 1000000000. / FPS_SET;
    final double timePerUpdate = 1000000000. / UPS_SET;

    double deltaFrame = 0;
    double deltaUpdate = 0;

    // int frames = 0;
    // int updates = 0;

    long previousTime = System.nanoTime();
    // long lastCheck = System.currentTimeMillis();

    // Game Loop
    do {
      final long currentTime = System.nanoTime();

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

      // // prints the FPS and UPS every second
      // if (System.currentTimeMillis() - lastCheck >= 1000) {
      // lastCheck = System.currentTimeMillis();
      // System.out.println("FPS " + frames + " | UPS " + updates);
      // frames = 0;
      // updates = 0;
      // }

      Thread.yield();

    } while (!exit);

    gameWindow.dispose();
    System.exit(0);
  }

  private void startGameLoop() {
    new Thread(new Sound()).start();
    final Thread gameThread = new Thread(this);
    gameThread.start();
  }
}
