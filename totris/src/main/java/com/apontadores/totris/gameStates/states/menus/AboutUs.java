package com.apontadores.totris.gameStates.states.menus;

import static com.apontadores.totris.utils.Constants.FRAMES_PATH;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;

import com.apontadores.totris.gameStates.GameState;
import com.apontadores.totris.gameStates.GameStateHandler.GameStatesEnum;
import com.apontadores.totris.ui.ImageElement;
import com.apontadores.totris.ui.SwitchStateAction;
import com.apontadores.totris.utils.LoadSave;
import com.apontadores.totris.ui.Frame;

public class AboutUs extends GameState {

  private final Frame frame;
  private final SwitchStateAction stateAction = new SwitchStateAction();

  private int textRenderX;
  private int textRenderY;
  private Font font;

  private final String text = """
      DAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUN
      DAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUN
      DAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUN
      DAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUN
      DAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUN
      DAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUN
      DAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUN
      DAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUN
      DAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUN
      DAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUN
      DAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUN
      DAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUN
      DAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUN
      DAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUN
      DAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUNDAHYUN
      """;

  public AboutUs() {
    super(GameStatesEnum.ABOUT_US);
    frame = Frame.loadFromJson(FRAMES_PATH + "aboutUs.json");
  }

  @Override
  public void render(final Graphics g) {
    frame.render(g);



    font = LoadSave.loadFont("CascadiaCode-BoldItalic.ttf");
    font = font.deriveFont(26.6f);

    g.setColor(Color.BLACK);
    g.setFont(font);
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

    textRenderX = 429;
    textRenderY = 180;

    for (String line : text.split("\n"))
      g2.drawString(line, textRenderX, textRenderY += g.getFontMetrics().getHeight());
  }

  @Override
  public void mouseClicked(final MouseEvent e) {
    ((ImageElement) frame.getElement("returnToMainMenu"))
        .execIfClicked(e.getX(), e.getY(), stateAction, GameStatesEnum.MAIN_MENU);
  }
}
