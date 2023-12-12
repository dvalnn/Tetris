package com.apontadores.utils;

public class LeaderboardEntry {

  private String name;
  private int score;
  private int level;
  private int lines;
  private String timeString;

  public int getLines() {
    return lines;
  }

  public LeaderboardEntry setLines(int lines) {
    this.lines = lines;
    return this;
  }

  public String getName() {
    return name;
  }

  public LeaderboardEntry setName(String username) {
    this.name = username;
    return this;
  }

  public int getScore() {
    return score;
  }

  public LeaderboardEntry setScore(Integer score) {
    this.score = score;
    return this;
  }

  public int getLevel() {
    return level;
  }

  public LeaderboardEntry setLevel(Integer level) {
    this.level = level;
    return this;
  }

  public String getTimeString() {
    return timeString;
  }

  public LeaderboardEntry setTimeString(String time_in_string) {
    this.timeString = time_in_string;
    return this;
  }

}
