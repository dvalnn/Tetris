package com.apontadores.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.validator.routines.InetAddressValidator;

import com.apontadores.main.Server;
import com.apontadores.packets.HeartbeatPacket;
import com.apontadores.packets.LoginPacket;
import com.apontadores.packets.PacketException;
import com.apontadores.packets.RedirectPacket;
import com.apontadores.packets.GameStartPacket;
import com.apontadores.packets.PlayerUpdatePacket;

public class GameClient implements Runnable {

  public static enum ClientStatus {
    INACTIVE,
    USERNAME_TAKEN,
    ROOM_FULL,
    CONNECTION_ABORTED,
    CONNECTION_TIMEOUT,
    CONNECTION_ERROR,
    CONNECTION_LOST,
    SOCKET_ERROR,
    OK;

    private static ClientStatus status = INACTIVE;

    private static final String[] statusMessages = {
        "Client is inactive",
        "Username is already taken",
        "Room is full",
        "Connection aborted",
        "Connection timed out",
        "Connection error",
        "Connection lost",
        "Server is active",
    };

    public static ClientStatus getStatus() {
      return status;
    }

    public String getMessage() {
      return statusMessages[this.ordinal()];
    }
  }

  private enum ConnectionStage {
    SERVER_LOGIN,
    ROOM_LOGIN,
    WAITING_FOR_PLAYERS,
    WAITING_FOR_START,
    IN_GAME,
    POST_GAME,
  }

  private static final int DEFAULT_SERVER_PORT = 42069;
  private static final int MAX_ERRORS = 10;
  private static final int MAX_TIMEOUTS = 10;

  private static ConnectionStage conStage = ConnectionStage.SERVER_LOGIN;

  public static boolean gameStarted() {
    return ClientStatus.status == ClientStatus.OK &&
        conStage == ConnectionStage.IN_GAME;
  }

  private DatagramSocket socket;

  private int transactionID = 0;

  private int serverPort = DEFAULT_SERVER_PORT;
  private String username;

  private String roomName;
  private int timeoutCount = 0;

  private int consecutiveErrorCount = 0;
  private InetAddress serverAddress;

  private boolean abortConnection = false;
  private ArrayBlockingQueue<PlayerUpdatePacket> incomingUpdates;
  private ArrayBlockingQueue<DatagramPacket> outgoingPacketQueue;

  private String opponentName;

  Timer packetTransmissionThread;

  TimerTask packetTransmissionTask;

  private int lastReceivedID;

  public GameClient() throws IOException {
    socket = new DatagramSocket();
    socket.setSoTimeout(1000);

    conStage = ConnectionStage.SERVER_LOGIN;
    incomingUpdates = new ArrayBlockingQueue<PlayerUpdatePacket>(100);
    outgoingPacketQueue = new ArrayBlockingQueue<DatagramPacket>(1000);

    packetTransmissionThread = new Timer();
  }

  public PlayerUpdatePacket getUpdate() {
    return incomingUpdates.poll();
  }

  public void sendUpdate(PlayerUpdatePacket packet) {

    packet.setTransactionID(++transactionID);
    outgoingPacketQueue.add(new DatagramPacket(
        packet.asBytes(),
        packet.asBytes().length,
        serverAddress,
        serverPort));
  }

