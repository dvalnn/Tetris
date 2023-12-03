package com.apontadores.packets;

public interface Packet {
  public static enum PacketTypes {
    INVALID(-1),
    REDIRECT(00),
    LOGIN(01),
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

  public static String readData(final byte[] data, int length) {
    final String message = new String(data, 0, length).trim();
    return message;
  }

  public byte[] getBytes();

  public PacketTypes getType();

}
