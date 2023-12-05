package com.apontadores.packets;

import java.util.StringJoiner;
import java.util.zip.CRC32;

public class LoginPacket implements Packet {

  private static final int PACKET_ID = 0;
  private static final int TOKEN_COUNT = 5;

  private int packetID;
  private int transactionID;
  private long checksum;
  private String username;
  private String roomName;

  private CRC32 crc32 = new CRC32();

  public LoginPacket(
      final String username,
      final String roomName) {

    packetID = PACKET_ID;
    this.username = username;
    this.roomName = roomName;
    this.crc32.update((username + roomName).getBytes());
    this.checksum = crc32.getValue();
  }

  public LoginPacket() {
    packetID = PACKET_ID;
  }

  @Override
  public byte[] asBytes() {
    StringJoiner joiner = new StringJoiner(",");
    joiner.add(String.valueOf(packetID))
        .add(String.valueOf(transactionID))
        .add(String.valueOf(checksum))
        .add(username)
        .add(roomName);

    return joiner.toString().getBytes();
  }

  public String getUsername() {
    return username;
  }

  public String getRoomName() {
    return roomName;
  }

  @Override
  public String[] asTokens() {
    String tokens[] = new String[TOKEN_COUNT];
    tokens[0] = String.valueOf(packetID);
    tokens[1] = String.valueOf(transactionID);
    tokens[2] = String.valueOf(checksum);
    tokens[3] = username;
    tokens[4] = roomName;
    return tokens;
  }

  @Override
  public LoginPacket fromBytes(byte[] bytes, int length) throws PacketException {
    String data = new String(bytes, 0, length).trim();
    String tokens[] = data.split(",");
    return fromTokens(tokens);
  }

  @Override
  public LoginPacket fromTokens(String[] tokens) throws PacketException {
    if (tokens.length != TOKEN_COUNT)
      throw new PacketException("Invalid packet length");

    int metadata[] = Packet.parseMetadata(tokens);

    packetID = metadata[0];
    transactionID = metadata[1];
    if (packetID != PACKET_ID)
      throw new PacketException("Invalid packet ID for LoginPacket");

    try {
      checksum = Long.parseLong(tokens[2]);
    } catch (NumberFormatException e) {
      throw new PacketException("Invalid data");
    }

    username = tokens[3];
    roomName = tokens[4];

    CRC32 crc32 = new CRC32();
    crc32.update((username + roomName).getBytes());
    if (checksum != crc32.getValue())
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
