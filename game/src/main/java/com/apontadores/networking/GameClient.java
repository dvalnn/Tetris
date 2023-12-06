package com.apontadores.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.apache.commons.validator.routines.InetAddressValidator;

import com.apontadores.main.Server;
import com.apontadores.packets.HeartbeatPacket;
import com.apontadores.packets.LoginPacket;
import com.apontadores.packets.PacketException;
import com.apontadores.packets.RedirectPacket;

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

  private DatagramSocket socket;
  private DatagramPacket inPacket;

  private ConnectionStage conStage;

  private int transactionID = 0;
  private int serverPort = DEFAULT_SERVER_PORT;

  private String username;
  private String roomName;

  private int timeoutCount = 0;
  private int consecutiveErrorCount = 0;

  private InetAddress serverAddress;
  private boolean abortConnection = false;

  public GameClient() throws IOException {
    socket = new DatagramSocket();
    socket.setSoTimeout(1000);
    inPacket = new DatagramPacket(
        new byte[Server.MAX_PACKET_SIZE],
        Server.MAX_PACKET_SIZE);

    conStage = ConnectionStage.SERVER_LOGIN;
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
        System.out.println("[CLIENT] Listening on port "
            + socket.getLocalPort());
        socket.receive(inPacket);
        parsePacket(
            inPacket.getData(),
            inPacket.getLength(),
            inPacket.getAddress(),
            inPacket.getPort());

      } catch (java.net.SocketTimeoutException e) {
        try {
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

  private void handleTimeout() throws IOException {
    switch (conStage) {
      case SERVER_LOGIN:
        sendLoginRequest();
        timeoutCount++;
        System.out.println("[CLIENT] Server Login timed out");
        break;

      case ROOM_LOGIN:
        sendLoginRequest();
        timeoutCount++;
        System.out.println("[CLIENT] Room Login timed out");
        break;

      case WAITING_FOR_PLAYERS:
        timeoutCount++;
        HeartbeatPacket heartbeatPacket = new HeartbeatPacket();
        heartbeatPacket.setTransactionID(++transactionID);
        System.out.println("[CLIENT] Sending heartbeat");
        System.out.println("[CLIENT] Heartbeat transaction ID: "
            + heartbeatPacket.getTransactionID());

        socket.send(new DatagramPacket(
            heartbeatPacket.asBytes(),
            heartbeatPacket.asBytes().length,
            serverAddress,
            serverPort));
        break;

      case WAITING_FOR_START:
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
        System.out.println("[CLIENT] Waiting for players");
        handleWaitingPlayers(
            datagramData,
            datagramLength,
            datagramAddress,
            datagramPort);
        break;

      case WAITING_FOR_START:
        System.out.println("[CLIENT] Waiting for start");
        handleWaitingStart(
            datagramData,
            datagramLength,
            datagramAddress,
            datagramPort);
        break;

      case IN_GAME:
        break;

      case POST_GAME:
        break;

      default:
        break;
    }
  }

  private void handleWaitingStart(
      byte[] datagramData,
      int datagramLength,
      InetAddress datagramAddress,
      int datagramPort) {

  }

  private void handleWaitingPlayers(
      byte[] datagramData,
      int datagramLength,
      InetAddress datagramAddress,
      int datagramPort) throws IOException {

    HeartbeatPacket heartbeatPacket;
    try {
      heartbeatPacket = new HeartbeatPacket().fromBytes(
          inPacket.getData(),
          inPacket.getLength());
      System.out.println("[CLIENT] Received heartbeat");
    } catch (PacketException e) {
      consecutiveErrorCount++;
      return;
    }

    if (heartbeatPacket.getTransactionID() != transactionID) {
      consecutiveErrorCount++;
      System.out.println("[CLIENT] Invalid transaction ID");
    }

    heartbeatPacket.setTransactionID(++transactionID);
    System.out.println("[CLIENT] Sending heartbeat");
    System.out.println("[CLIENT] Heartbeat transaction ID: "
        + heartbeatPacket.getTransactionID());
    socket.send(new DatagramPacket(
        heartbeatPacket.asBytes(),
        heartbeatPacket.asBytes().length,
        serverAddress,
        serverPort));
  }

  private void handleRoomLogin(
      byte[] datagramData,
      int datagramLength,
      InetAddress datagramAddress,
      int datagramPort) {

    LoginPacket loginPacket;
    try {
      loginPacket = new LoginPacket().fromBytes(
          inPacket.getData(),
          inPacket.getLength());
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

    socket.send(outPacket);
  }

  private boolean loginResponseIsValid(LoginPacket packet) {
    System.out.println("[CLIENT] Checking login response");
    System.out.println("[CLIENT] Username: " + packet.getUsername());
    System.out.println("[CLIENT] Room name: " + packet.getRoomName());
    return (packet.getUsername().equals(username) &&
        packet.getRoomName().equals(roomName));
  }

  private void handleServerLogin(
      byte[] data,
      int dataLenght,
      InetAddress address,
      int port)
      throws IOException {

    RedirectPacket redirectPacket;
    try {
      redirectPacket = new RedirectPacket().fromBytes(
          inPacket.getData(),
          inPacket.getLength());
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
