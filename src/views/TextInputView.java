package views;

import components.Layout;
import components.View;
import core.AttributesParser;
import core.JUIXApplication;
import exceptions.InvalidAttributeException;
import org.jdom2.Element;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class TextInputView extends View {
    private Color backgroundColor;
    private boolean roundedCorners;

    private Color textColor, hintColor;
    private String font;
    private int textSize;
    private String text, hint;

    private boolean active = false;
    private JUIXCursor cursor;


    public TextInputView(Element xml, Layout layout) {
        super(xml, layout);
        text = "";
        AttributesParser attributes = new AttributesParser(xml, layout.getApplication());
        try {
            backgroundColor = attributes.getColor("backgroundColor", Color.white);
            roundedCorners = attributes.getBooleanValue("roundedCorners",true);
            textColor = attributes.getColor("textColor",Color.black);
            hintColor = attributes.getColor("hintColor",Color.gray);
            font = attributes.getStringValue("font","Arial");
            textSize = attributes.getIntValue("textSize",12);
            hint = attributes.getStringValue("hint","");
        } catch (InvalidAttributeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(backgroundColor);
        if(roundedCorners){
            g.fillRoundRect(getAbsoluteX(),getAbsoluteY(),getAbsoluteWidth(),getAbsoluteHeight(),10,10);
        }else {
            g.fillRect(getAbsoluteX(),getAbsoluteY(),getContentWidth(),getAbsoluteHeight());
        }
        g.setFont(new Font(font, Font.PLAIN, textSize));
        while(g.getFontMetrics().getHeight() > getAbsoluteHeight()-4){
            textSize--;
            g.setFont(new Font(font, Font.PLAIN, textSize));
        }
        if((text.isEmpty()) && !active){
            g.setColor(hintColor);
            g.drawString(hint,getAbsoluteX()+10,getAbsoluteY()+((getAbsoluteHeight()-g.getFontMetrics().getHeight())/2)+g.getFontMetrics().getAscent());
        }else {
            g.setColor(textColor);
            g.drawString(text,getAbsoluteX()+10,getAbsoluteY()+((getAbsoluteHeight()-g.getFontMetrics().getHeight())/2)+g.getFontMetrics().getAscent());
        }
        if(cursor != null){
            cursor.draw(g,getAbsoluteY()+((getAbsoluteHeight()-g.getFontMetrics().getHeight())/2));
        }
    }

    @Override
    public void update() {
        if(cursor != null){
            cursor.update();
        }
    }

    public void stopBeingActive() {
        active = false;
        cursor = null;
    }

    @Override
    public void onClick(MouseEvent e) {
        super.onClick(e);
        active = true;
        layout.setActiveInput(this);
        if(cursor == null) {
            cursor = new JUIXCursor(layout.getApplication(), textColor, getAbsoluteX() + 10);
        }
        if(!text.isEmpty())
            cursor.move(e.getX()- getAbsoluteX() - 10);
    }


    public void keyTyped(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if (cursor.cursorPos > 0) {
                StringBuilder builder = new StringBuilder(text);
                builder.deleteCharAt(cursor.cursorPos-1);
                text = builder.toString();
                cursor.cursorPos--;
            }
        }else if(e.getExtendedKeyCode() == KeyEvent.VK_DELETE){
            if(cursor.cursorPos < text.length()){
                StringBuilder builder = new StringBuilder(text);
                builder.deleteCharAt(cursor.cursorPos);
                text = builder.toString();
            }
        }else if(e.getKeyCode() == KeyEvent.VK_RIGHT && cursor.cursorPos != text.length()) {
            cursor.cursorPos++;
        }else if(e.getKeyCode() == KeyEvent.VK_LEFT && cursor.cursorPos != 0) {
            cursor.cursorPos--;
        }else if(Character.isDefined(e.getKeyChar())){
            //65535 is value, that returns function keys, 27 returns esc
            Font currentFont = new Font(font, Font.PLAIN, textSize);
            currentFont.canDisplay(e.getKeyChar());
            text = text + e.getKeyChar();

            //Moves cursor as you type
            if(cursor != null && cursor.cursorPos == text.length()-1){
                cursor.cursorPos++;
            }
        }


    }

    @Override
    public void onCursorEnter() {
        super.onCursorEnter();
        layout.getApplication().setCursor(new Cursor(Cursor.TEXT_CURSOR));
    }
    @Override
    public void onCursorExit() {
        super.onCursorExit();
        layout.getApplication().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    @Override
    public int getContentWidth() {
        return 0;
    }
    @Override
    public int getContentHeight() {
        return 0;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    public boolean isRoundedCorners() {
        return roundedCorners;
    }
    public void setRoundedCorners(boolean roundedCorners) {
        this.roundedCorners = roundedCorners;
    }
    public Color getTextColor() {
        return textColor;
    }
    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }
    public String getFont() {
        return font;
    }
    public void setFont(String font) {
        this.font = font;
    }
    public int getTextSize() {
        return textSize;
    }
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getHint() {
        return hint;
    }
    public void setHint(String hint) {
        this.hint = hint;
    }

    private class JUIXCursor {
        private boolean isDisplayed = true;
        private int tickCounter=0;
        private final double ticksPerSecond;
        private final Color color;
        private int cursorPos = 0;
        private final int x;

        private boolean needsMove = false;
        private int clickX;

        private JUIXCursor(JUIXApplication application, Color color, int x) {
            ticksPerSecond = application.getTicksPerSecond();
            this.color = color;
            this.x = x;
        }

        public void update(){
            tickCounter++;
            if(tickCounter>=ticksPerSecond/2){
                tickCounter = 0;
                isDisplayed = !isDisplayed;
            }
        }
        public void draw(Graphics g, int y){
            if(needsMove) {
                cursorPos = 0;
                while (g.getFontMetrics().stringWidth(text.substring(0, cursorPos)) <= clickX && cursorPos < text.length()) {
                    System.out.println(g.getFontMetrics().stringWidth(text.substring(0, cursorPos)));
                    cursorPos++;
                }
                //handles the click inside of character
                if(clickX - g.getFontMetrics().stringWidth(text.substring(0, cursorPos-1)) < g.getFontMetrics().stringWidth(text.substring(0, cursorPos)) - clickX){
                    cursorPos--;
                }
                needsMove = false;
            }

            if (isDisplayed){
                g.setColor(color);
                int pos = g.getFontMetrics().stringWidth(text.substring(0,cursorPos));
                g.drawLine(x+pos,y,x+pos,y+g.getFontMetrics().getHeight());
            }
        }
        public void move(int clickX){
            System.out.println("Click X: "+clickX);
            needsMove = true;
            this.clickX = clickX;
            isDisplayed = true;
            tickCounter = 0;
        }
    }

}
