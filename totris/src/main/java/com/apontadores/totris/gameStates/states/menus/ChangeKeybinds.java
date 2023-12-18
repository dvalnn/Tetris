package com.apontadores.totris.gameStates.states.menus;

import static com.apontadores.totris.utils.Constants.FRAMES_PATH;
import static com.apontadores.totris.utils.Constants.RESOURCES_PATH;
import static com.apontadores.totris.utils.Constants.SYS_SEPARATOR;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.apontadores.totris.gameStates.GameState;
import com.apontadores.totris.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.totris.main.Game;
import com.apontadores.totris.ui.ButtonAction;
import com.apontadores.totris.ui.Frame;
import com.apontadores.totris.ui.ImageElement;
import com.apontadores.totris.ui.SwitchStateAction;
import com.apontadores.totris.utils.LoadSave;

//TODO: Refactor this piece of code 

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
    PAUSE
  }

  private static final String KEYBINDS_PATH = RESOURCES_PATH +
      SYS_SEPARATOR + "config" + SYS_SEPARATOR + "keybinds.json";

  private keyAction Action = keyAction.OFF;

  private final Frame frame;
  private final SwitchStateAction switchState = new SwitchStateAction();

  private final ButtonAction<keyAction, Void> changeKeyAction = (final keyAction a) -> {
    Action = a;
    return null;
  };

  private final ButtonAction<Void, Void> resetKeyAction = (Void) -> {
    Game.resetKeybind();
    updateKeybinds();
    LoadSave.saveJson(KEYBINDS_PATH, Game.getKeybindings());
    return null;
  };

  public ChangeKeybinds() {
    super(GameStatesEnum.CHANGE_KEYBINDS);
    frame = Frame.loadFromJson(FRAMES_PATH + "changeKeybinds.json");

    if (!checksSpecialKeys(Game.getKeybindings().rotatesLeft, "changeKeybindRotateLeft")) {
      ((ImageElement) frame.getElement("changeKeybindRotateLeft"))
          .getTextElement()
          .setText(
              String.valueOf((char) Game.getKeybindings().rotatesLeft));
    }

    if (!checksSpecialKeys(Game.getKeybindings().rotatesRight, "changeKeybindRotateRight")) {
      ((ImageElement) frame.getElement("changeKeybindRotateRight"))
          .getTextElement()
          .setText(
              String.valueOf((char) Game.getKeybindings().rotatesRight));
    }

    if (!checksSpecialKeys(Game.getKeybindings().movesRight, "changeKeybindMoveRight")) {
      ((ImageElement) frame.getElement("changeKeybindMoveRight"))
          .getTextElement()
          .setText(
              String.valueOf((char) Game.getKeybindings().movesRight));
    }

    if (!checksSpecialKeys(Game.getKeybindings().movesLeft, "changeKeybindMoveLeft")) {
      ((ImageElement) frame.getElement("changeKeybindMoveLeft"))
          .getTextElement()
          .setText(
              String.valueOf((char) Game.getKeybindings().movesLeft));
    }

    if (!checksSpecialKeys(Game.getKeybindings().softDrop, "changeKeybindSoftDrop")) {
      ((ImageElement) frame.getElement("changeKeybindSoftDrop"))
          .getTextElement()
          .setText(
              String.valueOf((char) Game.getKeybindings().softDrop));
    }

    if (!checksSpecialKeys(Game.getKeybindings().hardDrop, "changeKeybindHardDrop")) {
      ((ImageElement) frame.getElement("changeKeybindHardDrop"))
          .getTextElement()
          .setText(
              String.valueOf((char) Game.getKeybindings().hardDrop));
    }

    if (!checksSpecialKeys(Game.getKeybindings().hold, "changeKeybindHold")) {
      ((ImageElement) frame.getElement("changeKeybindHold"))
          .getTextElement()
          .setText(
              String.valueOf((char) Game.getKeybindings().hold));
    }

    if (!checksSpecialKeys(Game.getKeybindings().pause, "changeKeybindPause")) {
      ((ImageElement) frame.getElement("changeKeybindPause"))
          .getTextElement()
          .setText(
              String.valueOf((char) Game.getKeybindings().pause));
    }
  }

  @Override
  public void render(final Graphics g) {
    frame.render(g);
  }

  @Override
  public void mouseClicked(final MouseEvent e) {

    final int x = e.getX();
    final int y = e.getY();

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

        if (checkDuplicateKeys(e))
          Game.getKeybindings().rotatesLeft = e.getKeyCode();

        if (checksSpecialKeys(Game.getKeybindings().rotatesLeft, "changeKeybindRotateLeft"))
          break;

        ((ImageElement) frame.getElement("changeKeybindRotateLeft"))
            .getTextElement()
            .setText(
                String.valueOf((char) Game.getKeybindings().rotatesLeft));

        break;

      case ROTATE_RIGHT:

        if (checkDuplicateKeys(e))
          Game.getKeybindings().rotatesRight = e.getKeyCode();

        if (checksSpecialKeys(Game.getKeybindings().rotatesRight, "changeKeybindRotateRight"))
          break;

        ((ImageElement) frame.getElement("changeKeybindRotateRight"))
            .getTextElement()
            .setText(
                (String.valueOf((char) Game.getKeybindings().rotatesRight)));

        break;
      case MOVE_LEFT:

        if (checkDuplicateKeys(e))
          Game.getKeybindings().movesLeft = e.getKeyCode();

        if (checksSpecialKeys(Game.getKeybindings().movesLeft, "changeKeybindMoveLeft"))
          break;

        ((ImageElement) frame.getElement("changeKeybindMoveLeft"))
            .getTextElement()
            .setText(
                String.valueOf((char) Game.getKeybindings().movesLeft));

        break;
      case MOVE_RIGHT:

        if (checkDuplicateKeys(e))
          Game.getKeybindings().movesRight = e.getKeyCode();

        if (checksSpecialKeys(Game.getKeybindings().movesRight, "changeKeybindMoveRight"))
          break;

        ((ImageElement) frame.getElement("changeKeybindMoveRight"))
            .getTextElement()
            .setText(
                String.valueOf((char) Game.getKeybindings().movesRight));

        break;
      case SOFT_DROP:

        if (checkDuplicateKeys(e))
          Game.getKeybindings().softDrop = e.getKeyCode();

        if (checksSpecialKeys(Game.getKeybindings().softDrop, "changeKeybindSoftDrop"))
          break;

        ((ImageElement) frame.getElement("changeKeybindSoftDrop"))
            .getTextElement()
            .setText(
                String.valueOf((char) Game.getKeybindings().softDrop));

        break;
      case HARD_DROP:

        if (checkDuplicateKeys(e))
          Game.getKeybindings().hardDrop = e.getKeyCode();

        if (checksSpecialKeys(Game.getKeybindings().hardDrop, "changeKeybindHardDrop"))
          break;

        ((ImageElement) frame.getElement("changeKeybindHardDrop"))
            .getTextElement()
            .setText(
                String.valueOf((char) Game.getKeybindings().hardDrop));

        break;
      case HOLD:

        if (checkDuplicateKeys(e))
          Game.getKeybindings().hold = e.getKeyCode();

        if (checksSpecialKeys(Game.getKeybindings().hold, "changeKeybindHold"))
          break;

        ((ImageElement) frame.getElement("changeKeybindHold"))
            .getTextElement()
            .setText(
                String.valueOf((char) Game.getKeybindings().hold));

        break;
      case PAUSE:
        if (checkDuplicateKeys(e))
          Game.getKeybindings().pause = e.getKeyCode();

        if (checksSpecialKeys(Game.getKeybindings().pause, "changeKeybindPause"))
          break;

        ((ImageElement) frame.getElement("changeKeybindPause"))
            .getTextElement()
            .setText(
                String.valueOf((char) Game.getKeybindings().pause));

        break;
      default:
        return;
    }
    LoadSave.saveJson(RESOURCES_PATH + "/config/keybinds.json", Game.getKeybindings());

    Action = keyAction.OFF;
  }

  public void updateKeybinds() {

    ((ImageElement) frame.getElement("changeKeybindRotateLeft"))
        .getTextElement()
        .setText(
            String.valueOf((char) Game.getKeybindings().rotatesLeft));

    ((ImageElement) frame.getElement("changeKeybindRotateRight"))
        .getTextElement()
        .setText(
            String.valueOf((char) Game.getKeybindings().rotatesRight));

    ((ImageElement) frame.getElement("changeKeybindMoveRight"))
        .getTextElement()
        .setText("\u2190");

    ((ImageElement) frame.getElement("changeKeybindMoveLeft"))
        .getTextElement()
        .setText("\u2192");

    ((ImageElement) frame.getElement("changeKeybindSoftDrop"))
        .getTextElement()
        .setText("\u2193");

    ((ImageElement) frame.getElement("changeKeybindHardDrop"))
        .getTextElement()
        .setText("\u2423");

    ((ImageElement) frame.getElement("changeKeybindHold"))
        .getTextElement()
        .setText(
            String.valueOf((char) Game.getKeybindings().hold));

    ((ImageElement) frame.getElement("changeKeybindPause"))
        .getTextElement()
        .setText(
            String.valueOf((char) Game.getKeybindings().pause));
  }

  public boolean checksSpecialKeys(final int keybind, final String key) {
    final int leftArrow = 37;
    if (keybind == leftArrow) {

      ((ImageElement) frame.getElement(key))
          .getTextElement()
          .setText("\u2192");

      return true;
    }
    final int rightArrow = 39;
    if (keybind == rightArrow) {

      ((ImageElement) frame.getElement(key))
          .getTextElement()
          .setText("\u2190");

      return true;
    }
    final int downArrow = 40;
    if (keybind == downArrow) {

      ((ImageElement) frame.getElement(key))
          .getTextElement()
          .setText("\u2193");

      return true;
    }
    final int spacebar = 32;
    if (keybind == spacebar) {

      ((ImageElement) frame.getElement(key))
          .getTextElement()
          .setText("\u2423");

      return true;
    }

    return false;
  }

  // TODO: remove this
  public boolean checkDuplicateKeys(final KeyEvent e) {

    return (e.getKeyCode() != Game.getKeybindings().rotatesLeft)
        && (e.getKeyCode() != Game.getKeybindings().rotatesRight)
        && (e.getKeyCode() != Game.getKeybindings().movesLeft)
        && (e.getKeyCode() != Game.getKeybindings().movesRight)
        && (e.getKeyCode() != Game.getKeybindings().softDrop)
        && (e.getKeyCode() != Game.getKeybindings().hardDrop)
        && (e.getKeyCode() != Game.getKeybindings().hold)
        && (e.getKeyCode() != Game.getKeybindings().pause);
  }

}
