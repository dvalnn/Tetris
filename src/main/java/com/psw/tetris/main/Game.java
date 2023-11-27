package com.psw.tetris.main;

import static com.psw.tetris.utils.Constants.GameConstants.FPS_SET;
import static com.psw.tetris.utils.Constants.GameConstants.UPS_SET;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.net.InetAddress;

import javax.swing.JOptionPane;

import com.psw.tetris.gameStates.GameStateHandler;
import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.gameStates.states.multiP.PlayingMP;
import com.psw.tetris.gameStates.states.singleP.GameOver;
import com.psw.tetris.networking.GameClient;
import com.psw.tetris.networking.GameServer;
import com.psw.tetris.networking.packets.Packet00Login;

public class Game implements Runnable {

  private final GameWindow gameWindow;
  private final GamePanel gamePanel;
  private Thread gameThread;

  private static GameClient client;
  private static GameServer server;

  private static boolean serverActive = false;
  private static boolean clientActive = false;
  private static boolean exit = false;

  private static String username = null;

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
  }

  public static void initNetworking() {
    // TODO: make this a dialog box instead of a yes/no option
    if (JOptionPane.showConfirmDialog(null, "Run as server?") == JOptionPane.YES_OPTION) {
      final String hostName = JOptionPane.showInputDialog("Enter server name:").trim();
      server = new GameServer(hostName);
      server.start();
      serverActive = true;
    } else {
      // TODO: make this safer by checking if the IP address is valid
      // TODO: make this a text field instead of a dialog box
      final String ipAddress = JOptionPane.showInputDialog("Enter server IP address:").trim();
      System.out.println("Connecting to " + ipAddress);
      final Packet00Login loginPacket = new Packet00Login(JOptionPane.showInputDialog("Enter username:"));
      client = new GameClient(ipAddress, loginPacket.getUsername());
      client.start();
      loginPacket.writeData(client);
      clientActive = true;
    }
  }

  public void terminateConnection() {
    if (serverActive) {
      server.terminateConnection();
    }
    if (clientActive) {
      client.terminateConnection();
    }
  }

  public static void addPlayer(final String username, final InetAddress address, final int port) {
    final PlayingMP playingMP = (PlayingMP) GameStateHandler.getState(GameStatesEnum.PLAYING_MP);
    playingMP.addBoardMP(username, address, port);
  }

  public static void removePlayer(final String username) {
    final PlayingMP playingMP = (PlayingMP) GameStateHandler.getState(GameStatesEnum.PLAYING_MP);
    playingMP.removeBoardMP(username);
  }

  public static void updateShapeMP(final Point2D[] points, final Color color) {
    final PlayingMP playingMP = (PlayingMP) GameStateHandler.getState(GameStatesEnum.PLAYING_MP);
    playingMP.getShapeMP().update(points, color);
  }

  public static void sendShapeUpdate(final Point2D[] points, final Color color) {
    if (serverActive) {
      server.sendShapeUpdate(points, color);
    } else if (clientActive) {
      client.sendShapeUpdate(points, color);
    }
  }

  public static void updateBoardMP(final int row, final Color[] lineColors) {
    final PlayingMP playingMP = (PlayingMP) GameStateHandler.getState(GameStatesEnum.PLAYING_MP);
    playingMP.getBoardMP().update(row, lineColors);
  }

  public static void sendBoardUpdate(final int row, final Color[] lineColors) {
    if (serverActive) {
      server.sendBoardUpdate(row, lineColors);
    } else if (clientActive) {
      client.sendBoardUpdate(row, lineColors);
    }
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

    // int frames = 0;
    // int updates = 0;

    long previousTime = System.nanoTime();

    // long lastCheck = System.currentTimeMillis();

    // Game Loop
    while (true) {
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

  public static void exit() {
    exit = true;
  }

  public static void setUsername(final String username) {

    Game.username = username;

    // TODO: remove this
    ((PlayingMP) (GameStateHandler.getState(GameStatesEnum.PLAYING_MP)))
        .getPlayerBoard().setUsername(username);

    // TODO: check if this is necessary
    ((GameOver) (GameStateHandler.getState(GameStatesEnum.GAME_OVER)))
        .setUsername(username);

  }

  public static final String getUsername() {
    return username;
  }

  public boolean isServerActive() {
    return serverActive;
  }

  public void setServerActive(final boolean serverActive) {
    Game.serverActive = serverActive;
  }

  public boolean isClientActive() {
    return clientActive;
  }

  public static void setClientActive(final boolean clientActive) {
    Game.clientActive = clientActive;
  }

  public GameClient getClient() {
    return client;
  }

  public GameServer getServer() {
    return server;
  }
}
