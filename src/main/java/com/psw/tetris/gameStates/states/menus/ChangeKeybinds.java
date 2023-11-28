package com.psw.tetris.gameStates.states.menus;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import com.psw.tetris.utils.Keybindings;
import com.psw.tetris.utils.LoadSave;

import static com.psw.tetris.utils.Constants.*;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_WIDTH;
import static com.psw.tetris.utils.Constants.UI.Buttons.NEW_GAME;

import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.main.Game;
import com.psw.tetris.gameStates.GameState;

import com.psw.tetris.ui.Button;
import com.psw.tetris.ui.ButtonAction;
import com.psw.tetris.ui.SwitchStateAction;

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
    PAUSE,
    RESET;
  }

  private keyAction Action = keyAction.ROTATE_LEFT;

  private final Button rotateLeftButton;
  private final Button rotateRightButton;
  private final Button moveRightButton;
  private final Button moveLeftButton;
  private final Button softDropButton;
  private final Button hardDropButton;
  private final Button holdButton;
  private final Button pauseButton;
  private final Button resetButton;
  private final Button resetKeybindsButton;
  private final Button returnButton;

  private final BufferedImage rotateLeftButtonImage = LoadSave.loadImage(NEW_GAME);
  private final BufferedImage rotateRightButtonImage = LoadSave.loadImage(NEW_GAME);
  private final BufferedImage moveRightButtonImage = LoadSave.loadImage(NEW_GAME);
  private final BufferedImage moveLeftButtonImage = LoadSave.loadImage(NEW_GAME);
  private final BufferedImage softDropButtonImage = LoadSave.loadImage(NEW_GAME);
  private final BufferedImage hardDropButtonImage = LoadSave.loadImage(NEW_GAME);
  private final BufferedImage holdButtonImage = LoadSave.loadImage(NEW_GAME);
  private final BufferedImage pauseButtonImage = LoadSave.loadImage(NEW_GAME);
  private final BufferedImage resetButtonImage = LoadSave.loadImage(NEW_GAME);
  private final BufferedImage resetKeybidingsButtonImage = LoadSave.loadImage(NEW_GAME);

  private final BufferedImage returnButtonImage = LoadSave.loadImage(NEW_GAME);

  private final BufferedImage keyBackground;

  private final SwitchStateAction switchGameStateAction = new SwitchStateAction();
  private final ButtonAction<keyAction, Void> changeKeyAction = (keyAction a) -> {
    Action = a;
    return null;
  };
  private final ButtonAction<Void, Void> resetKeyAction = (Void) -> {
    Game.resetKeybind();
    Keybindings.saveToFile(Game.getKeybind(), KEYBINDINGS_PATH);
    return null;
  };

  private final double SCALE = 0.02;
  private final int FIRST_BUTTON_X = 100;
  private final int FIRST_BUTTON_Y = 200;
  private final int FIFTH_BUTTON_X = 700;
  private final int BUTTON_SPACING = 25;

  public ChangeKeybinds() {
    super(GameStatesEnum.CHANGE_KEYBINDS);

    keyBackground = LoadSave.loadBackground("aboutUs.png");

    rotateLeftButton = new Button(
        FIRST_BUTTON_X,
        FIRST_BUTTON_Y,
        rotateLeftButtonImage,
        SCALE);

    final int secondButtonY = (int) (FIRST_BUTTON_Y + rotateLeftButton.getBounds().getHeight() + BUTTON_SPACING);
    rotateRightButton = new Button(
        FIRST_BUTTON_X,
        secondButtonY,
        rotateRightButtonImage,
        SCALE);

    final int thirdButtonY = (int) (secondButtonY + rotateRightButton.getBounds().getHeight() + BUTTON_SPACING);
    moveRightButton = new Button(
        FIRST_BUTTON_X,
        thirdButtonY,
        moveRightButtonImage,
        SCALE);
    final int fourthButtonY = (int) (thirdButtonY + moveRightButton.getBounds().getHeight() + BUTTON_SPACING);
    moveLeftButton = new Button(
        FIRST_BUTTON_X,
        fourthButtonY,
        moveLeftButtonImage,
        SCALE);
    final int fifthButtonY = (int) (fourthButtonY + moveLeftButton.getBounds().getHeight() + BUTTON_SPACING);
    softDropButton = new Button(
        FIRST_BUTTON_X,
        fifthButtonY,
        softDropButtonImage,
        SCALE);

    hardDropButton = new Button(
        FIFTH_BUTTON_X,
        FIRST_BUTTON_Y,
        hardDropButtonImage,
        SCALE);

    holdButton = new Button(
        FIFTH_BUTTON_X,
        secondButtonY,
        holdButtonImage,
        SCALE);

    pauseButton = new Button(
        FIFTH_BUTTON_X,
        thirdButtonY,
        pauseButtonImage,
        SCALE);

    resetButton = new Button(
        FIFTH_BUTTON_X,
        fourthButtonY,
        resetButtonImage,
        SCALE);
    
    resetKeybindsButton = new Button(
        FIFTH_BUTTON_X+100,
        fifthButtonY+200,
        resetKeybidingsButtonImage,
        SCALE);

    returnButton = new Button(
        FIFTH_BUTTON_X,
        fifthButtonY,
        returnButtonImage,
        SCALE);
  }

  @Override
  public void render(final Graphics g) {

    g.drawImage(keyBackground, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
    rotateLeftButton.render(g);
    rotateRightButton.render(g);
    moveRightButton.render(g);
    moveLeftButton.render(g);
    softDropButton.render(g);
    hardDropButton.render(g);
    holdButton.render(g);
    pauseButton.render(g);
    resetButton.render(g);
    resetKeybindsButton.render(g);
    returnButton.render(g);
  }

  // actions ainda a definir
  @Override
  public void mouseClicked(final MouseEvent e) {

    rotateLeftButton.execIfClicked(
        e.getPoint(),
        changeKeyAction,
        keyAction.ROTATE_LEFT);

    rotateRightButton.execIfClicked(
        e.getPoint(),
        changeKeyAction,
        keyAction.ROTATE_RIGHT);

    moveRightButton.execIfClicked(
        e.getPoint(),
        changeKeyAction,
        keyAction.MOVE_RIGHT);

    moveLeftButton.execIfClicked(
        e.getPoint(),
        changeKeyAction,
        keyAction.MOVE_LEFT);

    softDropButton.execIfClicked(
        e.getPoint(),
        changeKeyAction,
        keyAction.SOFT_DROP);

    hardDropButton.execIfClicked(
        e.getPoint(),
        changeKeyAction,
        keyAction.HARD_DROP);

    holdButton.execIfClicked(
        e.getPoint(),
        changeKeyAction,
        keyAction.HOLD);

    pauseButton.execIfClicked(
        e.getPoint(),
        changeKeyAction,
        keyAction.PAUSE);

    resetButton.execIfClicked(
        e.getPoint(),
        changeKeyAction,
        keyAction.RESET);

    resetKeybindsButton.execIfClicked(
        e.getPoint(),
        resetKeyAction,
        null);

    returnButton.execIfClicked(
        e.getPoint(),
        switchGameStateAction,
        GameStatesEnum.SETTINGS);
  }

  @Override
  public void keyPressed(final KeyEvent e) {
    switch (Action) {
      case ROTATE_LEFT:
      Game.getKeybind().rotatesLeft = e.getKeyCode();
        break;
      case ROTATE_RIGHT:
      Game.getKeybind().rotatesRight = e.getKeyCode();
        break;
      case MOVE_LEFT:
      Game.getKeybind().movesLeft = e.getKeyCode();
        break;
      case MOVE_RIGHT:
      Game.getKeybind().movesRight = e.getKeyCode();
        break;
      case SOFT_DROP:
      Game.getKeybind().softDrop = e.getKeyCode();
        break;
      case HARD_DROP:
      Game.getKeybind().hardDrop = e.getKeyCode();
        break;
      case HOLD:
      Game.getKeybind().hold = e.getKeyCode();
        break;
      case PAUSE:
      Game.getKeybind().pause = e.getKeyCode();
        break;
      case RESET:
      //Game.resetKeybind();
      Game.getKeybind().restart = e.getKeyCode();
        break;
      default:
        return;
    }
    Keybindings.saveToFile(Game.getKeybind(), KEYBINDINGS_PATH);
    Action = keyAction.OFF;
  }

}
