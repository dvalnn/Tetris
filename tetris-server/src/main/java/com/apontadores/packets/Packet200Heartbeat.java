package com.apontadores.packets;

import java.util.StringJoiner;

public class Packet200Heartbeat extends Packet {
  public static final int PACKET_ID = 200;
  private static final int TOKEN_COUNT = 2;

  public Packet200Heartbeat() {
    super(PACKET_ID);
  }

  @Override
  public byte[] asBytes() {
    final StringJoiner joiner = new StringJoiner(",");
    joiner.add(String.valueOf(packetID))
        .add(String.valueOf(transactionID));

    return joiner.toString().getBytes();
  }

  @Override
  public String[] asTokens() {
    return new String[] {
        String.valueOf(packetID),
        String.valueOf(transactionID)
    };
  }

  @Override
  public Packet200Heartbeat fromTokens(final String[] tokens)
      throws PacketException {
    if (tokens.length != TOKEN_COUNT)
      throw new PacketException("Invalid packet length");

    try {
      packetID = Integer.parseInt(tokens[0]);
      transactionID = Integer.parseInt(tokens[1]);
    } catch (final NumberFormatException e) {
      throw new PacketException("Invalid data");
    }

    if (packetID != PACKET_ID)
      throw new PacketException("Invalid packet ID");

    if (transactionID <= 0)
      throw new PacketException("Invalid transaction ID");

    return this;
  }

  @Override
  public int getTransactionID() {
    return transactionID;
  }

  @Override
  public void setTransactionID(final int transactionID) {
    this.transactionID = transactionID;
  }

}
