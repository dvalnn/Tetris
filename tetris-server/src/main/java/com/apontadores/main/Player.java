package com.apontadores.main;

import java.net.InetAddress;

public class Player {
  public static final int MAX_PACKET_TIME = 3000;

  public final String username;
  public final InetAddress address;
  public final int port;

  private boolean ready = false;

  private long lastPacketTime;

  public Player(
      final String username,
      final InetAddress address,
      final int port) {
    this.username = username;
    this.address = address;
    this.port = port;
    lastPacketTime = System.currentTimeMillis();
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
    return System.currentTimeMillis() - lastPacketTime <= MAX_PACKET_TIME;
  }

  public void packetHit() {
    lastPacketTime = System.currentTimeMillis();
  }

}
