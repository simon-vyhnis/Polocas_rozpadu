package views;

import components.Layout;
import components.View;
import org.jdom2.Element;

import java.awt.*;
import java.util.Random;

public class MoleculesView extends View {
    private Color unconvertedColor;
    private Color convertedColor;
    private final Random r;
    private int numberOfConverted, numberOfUnconverted;
    private boolean running;

    Molecule[] molecules;

    public MoleculesView(Element xml, Layout layout) {
        super(xml, layout);
        unconvertedColor = Color.green;
        convertedColor = Color.red;
        r = new Random();
        numberOfConverted = 0;
        numberOfUnconverted = 1000000;
        molecules = new Molecule[numberOfUnconverted];
        for (int i = 0; i < molecules.length; i++){
            molecules[i] = new Molecule();
        }
        running = false;
    }

    @Override
    public void update() {
        super.update();
        if (numberOfUnconverted != 0 && running){
            for (Molecule molecule : molecules) {
                molecule.update();
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.black);
        g.drawRect(getAbsoluteX(),getAbsoluteY(),getAbsoluteHeight(),getAbsoluteWidth());
        for(int i = 0; i < molecules.length; i++){
            molecules[i].draw(g, i);
        }
    }

    @Override
    public int getContentWidth() {
        return 0;
    }

    @Override
    public int getContentHeight() {
        return 0;
    }

    public Color getUnconvertedColor() {
        return unconvertedColor;
    }

    public void setUnconvertedColor(Color unconvertedColor) {
        this.unconvertedColor = unconvertedColor;
    }

    public Color getConvertedColor() {
        return convertedColor;
    }

    public void setConvertedColor(Color convertedColor) {
        this.convertedColor = convertedColor;
    }

    public int getNumberOfConverted() {
        return numberOfConverted;
    }

    public int getNumberOfUnconverted() {
        return numberOfUnconverted;
    }

    public int getTotalMolecules(){
        return molecules.length;
    }

    private class Molecule {
        private boolean converted = false;

        public void update(){
            if(!converted){
                converted = r.nextBoolean();
                if (converted){
                    numberOfConverted++;
                    numberOfUnconverted--;
                }
            }
        }
        public void draw (Graphics g, int pos){
            if(converted) {
                g.setColor(convertedColor);
            }else {
                g.setColor(unconvertedColor);
            }
            g.fillRect(getAbsoluteX()+1+pos%(getAbsoluteWidth()-2),getAbsoluteY()+1+pos/(getAbsoluteWidth()-2),1,1);
        }
    }

    public void run(){
        running = true;
        numberOfConverted = 0;
        numberOfUnconverted = 1000000;
        for (int i = 0; i < molecules.length; i++){
            molecules[i] = new Molecule();
        }
    }
    public void stopRunning(){
        running = false;
    }
}
