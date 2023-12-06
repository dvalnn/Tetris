package com.apontadores.entities;

import java.net.InetAddress;

public class Player {
  public final String username;
  public final InetAddress address;
  public final int port;

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
}
