package com.apontadores.packets;

public interface PacketMethods {
  public byte[] asBytes();

  public String[] asTokens();

  public Packet fromTokens(String[] tokens) throws PacketException;

  public int getTransactionID();

  public void setTransactionID(int transactionID);
}
