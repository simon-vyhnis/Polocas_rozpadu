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

public class ButtonView extends View {
    private Color color;
    private boolean roundedCorners;

    private String text;
    private String font;
    private Color textColor;
    private int textSize;

    private int paddingTop, paddingBottom, paddingLeft, paddingRight;

    private int textWidth = 0, textHeight = 0;


    public ButtonView(Element xml, Layout layout) {
        super(xml, layout);
        AttributesParser attributes = new AttributesParser(xml, layout.getApplication());
        try {
            color = attributes.getColor("color", Color.orange);
            roundedCorners = attributes.getBooleanValue("roundedCorners",true);
            textColor = attributes.getColor("textColor",Color.white);
            font = attributes.getStringValue("font","Arial");
            textSize = attributes.getIntValue("textSize",12);
            text = attributes.getStringValue("text", "BUTTON");

            paddingTop = attributes.getIntValue("paddingTop",5);
            paddingBottom = attributes.getIntValue("paddingBottom",5);
            paddingRight = attributes.getIntValue("paddingRight",10);
            paddingLeft = attributes.getIntValue("paddingLeft",10);
        } catch (InvalidAttributeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setFont(new Font(font, Font.BOLD, textSize));

        if(textHeight != g.getFontMetrics().getHeight() || textWidth != g.getFontMetrics().stringWidth(text)){
            textHeight = g.getFontMetrics().getHeight();
            textWidth = g.getFontMetrics().stringWidth(text);
            System.out.println("button: "+ textWidth +" "+ textHeight);
            notifyIfNeeded();
        }

        g.setColor(color);
        if(roundedCorners){
            g.fillRoundRect(getAbsoluteX(),getAbsoluteY(),getAbsoluteWidth(),getAbsoluteHeight(),10,10);
        }else {
            g.fillRect(getAbsoluteX(),getAbsoluteY(),getContentWidth(),getAbsoluteHeight());
        }
        while(!getRawHeight().equals("content") && g.getFontMetrics().getHeight() > getAbsoluteHeight()-paddingTop-paddingBottom){
            textSize--;
            g.setFont(new Font(font, Font.PLAIN, textSize));
        }

        g.setColor(textColor);
        g.drawString(text,getAbsoluteX()+paddingLeft,getAbsoluteY()+((getAbsoluteHeight()-g.getFontMetrics().getHeight())/2)+g.getFontMetrics().getAscent());
    }

    @Override
    public int getContentWidth() {
        return textWidth + paddingLeft + paddingRight;
    }

    @Override
    public int getContentHeight() {
        return textHeight + paddingTop + paddingBottom;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isRoundedCorners() {
        return roundedCorners;
    }

    public void setRoundedCorners(boolean roundedCorners) {
        this.roundedCorners = roundedCorners;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getPaddingBottom() {
        return paddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public int getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }
}
