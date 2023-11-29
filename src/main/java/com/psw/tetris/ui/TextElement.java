package com.psw.tetris.ui;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class TextElement implements UiElement {

  public enum Alignment {
    LEFT, CENTER, RIGHT;

    public static final Alignment DEFAULT_ALIGN = LEFT;
  }

  public static class Builder {
    private TextElement textAsset;

    public Builder() {
      textAsset = new TextElement();
    }

    public Builder name(String name) {
      textAsset.name = name;
      return this;
    }

    public Builder text(String text) {
      textAsset.text = text;
      return this;
    }

    public Builder font(String font) {
      textAsset.font = font;
      return this;
    }

    public Builder fontType(String fontType) {
      textAsset.fontType = fontType;
      switch (fontType) {
        case "bold":
          textAsset.fontTypeInt = Font.BOLD;
          break;
        case "italic":
          textAsset.fontTypeInt = Font.ITALIC;
          break;
        case "plain":
          textAsset.fontTypeInt = Font.PLAIN;
          break;
        default:
          textAsset.fontTypeInt = DEFAULT_TYPE;
          textAsset.fontType = DEFAULT_TYPE_NAME;
          break;
      }

      return this;
    }

    public Builder size(String size) {
      try {
        textAsset.size = Integer.parseInt(size);
      } catch (NumberFormatException e) {
        textAsset.size = DEFAULT_SIZE;
      }

      return this;
    }

    public Builder alignment(String alignment) {
      switch (alignment) {
        case "left":
          textAsset.align = Alignment.LEFT;
          break;
        case "center":
          textAsset.align = Alignment.CENTER;
          break;
        case "right":
          textAsset.align = Alignment.RIGHT;
          break;
        default:
          textAsset.align = Alignment.DEFAULT_ALIGN;
          break;
      }
      return this;
    }

    public Builder renderPriority(String renderPriority) {
      try {
        textAsset.renderPriority = Integer.parseInt(renderPriority);
      } catch (NumberFormatException e) {
        textAsset.renderPriority = 0;
      }

      return this;
    }

    public Builder x(String x) {
      try {
        textAsset.x = Integer.parseInt(x);
      } catch (NumberFormatException e) {
        textAsset.x = 0;
      }

      return this;
    }

    public Builder y(String y) {
      try {
        textAsset.y = Integer.parseInt(y);
      } catch (NumberFormatException e) {
        textAsset.y = 0;
      }

      return this;
    }

    public Builder size(int size) {
      textAsset.size = size;
      return this;
    }

    public Builder renderPriority(int renderPriority) {
      textAsset.renderPriority = renderPriority;
      return this;
    }

    public Builder x(int x) {
      textAsset.x = x;
      return this;
    }

    public Builder y(int y) {
      textAsset.y = y;
      return this;
    }

    public TextElement build() {
      return textAsset;
    }
  }

  public static final int DEFAULT_SIZE = 20;
  public static final String DEFAULT_FONT = "SansSerif";
  public static final int DEFAULT_TYPE = Font.PLAIN;
  public static final String DEFAULT_TYPE_NAME = "plain";

  private String name;
  private String text;
  private int x;
  private int y;
  private int size;
  private String font;
  private String fontType;
  private Alignment align;
  private int renderPriority;

  // dont serialize this
  private transient int fontTypeInt;
  private transient boolean enabled = true;

  @Override
  public void render(Graphics g) {
    if (!enabled | text == null)
      return;

    Graphics2D g2d = (Graphics2D) g;

    if (font == null)
      font = DEFAULT_FONT;
    if (fontTypeInt == 0)
      fontTypeInt = DEFAULT_TYPE;
    if (size == 0)
      size = DEFAULT_SIZE;
    if (align == null)
      align = Alignment.DEFAULT_ALIGN;

    switch (fontType) {
      case "bold":
        fontTypeInt = Font.BOLD;
        break;
      case "italic":
        fontTypeInt = Font.ITALIC;
        break;
      case "plain":
        fontTypeInt = Font.PLAIN;
        break;
      default:
        fontTypeInt = DEFAULT_TYPE;
        break;
    }

    g2d.setFont(new Font(font, fontTypeInt, size));

    int textHeight = g2d.getFontMetrics().getHeight();

    g2d.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

    for (String line : text.split("\n")) {
      int textWidth = g2d.getFontMetrics().stringWidth(line);
      drawAligned(g2d, line, textWidth);
      y += textHeight;
    }
  }

  public void drawAligned(Graphics2D g2d, String line, int textWidth) {

    switch (align) {
      case LEFT:
        g2d.drawString(line, x, y);
        break;
      case CENTER:
        g2d.drawString(line, x - textWidth / 2, y);
        break;
      case RIGHT:
        g2d.drawString(line, x - textWidth, y);
        break;
    }
  }

  @Override
  public void update() {
    if (!enabled)
      return;

  }

  @Override
  public int getRenderPriority() {
    return renderPriority;
  }

  @Override
  public void setRenderPriority(int priority) {
    renderPriority = priority;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getType() {
    return "text";
  }

  @Override
  public void enable() {
    enabled = true;
  }

  @Override
  public void disable() {
    enabled = false;
  }
}
