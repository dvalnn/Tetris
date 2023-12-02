package com.apontadores.gameStates.states.menus;

import static com.apontadores.utils.Constants.RESOURCES_PATH;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.apontadores.gameStates.GameState;
import com.apontadores.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.main.Game;
import com.apontadores.ui.ImageElement;
import com.apontadores.ui.SwitchStateAction;
import com.apontadores.ui.Frame;

public class MainMenu extends GameState {

  private Frame frame;

  private SwitchStateAction switchState = new SwitchStateAction();

  public MainMenu() {
    super(GameStatesEnum.MAIN_MENU);
    String path = RESOURCES_PATH + "/frames/mainMenu.json";
    frame = Frame.loadFromJson(path);

  }

  @Override
  public void render(Graphics g) {
    frame.render(g);
  }

  @Override
  public void update() {
    frame.update();
  }

  @Override
  public void mouseClicked(MouseEvent e) {

    int x = e.getX();
    int y = e.getY();

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