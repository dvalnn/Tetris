package com.apontadores.packets;

import java.util.StringJoiner;
import java.util.zip.CRC32;

public class RedirectPacket implements Packet {

  private static final int PACKET_ID = 0;
  private static final int TOKEN_COUNT = 4;

  private int packetID;
  private int transactionID;
  private long checksum;
  private int port;

  public RedirectPacket(final int port) {
    packetID = PACKET_ID;
    transactionID = 0;

    this.port = port;

    CRC32 crc32 = new CRC32();
    crc32.update(String.valueOf(port).getBytes());
    checksum = crc32.getValue();
  }

  public RedirectPacket() {
    packetID = PACKET_ID;
  }

  @Override
  public RedirectPacket fromBytes(byte[] bytes, int length)
      throws PacketException {

    String data = new String(bytes, 0, length).trim();
    String tokens[] = data.split(",");
    return fromTokens(tokens);
  }

  @Override
  public RedirectPacket fromTokens(String[] tokens)
      throws PacketException {

    if (tokens.length != TOKEN_COUNT)
      throw new PacketException("Invalid packet length");

    int metadata[] = Packet.parseMetadata(tokens);
    packetID = metadata[0];
    transactionID = metadata[1];
    if (packetID != PACKET_ID)
      throw new PacketException("Invalid packet ID");

    try {
      checksum = Long.parseLong(tokens[2]);
      port = Integer.parseInt(tokens[3]);
    } catch (NumberFormatException e) {
      throw new PacketException("Invalid data");
    }

    CRC32 crc32 = new CRC32();
    crc32.update(String.valueOf(port).getBytes());
    if (checksum != crc32.getValue())
      throw new PacketException("Invalid checksum");

    return this;
  }

  @Override
  public byte[] asBytes() {
    StringJoiner joiner = new StringJoiner(",");
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
  public void setTransactionID(int transactionID) {
    this.transactionID = transactionID;
  }

  public int getPort() {
    return port;
  }
}
