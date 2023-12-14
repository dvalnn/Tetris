package com.apontadores.gameStates;

import java.util.HashMap;

import com.apontadores.gameStates.states.menus.AboutUs;
import com.apontadores.gameStates.states.menus.ChangeKeybinds;
import com.apontadores.gameStates.states.menus.GameModeSelect;
import com.apontadores.gameStates.states.menus.Leaderboard;
import com.apontadores.gameStates.states.menus.MainMenu;
import com.apontadores.gameStates.states.menus.Pause;
import com.apontadores.gameStates.states.menus.Settings;
import com.apontadores.gameStates.states.menus.TitleScreen;
import com.apontadores.gameStates.states.menus.Username;
import com.apontadores.gameStates.states.multiP.Connecting;
import com.apontadores.gameStates.states.multiP.GameModeSelectMP;
import com.apontadores.gameStates.states.multiP.Host;
import com.apontadores.gameStates.states.multiP.Join;
import com.apontadores.gameStates.states.multiP.PlayingMP;
import com.apontadores.gameStates.states.singleP.GameOver;
import com.apontadores.gameStates.states.singleP.Playing;
import com.apontadores.main.Game;

public final class GameStateHandler {
  public enum GameStatesEnum {
    TITLE_SCREEN,
    USERNAME,
    MAIN_MENU,
    SETTINGS,
    LEADERBOARD,
    ABOUT_US,
    CHANGE_KEYBINDS,
    MODE_SELECT,
    MODE_SELECT_MP,
    JOIN,
    CONNECTING,
    HOST,
    PLAYING,
    PLAYING_MP,
    PAUSE,
    GAME_OVER
  }

  private static GameStatesEnum activeState;
  private static final HashMap<GameStatesEnum, GameState> statesMap = new HashMap<>();

  public static void reloadState(final GameStatesEnum state) {
    GameState newInstance;
    try {
      newInstance = statesMap.get(state).getClass().getDeclaredConstructor().newInstance();
      statesMap.replace(state, newInstance);
    } catch (final Exception e) {
      System.err.println("Failed to reload state: " + state);
      e.printStackTrace();
      Game.exit();
    }
  }

  public static void init() {
    activeState = GameStatesEnum.TITLE_SCREEN;
    addState(new TitleScreen());
    addState(new Username());
    addState(new MainMenu());
    addState(new Settings());
    addState(new Leaderboard());
    addState(new AboutUs());
    addState(new ChangeKeybinds());
    addState(new GameModeSelect());
    addState(new Playing());
    addState(new Pause());
    addState(new GameOver());
    addState(new GameModeSelectMP());
    addState(new Host());
    addState(new Join());
    addState(new Connecting());
    addState(new PlayingMP());
  }

  public static GameState getState(final GameStatesEnum stateID) {
    return statesMap.get(stateID);
  }

  public static GameStatesEnum getActiveStateID() {
    return activeState;
  }

  public static GameState getActiveState() {
    return statesMap.get(activeState);
  }

  public static void switchState(final GameStatesEnum state) {
    activeState = state;
  }

  private static void addState(final GameState state) {
    statesMap.put(state.getStateID(), state);
  }

}
