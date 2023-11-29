package com.psw.tetris.gameStates.states.menus;

import static com.psw.tetris.utils.Constants.RESOURCES_PATH;
import static com.psw.tetris.utils.Constants.KEYBINDINGS_PATH;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.psw.tetris.gameStates.GameState;
import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.main.Game;
import com.psw.tetris.ui.ImageElement;
import com.psw.tetris.ui.SwitchStateAction;
import com.psw.tetris.utils.Keybindings;
import com.psw.tetris.ui.ButtonAction;
import com.psw.tetris.ui.Frame;

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
    updatesKeybinds();
    Keybindings.saveToFile(Game.getKeybind(), KEYBINDINGS_PATH);
    return null;
  };

  public ChangeKeybinds() {
    super(GameStatesEnum.CHANGE_KEYBINDS);
    frame = Frame.loadFromJson(RESOURCES_PATH + "/frames/changeKeybinds.json");

     ((ImageElement) frame.getElement("changeKeybindRotateLeft"))
      .getTextElement()
      .setText(
      new String(String.valueOf((char)Game.getKeybind().rotatesLeft)));

      ((ImageElement) frame.getElement("changeKeybindRotateRight"))
      .getTextElement()
      .setText(
      new String(String.valueOf((char)Game.getKeybind().rotatesRight)));

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
      new String(String.valueOf((char)Game.getKeybind().hold)));

      ((ImageElement) frame.getElement("changeKeybindPause"))
      .getTextElement()
      .setText(
      new String(String.valueOf((char)Game.getKeybind().pause)));

  
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
      Game.getKeybind().rotatesLeft = e.getKeyCode();

     ((ImageElement) frame.getElement("changeKeybindRotateLeft"))
      .getTextElement()
      .setText(
      new String(String.valueOf((char)Game.getKeybind().rotatesLeft)));

        break;

      case ROTATE_RIGHT:
      Game.getKeybind().rotatesRight = e.getKeyCode();

      ((ImageElement) frame.getElement("changeKeybindRotateRight"))
      .getTextElement()
      .setText(
      (new String (String.valueOf((char)Game.getKeybind().rotatesRight))));

        break;
      case MOVE_LEFT:
      Game.getKeybind().movesLeft = e.getKeyCode();

      ((ImageElement) frame.getElement("changeKeybindMoveLeft"))
      .getTextElement()
      .setText(
      new String(String.valueOf((char)Game.getKeybind().movesLeft)));

        break;
      case MOVE_RIGHT:
      Game.getKeybind().movesRight = e.getKeyCode();

      ((ImageElement) frame.getElement("changeKeybindMoveRight"))
      .getTextElement()
      .setText(
      new String(String.valueOf((char)Game.getKeybind().movesRight)));

        break;
      case SOFT_DROP:
      Game.getKeybind().softDrop = e.getKeyCode();

      ((ImageElement) frame.getElement("changeKeybindSoftDrop"))
      .getTextElement()
      .setText(
      new String(String.valueOf((char)Game.getKeybind().softDrop)));

        break;
      case HARD_DROP:
      Game.getKeybind().hardDrop = e.getKeyCode();

      ((ImageElement) frame.getElement("changeKeybindHardDrop"))
      .getTextElement()
      .setText(
      new String(String.valueOf((char)Game.getKeybind().hardDrop)));

        break;
      case HOLD:
      Game.getKeybind().hold = e.getKeyCode();

      ((ImageElement) frame.getElement("changeKeybindHold"))
      .getTextElement()
      .setText(
      new String(String.valueOf((char)Game.getKeybind().hold)));

        break;
      case PAUSE:
      Game.getKeybind().pause = e.getKeyCode();

      ((ImageElement) frame.getElement("changeKeybindPause"))
      .getTextElement()
      .setText(
      new String(String.valueOf((char)Game.getKeybind().pause)));

        break;
      default:
        return;
    }
    Keybindings.saveToFile(Game.getKeybind(), KEYBINDINGS_PATH);
    Action = keyAction.OFF;
  }


  public void updatesKeybinds(){

    ((ImageElement) frame.getElement("changeKeybindRotateLeft"))
      .getTextElement()
      .setText(
      new String(String.valueOf((char)Game.getKeybind().rotatesLeft)));

      ((ImageElement) frame.getElement("changeKeybindRotateRight"))
      .getTextElement()
      .setText(
      new String(String.valueOf((char)Game.getKeybind().rotatesRight)));

      ((ImageElement) frame.getElement("changeKeybindMoveRight"))
      .getTextElement()
      .setText(
      new String(String.valueOf((char)Game.getKeybind().movesRight)));

      ((ImageElement) frame.getElement("changeKeybindMoveLeft"))
      .getTextElement()
      .setText(
      new String(String.valueOf((char)Game.getKeybind().movesLeft)));

      ((ImageElement) frame.getElement("changeKeybindSoftDrop"))
      .getTextElement()
      .setText(
      new String(String.valueOf((char)Game.getKeybind().softDrop)));

      ((ImageElement) frame.getElement("changeKeybindHardDrop"))
      .getTextElement()
      .setText(
      new String(String.valueOf((char)Game.getKeybind().hardDrop)));

      ((ImageElement) frame.getElement("changeKeybindHold"))
      .getTextElement()
      .setText(
      new String(String.valueOf((char)Game.getKeybind().hold)));

      ((ImageElement) frame.getElement("changeKeybindPause"))
      .getTextElement()
      .setText(
      new String(String.valueOf((char)Game.getKeybind().pause)));
  }

}
