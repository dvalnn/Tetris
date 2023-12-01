package com.psw.tetris.gameElements;

import static com.psw.tetris.utils.Constants.RESOURCES_PATH;

import java.awt.Image;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.ui.ImageElement;
import com.psw.tetris.gameStates.GameStateHandler;

// this class implemnts the sound effects for the game

public class Sound implements Runnable {

  private static Clip gameMusic;
  private static Clip menuMusic;

  private static Clip clipEffect;
  private static FloatControl gainControl;
  private static float volumeMusic = 0.0f;
  private static float volumeEffect = 0.0f;
  private static boolean mute = false;

  @Override
  public void run() {

    menuMusic = setFileMusic(RESOURCES_PATH + "/sounds/tetrisTheme.wav");
    setMusicVolume(-20.0f);

    gameMusic = setFileMusic(RESOURCES_PATH + "/sounds/tetrisThemeOld.wav");
    setMusicVolume(-20.0f);
    
    GameStatesEnum oldState = GameStateHandler.getActiveStateID();

    playMusic(menuMusic);

    clipEffect = ImageElement.getClipEffect();

    while (true) {
      GameStatesEnum newState = GameStateHandler.getActiveStateID();

      if (oldState == newState) {
        // HACK: this is a workaround to avoid a busy loop.
        // This should probably be replaced. Maybe. Someday.
        Thread.yield();
        continue;
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
      gainControl = (FloatControl) gameMusic.getControl(FloatControl.Type.MASTER_GAIN);

    } catch (Exception e) {
      System.out.println("Error with reading file for music.");
      e.printStackTrace();
    }
    return gameMusic;
  }

  public static Clip setFileEffect(String filename) {
    try {
      File file = new File(filename);
      AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
      gameMusic = AudioSystem.getClip();
      gameMusic.open(audioInputStream);
      // gainControl = (FloatControl)
      // gameMusic.getControl(FloatControl.Type.MASTER_GAIN);

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

  public static void setMusicVolume(float volumeMusic) {
    Sound.volumeMusic = volumeMusic;
    gainControl.setValue(volumeMusic);
  }

  public void setEffectVolume(float volumeEffect) {
    Sound.volumeEffect = volumeEffect;
    gainControl.setValue(volumeEffect);
  }

}
