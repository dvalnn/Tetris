package com.apontadores.packets;

import java.util.StringJoiner;
import java.util.zip.CRC32;

public class Packet105GameOver extends Packet {

  public static final int PACKET_ID = 105;
  public static final int TOKEN_COUNT = 7;

  private String lines, score, level, time;
  private long checksum;

  public Packet105GameOver() {
    super(PACKET_ID);
  }

  public Packet105GameOver(int score, int lines, int level, String time) {
    super(PACKET_ID);
    this.score = String.valueOf(score);
    this.lines = String.valueOf(lines);
    this.level = String.valueOf(level);
    this.time = time;

    final CRC32 crc = new CRC32();
    crc.update((this.lines).getBytes());
    crc.update((this.score).getBytes());
    crc.update((this.level).getBytes());
    crc.update((this.time).getBytes());
    checksum = crc.getValue();
  }

  @Override
  public byte[] asBytes() {
    return new StringJoiner(",")
        .add(String.valueOf(packetID))
        .add(String.valueOf(transactionID))
        .add(String.valueOf(checksum))
        .add(lines)
        .add(score)
        .add(level)
        .add(time)
        .toString()
        .getBytes();
  }

  @Override
  public String[] asTokens() {
    return new String[] {
        String.valueOf(packetID),
        String.valueOf(transactionID),
        String.valueOf(checksum),
        lines,
        score,
        level,
        time
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
    } catch (final NumberFormatException e) {
      throw new PacketException("Invalid data");
    }

    if (packetID != PACKET_ID)
      throw new PacketException("Invalid packet ID");

    if (transactionID <= 0)
      throw new PacketException("Invalid transaction ID");

    lines = tokens[3];
    score = tokens[4];
    level = tokens[5];
    time = tokens[6];

    final CRC32 crc = new CRC32();
    crc.update((lines).getBytes());
    crc.update((score).getBytes());
    crc.update((level).getBytes());
    crc.update((time).getBytes());
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

}
