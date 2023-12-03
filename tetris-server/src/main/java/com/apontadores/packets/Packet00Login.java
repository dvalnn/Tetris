package com.apontadores.packets;

public class Packet00Login extends Packet {

  public static Packet00Login fromDatagram(byte[] bytes) {
    String data = readData(bytes);
    System.out.println("[Packat00Login] from data: " + data);
    String dataBlocks[] = data.split(",");

    if (dataBlocks.length != 2 || !dataBlocks[0].equals("00")) {
      return null;
    }

    return new Packet00Login(dataBlocks[1]);
  }

  private final String username;

  public Packet00Login(final String username) {
    super(00);
    this.username = username;
  }

  @Override
  public byte[] getBytes() {
    return ("00" + "," + this.username).getBytes();
  }

  public String getUsername() {
    return username;
  }

}
