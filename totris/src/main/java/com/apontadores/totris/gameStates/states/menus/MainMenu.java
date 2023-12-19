package com.apontadores.totris.gameStates.states.menus;

import static com.apontadores.totris.utils.Constants.FRAMES_PATH;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.apontadores.totris.gameStates.GameState;
import com.apontadores.totris.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.totris.main.Game;
import com.apontadores.totris.ui.ImageElement;
import com.apontadores.totris.ui.SwitchStateAction;
import com.apontadores.totris.ui.Frame;

public class MainMenu extends GameState {

  private final Frame frame;

  private final SwitchStateAction switchState = new SwitchStateAction();

  public MainMenu() {
    super(GameStatesEnum.MAIN_MENU);
    final String path = FRAMES_PATH + "mainMenu.json";
    frame = Frame.loadFromJson(path);

  }

  @Override
  public void render(final Graphics g) {
    frame.render(g);
  }

  @Override
  public void update() {
    frame.update();
  }

  @Override
  public void mouseClicked(final MouseEvent e) {

    final int x = e.getX();
    final int y = e.getY();

    ((ImageElement) frame.getElement("newGame"))
        .execIfClicked(x, y, switchState, GameStatesEnum.MODE_SELECT);

    ((ImageElement) frame.getElement("aboutUs"))
        .execIfClicked(x, y, switchState, GameStatesEnum.ABOUT_US);

    ((ImageElement) frame.getElement("settings"))
        .execIfClicked(x, y, switchState, GameStatesEnum.SETTINGS);

    ((ImageElement) frame.getElement("leaderboard"))
        .execIfClicked(x, y, switchState, GameStatesEnum.LEADERBOARD);

    ((ImageElement) frame.getElement("exitGame"))
        .execIfClicked(
            x, y,
            (Void) -> {
              Game.exit();
              return null;
            },
            null);
  }

}
