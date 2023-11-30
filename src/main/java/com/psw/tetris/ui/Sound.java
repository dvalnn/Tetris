package com.psw.tetris.ui;

import static com.psw.tetris.utils.Constants.RESOURCES_PATH;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.gameStates.GameStateHandler;

// this class implemnts the sound effects for the game

public class Sound implements Runnable {

    static Clip gameMusic;
    static Clip menuMusic;
    
    static Clip clipEffect;
    static FloatControl gainControl;
    static float volumeMusic = 0.0f;
    static float volumeEffect = 0.0f;
    static boolean mute = false;

    // this method plays the sound effect for when a line is cleared

    @Override
    public void run() {

        menuMusic = setFileMusic(RESOURCES_PATH + "/sounds/tetrisTheme.wav");
        playMusic(menuMusic);
        setMusicVolume(-10.0f);
        menuMusic.setFramePosition(0);

        gameMusic = setFileMusic(RESOURCES_PATH + "/sounds/tetrisThemeOld.wav");
        playMusic(gameMusic);
        setMusicVolume(-10.0f);
        gameMusic.setFramePosition(0);
        

        while (true) {

            if (GameStateHandler.getActiveStateID() == GameStatesEnum.PLAYING) {
                menuMusic.setFramePosition(0);
                menuMusic.stop();
                gameMusic.start();
            }
            else {
                gameMusic.setFramePosition(0);
                gameMusic.stop();
                menuMusic.start();
            }
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
            //gainControl = (FloatControl) gameMusic.getControl(FloatControl.Type.MASTER_GAIN);

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
