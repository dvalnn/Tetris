package com.psw.tetris.gameStates;

import java.util.HashMap;

import com.psw.tetris.gameStates.stateTypes.GameOver;
import com.psw.tetris.gameStates.stateTypes.MainMenu;
import com.psw.tetris.gameStates.stateTypes.Playing;
import com.psw.tetris.gameStates.stateTypes.PlayingMP;
import com.psw.tetris.gameStates.stateTypes.TitleScreen;

public final class GameStateHandler {
  public enum GameStatesEnum {
    PLAYING,
    PLAYING_MP,
    TITLE_SCREEN,
    MAIN_MENU,
    GAME_OVER;
  }

  private static GameStatesEnum activeState;
  private static HashMap<GameStatesEnum, State> statesMap = new HashMap<GameStatesEnum, State>();

  private static void addState(State state) {
    statesMap.put(state.getStateID(), state);
  }

  public static void init() {

    activeState = GameStatesEnum.TITLE_SCREEN;

    addState(new TitleScreen());
    addState(new MainMenu());
    addState(new PlayingMP());
    addState(new Playing());
    addState(new GameOver());
  }

  public static State getState(GameStatesEnum stateID) {
    return statesMap.get(stateID);
  }

  public static GameStatesEnum getActiveStateID() {
    return activeState;
  }

  public static State getActiveState() {
    return statesMap.get(activeState);
  }

  // TODO: replace with switchState
  public static void setActiveState(GameStatesEnum state) {
    activeState = state;
  }

  public static void switchState(GameStatesEnum state) {
    activeState = state;
  }
}
