package com.apontadores.networking.packets;

import com.apontadores.networking.GameClient;
import com.apontadores.networking.GameServer;

public abstract class Packet {
  public static enum PacketTypes {
    INVALID(-1),
    LOGIN(00),
    DISCONNECT(01),
    SERVER_FULL(02),
    BOARD(03),
    SHAPE(04);

    private final int packetId;

    private PacketTypes(final int packetId) {
      this.packetId = packetId;
    }

    public int getId() {
      return packetId;
    }
  }

  public byte packetId;

  public Packet(final int packetId) {
    this.packetId = (byte) packetId;
  }

  public abstract void writeData(GameClient client);

  public abstract void writeData(GameServer server);

  public String readData(final byte[] data) {
    final String message = new String(data).trim();
    return message.substring(2);
  }

  public abstract byte[] getData();

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
}
