package com.apontadores.packets;

public abstract class Packet {
  public static enum PacketTypes {
    INVALID(-1),
    LOGIN(00),
    HEARTBEAT(99);

    private final int packetId;

    private PacketTypes(final int packetId) {
      this.packetId = packetId;
    }

    public int getId() {
      return packetId;
    }
  }

  public static PacketTypes lookupPacket(final String packetId) {
    try {
      return lookupPacket(Integer.parseInt(packetId));
    } catch (final NumberFormatException e) {
      return PacketTypes.INVALID;
    }
  }

  public static PacketTypes lookupPacket(final int id) {
    for (final PacketTypes p : PacketTypes.values()) {
      if (p.getId() == id) {
        return p;
      }
    }
    return PacketTypes.INVALID;
  }

  public static String readData(final byte[] data) {
    final String message = new String(data).trim();
    return message;
  }

  public byte packetId;

  public Packet(final int packetId) {
    this.packetId = (byte) packetId;
  }

  public abstract byte[] getBytes();

}
