package com.apontadores.networking;

public class NetworkControl {

  public static enum ClientStates {
    INACTIVE,
    USERNAME_TAKEN,
    ROOM_FULL,
    CONNECTION_TIMEOUT,
    CONNECTION_ABORTED,
    SOCKET_ERROR,
    RUNNING;
  }

  public static enum ConnectionPhases {
    INACTIVE,
    DISCONNECTED,
    CONNECTING,
    WAITING_FOR_OPPONENT,
    PLAYING,
    GAME_OVER,
  }
}
