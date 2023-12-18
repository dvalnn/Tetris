package com.apontadores.totris.main;

import static com.apontadores.totris.utils.Constants.KEYBINDINGS_PATH;
import static com.apontadores.totris.utils.Constants.GameConstants.FPS_SET;
import static com.apontadores.totris.utils.Constants.GameConstants.UPS_SET;

import java.awt.Graphics;

import com.apontadores.main.Server;
import com.apontadores.totris.gameElements.Sound;
import com.apontadores.totris.gameStates.GameStateHandler;
import com.apontadores.totris.networking.TetrisClient;
import com.apontadores.totris.utils.Keybindings;
import com.apontadores.totris.utils.LoadSave;

public class Game implements Runnable {

  private static Keybindings keybindings;

  private static TetrisClient client;
  private static Server server;

  private static boolean exit = false;
  private static String username = null;
  private static String roomName;
  private static String opponentName;

  private static boolean serverActive;

  static {
    keybindings = LoadSave.loadJson(KEYBINDINGS_PATH + "keybinds.json",
        Keybindings.class);
  }

  public static void exit() {
    exit = true;
  }

  public static void setUsername(final String username) {
    Game.username = username;
  }

  public static void setOpponentName(final String opponentName) {
    Game.opponentName = opponentName;
  }

  public static void setRoomName(final String roomName) {
    Game.roomName = roomName;
  }

  public static String getUsername() {
    return username;
  }

  public static String getOpponentName() {
    return opponentName;
  }

  public static String getRoomName() {
    return roomName;
  }

  public static void initClient() {
    client = new TetrisClient();
    client.start();
  }

  public static void initServer() {
    if (serverActive)
      return;

    server = new Server();
    Thread serverThread = new Thread(server);
    serverThread.start();
    serverActive = true;
  }

  public static void stopServer() {
    if (!serverActive)
      return;

    server.close();
    serverActive = false;
  }

  public static TetrisClient getClient() {
    return client;
  }

  public static Server getServer() {
    return server;
  }

  public static Keybindings getKeybindings() {
    return keybindings;
  }

  public static void resetKeybind() {
    Game.keybindings = new Keybindings();
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

    long previousTime = System.nanoTime();

    // Game Loop
    do {
      final long currentTime = System.nanoTime();

      deltaFrame += (currentTime - previousTime) / timePerFrame;
      deltaUpdate += (currentTime - previousTime) / timePerUpdate;
      previousTime = currentTime;

      // ensures that the game logic is updated at a constant rate
      if (deltaUpdate >= 1) {
        update();
        deltaUpdate--;
      }

      // ensures that the game is rendered at a constant rate
      if (deltaFrame >= 1) {
        gamePanel.repaint();
        deltaFrame--;
      }

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
