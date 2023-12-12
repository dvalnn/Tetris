package com.apontadores.gameStates.states.menus;

import static com.apontadores.utils.Constants.FRAMES_PATH;
import static com.apontadores.utils.Constants.RESOURCES_PATH;
import static com.apontadores.utils.Constants.SYS_SEPARATOR;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.apontadores.gameStates.GameState;
import com.apontadores.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.ui.ImageElement;
import com.apontadores.ui.SwitchStateAction;
import com.apontadores.utils.LoadSave;
import com.google.gson.reflect.TypeToken;
import com.apontadores.ui.Frame;
import com.apontadores.utils.LeaderboardEntry;

public class Leaderboard extends GameState {

  private final Frame frame;
  private final SwitchStateAction action = new SwitchStateAction();

  private List<LeaderboardEntry> entries;
  private static boolean isLeaderBoardLoaded = false;

  private static final String SCORES_PATH = RESOURCES_PATH +
      SYS_SEPARATOR +
      "Scores.json";

  public Leaderboard() {
    super(GameStatesEnum.LEADERBOARD);
    frame = Frame.loadFromJson(FRAMES_PATH + "leaderboard.json");
    entries = loadEntries();
    if (entries != null)
      isLeaderBoardLoaded = true;
    System.out.println(entries);
  }

  @Override
  public void update() {
    if (!isLeaderBoardLoaded) {
      entries = loadEntries();
      isLeaderBoardLoaded = true;
    }
  }

  @Override
  public void render(final Graphics g) {
    frame.render(g);
    // FIXME: This is not the best way to render the leaderboard
    if (entries != null)
      System.out.println(entries);
  }

  @Override
  public void mouseClicked(final MouseEvent e) {
    ((ImageElement) frame.getElement("returnToMainMenu"))
        .execIfClicked(e.getX(), e.getY(), action, GameStatesEnum.MAIN_MENU);
  }

  private static List<LeaderboardEntry> loadEntries() {
    final Type listType = new TypeToken<ArrayList<LeaderboardEntry>>() {
    }.getType();
    return LoadSave.loadJson(SCORES_PATH, listType);
  }

  public static void saveNewScore(
      final String name,
      final int score,
      final int level,
      final int lines,
      final String time) {

    final LeaderboardEntry newEntry = new LeaderboardEntry()
        .setName(name)
        .setScore(score)
        .setLevel(level)
        .setLines(lines)
        .setTimeString(time);

    List<LeaderboardEntry> entries = loadEntries();

    if (entries == null) {
      entries = new ArrayList<LeaderboardEntry>();
      entries.add(newEntry);
      LoadSave.saveJson(SCORES_PATH, entries);
      isLeaderBoardLoaded = false;
      return;
    }

    if (entries.size() < 10) {
      entries.add(newEntry);
      Collections.sort(entries, (a, b) -> b.getScore() - a.getScore());
      LoadSave.saveJson(SCORES_PATH, entries);
      isLeaderBoardLoaded = false;
      return;
    }

    for (final LeaderboardEntry topScore : entries) {
      if (topScore.getScore() < newEntry.getScore()) {
        entries.set(entries.indexOf(topScore), newEntry);
        LoadSave.saveJson(SCORES_PATH, entries);
        isLeaderBoardLoaded = false;
        return;
      }
    }
  }
}
