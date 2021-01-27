package views;

import components.Bounds;
import components.Layout;
import components.View;
import core.AttributesParser;
import exceptions.InvalidAttributeException;
import org.jdom2.Attribute;
import org.jdom2.Element;

import java.awt.*;
import java.util.List;

public class TextView extends View {
    private String text;
    private int textSize;
    private String font;
    private Color textColor;
    private int textWidth = 0;
    private int textHeight = 0;


    public TextView(Element xml, Layout layout) {
        super(xml, layout);
        AttributesParser attributes = new AttributesParser(xml, layout.getApplication());
        try {
            text = attributes.getStringValue("text", "");
            textSize = attributes.getIntValue("textSize",12);
            font = attributes.getStringValue("font", "Arial");
            textColor = attributes.getColor("textColor", Color.black);
        } catch (InvalidAttributeException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void draw(Graphics g) {
        g.setFont(new Font(font,Font.PLAIN,textSize));
        if(textWidth != g.getFontMetrics().stringWidth(text) || textHeight != g.getFontMetrics().getHeight()) {
            textWidth = g.getFontMetrics().stringWidth(text);
            textHeight = g.getFontMetrics().getHeight();
            System.out.println("Text view: " + textWidth + " " + textHeight);
            System.out.println("Text view: " + g.getFontMetrics().stringWidth(text) + " " + g.getFontMetrics().getHeight());
            notifyIfNeeded();
        }

        g.setColor(textColor);
        g.drawString(text, getAbsoluteX(), getAbsoluteY());
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public int getContentWidth() {
        return textWidth;
    }

    @Override
    public int getContentHeight() {
        return textHeight;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

}
