package com.apontadores.packets;

import java.util.StringJoiner;
import java.util.zip.CRC32;

public class Packet100Update extends Packet {

  public static final int PACKET_ID = 100;
  public static final int TOKEN_COUNT = 5;

  public static enum UpdateTypesEnum {
    INVALID(-1),
    TETROMINO(0),
    BOARD(1),
    SCORE(2),
    HOLD(3),
    NEXT(4);

    private final int updateId;

    UpdateTypesEnum(final int updateId) {
      this.updateId = updateId;
    }

    public int getId() {
      return updateId;
    }
  }

  private long checksum;
  private int updateID;
  private String updateData;

  public Packet100Update() {
    super(PACKET_ID);
  }

  public Packet100Update(
      final UpdateTypesEnum updateType,
      final String updateData) {
    super(PACKET_ID);

    updateID = updateType.getId();
    this.updateData = updateData;

    final CRC32 crc = new CRC32();
    crc.update(updateID);
    crc.update((updateData).getBytes());
    checksum = crc.getValue();
  }

  @Override
  public byte[] asBytes() {
    return new StringJoiner(",")
        .add(String.valueOf(packetID))
        .add(String.valueOf(transactionID))
        .add(String.valueOf(checksum))
        .add(String.valueOf(updateID))
        .add(updateData)
        .toString()
        .getBytes();
  }

  @Override
  public String[] asTokens() {
    return new String[] {
        String.valueOf(packetID),
        String.valueOf(transactionID),
        String.valueOf(checksum),
        String.valueOf(updateID),
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
      updateID = Integer.parseInt(tokens[3]);
    } catch (final NumberFormatException e) {
      throw new PacketException("[Packet100Update] Invalid data");
    }

    if (packetID != PACKET_ID)
      throw new PacketException("[Packet100Update] Invalid packet ID");

    if (transactionID <= 0)
      throw new PacketException("[Packet100Update] Invalid transaction ID");

    updateData = tokens[4];

    final CRC32 crc = new CRC32();
    crc.update(updateID);
    crc.update(updateData.getBytes());
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

  public UpdateTypesEnum getUpdateType() {
    for (final UpdateTypesEnum updateType : UpdateTypesEnum.values())
      if (updateType.getId() == updateID)
        return updateType;
    return UpdateTypesEnum.INVALID;
  }

  public String getUpdateData() {
    return updateData;
  }

}
