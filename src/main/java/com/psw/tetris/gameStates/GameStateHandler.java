package com.psw.tetris.gameStates;

import java.util.HashMap;

import com.psw.tetris.gameStates.stateTypes.AboutUs;
import com.psw.tetris.gameStates.stateTypes.ChangeKeybinds;
import com.psw.tetris.gameStates.stateTypes.GameModeSelect;
import com.psw.tetris.gameStates.stateTypes.GameModeSelectMP;
import com.psw.tetris.gameStates.stateTypes.GameOver;
import com.psw.tetris.gameStates.stateTypes.Leaderboard;
import com.psw.tetris.gameStates.stateTypes.Lobby;
import com.psw.tetris.gameStates.stateTypes.HostGame;
import com.psw.tetris.gameStates.stateTypes.JoinGame;
import com.psw.tetris.gameStates.stateTypes.MainMenu;
import com.psw.tetris.gameStates.stateTypes.Pause;
import com.psw.tetris.gameStates.stateTypes.Playing;
import com.psw.tetris.gameStates.stateTypes.PlayingMP;
import com.psw.tetris.gameStates.stateTypes.Settings;
import com.psw.tetris.gameStates.stateTypes.TitleScreen;
import com.psw.tetris.main.Game;

public final class GameStateHandler {
  public enum GameStatesEnum {
    TITLE_SCREEN,
    MAIN_MENU,
    SETTINGS,
    LEADERBOARD,
    ABOUT_US,
    CHANGE_KEYBINDS,
    GAME_MODE_SELECT,
    GAME_MODE_SELECT_MP,
    LOBBY,
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
    addState(new Lobby());
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
