package com.apontadores.ui;

import static com.apontadores.utils.Constants.GameConstants.GAME_HEIGHT;
import static com.apontadores.utils.Constants.GameConstants.GAME_WIDTH;
import static com.apontadores.utils.Constants.GameConstants.UPS_SET;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

public class TextElement implements FrameElement {

  public enum Alignment {
    LEFT, CENTER, RIGHT;

    public static final Alignment DEFAULT_ALIGN = LEFT;
  }

  public static class Builder {
    private final TextElement textAsset;

    public Builder() {
      textAsset = new TextElement();
    }

    public Builder name(final String name) {
      textAsset.name = name;
      return this;
    }

    public Builder text(final String text) {
      textAsset.text = text;
      return this;
    }

    public Builder font(final String font) {
      textAsset.font = font;
      return this;
    }

    public Builder fontType(final String fontType) {
      textAsset.fontType = fontType;
      switch (fontType) {
        case "bold" -> textAsset.fontTypeInt = Font.BOLD;
        case "italic" -> textAsset.fontTypeInt = Font.ITALIC;
        case "plain" -> textAsset.fontTypeInt = Font.PLAIN;
        default -> {
          textAsset.fontTypeInt = DEFAULT_TYPE;
          textAsset.fontType = DEFAULT_TYPE_NAME;
        }
      }

      return this;
    }

    public Builder size(final String size) {
      try {
        textAsset.size = Integer.parseInt(size);
      } catch (final NumberFormatException e) {
        textAsset.size = DEFAULT_SIZE;
      }

      return this;
    }

    public Builder alignment(final String alignment) {
      switch (alignment) {
        case "left" -> textAsset.align = Alignment.LEFT;
        case "center" -> textAsset.align = Alignment.CENTER;
        case "right" -> textAsset.align = Alignment.RIGHT;
        default -> textAsset.align = Alignment.DEFAULT_ALIGN;
      }
      return this;
    }

    public Builder x(final String x) {
      try {
        textAsset.x = Integer.parseInt(x);
      } catch (final NumberFormatException e) {
        textAsset.x = 0;
      }

      return this;
    }

    public Builder y(final String y) {
      try {
        textAsset.y = Integer.parseInt(y);
      } catch (final NumberFormatException e) {
        textAsset.y = 0;
      }

      return this;
    }

    public Builder size(final int size) {
      textAsset.size = size;
      return this;
    }

    public Builder x(final int x) {
      textAsset.x = x;
      return this;
    }

    public Builder y(final int y) {
      textAsset.y = y;
      return this;
    }

    public TextElement build() {
      return textAsset;
    }
  }

  // NOTE: Default values - not serialized
  public static final int DEFAULT_SIZE = 20;
  public static final String DEFAULT_FONT = "SansSerif";
  public static final int DEFAULT_TYPE = Font.PLAIN;
  public static final String DEFAULT_TYPE_NAME = "plain";

  // NOTE: Json fields
  private String name;
  private String text;
  private String textFile;
  private double x;
  private double y;
  private double angle;
  private String anchorPoint;
  private String font;
  private String fontType;
  private int size;
  private int[] color;
  private Alignment align;
  private boolean editable;
  private boolean clearDefaultText;
  private int maxLength;

  // NOTE: not serialized
  private transient int fontTypeInt;
  private transient boolean enabled = true;
  private transient int xAbs, yAbs;
  private transient Color textColor;
  private transient FrameElement parent;
  private transient boolean hasFocus;
  private transient boolean textFromFile;
  private transient String[] fileLines;

  private transient int animationTick = 0;
  private transient boolean animationUp = true;

  public void setParent(final FrameElement parent) {
    this.parent = parent;
  }

  public void giveFocus() {
    hasFocus = true;
    if (clearDefaultText)
      text = "";
    clearDefaultText = false;
  }

  public void removeFocus() {
    hasFocus = false;
    if (!text.isEmpty() && (text.charAt(text.length() - 1) == '|'))
      text = text.substring(0, text.length() - 1);
  }

  public void setText(final String text) {
    this.text = text;
  }

  public String getText() {
    if (clearDefaultText)
      return "";

    if (text.endsWith("|"))
      return text.substring(0, text.length() - 1);

    return text;
  }

