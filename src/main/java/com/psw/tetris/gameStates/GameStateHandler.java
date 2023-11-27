package com.psw.tetris.gameStates;

import java.util.HashMap;

import com.psw.tetris.gameStates.states.menus.AboutUs;
import com.psw.tetris.gameStates.states.menus.ChangeKeybinds;
import com.psw.tetris.gameStates.states.menus.GameModeSelect;
import com.psw.tetris.gameStates.states.menus.Leaderboard;
import com.psw.tetris.gameStates.states.menus.MainMenu;
import com.psw.tetris.gameStates.states.menus.Pause;
import com.psw.tetris.gameStates.states.menus.Settings;
import com.psw.tetris.gameStates.states.menus.TitleScreen;
import com.psw.tetris.gameStates.states.menus.Username;
import com.psw.tetris.gameStates.states.multiP.GameModeSelectMP;
import com.psw.tetris.gameStates.states.multiP.HostGame;
import com.psw.tetris.gameStates.states.multiP.JoinGame;
import com.psw.tetris.gameStates.states.multiP.PlayingMP;
import com.psw.tetris.gameStates.states.singleP.GameOver;
import com.psw.tetris.gameStates.states.singleP.Playing;
import com.psw.tetris.main.Game;

public final class GameStateHandler {
  public enum GameStatesEnum {
    TITLE_SCREEN,
    USERNAME,
    MAIN_MENU,
    SETTINGS,
    LEADERBOARD,
    ABOUT_US,
    CHANGE_KEYBINDS,
    GAME_MODE_SELECT,
    GAME_MODE_SELECT_MP,
    HOST_GAME,
    JOIN_GAME,
    PLAYING,
    PLAYING_MP,
    PAUSE,
    GAME_OVER;
  }

  private static GameStatesEnum activeState;
  private static HashMap<GameStatesEnum, GameState> statesMap = new HashMap<GameStatesEnum, GameState>();

  private static void addState(final GameState state) {
    statesMap.put(state.getStateID(), state);
  }

  public static void reloadState(final GameStatesEnum state) {
    GameState newInstance;
    try {
      newInstance = statesMap.get(state).getClass().getDeclaredConstructor().newInstance();
      statesMap.replace(state, newInstance);
    } catch (Exception e) {
      System.err.println("Failed to reload state: " + state);
      e.printStackTrace();
      Game.exit();
    }
  }

  public static void init() {

    activeState = GameStatesEnum.TITLE_SCREEN;
    addState(new TitleScreen());
    addState(new MainMenu());
    addState(new Settings());
    addState(new Leaderboard());
    addState(new AboutUs());
    addState(new ChangeKeybinds());
    addState(new GameModeSelect());
    addState(new GameModeSelectMP());
    addState(new Username());
    addState(new HostGame());
    addState(new JoinGame());
    addState(new Playing());
    addState(new PlayingMP());
    addState(new Pause());
    addState(new GameOver());
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

}
