package com.apontadores.gameStates.states.menus;

import static com.apontadores.utils.Constants.RESOURCES_PATH;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.apontadores.gameElements.Sound;
import com.apontadores.gameStates.GameState;
import com.apontadores.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.ui.ImageElement;
import com.apontadores.ui.SwitchStateAction;
import com.apontadores.ui.ButtonAction;
import com.apontadores.ui.Frame;

public class Settings extends GameState {

  private final Frame frame;

  private final SwitchStateAction switchState = new SwitchStateAction();

  private final ButtonAction<String, Void> volumeManager = (String volume) -> {
    int volumeInt = Integer.parseInt(volume);
    Sound.setMusicVolume(volumeInt);
    return null;
  };
  private final ButtonAction<String, Void> effectManager = (String volume) -> {
    int volumeInt = Integer.parseInt(volume);
    Sound.setEffectVolume(volumeInt);
    return null;
  };

  public Settings() {
    super(GameStatesEnum.SETTINGS);
    frame = Frame.loadFromJson(RESOURCES_PATH + "/frames/settings.json");

    ((ImageElement) frame.getElement("musicVolume"))
        .getTextElement()
        .setText(
            new String(String.valueOf((Sound.getMusicVolume()))));

    ((ImageElement) frame.getElement("effectsVolume"))
        .getTextElement()
        .setText(
            new String(String.valueOf((Sound.getEffectVolume()))));


  }

  @Override
  public void render(final Graphics g) {
    frame.render(g);
  }

  @Override
  public void mouseClicked(final MouseEvent e) {

    ImageElement volumeInputField = (ImageElement) frame.getElement("musicVolume");
    volumeInputField.getTextElement().removeFocus();

    ImageElement effectInputField = (ImageElement) frame.getElement("effectsVolume");
    effectInputField.getTextElement().removeFocus();


    volumeManager.exec(volumeInputField.getTextElement().getText());

    effectManager.exec(effectInputField.getTextElement().getText());
 
    volumeInputField.execIfClicked(
        e.getX(),
        e.getY(),
        (Void) -> {
          volumeInputField.getTextElement().giveFocus();
          return null;
        },
        null);

    effectInputField.execIfClicked(
    e.getX(),
    e.getY(),
    (Void) -> {
    effectInputField.getTextElement().giveFocus();
    return null;
    },
    null);

    ((ImageElement) frame.getElement("returnToMainMenu"))
        .execIfClicked(e.getX(), e.getY(), switchState, GameStatesEnum.MAIN_MENU);

    ((ImageElement) frame.getElement("changeKeybinds"))
        .execIfClicked(e.getX(), e.getY(), switchState, GameStatesEnum.CHANGE_KEYBINDS);

    ((ImageElement) frame.getElement("changeUsername"))
        .execIfClicked(e.getX(), e.getY(), switchState, GameStatesEnum.USERNAME);
  }

  @Override
  public void keyPressed(final KeyEvent e) {
    ((ImageElement) frame.getElement("musicVolume"))
        .getTextElement().keyboardInput(e);

    ((ImageElement) frame.getElement("effectsVolume"))
    .getTextElement().keyboardInput(e);
  }

}
