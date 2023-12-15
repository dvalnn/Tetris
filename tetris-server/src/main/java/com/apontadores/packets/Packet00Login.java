package com.apontadores.packets;

import java.util.StringJoiner;
import java.util.zip.CRC32;

public class Packet00Login extends Packet {

  private static final int PACKET_ID = 0;
  private static final int TOKEN_COUNT = 5;

  private long checksum;
  private String username;
  private String roomName;

  private final CRC32 crc32 = new CRC32();

  public Packet00Login(
      final String username,
      final String roomName) {

    super(PACKET_ID);

    this.username = username;
    this.roomName = roomName;
    this.crc32.update((username + roomName).getBytes());
    this.checksum = crc32.getValue();
  }

  public Packet00Login() {
    super(PACKET_ID);
  }

  public String getUsername() {
    return username;
  }

  public String getRoomName() {
    return roomName;
  }

  @Override
  public byte[] asBytes() {
    final StringJoiner joiner = new StringJoiner(",");
    joiner.add(String.valueOf(packetID))
        .add(String.valueOf(transactionID))
        .add(String.valueOf(checksum))
        .add(username)
        .add(roomName);

    return joiner.toString().getBytes();
  }

  @Override
  public String[] asTokens() {
    final String tokens[] = new String[TOKEN_COUNT];
    tokens[0] = String.valueOf(packetID);
    tokens[1] = String.valueOf(transactionID);
    tokens[2] = String.valueOf(checksum);
    tokens[3] = username;
    tokens[4] = roomName;
    return tokens;
  }

  @Override
  public Packet00Login fromTokens(final String[] tokens) throws PacketException {
    if (tokens.length != TOKEN_COUNT)
      throw new PacketException("[Packet00Login] Invalid packet length");

    try {
      packetID = Integer.parseInt(tokens[0]);
      transactionID = Integer.parseInt(tokens[1]);
      checksum = Long.parseLong(tokens[2]);
    } catch (final NumberFormatException e) {
      throw new PacketException("[Packet00Login] Invalid data type");
    }

    if (packetID != PACKET_ID)
      throw new PacketException("[Packet00Login] Invalid packet ID. Expected: "
          + PACKET_ID + " Got: " + packetID);

    username = tokens[3];
    roomName = tokens[4];

    final CRC32 crc32 = new CRC32();
    crc32.update((username + roomName).getBytes());
    if (checksum != crc32.getValue())
      throw new PacketException("[Packet00Login] Invalid checksum");

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

}
