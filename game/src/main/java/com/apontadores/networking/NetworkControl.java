package com.apontadores.networking;

public class NetworkControl {
  public enum ClientStates {
    INACTIVE,
    CONNECTION_TIMEOUT,
    SOCKET_ERROR,
    ROOM_FULL,
    USERNAME_IN_USE,
    ERROR,
    RUNNING
  }

  public enum ConnectionPhases {
    INACTIVE,
    DISCONNECTED,
    CONNECTING,
    WAITING_FOR_OPPONENT,
    PLAYING,
    FINISHED
  }
}
