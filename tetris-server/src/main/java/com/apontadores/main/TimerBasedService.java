package com.apontadores.main;

import java.util.Timer;
import java.util.TimerTask;

public class TimerBasedService {
  private Timer timer;
  private TimerTask task;
  private int delay;
  private int period;
  private boolean active = false;

  public TimerBasedService(
      final TimerTask task,
      final int delay,
      final int period) {

    this.task = task;
    this.delay = delay;
    this.period = period;
    this.timer = new Timer();
  }

  public void start() {
    if (active) {
      return;
    }
    active = true;
    timer.schedule(task, delay, period);
  }

  public void stop() {
    if (!active) {
      return;
    }
    active = false;
    task.cancel();
  }

}
