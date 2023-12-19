package com.apontadores.packets;

public class Packet200Heartbeat extends Packet {
  public static final int PACKET_ID = 200;

  public Packet200Heartbeat() {
    super(PACKET_ID);
  }
}
