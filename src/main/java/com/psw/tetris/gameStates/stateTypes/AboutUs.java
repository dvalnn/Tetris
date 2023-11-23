package com.psw.tetris.gameStates.stateTypes;

import static com.psw.tetris.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.psw.tetris.utils.Constants.GameConstants.GAME_WIDTH;
import static com.psw.tetris.utils.Constants.UI.Buttons.RETURN_BUTTON;

import com.psw.tetris.gameStates.GameState;
import com.psw.tetris.gameStates.GameStateHandler.GameStatesEnum;
import com.psw.tetris.ui.Button;
import com.psw.tetris.ui.SwitchGameStateAction;
import com.psw.tetris.utils.LoadSave;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;

public class AboutUs extends GameState {

    private final Button buttonReturn;

    private final BufferedImage returnButtonImage = LoadSave.loadImage(RETURN_BUTTON);
    private final BufferedImage aboutUsBackground;

    private final double RETURN_BUTTON_SCALE = 0.29;

    private final int returnButtonX = 40;
    private final int returnButtonY = 620;

    private int textRenderX = 80;
    private int textRenderY = 100;

    private final SwitchGameStateAction switchGameStateAction = new SwitchGameStateAction();

    //TODO: change this to a proper text file
    private final String text = """
            DAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUN 
            DAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUN
            DAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUN
            DAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUN
            DAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUN
            DAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUN
            DAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUN
            DAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUN
            DAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUN
            DAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUN
            DAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUN
            DAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUN
            """;

    public AboutUs() {
        super(GameStatesEnum.ABOUT_US);

        aboutUsBackground = LoadSave.loadBackground("aboutUs.png");

        buttonReturn = new Button(returnButtonX, returnButtonY, returnButtonImage, RETURN_BUTTON_SCALE);
    }

    @Override
    public void render(final Graphics g) {
        g.drawImage(aboutUsBackground, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        buttonReturn.render(g);

        // text elements rendering (score and level)
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(30f));
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        textRenderY = 100;
        for (String line : text.split("\n"))
            g2.drawString(line,textRenderX, textRenderY += g.getFontMetrics().getHeight());

    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        buttonReturn.execIfClicked(
                e.getPoint(),
                switchGameStateAction,
                GameStatesEnum.MAIN_MENU);
    }

}
