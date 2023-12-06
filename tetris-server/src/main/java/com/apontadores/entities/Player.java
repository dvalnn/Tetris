package com.apontadores.entities;

import java.net.InetAddress;

public class Player {
  public final String username;
  public final InetAddress address;
  public final int port;
  public static final int MAX_PACKET_MISSES = 5;

  private boolean ready = false;

  private int packetMisses = 0;

  public Player(
      final String username,
      final InetAddress address,
      final int port) {
    this.username = username;
    this.address = address;
    this.port = port;
  }

  public Player clone() {
    return new Player(username, address, port);
  }

  public boolean isReady() {
    return ready;
  }

  public void setReady(boolean ready) {
    this.ready = ready;
  }

  public boolean isAlive() {
    return packetMisses < MAX_PACKET_MISSES;
  }

  public void packetMiss() {
    packetMisses++;
  }

  public void packetHit() {
    packetMisses--;
    if (packetMisses < 0)
      packetMisses = 0;
  }

}
