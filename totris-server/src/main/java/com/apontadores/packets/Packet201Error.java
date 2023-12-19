package com.apontadores.packets;

import java.util.StringJoiner;
import java.util.zip.CRC32;

public class Packet201Error extends Packet {
  public static final int PACKET_ID = 201;
  public static final int TOKEN_COUNT = 4;

  public static enum ErrorTypesEnum {
    INVALID(-1),
    ROOM_FULL(0),
    USERNAME_IN_USE(1),
    OPPONENT_DISCONNECTED(2),
    ERROR(3); // generic error - should not happen

    private final int errorId;

    ErrorTypesEnum(final int errorId) {
      this.errorId = errorId;
    }

    public int getId() {
      return errorId;
    }
  }

  public Packet201Error() {
    super(PACKET_ID);
  }

  private int errorId;
  private long checksum;

  public Packet201Error(final ErrorTypesEnum errorType) {
    super(PACKET_ID);
    this.errorId = errorType.getId();

    final CRC32 crc = new CRC32();
    crc.update(errorId);
    checksum = crc.getValue();
  }

  @Override
  public byte[] asBytes() {
    return new StringJoiner(",")
        .add(String.valueOf(packetID))
        .add(String.valueOf(transactionID))
        .add(String.valueOf(checksum))
        .add(String.valueOf(errorId))
        .toString()
        .getBytes();
  }

  @Override
  public String[] asTokens() {
    return new String[] {
        String.valueOf(packetID),
        String.valueOf(transactionID),
        String.valueOf(checksum),
        String.valueOf(errorId)
    };
  }

  @Override
  public Packet fromTokens(String[] tokens) throws PacketException {
    if (tokens.length != TOKEN_COUNT)
      throw new PacketException("Invalid packet length");

    try {
      packetID = Integer.parseInt(tokens[0]);
      transactionID = Integer.parseInt(tokens[1]);
      checksum = Long.parseLong(tokens[2]);
      errorId = Integer.parseInt(tokens[3]);
    } catch (final NumberFormatException e) {
      throw new PacketException("Invalid data");
    }

    if (packetID != PACKET_ID)
      throw new PacketException("Invalid packet ID");

    final CRC32 crc32 = new CRC32();
    crc32.update(errorId);
    if (checksum != crc32.getValue())
      throw new PacketException("Invalid checksum");

    return this;
  }

  public ErrorTypesEnum getErrorType() {
    for (final ErrorTypesEnum errorType : ErrorTypesEnum.values())
      if (errorType.getId() == errorId)
        return errorType;

    return ErrorTypesEnum.INVALID;
  }
}
