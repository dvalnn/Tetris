package gameStates;

public enum GameState {
  PLAYING,
  PLAYING_MP,
  TITLE_SCREEN,
  GAME_OVER;

  public static GameState state = GameState.TITLE_SCREEN;
}
