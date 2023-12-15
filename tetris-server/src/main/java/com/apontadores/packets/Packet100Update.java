package com.apontadores.packets;

import java.util.StringJoiner;
import java.util.zip.CRC32;

public class Packet100Update extends Packet {

  public static final int PACKET_ID = 100;
  public static final int TOKEN_COUNT = 5;

  // FIXME: change this to an enum
  public static final String[] updateTypes = {
      "tetromino",
      "board",
      "score",
      "gameover",
      "retransmit",
  };

  private long checksum;
  private String updateType;
  private String updateData;

  public Packet100Update() {
    super(PACKET_ID);
  }

  public Packet100Update(final String updateType, final String updateData) {
    super(PACKET_ID);
    this.updateType = updateType;
    this.updateData = updateData;

    final CRC32 crc = new CRC32();
    crc.update((updateType + updateData).getBytes());
    checksum = crc.getValue();
  }

  @Override
  public byte[] asBytes() {
    final StringJoiner joiner = new StringJoiner(",");
    joiner.add(String.valueOf(packetID))
        .add(String.valueOf(transactionID))
        .add(String.valueOf(checksum))
        .add(updateType)
        .add(updateData);

    return joiner.toString().getBytes();
  }

  @Override
  public String[] asTokens() {
    return new String[] {
        String.valueOf(packetID),
        String.valueOf(transactionID),
        String.valueOf(checksum),
        String.valueOf(updateType),
        String.valueOf(updateData),
    };
  }

  @Override
  public Packet100Update fromTokens(final String[] tokens) throws PacketException {

    if (tokens.length != TOKEN_COUNT)
      throw new PacketException("[Packet100Update] Invalid packet length");

    try {
      packetID = Integer.parseInt(tokens[0]);
      transactionID = Integer.parseInt(tokens[1]);
      checksum = Long.parseLong(tokens[2]);
    } catch (final NumberFormatException e) {
      throw new PacketException("[Packet100Update] Invalid data");
    }

    if (packetID != PACKET_ID)
      throw new PacketException("[Packet100Update] Invalid packet ID");

    if (transactionID <= 0)
      throw new PacketException("[Packet100Update] Invalid transaction ID");

    updateType = tokens[3];
    updateData = tokens[4];

    final CRC32 crc = new CRC32();
    crc.update((updateType + updateData).getBytes());
    if (checksum != crc.getValue())
      throw new PacketException("Invalid checksum");

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

  public String getUpdateType() {
    return updateType;
  }

  public String getUpdateData() {
    return updateData;
  }

}