  public void keyboardInput(final KeyEvent e) {
    if (!enabled | !hasFocus | !editable)
      return;

    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
      if (text.length() > 0) {
        if (text.contains("|") && text.length() == 1) {
          return;
        }

        if (text.contains("|")) {
          text = text.substring(0, text.length() - 2);
          text += "|";
          return;
        }

        text = text.substring(0, text.length() - 1);
      }
      return;
    }

    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
      removeFocus();
      return;
    }

    char keyChar = e.getKeyChar();
    if ((Character.isAlphabetic(keyChar) ||
        Character.isDigit(keyChar) ||
        keyChar == '.') // needs to be allowed for IPV4 address input
        && text.length() < maxLength) {

      if (text.contains("|")) {
        text = text.substring(0, text.length() - 1) + e.getKeyChar() + "|";
        return;
      }
      text += e.getKeyChar();

    }
  }

  @Override
  public void init() {
    if (text == null)
      text = "";
    assert font != null;
    if (font.isEmpty())
      font = DEFAULT_FONT;
    if (size == 0)
      size = DEFAULT_SIZE;
    if (align == null)
      align = Alignment.DEFAULT_ALIGN;

    switch (fontType) {
      case "bold" -> fontTypeInt = Font.BOLD;
      case "italic" -> fontTypeInt = Font.ITALIC;
      case "plain" -> fontTypeInt = Font.PLAIN;
      default -> fontTypeInt = DEFAULT_TYPE;
    }

    if (anchorPoint.equals("game")) {
      xAbs = (int) (x / 100 * GAME_WIDTH);
      yAbs = (int) (y / 100 * GAME_HEIGHT);
    } else if (anchorPoint.equals("parent")) {
      xAbs = parent.getAnchorPoint().x + (int) (x / 100 * parent.getDimensions().x);
      yAbs = parent.getAnchorPoint().y + (int) (y / 100 * parent.getDimensions().y);
      angle = parent.getRotation();
    }

    if (color != null) {
      textColor = new Color(color[0], color[1], color[2]);
    } else {
      textColor = Color.WHITE;
    }
    // TODO: load text from file
    if (textFile != null && !textFile.isEmpty()) {
      final String loadedText = ""; // TODO: load text from file
      fileLines = loadedText.split("\n");
      textFromFile = true;
    }
  }

  @Override
  public void render(final Graphics g) {

    final Graphics2D g2d = (Graphics2D) g;

    g2d.setFont(new Font(font, fontTypeInt, size));
    g2d.setColor(textColor);

    final int textHeight = g2d.getFontMetrics().getHeight();

    g2d.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

    final AffineTransform orig = g2d.getTransform();
    if (angle != 0)
      g2d.rotate(Math.toRadians(angle));

    if (textFromFile) {
      renderTextFile(g2d, textHeight);
    } else {
      renderText(g2d);
    }

    g2d.setTransform(orig);
  }

  public void drawAligned(final Graphics2D g2d, final String line, final int textWidth, final int yLine) {
    switch (align) {
      case LEFT -> g2d.drawString(line, xAbs, yLine);
      case CENTER -> g2d.drawString(line, xAbs - textWidth / 2, yLine);
      case RIGHT -> g2d.drawString(line, xAbs - textWidth, yLine);
    }
  }

  @Override
  public void update() {
    if (!enabled)
      return;

    if (!editable || !hasFocus)
      return;

    animationTick++;
    final int animationSpeed = UPS_SET / 2;
    if (animationTick >= animationSpeed) {
      animationTick = 0;
      animationUp = !animationUp;

      if (animationUp) {
        text += "|";
      } else if (text.length() > 0 && text.charAt(text.length() - 1) == '|') {
        text = text.substring(0, text.length() - 1);
      }

    }

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

  @Override
  public Point getAnchorPoint() {
    return new Point(xAbs, yAbs);
  }

  @Override
  public Point getDimensions() {
    if (textFromFile)
      return new Point(fileLines[0].length(), fileLines.length);
    return new Point(text.length(), 1);
  }

  @Override
  public double getRotation() {
    return angle;
  }

  private void renderTextFile(final Graphics2D g2d, final int textHeight) {
    int yLine = yAbs;

    for (final String line : fileLines) {
      final int textWidth = g2d.getFontMetrics().stringWidth(line);
      drawAligned(g2d, line, textWidth, yLine);
      yLine += textHeight;
    }
  }

  private void renderText(final Graphics2D g2d) {
    final int textWidth = g2d.getFontMetrics().stringWidth(text);
    drawAligned(g2d, text, textWidth, yAbs);
  }
}
