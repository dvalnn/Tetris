
package com.apontadores.main;

public class MainClass {
  private final static int defaultPort = 42069;

  public static void main(String[] args) {
    System.out.println("Hello World!");
    new Thread(new Server(MainClass.defaultPort)).start();
  }
}
