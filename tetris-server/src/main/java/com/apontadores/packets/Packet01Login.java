package com.apontadores.packets;

import java.util.StringJoiner;

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

    int checksum = 0;
    try {
      checksum = Integer.parseInt(tokens[3]);
    } catch (NumberFormatException e) {
      System.out.println("[Packet01Login] Checksum parse failed");
      return null;
    }
    if (checksum != (tokens[1].hashCode() | tokens[2].hashCode())) {
      System.out.println("[Packet01Login] Checksum failed");
      return null;
    }

    return new Packet01Login(tokens[1], tokens[2]);
  }

  private final String username;
  private final String roomName;
  private final PacketTypes type = PacketTypes.LOGIN;

  private final int checksum;

  public Packet01Login(
      final String username,
      final String roomName) {
    this.username = username;
    this.roomName = roomName;
    checksum = username.hashCode() | roomName.hashCode();
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
        .add(Integer.toString(checksum));

    return joiner.toString().getBytes();
  }

  public String getUsername() {
    return username;
  }

  public String getRoomName() {
    return roomName;
  }

}
