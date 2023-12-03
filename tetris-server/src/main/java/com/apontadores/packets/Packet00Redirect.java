package com.apontadores.packets;

import java.util.StringJoiner;

public class Packet00Redirect implements Packet {

  private final int port;
  private final int checksum;
  private final PacketTypes type = PacketTypes.REDIRECT;

  public Packet00Redirect(final int port) {
    this.port = port;
    checksum = port;
  }

  public static Packet00Redirect fromBytes(byte[] bytes, int length) {
    String data = Packet.readData(bytes, length);
    System.out.println("[Packet01Redirect] from data: " + data);
    String tokens[] = data.split(",");
    if (tokens.length != 3 || !tokens[0].equals("00")) {
      System.out.println("[Packet00Redirect] Expected 3 blocks, got " + tokens.length);
      return null;
    }

    int port = 0;
    int checksum = 0;
    try {
      port = Integer.parseInt(tokens[1]);
      checksum = Integer.parseInt(tokens[2]);
    } catch (NumberFormatException e) {
      return null;
    }

    if (checksum != port) {
      return null;
    }

    return new Packet00Redirect(port);
  }

  @Override
  public byte[] getBytes() {
    StringJoiner joiner = new StringJoiner(",");
    joiner.add("00")
        .add(String.valueOf(port))
        .add(String.valueOf(checksum));

    System.out.println("[Packet00Redirect] to data: " + joiner.toString());

    return joiner.toString().getBytes();
  }

  @Override
  public PacketTypes getType() {
    return type;
  }

  public int getPort() {
    return port;
  }

}
