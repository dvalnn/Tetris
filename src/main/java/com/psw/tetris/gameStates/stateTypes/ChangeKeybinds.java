package com.psw.tetris.gameStates.stateTypes;

import java.awt.Graphics;
import java.awt.Point;
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
import com.psw.tetris.main.Game;
import com.psw.tetris.ui.Button;
import com.psw.tetris.ui.ButtonAction;
import com.psw.tetris.ui.SwitchGameStateAction;
import com.psw.tetris.utils.LoadSave;

public class ChangeKeybinds extends GameState {

    // background
    // botão de return
    // botões de keybinds
    // logica de trocar keybinds // doneee
    // logica de resetar para defaults keybinds // doneee
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
    private final Button<keyAction, Void> buttonRotateLeft;
    private final Button<keyAction, Void> buttonRotateRight;
    private final Button<keyAction, Void> buttonMoveRight;
    private final Button<keyAction, Void> buttonMoveLeft;
    private final Button<keyAction, Void> buttonSoftDrop;
    private final Button<keyAction, Void> buttonHardDrop;
    private final Button<keyAction, Void> buttonHold;
    private final Button<keyAction, Void> buttonPause;
    private final Button<keyAction, Void> buttonReset;
    private final Button<GameStatesEnum, Void> buttonReturn;

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

        buttonRotateLeft = new Button<keyAction, Void>(
                FIRST_BUTTON_X,
                FIRST_BUTTON_Y,
                rotateLeftButtonImage,
                SCALE,
                changeKeyAction);

        final int secondButtonY = (int) (FIRST_BUTTON_Y + buttonRotateLeft.getBounds().getHeight() + BUTTON_SPACING);
        buttonRotateRight = new Button<keyAction, Void>(
                FIRST_BUTTON_X,
                secondButtonY,
                rotateRightButtonImage,
                SCALE,
                changeKeyAction);

        final int thirdButtonY = (int) (secondButtonY + buttonRotateRight.getBounds().getHeight() + BUTTON_SPACING);
        buttonMoveRight = new Button<keyAction, Void>(
                FIRST_BUTTON_X,
                thirdButtonY,
                moveRightButtonImage,
                SCALE,
                changeKeyAction);
        final int fourthButtonY = (int) (thirdButtonY + buttonMoveRight.getBounds().getHeight() + BUTTON_SPACING);
        buttonMoveLeft = new Button<keyAction, Void>(
                FIRST_BUTTON_X,
                fourthButtonY,
                moveLeftButtonImage,
                SCALE,
                changeKeyAction);
        final int fifthButtonY = (int) (fourthButtonY + buttonMoveLeft.getBounds().getHeight() + BUTTON_SPACING);
        buttonSoftDrop = new Button<keyAction, Void>(
                FIRST_BUTTON_X,
                fifthButtonY,
                softDropButtonImage,
                SCALE,
                changeKeyAction);
        // final int sixthButtonY = (int) (fifthButtonY +
        // buttonSoftDrop.getBounds().getHeight() + BUTTON_SPACING);

        buttonHardDrop = new Button<keyAction, Void>(
                FIFTH_BUTTON_X,
                FIRST_BUTTON_Y,
                hardDropButtonImage,
                SCALE,
                changeKeyAction);
        // final int seventhButtonY = (int) (sixthButtonY +
        // buttonHardDrop.getBounds().getHeight() + BUTTON_SPACING);
        buttonHold = new Button<keyAction, Void>(
                FIFTH_BUTTON_X,
                secondButtonY,
                holdButtonImage,
                SCALE,
                changeKeyAction);
        // final int eighthButtonY = (int) (seventhButtonY +
        // buttonHold.getBounds().getHeight() + BUTTON_SPACING);
        buttonPause = new Button<keyAction, Void>(
                FIFTH_BUTTON_X,
                thirdButtonY,
                pauseButtonImage,
                SCALE,
                changeKeyAction);
        // final int ninthButtonY = (int) (eighthButtonY +
        // buttonPause.getBounds().getHeight() + BUTTON_SPACING);
        buttonReset = new Button<keyAction, Void>(
                FIFTH_BUTTON_X,
                fourthButtonY,
                resetButtonImage,
                SCALE,
                changeKeyAction);
        // final int tenthButtonY = (int) (ninthButtonY +
        // buttonReset.getBounds().getHeight() + BUTTON_SPACING);
        buttonReturn = new Button<GameStatesEnum, Void>(
                FIFTH_BUTTON_X,
                fifthButtonY,
                returnButtonImage,
                SCALE,
                switchGameStateAction);
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
        if (buttonRotateLeft.getBounds().contains(e.getPoint())) {
            buttonRotateLeft.execAction(keyAction.ROTATE_LEFT);

        } else if (buttonRotateRight.getBounds().contains(e.getPoint())) {
            buttonRotateRight.execAction(keyAction.ROTATE_RIGHT);

        } else if (buttonMoveRight.getBounds().contains(e.getPoint())) {
            buttonMoveRight.execAction(keyAction.MOVE_RIGHT);

        } else if (buttonMoveLeft.getBounds().contains(e.getPoint())) {
            buttonMoveLeft.execAction(keyAction.MOVE_LEFT);

        } else if (buttonSoftDrop.getBounds().contains(e.getPoint())) {
            buttonReset.execAction(keyAction.SOFT_DROP);

        } else if (buttonHardDrop.getBounds().contains(e.getPoint())) {
            buttonReset.execAction(keyAction.HARD_DROP);

        } else if (buttonHold.getBounds().contains(e.getPoint())) {
            buttonReset.execAction(keyAction.HOLD);

        } else if (buttonPause.getBounds().contains(e.getPoint())) {
            buttonReset.execAction(keyAction.PAUSE);

        } else if (buttonReset.getBounds().contains(e.getPoint())) {
            buttonReset.execAction(keyAction.RESET);

        } else if (buttonReturn.getBounds().contains(e.getPoint())) {
            buttonReturn.execAction(GameStatesEnum.MAIN_MENU);
        }
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        switch (Action) {
            case keyAction.ROTATE_LEFT:
                keybind.rotatesLeft = e.getKeyCode();
                break;
            case keyAction.ROTATE_RIGHT:
                keybind.rotatesRight = e.getKeyCode();
                break;
            case keyAction.MOVE_LEFT:
                keybind.movesLeft = e.getKeyCode();
                break;
            case keyAction.MOVE_RIGHT:
                keybind.movesRight = e.getKeyCode();
                break;
            case keyAction.SOFT_DROP:
                keybind.softDrop = e.getKeyCode();
                break;
            case keyAction.HARD_DROP:
                keybind.hardDrop = e.getKeyCode();
                break;
            case keyAction.HOLD:
                keybind.hold = e.getKeyCode();
                break;
            case keyAction.PAUSE:
                keybind.pause = e.getKeyCode();
                break;
            case keyAction.RESET:
                keybind = new Keybindings();
                break;
            default:
                return;
        }
        Keybindings.saveToFile(keybind, KEYBINDINGS_PATH);
    }

}
