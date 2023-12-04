package com.apontadores.packets;

import java.util.StringJoiner;
import java.util.zip.CRC32;

public class Packet01Login implements Packet {

  public static Packet01Login fromBytes(byte[] bytes, int length) {
    String data = Packet.readData(bytes, length);
    System.out.println("[Packet01Login] from data: " + data);
    String tokens[] = data.split(",");
    return fromTokens(tokens);
  }

  public static Packet01Login fromTokens(String[] tokens) {
    if (tokens.length != 4 || !tokens[0].equals("01")) {
      System.out.println("[Packet00Login] Expected 4 blocks, got " + tokens.length);
      return null;
    }

    long checksum = 0;
    try {
      checksum = Long.parseLong(tokens[3]);
    } catch (NumberFormatException e) {
      System.out.println("[Packet01Login] Checksum parse failed");
      return null;
    }
    CRC32 crc32 = new CRC32();
    crc32.update((tokens[1] + tokens[2]).getBytes());
    if (checksum != crc32.getValue()) {
      System.out.println("[Packet01Login] Checksum failed");
      return null;
    }

    return new Packet01Login(tokens[1], tokens[2], checksum);
  }

  private final String username;
  private final String roomName;
  private final PacketTypes type = PacketTypes.LOGIN;

  private final long checksum;
  private CRC32 crc32 = new CRC32();

  public Packet01Login(
      final String username,
      final String roomName) {
    this.username = username;
    this.roomName = roomName;
    this.crc32.update((username + roomName).getBytes());
    this.checksum = crc32.getValue();
  }

  public Packet01Login(
      final String username,
      final String roomName,
      final long checksum) {
    this.username = username;
    this.roomName = roomName;
    this.checksum = checksum;
  }

  @Override
  public PacketTypes getType() {
    return type;
  }

  @Override
  public byte[] getBytes() {
    StringJoiner joiner = new StringJoiner(",");
    joiner.add("01")
        .add(username)
        .add(roomName)
        .add(String.valueOf(checksum));

    return joiner.toString().getBytes();
  }

  public String getUsername() {
    return username;
  }

  public String getRoomName() {
    return roomName;
  }

}
