package com.psw.tetris.gameStates.stateTypes;

import static com.psw.tetris.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_WIDTH;
import static com.psw.tetris.utils.Constants.UI.Buttons.CHANGE_GAME_INPUTS;
import static com.psw.tetris.utils.Constants.UI.Buttons.MINUS_V1;
import static com.psw.tetris.utils.Constants.UI.Buttons.MINUS_V2;
import static com.psw.tetris.utils.Constants.UI.Buttons.PLUS_V1;
import static com.psw.tetris.utils.Constants.UI.Buttons.PLUS_V2;
import static com.psw.tetris.utils.Constants.UI.Buttons.RETURN_BUTTON;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;

import com.psw.tetris.gameStates.GameState;
import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.ui.Button;
import com.psw.tetris.ui.ButtonAction;
import com.psw.tetris.ui.SwitchGameStateAction;
import com.psw.tetris.utils.LoadSave;


//TODO: Add volume and lower volume effect functions

public class Settings extends GameState {

    private final Button<Void, Void> buttonPlusVolume;
    private final Button<Void, Void> buttonMinusVolume;
    private final Button<Void, Void> buttonPlusEffects;
    private final Button<Void, Void> buttonMinusEffects;
    private final Button<GameStatesEnum, Void> buttonChangeGameInputs;
    private final Button<GameStatesEnum, Void> buttonReturn;

    private final BufferedImage plusVolumeButtonImage = LoadSave.loadImage(PLUS_V1);
    private final BufferedImage minusVolumeButtonImage = LoadSave.loadImage(MINUS_V1);
    private final BufferedImage plusEffectsButtonImage = LoadSave.loadImage(PLUS_V2);
    private final BufferedImage minusEffectsButtonImage = LoadSave.loadImage(MINUS_V2);
    private final BufferedImage changeGameInputsButtonImage = LoadSave.loadImage(CHANGE_GAME_INPUTS);
    private final BufferedImage returnButtonImage = LoadSave.loadImage(RETURN_BUTTON);

    private final BufferedImage settingsBackground;

    private final SwitchGameStateAction switchGameStateAction = new SwitchGameStateAction();

    private final ButtonAction<Void, Void> volumeManager = (Void) -> {
        return null;
    };
    private final ButtonAction<Void, Void> effectManager = (Void) -> {
        return null;
    };


    private final double SMALL_BUTTON_SCALE = 0.25;
    private final double CGI_BUTTON_SCALE = 0.29;  
    private final double RETURN_BUTTON_SCALE = 0.35;

    private final int FIRST_SMALL_BUTTON_X = 420;
    private final int SECOND_SMALL_BUTTON_X = 500;   
    private final int FIRST_BUTTON_Y = 202;

    private final int SMALLER_BUTTON_SPACING = 35;

    private final int cgiButtonX = 40;
    private final int cgiButtonY = 400;

    private final int returnButtonX = 40;
    private final int returnButtonY = 620;

    public Settings() {
        super(GameStatesEnum.SETTINGS);

        settingsBackground = LoadSave.loadBackground("settings.png");

        buttonPlusVolume = new Button<Void, Void>(
                FIRST_SMALL_BUTTON_X,
                FIRST_BUTTON_Y,
                plusVolumeButtonImage,
                SMALL_BUTTON_SCALE,
                volumeManager);

        buttonMinusVolume = new Button<Void, Void>(
                SECOND_SMALL_BUTTON_X,
                FIRST_BUTTON_Y,
                minusVolumeButtonImage,
                SMALL_BUTTON_SCALE,
                volumeManager);

        final int secondButtonY = (int) (FIRST_BUTTON_Y + buttonPlusVolume.getBounds().getHeight() + SMALLER_BUTTON_SPACING);
        
        buttonPlusEffects = new Button<Void, Void>(
                FIRST_SMALL_BUTTON_X,
                secondButtonY,
                plusEffectsButtonImage,
                SMALL_BUTTON_SCALE,
                effectManager);
        
        buttonMinusEffects = new Button<Void, Void>(
                SECOND_SMALL_BUTTON_X,
                secondButtonY,
                minusEffectsButtonImage,
                SMALL_BUTTON_SCALE,
                effectManager);


        buttonChangeGameInputs = new Button<GameStatesEnum, Void>(
                cgiButtonX,
                cgiButtonY,
                changeGameInputsButtonImage,
                CGI_BUTTON_SCALE,
                switchGameStateAction);


        buttonReturn = new Button<GameStatesEnum, Void>(
                returnButtonX,
                returnButtonY,
                returnButtonImage,
                RETURN_BUTTON_SCALE,
                switchGameStateAction);


    }
    @Override
    public void render(final Graphics g) {

        g.drawImage(settingsBackground, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        buttonPlusVolume.render(g);
        buttonMinusVolume.render(g);
        buttonPlusEffects.render(g);
        buttonMinusEffects.render(g);
        buttonChangeGameInputs.render(g);
        buttonReturn.render(g);
    }

    @Override
    public void mouseClicked(final MouseEvent e) {

        if (buttonPlusVolume.getBounds().contains(e.getPoint())) {
            buttonPlusVolume.execAction(null);
        }

        else if(buttonMinusVolume.getBounds().contains(e.getPoint())) {
            buttonMinusVolume.execAction(null);
        } 

        else if(buttonPlusEffects.getBounds().contains(e.getPoint())) {
            buttonPlusEffects.execAction(null);
        } 

        else if(buttonMinusEffects.getBounds().contains(e.getPoint())) {
            buttonMinusEffects.execAction(null);
        } 

        else if(buttonChangeGameInputs.getBounds().contains(e.getPoint())) {
            buttonChangeGameInputs.execAction(GameStatesEnum.CHANGE_KEYBINDS);
        } 

        else if(buttonReturn.getBounds().contains(e.getPoint())) {
            buttonReturn.execAction(GameStatesEnum.MAIN_MENU);
        }

    }


}
