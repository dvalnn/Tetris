package main;

import static utils.Constants.GameConstants.*;

import gameStates.GameOver;
import gameStates.GameState;
import gameStates.Playing;
import gameStates.PlayingMP;
import gameStates.TitleScreen;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.net.InetAddress;
import javax.swing.JOptionPane;
import networking.GameClient;
import networking.GameServer;
import networking.packets.Packet00Login;

public class Game implements Runnable {

  private GameWindow gameWindow;
  private GamePanel gamePanel;
  private Thread gameThread;

  private TitleScreen menu;
  private Playing playing;
  private GameOver gameOver;
  private PlayingMP playingMP;

  private GameClient client;
  private GameServer server;

  private boolean serverActive = false;
  private boolean clientActive = false;
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
    menu = new TitleScreen(this);
    playing = new Playing(this);
    gameOver = new GameOver(this);
    playingMP = new PlayingMP(this);
  }

  private void startGameLoop() {
    gameThread = new Thread(this);
    gameThread.start();
  }

  public void initNetworking() {
    // TODO make this a dialog box instead of a yes/no option
    if (JOptionPane.showConfirmDialog(null, "Run as server?") == JOptionPane.YES_OPTION) {
      String hostName = JOptionPane.showInputDialog("Enter server name:").trim();
      server = new GameServer(this, hostName);
      server.start();
      serverActive = true;
    } else {
      // TODO make this safer by checking if the IP address is valid
      // TODO make this a text field instead of a dialog box
      String ipAddress = JOptionPane.showInputDialog("Enter server IP address:").trim();
      System.out.println("Connecting to " + ipAddress);
      Packet00Login loginPacket = new Packet00Login(JOptionPane.showInputDialog("Enter username:"));
      client = new GameClient(this, ipAddress, loginPacket.getUsername());
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

  public void addPlayer(String username, InetAddress address, int port) {
    playingMP.addBoardMP(username, address, port);
  }

  public void removePlayer(String username) {
    playingMP.removeBoardMP(username);
  }

  public void sendShapeUpdate(Point2D[] points, Color color) {
    if (serverActive) {
      server.sendShapeUpdate(points, color);
    } else if (clientActive) {
      client.sendShapeUpdate(points, color);
    }
  }

  public void sendBoardUpdate(int row, int col, Color color) {
    if (serverActive) {
      server.sendBoardUpdate(row, col, color);
    } else if (clientActive) {
      client.sendBoardUpdate(row, col, color);
    }
  }

  public void update() {
    switch (GameState.state) {
      case TITLE_SCREEN:
        menu.update();
        break;
      case PLAYING:
        playing.update();
        break;
      case PLAYING_MP:
        playingMP.update();
        break;
      case GAME_OVER:
        gameOver.update();
        break;
    }
  }

  public void render(Graphics g) {
    switch (GameState.state) {
      case TITLE_SCREEN:
        menu.render(g);
        break;
      case PLAYING:
        playing.render(g);
        break;
      case PLAYING_MP:
        playingMP.render(g);
        break;
      case GAME_OVER:
        gameOver.render(g);
        break;
    }
  }

  public void windowLostFocus() {
    switch (GameState.state) {
      case TITLE_SCREEN:
        menu.windowLostFocus();
        break;
      case PLAYING:
        playing.windowLostFocus();
        break;
      case PLAYING_MP:
        playingMP.windowLostFocus();
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

      if (exit) break;
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

  public TitleScreen getMenu() {
    return menu;
  }

  public GameOver getGameOver() {
    return gameOver;
  }

  public boolean isServerActive() {
    return serverActive;
  }

  public void setServerActive(boolean serverActive) {
    this.serverActive = serverActive;
  }

  public boolean isClientActive() {
    return clientActive;
  }

  public void setClientActive(boolean clientActive) {
    this.clientActive = clientActive;
  }

  public GameClient getClient() {
    return client;
  }

  public GameServer getServer() {
    return server;
  }

  public PlayingMP getPlayingMP() {
    return playingMP;
  }
}
