package com.apontadores.totris.gameStates.states.menus;

import com.apontadores.totris.gameStates.GameState;
import com.apontadores.totris.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.totris.ui.Frame;
import com.apontadores.totris.ui.ImageElement;
import com.apontadores.totris.ui.SwitchStateAction;
import com.apontadores.totris.ui.TextElement;
import com.apontadores.totris.utils.LeaderboardEntry;
import com.apontadores.totris.utils.LoadSave;
import com.google.gson.reflect.TypeToken;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.apontadores.totris.utils.Constants.*;

public class Leaderboard extends GameState {

  private static boolean isLeaderBoardLoaded = false;
  private static final String SCORES_PATH = RESOURCES_PATH +
      SYS_SEPARATOR +
      "Scores.json";

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
      entries = new ArrayList<>();
      entries.add(newEntry);
      LoadSave.saveJson(SCORES_PATH, entries);
      isLeaderBoardLoaded = false;
      return;
    }

    if (entries.size() < 10) {
      entries.add(newEntry);
      entries.sort((a, b) -> b.getScore() - a.getScore());
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

  private static List<LeaderboardEntry> loadEntries() {
    final Type listType = new TypeToken<ArrayList<LeaderboardEntry>>() {
    }.getType();
    return LoadSave.loadJson(SCORES_PATH, listType);
  }

  private final Frame frame;

  private final SwitchStateAction action = new SwitchStateAction();

  private List<LeaderboardEntry> entries;

  public Leaderboard() {
    super(GameStatesEnum.LEADERBOARD);
    frame = Frame.loadFromJson(FRAMES_PATH + "leaderboard.json");
    entries = loadEntries();
    if (entries != null)
      isLeaderBoardLoaded = true;
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
    // if (entries != null)
    // System.out.println(entries);

    for (int i = 0; i < entries.size(); i++) {
      ((TextElement) frame.getElement("score" + (i + 1))).setText(createsScoreText(i));
    }
  }

  @Override
  public void mouseClicked(final MouseEvent e) {
    ((ImageElement) frame.getElement("returnToMainMenu"))
        .execIfClicked(e.getX(), e.getY(), action, GameStatesEnum.MAIN_MENU);
  }

  private String createsScoreText(final int index) {

    return entries.get(index).getName() +
        " " + "-" + " " +
        entries.get(index).getScore() +
        " " + "-" + " " +
        // entries.get(index).getLevel() +
        // " " + "-" + " " +
        // entries.get(index).getLines() +
        // " " + "-" + " " +
        entries.get(index).getTimeString();
  }
}
