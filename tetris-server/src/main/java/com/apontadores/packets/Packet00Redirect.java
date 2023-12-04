package com.apontadores.packets;

import java.util.StringJoiner;
import java.util.zip.CRC32;

public class Packet00Redirect implements Packet {

  private final int port;
  private final long checksum;
  private final PacketTypes type = PacketTypes.REDIRECT;

  public Packet00Redirect(final int port) {
    this.port = port;
    CRC32 crc32 = new CRC32();
    crc32.update(String.valueOf(port).getBytes());
    checksum = crc32.getValue();
  }

  public Packet00Redirect(final int port, final long checksum) {
    this.port = port;
    this.checksum = checksum;
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
    long checksum = 0;
    try {
      port = Integer.parseInt(tokens[1]);
      checksum = Long.parseLong(tokens[2]);
    } catch (NumberFormatException e) {
      return null;
    }

    CRC32 crc32 = new CRC32();
    crc32.update(tokens[1].getBytes());
    if (checksum != crc32.getValue()) {
      System.out.println("[Packet00Redirect] Checksum failed");
      return null;
    }

    return new Packet00Redirect(port, checksum);
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
