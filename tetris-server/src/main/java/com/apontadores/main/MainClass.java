
package com.apontadores.main;

public class MainClass {
  private final static int defaultPort = 42069;

  public static void main(final String[] args) {
    System.out.println("Hello World!");
    Thread serverThread = new Thread(new Server(MainClass.defaultPort));
    serverThread.start();
    try {
      serverThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