  public String getOpponentName() {
    return opponentName;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setRoomName(String roomName) {
    this.roomName = roomName;
  }

  public void setServerPort(int serverPort) {
    this.serverPort = serverPort;
  }

  public void abortConnection() {
    abortConnection = true;
  }

  public boolean setServerAddress(String address) {
    InetAddressValidator validator = new InetAddressValidator();
    if (validator.isValidInet4Address(address)) {
      try {
        serverAddress = InetAddress.getByName(address);
        return true;
      } catch (Exception e) {
        System.out.println("[CLIENT] Error setting server address: "
            + e.getMessage());
        return false;
      }
    } else {
      System.out.println("[CLIENT] Invalid server address");
      return false;
    }
  }

  public void start() {
    new Thread(this).start();
    ClientStatus.status = ClientStatus.OK;
  }

  @Override
  public void run() {
    DatagramPacket inPacket = new DatagramPacket(
        new byte[Server.MAX_PACKET_SIZE],
        Server.MAX_PACKET_SIZE);

    packetTransmissionTask = new TimerTask() {
      @Override
      public void run() {
        try {
          DatagramPacket outPacket = outgoingPacketQueue.poll();
          if (outPacket == null)
            return;

          socket.send(outPacket);
          System.out.println("[CLIENT] Sent packet");
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    };

    packetTransmissionThread.scheduleAtFixedRate(
        packetTransmissionTask,
        0,
        1);

    while (true) {
      if (abortConnection) {
        ClientStatus.status = ClientStatus.CONNECTION_ABORTED;
        break;
      }

      if (consecutiveErrorCount >= MAX_ERRORS) {
        System.out.println("[CLIENT] Too many errors, aborting connection");
        ClientStatus.status = ClientStatus.CONNECTION_ERROR;
        abortConnection = true;
        // TODO: try to send a disconnect packet
        break;
      }

      if (timeoutCount >= MAX_TIMEOUTS) {
        System.out.println("[CLIENT] Too many timeouts, aborting connection");
        ClientStatus.status = ClientStatus.CONNECTION_TIMEOUT;
        abortConnection = true;
        // TODO: try to send a disconnect packet
        break;
      }

      try {
        socket.receive(inPacket);
        System.out.println("[CLIENT] Received packet");
        timeoutCount = 0;
        parsePacket(
            inPacket.getData(),
            inPacket.getLength(),
            inPacket.getAddress(),
            inPacket.getPort());

      } catch (java.net.SocketTimeoutException e) {
        try {
          timeoutCount++;
          handleTimeout();
        } catch (IOException e1) {
          System.out.println("[CLIENT] socket error: "
              + e1.getMessage());
          ClientStatus.status = ClientStatus.SOCKET_ERROR;
          abortConnection = true;
          break;
        }
      } catch (Exception e) {
        System.out.println("[CLIENT] Error receiving packet: "
            + e.getMessage());
        ClientStatus.status = ClientStatus.SOCKET_ERROR;
        abortConnection = true;
        break;
      }
    }
  }

  public void sendHeartbeat() throws IOException {
    HeartbeatPacket heartbeatPacket = new HeartbeatPacket();
    heartbeatPacket.setTransactionID(++transactionID);
    // System.out.println("[CLIENT] Sending heartbeat");
    // System.out.println("[CLIENT] Heartbeat transaction ID: "
    // + heartbeatPacket.getTransactionID());

    outgoingPacketQueue.add(new DatagramPacket(
        heartbeatPacket.asBytes(),
        heartbeatPacket.asBytes().length,
        serverAddress,
        serverPort));
  }

  private void handleTimeout() throws IOException {
    switch (conStage) {
      case SERVER_LOGIN:
        sendLoginRequest();
        System.out.println("[CLIENT] Server Login timed out");
        break;

      case ROOM_LOGIN:
        sendLoginRequest();
        System.out.println("[CLIENT] Room Login timed out");
        break;

      case WAITING_FOR_PLAYERS:
        HeartbeatPacket heartbeatPacket = new HeartbeatPacket();
        heartbeatPacket.setTransactionID(++transactionID);
        // System.out.println("[CLIENT] Sending heartbeat");
        // System.out.println("[CLIENT] Heartbeat transaction ID: "
        // + heartbeatPacket.getTransactionID());

        outgoingPacketQueue.add(new DatagramPacket(
            heartbeatPacket.asBytes(),
            heartbeatPacket.asBytes().length,
            serverAddress,
            serverPort));
        break;

      case IN_GAME:
        break;

      case POST_GAME:
        break;

      default:
        break;
    }
  }

  private void parsePacket(
      byte[] datagramData,
      int datagramLength,
      InetAddress datagramAddress,
      int datagramPort) throws IOException {

    switch (conStage) {
      case SERVER_LOGIN:
        handleServerLogin(
            datagramData,
            datagramLength,
            datagramAddress,
            datagramPort);
        break;

      case ROOM_LOGIN:
        handleRoomLogin(
            datagramData,
            datagramLength,
            datagramAddress,
            datagramPort);
        break;

      case WAITING_FOR_PLAYERS:
        handleWaitingPlayers(
            datagramData,
            datagramLength,
            datagramAddress,
            datagramPort);
        break;

      case IN_GAME:
        handleUpdate(
            datagramData,
            datagramLength,
            datagramAddress,
            datagramPort);

        break;

      case POST_GAME:
        break;

      default:
        break;
    }
  }

  private void handleUpdate(
      byte[] datagramData,
      int datagramLength,
      InetAddress datagramAddress,
      int datagramPort) throws IOException {

    PlayerUpdatePacket updatePacket;
    try {
      updatePacket = new PlayerUpdatePacket().fromBytes(
          datagramData,
          datagramLength);

    } catch (PacketException e) {
      consecutiveErrorCount++;
      System.out.println("[CLIENT-Handle-Update] Invalid packet");
      System.out.println("[CLIENT-Handle-Update] Error: " + e.getMessage());
      return;
    }

    lastReceivedID = updatePacket.getTransactionID();
    incomingUpdates.add(updatePacket);
  }

  private void handleWaitingPlayers(
      byte[] datagramData,
      int datagramLength,
      InetAddress datagramAddress,
      int datagramPort) throws IOException {

    PacketException e1 = null;
    PacketException e2 = null;

    HeartbeatPacket heartbeatPacket;
    try {
      heartbeatPacket = new HeartbeatPacket().fromBytes(
          datagramData,
          datagramLength);

      if (heartbeatPacket.getTransactionID() != transactionID) {
        consecutiveErrorCount++;
        System.out.println("[CLIENT] Invalid transaction ID");
      }
      sendHeartbeat();
      return;
    } catch (PacketException e) {
      e1 = e;
    }

    try {
      GameStartPacket gameStartPacket = new GameStartPacket().fromBytes(
          datagramData,
          datagramLength);

      transactionID = gameStartPacket.getTransactionID();
      opponentName = gameStartPacket.getOpponentName();
      System.out.println("[CLIENT] Game starting");

      conStage = ConnectionStage.IN_GAME;
      return;
    } catch (PacketException e) {
      e2 = e;
    }

    System.out.println("[CLIENT] Heartbeat error: " + e1.getMessage());
    System.out.println("[CLIENT] Game start error: " + e2.getMessage());
  }

  private void handleRoomLogin(
      byte[] datagramData,
      int datagramLength,
      InetAddress datagramAddress,
      int datagramPort) {

    LoginPacket loginPacket;
    try {
      loginPacket = new LoginPacket().fromBytes(
          datagramData,
          datagramLength);
    } catch (PacketException e) {
      consecutiveErrorCount++;
      System.out.println("[CLIENT-Room-Login] Invalid packet");
      return;
    }

    if (loginResponseIsValid(loginPacket)) {
      System.out.println("[CLIENT] Login successful");
      conStage = ConnectionStage.WAITING_FOR_PLAYERS;
      consecutiveErrorCount = 0;
      transactionID = 1;
    }
  }

  private void sendLoginRequest() throws IOException {
    if (username == null || roomName == null)
      return;
    LoginPacket packet = new LoginPacket(username, roomName);
    packet.setTransactionID(0);

    DatagramPacket outPacket = new DatagramPacket(
        packet.asBytes(),
        packet.asBytes().length,
        serverAddress,
        serverPort);

    outgoingPacketQueue.add(outPacket);
  }

  private boolean loginResponseIsValid(LoginPacket packet) {
    System.out.println("[CLIENT] Checking login response");
    System.out.println("[CLIENT] Username: " + packet.getUsername());
    System.out.println("[CLIENT] Room name: " + packet.getRoomName());
    return (packet.getUsername().equals(username) &&
        packet.getRoomName().equals(roomName));
  }

  private void handleServerLogin(
      byte[] datagramData,
      int dataLength,
      InetAddress datagramAddress,
      int datagramPort)
      throws IOException {

    RedirectPacket redirectPacket;
    try {
      redirectPacket = new RedirectPacket().fromBytes(
          datagramData,
          dataLength);
    } catch (PacketException e) {
      consecutiveErrorCount++;
      System.out.println("[CLIENT-Server-Login] Invalid packet");
      return;
    }

    serverPort = redirectPacket.getPort();
    System.out.println("[CLIENT-Server-Login] Redirecting to port "
        + serverPort);
    sendLoginRequest();
    conStage = ConnectionStage.ROOM_LOGIN;
    consecutiveErrorCount = 0;
  }
}
