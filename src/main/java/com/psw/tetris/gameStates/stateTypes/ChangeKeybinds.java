package com.psw.tetris.gameStates.stateTypes;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import com.psw.tetris.utils.Keybindings;

import static com.psw.tetris.utils.Constants.*;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_WIDTH;
import static com.psw.tetris.utils.Constants.UI.Buttons.NEW_GAME;

import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.gameStates.GameState;
import com.psw.tetris.ui.Button;
import com.psw.tetris.ui.ButtonAction;
import com.psw.tetris.ui.SwitchGameStateAction;
import com.psw.tetris.utils.LoadSave;

public class ChangeKeybinds extends GameState {

    private enum keyAction {
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

    private final Button buttonRotateLeft;
    private final Button buttonRotateRight;
    private final Button buttonMoveRight;
    private final Button buttonMoveLeft;
    private final Button buttonSoftDrop;
    private final Button buttonHardDrop;
    private final Button buttonHold;
    private final Button buttonPause;
    private final Button buttonReset;
    private final Button buttonReturn;

    private final BufferedImage rotateLeftButtonImage = LoadSave.loadImage(NEW_GAME);
    private final BufferedImage rotateRightButtonImage = LoadSave.loadImage(NEW_GAME);
    private final BufferedImage moveRightButtonImage = LoadSave.loadImage(NEW_GAME);
    private final BufferedImage moveLeftButtonImage = LoadSave.loadImage(NEW_GAME);
    private final BufferedImage softDropButtonImage = LoadSave.loadImage(NEW_GAME);
    private final BufferedImage hardDropButtonImage = LoadSave.loadImage(NEW_GAME);
    private final BufferedImage holdButtonImage = LoadSave.loadImage(NEW_GAME);
    private final BufferedImage pauseButtonImage = LoadSave.loadImage(NEW_GAME);
    private final BufferedImage resetButtonImage = LoadSave.loadImage(NEW_GAME);
    private final BufferedImage returnButtonImage = LoadSave.loadImage(NEW_GAME);

    private final BufferedImage keyBackground;

    private Keybindings keybind = new Keybindings();

    private final SwitchGameStateAction switchGameStateAction = new SwitchGameStateAction();
    private final ButtonAction<keyAction, Void> changeKeyAction = (keyAction a) -> {
        Action = a;
        return null;
    };

    private final double SCALE = 0.25;
    private final int FIRST_BUTTON_X = 100;
    private final int FIRST_BUTTON_Y = 200;
    private final int FIFTH_BUTTON_X = 700;
    private final int BUTTON_SPACING = 25;

    public ChangeKeybinds() {
        super(GameStatesEnum.CHANGE_KEYBINDS);

        keyBackground = LoadSave.loadBackground("aboutUs.png");

        buttonRotateLeft = new Button(
                FIRST_BUTTON_X,
                FIRST_BUTTON_Y,
                rotateLeftButtonImage,
                SCALE);

        final int secondButtonY = (int) (FIRST_BUTTON_Y + buttonRotateLeft.getBounds().getHeight() + BUTTON_SPACING);
        buttonRotateRight = new Button(
                FIRST_BUTTON_X,
                secondButtonY,
                rotateRightButtonImage,
                SCALE);

        final int thirdButtonY = (int) (secondButtonY + buttonRotateRight.getBounds().getHeight() + BUTTON_SPACING);
        buttonMoveRight = new Button(
                FIRST_BUTTON_X,
                thirdButtonY,
                moveRightButtonImage,
                SCALE);
        final int fourthButtonY = (int) (thirdButtonY + buttonMoveRight.getBounds().getHeight() + BUTTON_SPACING);
        buttonMoveLeft = new Button(
                FIRST_BUTTON_X,
                fourthButtonY,
                moveLeftButtonImage,
                SCALE);
        final int fifthButtonY = (int) (fourthButtonY + buttonMoveLeft.getBounds().getHeight() + BUTTON_SPACING);
        buttonSoftDrop = new Button(
                FIRST_BUTTON_X,
                fifthButtonY,
                softDropButtonImage,
                SCALE);

        buttonHardDrop = new Button(
                FIFTH_BUTTON_X,
                FIRST_BUTTON_Y,
                hardDropButtonImage,
                SCALE);

        buttonHold = new Button(
                FIFTH_BUTTON_X,
                secondButtonY,
                holdButtonImage,
                SCALE);

        buttonPause = new Button(
                FIFTH_BUTTON_X,
                thirdButtonY,
                pauseButtonImage,
                SCALE);

        buttonReset = new Button(
                FIFTH_BUTTON_X,
                fourthButtonY,
                resetButtonImage,
                SCALE);

        buttonReturn = new Button(
                FIFTH_BUTTON_X,
                fifthButtonY,
                returnButtonImage,
                SCALE);
    }

    @Override
    public void render(final Graphics g) {

        g.drawImage(keyBackground, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        buttonRotateLeft.render(g);
        buttonRotateRight.render(g);
        buttonMoveRight.render(g);
        buttonMoveLeft.render(g);
        buttonSoftDrop.render(g);
        buttonHardDrop.render(g);
        buttonHold.render(g);
        buttonPause.render(g);
        buttonReset.render(g);
        buttonReturn.render(g);
    }

    // actions ainda a definir
    @Override
    public void mouseClicked(final MouseEvent e) {

        buttonRotateLeft.execIfClicked(
                e.getPoint(),
                changeKeyAction,
                keyAction.ROTATE_LEFT);
        
        buttonRotateRight.execIfClicked(
                e.getPoint(),
                changeKeyAction,
                keyAction.ROTATE_RIGHT);
        
        buttonMoveRight.execIfClicked(
                e.getPoint(),
                changeKeyAction,
                keyAction.MOVE_RIGHT); 
        
        buttonMoveLeft.execIfClicked(
                e.getPoint(),
                changeKeyAction,
                keyAction.MOVE_LEFT);
        
        buttonSoftDrop.execIfClicked(
                e.getPoint(),
                changeKeyAction,
                keyAction.SOFT_DROP);
        
        buttonHardDrop.execIfClicked(
                e.getPoint(),
                changeKeyAction,
                keyAction.HARD_DROP);
        
        buttonHold.execIfClicked(
                e.getPoint(),
                changeKeyAction,
                keyAction.HOLD);
        
        buttonPause.execIfClicked(
                e.getPoint(),
                changeKeyAction,
                keyAction.PAUSE);
        
        buttonReset.execIfClicked(
                e.getPoint(),
                changeKeyAction,
                keyAction.RESET);
        
        buttonReturn.execIfClicked(
                e.getPoint(),
                switchGameStateAction,
                GameStatesEnum.SETTINGS); 
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        switch (Action) {
            case ROTATE_LEFT:
                keybind.rotatesLeft = e.getKeyCode();
                break;
            case ROTATE_RIGHT:
                keybind.rotatesRight = e.getKeyCode();
                break;
            case MOVE_LEFT:
                keybind.movesLeft = e.getKeyCode();
                break;
            case MOVE_RIGHT:
                keybind.movesRight = e.getKeyCode();
                break;
            case SOFT_DROP:
                keybind.softDrop = e.getKeyCode();
                break;
            case HARD_DROP:
                keybind.hardDrop = e.getKeyCode();
                break;
            case HOLD:
                keybind.hold = e.getKeyCode();
                break;
            case PAUSE:
                keybind.pause = e.getKeyCode();
                break;
            case RESET:
                keybind = new Keybindings();
                break;
            default:
                return;
        }
        Keybindings.saveToFile(keybind, KEYBINDINGS_PATH);
    }

}
