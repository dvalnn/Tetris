package com.apontadores.packets;

import java.util.StringJoiner;
import java.util.zip.CRC32;

public class Packet03Start extends Packet {

  public static final int PACKET_ID = 3;
  public static final int TOKEN_COUNT = 4;

  private long checksum;
  private String opponentName;

  public Packet03Start() {
    super(PACKET_ID);
  }

  public Packet03Start(String opponentName) {
    super(PACKET_ID);
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
  public Packet03Start fromTokens(String[] tokens) throws PacketException {
    if (tokens.length != TOKEN_COUNT)
      throw new PacketException("[Packet03Start] Invalid packet length");

    try {
      packetID = Integer.parseInt(tokens[0]);
      transactionID = Integer.parseInt(tokens[1]);
      checksum = Long.parseLong(tokens[2]);
    } catch (NumberFormatException e) {
      throw new PacketException("[Packet03Start] Invalid data type");
    }

    if (packetID != PACKET_ID)
      throw new PacketException("[Packet03Start] Invalid packet ID. Expected "
          + PACKET_ID + ", got " + packetID);

    opponentName = tokens[3];

    CRC32 crc = new CRC32();
    crc.update(opponentName.getBytes());
    if (checksum != crc.getValue())
      throw new PacketException("[Packet03Start] Invalid checksum");

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
