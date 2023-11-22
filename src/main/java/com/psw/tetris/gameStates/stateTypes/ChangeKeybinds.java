package com.psw.tetris.gameStates.stateTypes;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

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

    private final Button<Integer, Void> buttonRotateLeft;
    private final Button<Integer, Void> buttonRotateRight;
    private final Button<Integer, Void> buttonMoveRight;
    private final Button<Integer, Void> buttonMoveLeft;
    private final Button<Integer, Void> buttonModeDown;
    private final Button<Integer, Void> buttonHardDrop;
    private final Button<Integer, Void> buttonHold;
    private final Button<Integer, Void> buttonPause;
    private final Button<Integer, Void> buttonReset;
    private final Button<GameStatesEnum, Void> buttonReturn;

    private final BufferedImage rotateLeftButtonImage = LoadSave.loadImage("pressEnter.png");
    private final BufferedImage rotateRightButtonImage = LoadSave.loadImage("pressEnter.png");
    private final BufferedImage moveRightButtonImage = LoadSave.loadImage("pressEnter.png");
    private final BufferedImage moveLeftButtonImage = LoadSave.loadImage("pressEnter.png");
    private final BufferedImage modeDownButtonImage = LoadSave.loadImage("pressEnter.png");
    private final BufferedImage hardDropButtonImage = LoadSave.loadImage("pressEnter.png");
    private final BufferedImage holdButtonImage = LoadSave.loadImage("pressEnter.png");
    private final BufferedImage pauseButtonImage = LoadSave.loadImage("pressEnter.png");
    private final BufferedImage resetButtonImage = LoadSave.loadImage("pressEnter.png");
    private final BufferedImage returnButtonImage = LoadSave.loadImage("returnButton.png");

    private final SwitchGameStateAction switchGameStateAction = new SwitchGameStateAction();
    private final ButtonAction<Integer, Void> changeKeyAction = (Void) -> {
        ChangeKeybinds(e.getKeyCode());
        return null;
    };

    private final double SCALE = 0.25/* */;
    private final int FIRST_BUTTON_X = 100/* */;
    private final int FIRST_BUTTON_Y = 300/* */;
    private final int BUTTON_SPACING = 25/* */;

    public ChangeKeybinds() {
        super(GameStatesEnum.CHANGE_KEYBINDS);

        keyBackground = LoadSave.loadBackground("aboutUs.png");

        buttonRotateLeft = new Button<Integer, Void>(
                FIRST_BUTTON_X,
                FIRST_BUTTON_Y,
                rotateLeftButtonImage,
                SCALE,
                changeKeyAction);

        final int secondButtonY = (int) (FIRST_BUTTON_Y + buttonRotateLeft.getBounds().getHeight() + BUTTON_SPACING);
        buttonRotateRight = new Button<Integer, Void>(
                FIRST_BUTTON_X,
                secondButtonY,
                rotateRightButtonImage,
                SCALE,
                changeKeyAction);

        final int thirdButtonY = (int) (secondButtonY + buttonRotateRight.getBounds().getHeight() + BUTTON_SPACING);
        buttonMoveRight = new Button<Integer, Void>(
                FIRST_BUTTON_X,
                thirdButtonY,
                moveRightButtonImage,
                SCALE,
                changeKeyAction);
        final int fourthButtonY = (int) (thirdButtonY + buttonMoveRight.getBounds().getHeight() + BUTTON_SPACING);
        buttonMoveLeft = new Button<Integer, Void>(
                FIRST_BUTTON_X,
                fourthButtonY,
                moveLeftButtonImage,
                SCALE,
                changeKeyAction);
        final int fifthButtonY = (int) (fourthButtonY + buttonMoveLeft.getBounds().getHeight() + BUTTON_SPACING);
        buttonModeDown = new Button<Integer, Void>(
                FIRST_BUTTON_X,
                fifthButtonY,
                modeDownButtonImage,
                SCALE,
                changeKeyAction);
        final int sixthButtonY = (int) (fifthButtonY + buttonModeDown.getBounds().getHeight() + BUTTON_SPACING);
        buttonHardDrop = new Button<Integer, Void>(
                FIRST_BUTTON_X,
                sixthButtonY,
                hardDropButtonImage,
                SCALE,
                changeKeyAction);
        final int seventhButtonY = (int) (sixthButtonY + buttonHardDrop.getBounds().getHeight() + BUTTON_SPACING);
        buttonHold = new Button<Integer, Void>(
                FIRST_BUTTON_X,
                seventhButtonY,
                holdButtonImage,
                SCALE,
                changeKeyAction);
        final int eighthButtonY = (int) (seventhButtonY + buttonHold.getBounds().getHeight() + BUTTON_SPACING);
        buttonPause = new Button<Integer, Void>(
                FIRST_BUTTON_X,
                eighthButtonY,
                pauseButtonImage,
                SCALE,
                changeKeyAction);
        final int ninthButtonY = (int) (eighthButtonY + buttonPause.getBounds().getHeight() + BUTTON_SPACING);
        buttonReset = new Button<Integer, Void>(
                FIRST_BUTTON_X,
                ninthButtonY,
                resetButtonImage,
                SCALE,
                changeKeyAction);
        final int tenthButtonY = (int) (ninthButtonY + buttonReset.getBounds().getHeight() + BUTTON_SPACING);
        buttonReturn = new Button<GameStatesEnum, Void>(
                FIRST_BUTTON_X,
                FIRST_BUTTON_Y,
                returnButtonImage,
                SCALE,
                switchGameStateAction);
    }

    public enum keyAction {
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

    @Override
    public void render(final Graphics g) {

        g.drawImage(keyBackground, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        //buttonRotateLeft.render(g);
        //buttonRotateRight.render(g);
        //buttonMoveRight.render(g);
        //buttonMoveLeft.render(g);
        //buttonSoftDrop.render(g);
        //buttonHardDrop.render(g);
        //buttonHold.render(g);
        //buttonPause.render(g);
        //buttonReset.render(g);
        buttonReturn.render(g);
    }

    // actions ainda a definir
    @Override
    public void mouseClicked(final KeyEvent e) {
        if (buttonRotateLeft.getBounds().contains(e.getPoint())) {
            keyAction = keyAction.ROTATE_LEFT;
            buttonRotateLeft.execAction(e.getKeyCode());

        } else if (buttonRotateRight.getBounds().contains(e.getPoint())) {
            keyAction = keyAction.ROTATE_RIGHT;
            buttonRotateRight.execAction(e.getKeyCode());

        } else if (buttonMoveRight.getBounds().contains(e.getPoint())) {
            keyAction = keyAction.MOVE_LEFT;
            buttonMoveRight.execAction(e.getKeyCode());

        } else if (buttonMoveRight.getBounds().contains(e.getPoint())) {
            keyAction = keyAction.MOVE_RIGHT;
            buttonMoveRight.execAction(e.getKeyCode());

        } else if (buttonSoftDrop.getBounds().contains(e.getPoint())) {
            keyAction = keyAction.SOFT_DROP;
            buttonSoftDrop.execAction(e.getKeyCode());

        } else if (buttonHardDrop.getBounds().contains(e.getPoint())) {
            keyAction = keyAction.HARD_DROP;
            buttonHardDrop.execAction(e.getKeyCode());

        } else if (buttonHold.getBounds().contains(e.getPoint())) {
            keyAction = keyAction.HOLD;
            buttonHold.execAction(e.getKeyCode());

        } else if (buttonPause.getBounds().contains(e.getPoint())) {
            keyAction = keyAction.PAUSE;
            buttonPause.execAction(e.getKeyCode());

        } else if (buttonReset.getBounds().contains(e.getPoint())) {
            keyAction = keyAction.RESET;
            buttonReset.execAction(e.getKeyCode());

        }else if (buttonReturn.getBounds().contains(e.getPoint())) {
            keyAction = keyAction.RETURN;
            GameStateHandler.setActiveState(GameStatesEnum.SETTINGS);
    }
}

    @Override
    public void keyPressed(final KeyEvent e) {
        switch (buttonIndex) {
            case keyAction.ROTATE_LEFT:
                keybind.rotateLeft = e.getKeyCode();
                break;
            case keyAction.ROTATE_RIGHT:
                Keybinds.rotateRight = e.getKeyCode();
                break;
            case keyAction.MOVE_LEFT:
                keybind.moveLeft = e.getKeyCode();
                break;
            case keyAction.MOVE_RIGHT:
                keybind.moveRight = e.getKeyCode();
                break;
            case keyAction.MOVE_DOWN:
                keybind.moveDown = e.getKeyCode();
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
                keybind = new Keybinds();
                break;
            default:
                return;
        }
        Keybinds.saveToFile(keybind, KEYBINDINGS_PATH);
    }

}
