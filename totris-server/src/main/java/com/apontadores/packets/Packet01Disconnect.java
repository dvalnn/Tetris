package com.apontadores.packets;

public class Packet01Disconnect extends Packet {
  public static final int PACKET_ID = 01;

  public Packet01Disconnect() {
    super(PACKET_ID);
  }
}
