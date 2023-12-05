package com.apontadores.packets;

// generic packet structure:
// first token is the packet type
// second token is the transaction ID
// third token is the checksum
// rest of the tokens are packet specific
public interface Packet {
  public byte[] asBytes();

  public String[] asTokens();

  public Packet fromBytes(byte[] bytes, int length) throws PacketException;

  public Packet fromTokens(String[] tokens) throws PacketException;

  public int getTransactionID();

  public void setTransactionID(int transactionID);

  public static int[] parseMetadata(String[] tokens) throws PacketException {
    if (tokens.length < 3)
      throw new PacketException("Invalid packet length");
    int[] metadata = new int[3];
    try {
      metadata[0] = Integer.parseInt(tokens[0]);
      metadata[1] = Integer.parseInt(tokens[1]);
    } catch (NumberFormatException e) {
      throw new PacketException("Invalid metadata");
    }
    return metadata;
  }

}
