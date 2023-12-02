package com.apontadores.gameStates.states.menus;

import static com.apontadores.utils.Constants.RESOURCES_PATH;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.apontadores.gameStates.GameState;
import com.apontadores.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.main.Game;
import com.apontadores.ui.ButtonAction;
import com.apontadores.ui.Frame;
import com.apontadores.ui.ImageElement;
import com.apontadores.ui.SwitchStateAction;
import com.apontadores.utils.LoadSave;

public class ChangeKeybinds extends GameState {

  private enum keyAction {
    OFF,
    ROTATE_LEFT,
    ROTATE_RIGHT,
    MOVE_LEFT,
    MOVE_RIGHT,
    SOFT_DROP,
    HARD_DROP,
    HOLD,
    PAUSE;
  }

  private keyAction Action = keyAction.OFF;

  private final Frame frame;
  private final SwitchStateAction switchState = new SwitchStateAction();

  private final ButtonAction<keyAction, Void> changeKeyAction = (keyAction a) -> {
    Action = a;
    return null;
  };

  private final ButtonAction<Void, Void> resetKeyAction = (Void) -> {
    Game.resetKeybind();
    updateKeybinds();
    LoadSave.saveJson(RESOURCES_PATH + "/config/keybinds.json", Game.getKeybinds());
    return null;
  };

  public ChangeKeybinds() {
    super(GameStatesEnum.CHANGE_KEYBINDS);
    frame = Frame.loadFromJson(RESOURCES_PATH + "/frames/changeKeybinds.json");

    ((ImageElement) frame.getElement("changeKeybindRotateLeft"))
        .getTextElement()
        .setText(
            new String(String.valueOf((char) Game.getKeybinds().rotatesLeft)));

    ((ImageElement) frame.getElement("changeKeybindRotateRight"))
        .getTextElement()
        .setText(
            new String(String.valueOf((char) Game.getKeybinds().rotatesRight)));

    ((ImageElement) frame.getElement("changeKeybindMoveRight"))
        .getTextElement()
        .setText("Right Arrow");

    ((ImageElement) frame.getElement("changeKeybindMoveLeft"))
        .getTextElement()
        .setText("Left Arrow");

    ((ImageElement) frame.getElement("changeKeybindSoftDrop"))
        .getTextElement()
        .setText("Down Arrow");

    ((ImageElement) frame.getElement("changeKeybindHardDrop"))
        .getTextElement()
        .setText("Spacebar");

    ((ImageElement) frame.getElement("changeKeybindHold"))
        .getTextElement()
        .setText(
            new String(String.valueOf((char) Game.getKeybinds().hold)));

    ((ImageElement) frame.getElement("changeKeybindPause"))
        .getTextElement()
        .setText(
            new String(String.valueOf((char) Game.getKeybinds().pause)));

  }

  @Override
  public void render(final Graphics g) {
    frame.render(g);
  }

  @Override
  public void mouseClicked(final MouseEvent e) {

    int x = e.getX();
    int y = e.getY();

    ((ImageElement) frame.getElement("changeKeybindRotateLeft"))
        .execIfClicked(x, y, changeKeyAction, keyAction.ROTATE_LEFT);

    ((ImageElement) frame.getElement("changeKeybindRotateRight"))
        .execIfClicked(x, y, changeKeyAction, keyAction.ROTATE_RIGHT);

    ((ImageElement) frame.getElement("changeKeybindMoveRight"))
        .execIfClicked(x, y, changeKeyAction, keyAction.MOVE_RIGHT);

    ((ImageElement) frame.getElement("changeKeybindMoveLeft"))
        .execIfClicked(x, y, changeKeyAction, keyAction.MOVE_LEFT);

    ((ImageElement) frame.getElement("changeKeybindSoftDrop"))
        .execIfClicked(x, y, changeKeyAction, keyAction.SOFT_DROP);

    ((ImageElement) frame.getElement("changeKeybindHardDrop"))
        .execIfClicked(x, y, changeKeyAction, keyAction.HARD_DROP);

    ((ImageElement) frame.getElement("changeKeybindHold"))
        .execIfClicked(x, y, changeKeyAction, keyAction.HOLD);

    ((ImageElement) frame.getElement("changeKeybindPause"))
        .execIfClicked(x, y, changeKeyAction, keyAction.PAUSE);

    ((ImageElement) frame.getElement("Reset"))
        .execIfClicked(x, y, resetKeyAction, null);

    ((ImageElement) frame.getElement("returnToSettings"))
        .execIfClicked(x, y, switchState, GameStatesEnum.SETTINGS);

  }

