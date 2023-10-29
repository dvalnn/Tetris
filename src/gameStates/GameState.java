package gameStates;

public enum GameState {
  PLAYING,
  TITLE_SCREEN,
  GAME_OVER;

  public static GameState state = GameState.TITLE_SCREEN;
}
