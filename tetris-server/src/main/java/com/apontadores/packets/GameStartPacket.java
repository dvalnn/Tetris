package com.apontadores.packets;

import java.util.StringJoiner;
import java.util.zip.CRC32;

public class GameStartPacket implements Packet {

  public static final int PACKET_ID = 3;
  public static final int TOKEN_COUNT = 4;

  private int packetID;
  private int transactionID;
  private long checksum;
  private String opponentName;

  public GameStartPacket() {
    packetID = PACKET_ID;
  }

  public GameStartPacket(String opponentName) {
    packetID = PACKET_ID;
    this.opponentName = opponentName;

    CRC32 crc = new CRC32();
    crc.update(opponentName.getBytes());
    checksum = crc.getValue();
  }

  @Override
  public byte[] asBytes() {
    StringJoiner joiner = new StringJoiner(",");
    joiner.add(String.valueOf(packetID))
        .add(String.valueOf(transactionID))
        .add(String.valueOf(checksum))
        .add(opponentName);

    return joiner.toString().getBytes();
  }

  @Override
  public String[] asTokens() {
    return new String[] {
        String.valueOf(packetID),
        String.valueOf(transactionID),
        String.valueOf(checksum),
        String.valueOf(opponentName),
    };
  }

  @Override
  public GameStartPacket fromBytes(byte[] bytes, int length) throws PacketException {
    String tokens[] = new String(bytes, 0, length).trim().split(",");
    return fromTokens(tokens);
  }

  @Override
  public GameStartPacket fromTokens(String[] tokens) throws PacketException {
    if (tokens.length != TOKEN_COUNT)
      throw new PacketException("Invalid packet length");

    int metadata[] = Packet.parseMetadata(tokens);

    packetID = metadata[0];
    transactionID = metadata[1];

    if (packetID != PACKET_ID)
      throw new PacketException("Invalid packet ID");

    if (transactionID <= 0)
      throw new PacketException("Invalid transaction ID");

    checksum = Long.parseLong(tokens[2]);
    opponentName = tokens[3];

    CRC32 crc = new CRC32();
    crc.update(opponentName.getBytes());
    if (checksum != crc.getValue())
      throw new PacketException("Invalid checksum");

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

  public String getOpponentName() {
    return opponentName;
  }

}
