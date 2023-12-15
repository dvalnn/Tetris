package com.apontadores.packets;

import java.util.StringJoiner;
import java.util.zip.CRC32;

public class Packet02Redirect extends Packet {

  private static final int PACKET_ID = 2;
  private static final int TOKEN_COUNT = 4;

  private long checksum;
  private int port;

  public Packet02Redirect(final int port) {
    super(PACKET_ID);
    transactionID = 0;

    this.port = port;

    final CRC32 crc32 = new CRC32();
    crc32.update(String.valueOf(port).getBytes());
    checksum = crc32.getValue();
  }

  public Packet02Redirect() {
    super(PACKET_ID);
  }

  @Override
  public Packet02Redirect fromTokens(final String[] tokens)
      throws PacketException {

    if (tokens.length != TOKEN_COUNT)
      throw new PacketException("[Packet02Redirect] Invalid packet length");

    try {
      packetID = Integer.parseInt(tokens[0]);
      transactionID = Integer.parseInt(tokens[1]);
      checksum = Long.parseLong(tokens[2]);
      port = Integer.parseInt(tokens[3]);
    } catch (final NumberFormatException e) {
      throw new PacketException("[Packet02Redirect] Invalid data");
    }

    if (packetID != PACKET_ID)
      throw new PacketException("[Packet02Redirect] Invalid packet ID. Expected "
          + PACKET_ID + ", got " + packetID);

    final CRC32 crc32 = new CRC32();
    crc32.update(String.valueOf(port).getBytes());
    if (checksum != crc32.getValue())
      throw new PacketException("[Packet02Redirect] Invalid checksum");

    return this;
  }

  @Override
  public byte[] asBytes() {
    final StringJoiner joiner = new StringJoiner(",");
    joiner.add(String.valueOf(packetID))
        .add(String.valueOf(transactionID))
        .add(String.valueOf(checksum))
        .add(String.valueOf(port));

    return joiner.toString().getBytes();
  }

  @Override
  public String[] asTokens() {
    return new String[] {
        String.valueOf(packetID),
        String.valueOf(transactionID),
        String.valueOf(checksum),
        String.valueOf(port)
    };
  }

  @Override
  public int getTransactionID() {
    return transactionID;
  }

  @Override
  public void setTransactionID(final int transactionID) {
    this.transactionID = transactionID;
  }

  public int getPort() {
    return port;
  }
}
