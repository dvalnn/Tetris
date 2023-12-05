package com.apontadores.gameStates.states.menus;

import static com.apontadores.utils.Constants.RESOURCES_PATH;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.apontadores.gameStates.GameState;
import com.apontadores.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.main.Game;
import com.apontadores.ui.ButtonAction;
import com.apontadores.ui.Frame;
import com.apontadores.ui.ImageElement;
import com.apontadores.ui.SwitchStateAction;
import com.apontadores.utils.LoadSave;

//TODO: Refactor this piece of code 

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
        PAUSE;
    }

    private final int spacebar = 32;
    private final int leftArrow = 37;
    private final int rightArrow = 39;
    private final int downArrow = 40;

    private keyAction Action = keyAction.OFF;

    private final Frame frame;
    private final SwitchStateAction switchState = new SwitchStateAction();

    private final ButtonAction<keyAction, Void> changeKeyAction = (keyAction a) -> {
        Action = a;
        return null;
    };

    private final ButtonAction<Void, Void> resetKeyAction = (Void) -> {
        Game.resetKeybind();
        updateKeybinds();
        LoadSave.saveJson(RESOURCES_PATH + "/config/keybinds.json", Game.getKeybinds());
        return null;
    };

    public ChangeKeybinds() {
        super(GameStatesEnum.CHANGE_KEYBINDS);
        frame = Frame.loadFromJson(RESOURCES_PATH + "/frames/changeKeybinds.json");

        if (!checksSpecialKeys(Game.getKeybinds().rotatesLeft, "changeKeybindRotateLeft")) {
            ((ImageElement) frame.getElement("changeKeybindRotateLeft"))
                    .getTextElement()
                    .setText(
                            new String(String.valueOf((char) Game.getKeybinds().rotatesLeft)));
        }

        if (!checksSpecialKeys(Game.getKeybinds().rotatesRight, "changeKeybindRotateRight")) {
            ((ImageElement) frame.getElement("changeKeybindRotateRight"))
                    .getTextElement()
                    .setText(
                            new String(String.valueOf((char) Game.getKeybinds().rotatesRight)));
        }

        if (!checksSpecialKeys(Game.getKeybinds().movesRight, "changeKeybindMoveRight")) {
            ((ImageElement) frame.getElement("changeKeybindMoveRight"))
                    .getTextElement()
                    .setText(
                            new String(String.valueOf((char) Game.getKeybinds().movesRight)));
        }

        if (!checksSpecialKeys(Game.getKeybinds().movesLeft, "changeKeybindMoveLeft")) {
            ((ImageElement) frame.getElement("changeKeybindMoveLeft"))
                    .getTextElement()
                    .setText(
                            new String(String.valueOf((char) Game.getKeybinds().movesLeft)));
        }

        if (!checksSpecialKeys(Game.getKeybinds().softDrop, "changeKeybindSoftDrop")) {
            ((ImageElement) frame.getElement("changeKeybindSoftDrop"))
                    .getTextElement()
                    .setText(
                            new String(String.valueOf((char) Game.getKeybinds().softDrop)));
        }

        if (!checksSpecialKeys(Game.getKeybinds().hardDrop, "changeKeybindHardDrop")) {
            ((ImageElement) frame.getElement("changeKeybindHardDrop"))
                    .getTextElement()
                    .setText(
                            new String(String.valueOf((char) Game.getKeybinds().hardDrop)));
        }

        if (!checksSpecialKeys(Game.getKeybinds().hold, "changeKeybindHold")) {
            ((ImageElement) frame.getElement("changeKeybindHold"))
                    .getTextElement()
                    .setText(
                            new String(String.valueOf((char) Game.getKeybinds().hold)));
        }

        if (!checksSpecialKeys(Game.getKeybinds().pause, "changeKeybindPause")) {
            ((ImageElement) frame.getElement("changeKeybindPause"))
                    .getTextElement()
                    .setText(
                            new String(String.valueOf((char) Game.getKeybinds().pause)));
        }
    }

    @Override
    public void render(final Graphics g) {
        frame.render(g);
    }

    @Override
    public void mouseClicked(final MouseEvent e) {

        int x = e.getX();
        int y = e.getY();

        ((ImageElement) frame.getElement("changeKeybindRotateLeft"))
                .execIfClicked(x, y, changeKeyAction, keyAction.ROTATE_LEFT);

        ((ImageElement) frame.getElement("changeKeybindRotateRight"))
                .execIfClicked(x, y, changeKeyAction, keyAction.ROTATE_RIGHT);

        ((ImageElement) frame.getElement("changeKeybindMoveRight"))
                .execIfClicked(x, y, changeKeyAction, keyAction.MOVE_RIGHT);

        ((ImageElement) frame.getElement("changeKeybindMoveLeft"))
                .execIfClicked(x, y, changeKeyAction, keyAction.MOVE_LEFT);

        ((ImageElement) frame.getElement("changeKeybindSoftDrop"))
                .execIfClicked(x, y, changeKeyAction, keyAction.SOFT_DROP);

        ((ImageElement) frame.getElement("changeKeybindHardDrop"))
                .execIfClicked(x, y, changeKeyAction, keyAction.HARD_DROP);

        ((ImageElement) frame.getElement("changeKeybindHold"))
                .execIfClicked(x, y, changeKeyAction, keyAction.HOLD);

        ((ImageElement) frame.getElement("changeKeybindPause"))
                .execIfClicked(x, y, changeKeyAction, keyAction.PAUSE);

        ((ImageElement) frame.getElement("Reset"))
                .execIfClicked(x, y, resetKeyAction, null);

        ((ImageElement) frame.getElement("returnToSettings"))
                .execIfClicked(x, y, switchState, GameStatesEnum.SETTINGS);

    }

    @Override
    public void keyPressed(final KeyEvent e) {
        switch (Action) {
            case ROTATE_LEFT:

                if (checkDuplicateKeys(e))
                    Game.getKeybinds().rotatesLeft = e.getKeyCode();

                if (checksSpecialKeys(Game.getKeybinds().rotatesLeft, "changeKeybindRotateLeft"))
                    break;

                ((ImageElement) frame.getElement("changeKeybindRotateLeft"))
                        .getTextElement()
                        .setText(
                                new String(String.valueOf((char) Game.getKeybinds().rotatesLeft)));

                break;

            case ROTATE_RIGHT:

                if (checkDuplicateKeys(e))
                    Game.getKeybinds().rotatesRight = e.getKeyCode();

                if (checksSpecialKeys(Game.getKeybinds().rotatesRight, "changeKeybindRotateRight"))
                    break;

                ((ImageElement) frame.getElement("changeKeybindRotateRight"))
                        .getTextElement()
                        .setText(
                                (new String(String.valueOf((char) Game.getKeybinds().rotatesRight))));

                break;
            case MOVE_LEFT:

                if (checkDuplicateKeys(e))
                    Game.getKeybinds().movesLeft = e.getKeyCode();

                if (checksSpecialKeys(Game.getKeybinds().movesLeft, "changeKeybindMoveLeft"))
                    break;

                ((ImageElement) frame.getElement("changeKeybindMoveLeft"))
                        .getTextElement()
                        .setText(
                                new String(String.valueOf((char) Game.getKeybinds().movesLeft)));

                break;
            case MOVE_RIGHT:

                if (checkDuplicateKeys(e))
                    Game.getKeybinds().movesRight = e.getKeyCode();

                if (checksSpecialKeys(Game.getKeybinds().movesRight, "changeKeybindMoveRight"))
                    break;

                ((ImageElement) frame.getElement("changeKeybindMoveRight"))
                        .getTextElement()
                        .setText(
                                new String(String.valueOf((char) Game.getKeybinds().movesRight)));

                break;
            case SOFT_DROP:

                if (checkDuplicateKeys(e))
                    Game.getKeybinds().softDrop = e.getKeyCode();

                if (checksSpecialKeys(Game.getKeybinds().softDrop, "changeKeybindSoftDrop"))
                    break;

                ((ImageElement) frame.getElement("changeKeybindSoftDrop"))
                        .getTextElement()
                        .setText(
                                new String(String.valueOf((char) Game.getKeybinds().softDrop)));

                break;
            case HARD_DROP:

                if (checkDuplicateKeys(e))
                    Game.getKeybinds().hardDrop = e.getKeyCode();

                if (checksSpecialKeys(Game.getKeybinds().hardDrop, "changeKeybindHardDrop"))
                    break;

                ((ImageElement) frame.getElement("changeKeybindHardDrop"))
                        .getTextElement()
                        .setText(
                                new String(String.valueOf((char) Game.getKeybinds().hardDrop)));

                break;
            case HOLD:

                if (checkDuplicateKeys(e))
                    Game.getKeybinds().hold = e.getKeyCode();

                if (checksSpecialKeys(Game.getKeybinds().hold, "changeKeybindHold"))
                    break;

                ((ImageElement) frame.getElement("changeKeybindHold"))
                        .getTextElement()
                        .setText(
                                new String(String.valueOf((char) Game.getKeybinds().hold)));

                break;
            case PAUSE:
                if (checkDuplicateKeys(e))
                    Game.getKeybinds().pause = e.getKeyCode();

                if (checksSpecialKeys(Game.getKeybinds().pause, "changeKeybindPause"))
                    break;

                ((ImageElement) frame.getElement("changeKeybindPause"))
                        .getTextElement()
                        .setText(
                                new String(String.valueOf((char) Game.getKeybinds().pause)));

                break;
            default:
                return;
        }
        LoadSave.saveJson(RESOURCES_PATH + "/config/keybinds.json", Game.getKeybinds());

        Action = keyAction.OFF;
    }

    public void updateKeybinds() {

        ((ImageElement) frame.getElement("changeKeybindRotateLeft"))
                .getTextElement()
                .setText(
                        new String(String.valueOf((char) Game.getKeybinds().rotatesLeft)));

        ((ImageElement) frame.getElement("changeKeybindRotateRight"))
                .getTextElement()
                .setText(
                        new String(String.valueOf((char) Game.getKeybinds().rotatesRight)));

        ((ImageElement) frame.getElement("changeKeybindMoveRight"))
                .getTextElement()
                .setText("\u2192");

        ((ImageElement) frame.getElement("changeKeybindMoveLeft"))
                .getTextElement()
                .setText("\u2190");

        ((ImageElement) frame.getElement("changeKeybindSoftDrop"))
                .getTextElement()
                .setText("\u2193");

        ((ImageElement) frame.getElement("changeKeybindHardDrop"))
                .getTextElement()
                .setText("\u2423");

        ((ImageElement) frame.getElement("changeKeybindHold"))
                .getTextElement()
                .setText(
                        new String(String.valueOf((char) Game.getKeybinds().hold)));

        ((ImageElement) frame.getElement("changeKeybindPause"))
                .getTextElement()
                .setText(
                        new String(String.valueOf((char) Game.getKeybinds().pause)));
    }

    public boolean checksSpecialKeys(int keybind, String key) {
        if (keybind == leftArrow) {

            ((ImageElement) frame.getElement(key))
                    .getTextElement()
                    .setText("\u2190");

            return true;
        }
        if (keybind == rightArrow) {

            ((ImageElement) frame.getElement(key))
                    .getTextElement()
                    .setText("\u2192");

            return true;
        }
        if (keybind == downArrow) {

            ((ImageElement) frame.getElement(key))
                    .getTextElement()
                    .setText("\u2193");

            return true;
        }
        if (keybind == spacebar) {

            ((ImageElement) frame.getElement(key))
                    .getTextElement()
                    .setText("\u2423");

            return true;
        }

        return false;
    }

    // TODO: remove this
    public boolean checkDuplicateKeys(final KeyEvent e) {

        if ((e.getKeyCode() != Game.getKeybinds().rotatesLeft)
                && (e.getKeyCode() != Game.getKeybinds().rotatesRight)
                && (e.getKeyCode() != Game.getKeybinds().movesLeft)
                && (e.getKeyCode() != Game.getKeybinds().movesRight)
                && (e.getKeyCode() != Game.getKeybinds().softDrop)
                && (e.getKeyCode() != Game.getKeybinds().hardDrop)
                && (e.getKeyCode() != Game.getKeybinds().hold)
                && (e.getKeyCode() != Game.getKeybinds().pause)) {

            return true;
        }
        return false;
    }

}
