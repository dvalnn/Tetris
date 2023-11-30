
package com.psw.tetris.gameStates.states.multiP;

import static com.psw.tetris.utils.Constants.RESOURCES_PATH;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.psw.tetris.gameStates.GameState;
import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.ui.Frame;
import com.psw.tetris.ui.ImageElement;
import com.psw.tetris.ui.SwitchStateAction;

public class Host extends GameState {
  private Frame frame;
  private boolean displayIP = false;
  private boolean updateText = true;
  SwitchStateAction switchState = new SwitchStateAction();

  String ip = "XXX.XXX.XXX.XXX";

  public Host() {
    super(GameStatesEnum.HOST);
    frame = Frame.loadFromJson(RESOURCES_PATH + "/frames/hostMP.json");
  }

  @Override
  public void render(Graphics g) {
    frame.render(g);

    // TODO: switch banner and toggle start button
    // when a player connects to the server
  }

  @Override
  public void update() {
    frame.update();

    if (!updateText)
      return;

    if (!displayIP)
      ip = "XXX.XXX.XXX.XXX";
    else
      // TODO:
      ip = "127.0.0.1 -- placeholder";

    ((ImageElement) frame.getElement("IP"))
        .getTextElement()
        .setText(ip);

    updateText = false;
  }

  @Override
  public void mouseClicked(MouseEvent e) {

    int x = e.getX();
    int y = e.getY();

    ((ImageElement) frame.getElement("start"))
        .execIfClicked(e.getX(), e.getY(), switchState, GameStatesEnum.PLAYING_MP);

    ((ImageElement) frame.getElement("return"))
        .execIfClicked(e.getX(), e.getY(), switchState, GameStatesEnum.MODE_SELECT_MP);

    ImageElement showIP = (ImageElement) frame.getElement("showIP");
    ImageElement hideIP = (ImageElement) frame.getElement("hideIP");

    if (!displayIP)
      showIP.execIfClicked(
          x, y,
          (Void) -> {
            displayIP = true;
            updateText = true;
            hideIP.enable();
            showIP.disable();
            return null;
          },
          null);
    else
      hideIP.execIfClicked(
          x, y,
          (Void) -> {
            displayIP = false;
            updateText = true;
            hideIP.disable();
            showIP.enable();
            return null;
          },
          null);

  }
}
