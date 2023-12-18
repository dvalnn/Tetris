package com.apontadores.packets;

import java.util.StringJoiner;
import java.util.zip.CRC32;

public class Packet101GameOver extends Packet {

  public static final int PACKET_ID = 101;
  public static final int TOKEN_COUNT = 6;

  private String lines, score, level;
  private long checksum;

  public Packet101GameOver() {
    super(PACKET_ID);
  }

  public Packet101GameOver(int score, int lines, int level) {
    super(PACKET_ID);
    this.score = String.valueOf(score);
    this.lines = String.valueOf(lines);
    this.level = String.valueOf(level);

    final CRC32 crc = new CRC32();
    crc.update((this.lines).getBytes());
    crc.update((this.score).getBytes());
    crc.update((this.level).getBytes());
    checksum = crc.getValue();
  }

  public String getLines() {
    return lines;
  }

  public String getScore() {
    return score;
  }

  public String getLevel() {
    return level;
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

    final CRC32 crc = new CRC32();
    crc.update((lines).getBytes());
    crc.update((score).getBytes());
    crc.update((level).getBytes());
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
