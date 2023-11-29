package com.psw.tetris.gameStates.states.menus;

import static com.psw.tetris.utils.Constants.RESOURCES_PATH;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.psw.tetris.gameStates.GameState;
import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.ui.ImageElement;
import com.psw.tetris.ui.SwitchStateAction;
import com.psw.tetris.ui.ButtonAction;
import com.psw.tetris.ui.Frame;

public class Settings extends GameState {

  private final Frame frame;

  private final SwitchStateAction switchState = new SwitchStateAction();

  private final ButtonAction<Void, Void> volumeManager = (Void) -> {
    return null;
  };
  private final ButtonAction<Void, Void> effectManager = (Void) -> {
    return null;
  };

  public Settings() {
    super(GameStatesEnum.SETTINGS);
    frame = Frame.loadFromJson(RESOURCES_PATH + "/frames/settings.json");
  }

  @Override
  public void render(final Graphics g) {
    frame.render(g);
  }

  @Override
  public void mouseClicked(final MouseEvent e) {

    ((ImageElement) frame.getElement("returnToMainMenu"))
        .execIfClicked(e.getX(), e.getY(), switchState, GameStatesEnum.MAIN_MENU);

    ((ImageElement) frame.getElement("changeKeybinds"))
        .execIfClicked(e.getX(), e.getY(), switchState, GameStatesEnum.CHANGE_KEYBINDS);

    ((ImageElement) frame.getElement("changeUsername"))
        .execIfClicked(e.getX(), e.getY(), switchState, GameStatesEnum.USERNAME);
       
    /*((ImageElement) frame.getElement("volumeManager"))
        .execIfClicked(e.getX(), e.getY(), volumeManager,null);

    ((ImageElement) frame.getElement("effectManager"))
        .execIfClicked(e.getX(), e.getY(), effectManager,null);*/
  }
}
