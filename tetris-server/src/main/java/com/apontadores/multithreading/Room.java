package com.apontadores.multithreading;

import java.net.InetAddress;

public class Room {

  private class Client {
    public final int port;
    public final String username;
    public final InetAddress address;

    public Client(
        final InetAddress address,
        final int port,
        final String username) {
      this.address = address;
      this.port = port;
      this.username = username;
    }
  }

  public enum RoomState {
    WAITING, GAME_STARTING, PLAYING, FINISHED;

    public static RoomState state = WAITING;
  }

  public static final int MAX_PLAYERS = 2;

  public String name;
  public final int id;
  private boolean full;
  private Client[] players;

  public Room(
      final int id,
      final InetAddress ownerAddress,
      final int ownerPort,
      final String ownerUsername) {

    this.id = id;
    this.players = new Client[MAX_PLAYERS];

    Client roomOwner = new Client(ownerAddress, ownerPort, ownerUsername);
    players[0] = roomOwner;

    this.name = ownerUsername + "'s room";
    System.out.println("[Room] Created room " + name);
  }

  public boolean isFull() {
    return full;
  }

  public boolean addClient(Client c) {
    if (full) {
      return false;
    }

    this.players[1] = c;
    RoomState.state = RoomState.GAME_STARTING;
    return true;
  }

  public boolean isFinished() {
    return RoomState.state == RoomState.FINISHED;
  }
}
