package com.apontadores.gameStates.states.menus;

import static com.apontadores.utils.Constants.FRAMES_PATH;

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

  private static boolean muteEffect = false;
  private static boolean muteMusic = false;

  private final ButtonAction<Integer, Void> volumeManager = (Integer volume) -> {
    Sound.setMusicVolume(volume);
    return null;
  };
  private final ButtonAction<Integer, Void> effectManager = (Integer volume) -> {
    Sound.setEffectVolume(volume);
    return null;
  };
  private final ButtonAction<Void, Void> volumeMute = (Void) -> {

    muteMusic = !muteMusic;
    Sound.setMuteMusic(muteMusic);
    Sound.setMusicVolume(0); 
    return null;
  };
  private final ButtonAction<Void, Void> effectMute = (Void) -> {

    muteEffect = !muteEffect;
    Sound.setMuteEffect(muteEffect);
    Sound.setEffectVolume(0);
    return null;
  };

  public Settings() {
    super(GameStatesEnum.SETTINGS);
    frame = Frame.loadFromJson(FRAMES_PATH + "settings.json");
  }

  @Override
  public void render(final Graphics g) {
    frame.render(g);
  }

  @Override
  public void update() {

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
  public void mouseClicked(final MouseEvent e) {

    ((ImageElement) frame.getElement("effectsVolumePlusButton"))
        .execIfClicked(e.getX(), e.getY(), effectManager, 1);

    ((ImageElement) frame.getElement("effectsVolumeMinusButton"))
        .execIfClicked(e.getX(), e.getY(), effectManager, -1);

    ((ImageElement) frame.getElement("musicVolumePlusButton"))
        .execIfClicked(e.getX(), e.getY(), volumeManager, 1);

    ((ImageElement) frame.getElement("musicVolumeMinusButton"))
        .execIfClicked(e.getX(), e.getY(), volumeManager, -1);

    ((ImageElement) frame.getElement("musicMute"))
        .execIfClicked(e.getX(), e.getY(), volumeMute, null);

    ((ImageElement) frame.getElement("effectsMute"))
        .execIfClicked(e.getX(), e.getY(), effectMute, null);

    ((ImageElement) frame.getElement("returnToMainMenu"))
        .execIfClicked(e.getX(), e.getY(), switchState, GameStatesEnum.MAIN_MENU);

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
