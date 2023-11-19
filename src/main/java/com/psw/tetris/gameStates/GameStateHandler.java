package com.psw.tetris.gameStates;

import java.util.HashMap;

import com.psw.tetris.gameStates.stateTypes.GameModeSelect;
import com.psw.tetris.gameStates.stateTypes.GameOver;
import com.psw.tetris.gameStates.stateTypes.MainMenu;
import com.psw.tetris.gameStates.stateTypes.Playing;
import com.psw.tetris.gameStates.stateTypes.PlayingMP;
import com.psw.tetris.gameStates.stateTypes.TitleScreen;

public final class GameStateHandler {
  public enum GameStatesEnum {
    TITLE_SCREEN,
    MAIN_MENU,
    GAME_MODE_SELECT,
    PLAYING,
    PLAYING_MP,
    GAME_OVER;
  }

  private static GameStatesEnum activeState;
  private static HashMap<GameStatesEnum, GameState> statesMap = new HashMap<GameStatesEnum, GameState>();

  private static void addState(final GameState state) {
    statesMap.put(state.getStateID(), state);
  }

  public static void init() {

    activeState = GameStatesEnum.TITLE_SCREEN;

    addState(new TitleScreen());
    addState(new MainMenu());
    addState(new GameModeSelect());
    addState(new PlayingMP());
    addState(new Playing());
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

  // TODO: replace with switchState
  public static void setActiveState(final GameStatesEnum state) {
    activeState = state;
  }

  public static void switchState(final GameStatesEnum state) {
    activeState = state;
  }
}
