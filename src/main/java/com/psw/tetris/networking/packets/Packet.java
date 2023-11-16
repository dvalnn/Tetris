package com.psw.tetris.networking.packets;

import com.psw.tetris.networking.GameClient;
import com.psw.tetris.networking.GameServer;

public abstract class Packet {
  public static enum PacketTypes {
    INVALID(-1),
    LOGIN(00),
    DISCONNECT(01),
    SERVER_FULL(02),
    BOARD(03),
    SHAPE(04);

    private int packetId;

    private PacketTypes(int packetId) {
      this.packetId = packetId;
    }

    public int getId() {
      return packetId;
    }
  }

  public byte packetId;

  public Packet(int packetId) {
    this.packetId = (byte) packetId;
  }

  public abstract void writeData(GameClient client);

  public abstract void writeData(GameServer server);

  public String readData(byte[] data) {
    String message = new String(data).trim();
    return message.substring(2);
  }

  public abstract byte[] getData();

  public static PacketTypes lookupPacket(String packetId) {
    try {
      return lookupPacket(Integer.parseInt(packetId));
    } catch (NumberFormatException e) {
      return PacketTypes.INVALID;
    }
  }

  public static PacketTypes lookupPacket(int id) {
    for (PacketTypes p : PacketTypes.values()) {
      if (p.getId() == id) {
        return p;
      }
    }
    return PacketTypes.INVALID;
  }
}
