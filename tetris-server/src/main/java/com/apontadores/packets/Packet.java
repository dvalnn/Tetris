package com.apontadores.packets;

import java.util.StringJoiner;

// generic packet structure:
// first token is the packet type
// second token is the transaction ID
// third token is the checksum
// rest of the tokens are packet specific
public class Packet implements PacketMethods {
  public static enum PacketTypesEnum {
    INVALID(-1),

    // Login and Connection Control Packets
    LOGIN(00),
    DISCONNECT(01),
    REDIRECT(02),
    START(03),
    READY(04),

    // Player Update Packets (Playing state only)
    UPDATE(100), // player update
    BOARD(101), // current board state
    SHAPE(102), // player shape
    ELEMENTS(103), // next and hold UI elements
    SCORE(104), // player score and level
    GAME_OVER(105), // game over packet

    // Other Connection Control Packets
    HEARTBEAT(200), // keep alive packet
    ERROR(201), // error packet
    ACK(202), // acknowledge packet (used for handshakes)
    NACK(203), // negative acknowledge packet (retransmission request)
    ;

    private final int packetId;

    private PacketTypesEnum(final int packetId) {
      this.packetId = packetId;
    }

    public int getId() {
      return packetId;
    }
  }

  public static final int MIN_TOKENS = 2;

  public static String[] tokenize(final byte[] bytes, final int length) {
    return tokenize(new String(bytes, 0, length).trim());
  }

  public static String[] tokenize(final String packet) {
    final String tokens[] = packet.split(",");
    if (tokens.length < MIN_TOKENS) {
      return null;
    }
    return tokens;
  }

  public static PacketTypesEnum lookupPacket(final String[] packetTokens) {
    return lookupPacket(packetTokens[0]);
  }

  public static PacketTypesEnum lookupPacket(final String packetId) {
    try {
      return lookupPacket(Integer.parseInt(packetId));
    } catch (final NumberFormatException e) {
      return PacketTypesEnum.INVALID;
    }
  }

  public static PacketTypesEnum lookupPacket(final int id) {
    for (final PacketTypesEnum p : PacketTypesEnum.values()) {
      if (p.getId() == id) {
        return p;
      }
    }
    return PacketTypesEnum.INVALID;
  }

  protected int packetID;
  protected int transactionID;

  public Packet(final int packetID) {
    this.packetID = packetID;
  }

  @Override
  public byte[] asBytes() {
    return new StringJoiner(",")
        .add(String.valueOf(packetID))
        .add(String.valueOf(transactionID))
        .toString()
        .getBytes();
  }

  @Override
  public String[] asTokens() {
    return new String[] {
        String.valueOf(packetID),
        String.valueOf(transactionID)
    };
  }

  @Override
  public Packet fromTokens(String[] tokens) throws PacketException {
    if (tokens.length != MIN_TOKENS)
      throw new PacketException("Invalid packet length");

    int packetID;
    try {
      packetID = Integer.parseInt(tokens[0]);
      transactionID = Integer.parseInt(tokens[1]);
    } catch (final NumberFormatException e) {
      throw new PacketException("Invalid data");
    }

    if (packetID != this.packetID)
      throw new PacketException("Invalid packet ID");

    this.packetID = packetID;

    if (transactionID <= 0)
      throw new PacketException("Invalid transaction ID");

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
