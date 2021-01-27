package views;

import components.Layout;
import components.View;
import core.AttributesParser;
import exceptions.InvalidAttributeException;
import org.jdom2.Element;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GraphView extends View {
    private int paddingRight;
    private List<Integer> data;
    private int max=0;
    private double pixelPerPoint;
    private double pixelsPerRecord;
    //in case of too much data
    private int purgeIndex=1;

    private Color color;

    public GraphView(Element xml, Layout layout) {
        super(xml, layout);
        AttributesParser attributes = new AttributesParser(xml, layout.getApplication());
        try {
            color = attributes.getColor("color",Color.green);
        } catch (InvalidAttributeException e) {
            e.printStackTrace();
        }
        data = new ArrayList<>();
    }

    public void addData(int points){
        data.add(points);
        //counting max on graph's scale
        if(points>max){
            while(max<points || max%50!= 0){
                max++;
            }
            pixelPerPoint = (getAbsoluteHeight() -1.0)/(double)max;
        }
        countWidth();
        notifyIfNeeded();
    }

    private void countWidth(){
        pixelsPerRecord = (double) (getAbsoluteWidth()-paddingRight) / data.size();
        //in case of too much data to fit:
        if(pixelsPerRecord < 1){
            while (pixelsPerRecord * purgeIndex < 1){
                purgeIndex++;
                System.out.println(purgeIndex+" , "+ data.size()+" , "+pixelsPerRecord);
            }
            pixelsPerRecord = 1;
        }


    }


    @Override
    public void draw(Graphics g){
        if(data.size()>0) {
            //counting scale width
            g.setFont(new Font("arial", Font.PLAIN, 10));
            if(paddingRight != g.getFontMetrics().stringWidth(max+"")+5){
                paddingRight = g.getFontMetrics().stringWidth(max+"")+5;
                countWidth();
                notifyIfNeeded();
            }
            //drawing columns
            g.setColor(color);
            int posX = 1;
            for (int i = 0; i < data.size(); i += purgeIndex) {
                int rectHeight = (int) (data.get(i) * pixelPerPoint);
                int rectWidth = (int) pixelsPerRecord;
                g.fillRect(getAbsoluteX() + posX, getAbsoluteY() + getAbsoluteHeight() - rectHeight, rectWidth, rectHeight);
                posX += pixelsPerRecord;
            }

            //drawing grid
            g.setColor(Color.GRAY);
            g.drawRect(getAbsoluteX(), getAbsoluteY(), getAbsoluteWidth() - paddingRight, getAbsoluteHeight());
            g.drawLine(getAbsoluteX(), getAbsoluteY() + (getAbsoluteHeight() / 4), getAbsoluteX() + getAbsoluteWidth() - paddingRight, getAbsoluteY() + (getAbsoluteHeight() / 4));
            g.drawLine(getAbsoluteX(), getAbsoluteY() + 2 * (getAbsoluteHeight() / 4), getAbsoluteX() + getAbsoluteWidth() - paddingRight, getAbsoluteY() + 2 * (getAbsoluteHeight() / 4));
            g.drawLine(getAbsoluteX(), getAbsoluteY() + 3 * (getAbsoluteHeight() / 4), getAbsoluteX() + getAbsoluteWidth() - paddingRight, getAbsoluteY() + 3 * (getAbsoluteHeight() / 4));

            //drawing scale
            g.drawString(max+"", getAbsoluteX() + getAbsoluteWidth() - paddingRight + 5, getAbsoluteY() + 7);
            g.drawString(max / 4 * 3 + "", getAbsoluteX() + getAbsoluteWidth() - paddingRight + 5, getAbsoluteY() + (getAbsoluteHeight() / 4) + 5);
            g.drawString(max / 2 + "", getAbsoluteX() + getAbsoluteWidth() - paddingRight + 5, getAbsoluteY() + 2 * (getAbsoluteHeight() / 4) + 5);
            g.drawString(max / 4 + "", getAbsoluteX() + getAbsoluteWidth() - paddingRight + 5, getAbsoluteY() + 3 * (getAbsoluteHeight() / 4) + 5);
            g.drawString(0 + "", getAbsoluteX() + getAbsoluteWidth() - paddingRight + 5, getAbsoluteY() + getAbsoluteHeight());

        }
    }

    public void clearData(){
        data = new ArrayList<>();
        max = 0;
    }

    @Override
    public int getContentWidth() {
        return data.size()+paddingRight;
    }

    @Override
    public int getContentHeight() {
        return max;
    }
}
