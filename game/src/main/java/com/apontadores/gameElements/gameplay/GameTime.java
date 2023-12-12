package com.apontadores.gameElements.gameplay;

import static com.apontadores.utils.Constants.GameConstants.UPS_SET;

public class GameTime {
  private static int seconds = 0;
  private static int minutes = 0;
  private static int hours = 0;

  private static int tickCounter = 0;

  public static void tick() {
    tickCounter++;

    if (tickCounter == UPS_SET) {
      tickCounter = 0;
      seconds++;
    }

    if (seconds == 60) {
      seconds = 0;
      minutes++;
    }

    if (minutes == 60) {
      minutes = 0;
      hours++;
    }
  }

  public static void reset() {
    seconds = 0;
    minutes = 0;
    hours = 0;
  }

  public static String getTimeStr() {
    return hours + ":" + minutes + ":" + seconds;
  }

  public static int getSeconds() {
    return seconds;
  }

  public static int getMinutes() {
    return minutes;
  }

  public static int getHours() {
    return hours;
  }

}
