package com.apontadores.gameElements;

import static com.apontadores.utils.Constants.RESOURCES_PATH;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import com.apontadores.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.ui.ImageElement;
import com.apontadores.gameStates.GameStateHandler;

public class Sound implements Runnable {

  private static Clip gameMusic;
  private static Clip menuMusic;
  private static Clip clipEffect;

  private static FloatControl gainControlGameMusic;
  private static FloatControl gainControlMenuMusic;
  private static FloatControl gainControlEffectMusic;

  private static int musicVolume = 50; // musicVolume is a percentage and goes from 0 to 100
  private static int effectVolume = 80; // effectMusic is a percentage and goes from 0 to 100
  private static boolean mute = false;

  @Override
  public void run() {

    menuMusic = setFileMusic(RESOURCES_PATH + "/sounds/tetrisTheme.wav");
    gameMusic = setFileMusic(RESOURCES_PATH + "/sounds/tetrisThemeOld.wav");
    //clipEffect = setFileMusic(RESOURCES_PATH +"/sounds/clickSound.wav");

    clipEffect = ImageElement.getClipEffect();

    gainControlGameMusic = (FloatControl) gameMusic.getControl(FloatControl.Type.MASTER_GAIN);
    gainControlMenuMusic = (FloatControl) menuMusic.getControl(FloatControl.Type.MASTER_GAIN);
    gainControlEffectMusic = (FloatControl) clipEffect.getControl(FloatControl.Type.MASTER_GAIN);

    setMusicVolume(musicVolume);
    setEffectVolume(effectVolume);
    
    GameStatesEnum oldState = GameStateHandler.getActiveStateID();

    playMusic(menuMusic);


    while (true) {

      GameStatesEnum newState = GameStateHandler.getActiveStateID();

      if (oldState == newState) {
        // HACK: this is a workaround to avoid a busy loop.
        // This should probably be replaced. Maybe. Someday.
        Thread.yield();
        continue;
      }

      if(musicVolume == 0 || effectVolume == 0){
        System.out.println("muted");
      }

      if (newState.equals(GameStatesEnum.PLAYING)) {
        menuMusic.stop();
        playMusic(gameMusic);

      } else if (oldState.equals(GameStatesEnum.PLAYING)) {
        gameMusic.stop();
        playMusic(menuMusic);
      }

      oldState = newState;
    }
  }

  public static Clip setFileMusic(String filename) {
    try {
      File file = new File(filename);
      AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
      gameMusic = AudioSystem.getClip();
      gameMusic.open(audioInputStream);

    } catch (Exception e) {
      System.out.println("Error with reading file for music.");
      e.printStackTrace();
    }
    return gameMusic;
  }

  public static void playMusic(Clip clipMusic) {
    if (mute) {
      return;
    }
    clipMusic.setFramePosition(0);
    clipMusic.loop(Clip.LOOP_CONTINUOUSLY);
    clipMusic.start();
  }

  public static void playEffect(Clip clipEffect) {
    if (mute) {
      return;
    }
    clipEffect.setFramePosition(0);
    clipEffect.start();
  }

  public static void setMusicVolume(int musicVolume) {
    // A relation that turns the range of -20.0f to 6.0206f into a range of 0 to 100
    // and then adds the minimum value to the result

    Sound.musicVolume = musicVolume;

    float range = 6.0f - (-20.0f);
    float gain = (range * musicVolume / 100) + (- 20.0f);

    // if the value inserted is 0, then the sound is muted
    if(musicVolume == 0){
      gain = -80.0f;
    }

    gainControlGameMusic.setValue(gain);
    gainControlMenuMusic.setValue(gain);
  }
  public static void setEffectVolume(int effectVolume) {
    // A relation that turns the range of -20.0f to 6.0206f into a range of 0 to 100
    // and then adds the minimum value to the result

    Sound.effectVolume = effectVolume;

    float range = 6.0f - (-20.0f);
    float gain = (range * musicVolume / 100) + (- 20.0f);


    // if the value inserted is 0, then the sound is muted
    if(musicVolume == 0){
      gain = -80.0f;
    }

    gainControlEffectMusic.setValue(gain);
  }
  
  public static int getMusicVolume() {
    return musicVolume;
  }
  public static int getEffectVolume() {
    return effectVolume;
  }


  // não sei o porque de não funcionar , TIP: pode ser pela ordem de invocação das funções 
  //public static Clip getClipEffect() {
  //  return clipEffect;
  //}



}
