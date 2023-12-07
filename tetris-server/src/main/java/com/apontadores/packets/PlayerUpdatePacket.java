package com.apontadores.packets;

import java.util.StringJoiner;
import java.util.zip.CRC32;

public class PlayerUpdatePacket implements Packet {

  public static final int PACKET_ID = 4;
  public static final int TOKEN_COUNT = 5;

  public static final String[] updateTypes = {
      "tetromino",
      "board",
      "score",
      "gameover",
      "retransmit",
  };

  private int packetID;
  private int transactionID;
  private long checksum;
  private String updateType;
  private String updateData;

  public PlayerUpdatePacket() {
    packetID = PACKET_ID;
  }

  public PlayerUpdatePacket(String updateType, String updateData) {
    packetID = PACKET_ID;
    this.updateType = updateType;
    this.updateData = updateData;

    CRC32 crc = new CRC32();
    crc.update((updateType + updateData).getBytes());
    checksum = crc.getValue();
  }

  @Override
  public byte[] asBytes() {
    StringJoiner joiner = new StringJoiner(",");
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
  public PlayerUpdatePacket fromBytes(byte[] bytes, int length) throws PacketException {
    return fromTokens(new String(bytes, 0, length).trim().split(","));
  }

  @Override
  public PlayerUpdatePacket fromTokens(String[] tokens) throws PacketException {

    if (tokens.length != TOKEN_COUNT)
      throw new PacketException("Invalid packet length");

    int[] metadata = Packet.parseMetadata(tokens);
    packetID = metadata[0];
    transactionID = metadata[1];

    if (packetID != PACKET_ID)
      throw new PacketException("Invalid packet ID");

    if (transactionID <= 0)
      throw new PacketException("Invalid transaction ID");

    checksum = Long.parseLong(tokens[2]);
    updateType = tokens[3];
    updateData = tokens[4];

    CRC32 crc = new CRC32();
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
  public void setTransactionID(int transactionID) {
    this.transactionID = transactionID;
  }

  public String getUpdateType() {
    return updateType;
  }

  public String getUpdateData() {
    return updateData;
  }

}
