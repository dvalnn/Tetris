package com.psw.tetris.gameStates.stateTypes;

public class ChangeKeybinds extends GameState {

    // background
    // botão de return
    // botões de keybinds
    // logica de trocar keybinds
    // logica de resetar para defaults keybinds

    public ChangeKeybinds() {
        super(GameStatesEnum.CHANGE_KEYBINDS);
    }

    public enum keyAction {
        ROTATE_LEFT,
        ROTATE_RIGHT,
        MOVE_LEFT,
        MOVE_RIGHT,
        MOVE_DOWN,
        HARD_DROP,
        HOLD,
        PAUSE;
    }

    @Override
    public void render(final Graphics g) {

        // g.drawImage(background, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);// ! coordenadas
        // ainda a definir
        // g.drawImage(button1, 0, 0, GAME_WIDTH, GAME_HEIGHT, null); //! coordenadas
        // ainda a definir
        // g.drawImage(button2, 0, 0, GAME_WIDTH, GAME_HEIGHT, null); //! coordenadas
        // ainda a definir
        // g.drawImage(button3, 0, 0, GAME_WIDTH, GAME_HEIGHT, null); //! coordenadas
        // ainda a definir
        // g.drawImage(button4, 0, 0, GAME_WIDTH, GAME_HEIGHT, null); //! coordenadas
        // ainda a definir
        // g.drawImage(button5, 0, 0, GAME_WIDTH, GAME_HEIGHT, null); //! coordenadas
        // ainda a definir
        // g.drawImage(button6, 0, 0, GAME_WIDTH, GAME_HEIGHT, null); //! coordenadas
        // ainda a definir
        // g.drawImage(button7, 0, 0, GAME_WIDTH, GAME_HEIGHT, null); //! coordenadas
        // ainda a definir
        // g.drawImage(button8, 0, 0, GAME_WIDTH, GAME_HEIGHT, null); //! coordenadas
        // ainda a definir
        buttonRotateLeft.render(g);
        buttonRotateRight.render(g);
        buttonMoveRight.render(g);
        buttonMoveLeft.render(g);
        buttonModeDown.render(g);
        buttonHardDrop.render(g);
        buttonHold.render(g);
        buttonPause.render(g);
    }

    // actions ainda a definir
    @Override
    public void mouseClicked(final KeyEvent e) {
        if (button.getBounds().contains(e.getPoint())) {
            keyAction = keyAction.ROTATE_LEFT;
            // button.execAction(GameStatesEnum.MAIN_MENU);
        }
        else if (button1.getBounds().contains(e.getPoint())) {
            keyAction = keyAction.ROTATE_RIGHT;
            // button1.execAction(GameStatesEnum.MAIN_MENU);
        } 
        else if (button2.getBounds().contains(e.getPoint())) {
            keyAction = keyAction.MOVE_LEFT;
            // button2.execAction(GameStatesEnum.MAIN_MENU);
        } 
        else if (button3.getBounds().contains(e.getPoint())) {
            keyAction = keyAction.MOVE_RIGHT;
            // button3.execAction(GameStatesEnum.MAIN_MENU);
        } 
        else if (button4.getBounds().contains(e.getPoint())) {
            keyAction = keyAction.MOVE_DOWN;
            // button4.execAction(GameStatesEnum.MAIN_MENU);
        } 
        else if (button5.getBounds().contains(e.getPoint())) {
            keyAction = keyAction.HARD_DROP;
            // button5.execAction(GameStatesEnum.MAIN_MENU);
        } 
        else if (button6.getBounds().contains(e.getPoint())) {
            keyAction = keyAction.HOLD;
            // button6.execAction(GameStatesEnum.MAIN_MENU);
        } 
        else if (button7.getBounds().contains(e.getPoint())) {
            keyAction = keyAction.PAUSE;
            // button7.execAction(GameStatesEnum.MAIN_MENU);
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
            default:
                return;
        }
        
        Keybinds.saveToFile(keybind, KEYBINDINGS_PATH);
    }

}
