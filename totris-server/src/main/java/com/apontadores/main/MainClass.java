
package com.apontadores.main;

public class MainClass {

  public static void main(final String[] args) {
    System.out.println("Hello World!");
    Server server = new Server();
    server.calledFromMain = true;
    Thread serverThread = new Thread(server);

    serverThread.start();
    try {
      serverThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
