package views;

import components.Layout;
import components.View;
import core.AttributesParser;
import exceptions.InvalidAttributeException;
import org.jdom2.Element;

import java.awt.*;

public class SquareView extends View {
    private Color squareColor;
    public SquareView(Element xml, Layout layout) {
        super(xml, layout);
        AttributesParser attributes = new AttributesParser(xml, layout.getApplication());
        try {
            squareColor = attributes.getColor("color", Color.black);
            System.out.println("Square color: "+squareColor.getRed()+" "+squareColor.getGreen()+" "+squareColor.getBlue());
        } catch (InvalidAttributeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(squareColor);
        g.fillRect(getAbsoluteX(),getAbsoluteY(),getAbsoluteWidth(),getAbsoluteHeight());
    }


    @Override
    public int getContentWidth() {
        return 0;
    }

    @Override
    public int getContentHeight() {
        return 0;
    }
}