  @Override
  public void keyPressed(final KeyEvent e) {
    switch (Action) {
      case ROTATE_LEFT:
        Game.getKeybinds().rotatesLeft = e.getKeyCode();

        ((ImageElement) frame.getElement("changeKeybindRotateLeft"))
            .getTextElement()
            .setText(
                new String(String.valueOf((char) Game.getKeybinds().rotatesLeft)));

        break;

      case ROTATE_RIGHT:
        Game.getKeybinds().rotatesRight = e.getKeyCode();

        ((ImageElement) frame.getElement("changeKeybindRotateRight"))
            .getTextElement()
            .setText(
                (new String(String.valueOf((char) Game.getKeybinds().rotatesRight))));

        break;
      case MOVE_LEFT:
        Game.getKeybinds().movesLeft = e.getKeyCode();

        ((ImageElement) frame.getElement("changeKeybindMoveLeft"))
            .getTextElement()
            .setText(
                new String(String.valueOf((char) Game.getKeybinds().movesLeft)));

        break;
      case MOVE_RIGHT:
        Game.getKeybinds().movesRight = e.getKeyCode();

        ((ImageElement) frame.getElement("changeKeybindMoveRight"))
            .getTextElement()
            .setText(
                new String(String.valueOf((char) Game.getKeybinds().movesRight)));

        break;
      case SOFT_DROP:
        Game.getKeybinds().softDrop = e.getKeyCode();

        ((ImageElement) frame.getElement("changeKeybindSoftDrop"))
            .getTextElement()
            .setText(
                new String(String.valueOf((char) Game.getKeybinds().softDrop)));

        break;
      case HARD_DROP:
        Game.getKeybinds().hardDrop = e.getKeyCode();

        ((ImageElement) frame.getElement("changeKeybindHardDrop"))
            .getTextElement()
            .setText(
                new String(String.valueOf((char) Game.getKeybinds().hardDrop)));

        break;
      case HOLD:
        Game.getKeybinds().hold = e.getKeyCode();

        ((ImageElement) frame.getElement("changeKeybindHold"))
            .getTextElement()
            .setText(
                new String(String.valueOf((char) Game.getKeybinds().hold)));

        break;
      case PAUSE:
        Game.getKeybinds().pause = e.getKeyCode();

        ((ImageElement) frame.getElement("changeKeybindPause"))
            .getTextElement()
            .setText(
                new String(String.valueOf((char) Game.getKeybinds().pause)));

        break;
      default:
        return;
    }
    LoadSave.saveJson(RESOURCES_PATH + "/config/keybinds.json", Game.getKeybinds());

    Action = keyAction.OFF;
  }

  public void updateKeybinds() {

    ((ImageElement) frame.getElement("changeKeybindRotateLeft"))
        .getTextElement()
        .setText(
            new String(String.valueOf((char) Game.getKeybinds().rotatesLeft)));

    ((ImageElement) frame.getElement("changeKeybindRotateRight"))
        .getTextElement()
        .setText(
            new String(String.valueOf((char) Game.getKeybinds().rotatesRight)));

    ((ImageElement) frame.getElement("changeKeybindMoveRight"))
        .getTextElement()
        .setText(
            new String(String.valueOf((char) Game.getKeybinds().movesRight)));

    ((ImageElement) frame.getElement("changeKeybindMoveLeft"))
        .getTextElement()
        .setText(
            new String(String.valueOf((char) Game.getKeybinds().movesLeft)));

    ((ImageElement) frame.getElement("changeKeybindSoftDrop"))
        .getTextElement()
        .setText(
            new String(String.valueOf((char) Game.getKeybinds().softDrop)));

    ((ImageElement) frame.getElement("changeKeybindHardDrop"))
        .getTextElement()
        .setText(
            new String(String.valueOf((char) Game.getKeybinds().hardDrop)));

    ((ImageElement) frame.getElement("changeKeybindHold"))
        .getTextElement()
        .setText(
            new String(String.valueOf((char) Game.getKeybinds().hold)));

    ((ImageElement) frame.getElement("changeKeybindPause"))
        .getTextElement()
        .setText(
            new String(String.valueOf((char) Game.getKeybinds().pause)));
  }

}