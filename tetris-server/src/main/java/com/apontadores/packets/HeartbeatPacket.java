package com.apontadores.packets;

import java.util.StringJoiner;

public class HeartbeatPacket implements Packet {
  public static final int PACKET_ID = 2;
  private static final int TOKEN_COUNT = 2;

  private int packetID;
  private int transactionID;

  public HeartbeatPacket() {
    packetID = PACKET_ID;
  }

  @Override
  public byte[] asBytes() {
    StringJoiner joiner = new StringJoiner(",");
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
  public HeartbeatPacket fromBytes(byte[] bytes, int length) throws PacketException {
    String tokens[] = new String(bytes, 0, length).trim().split(",");
    return fromTokens(tokens);
  }

  @Override
  public HeartbeatPacket fromTokens(String[] tokens) throws PacketException {
    if (tokens.length != TOKEN_COUNT)
      throw new PacketException("Invalid packet length");

    int metadata[] = Packet.parseMetadata(tokens);
    packetID = metadata[0];
    transactionID = metadata[1];

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
  public void setTransactionID(int transactionID) {
    this.transactionID = transactionID;
  }
}
