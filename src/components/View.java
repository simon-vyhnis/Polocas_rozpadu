package components;

import core.JUIXApplication;
import org.jdom2.Attribute;
import org.jdom2.Element;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public abstract class View{
    private final String id;

    private String rawWidth;
    private String rawHeight;
    private String rawX;
    private String rawY;
    private int width = -1;
    private int height = -1;
    private int x = -1;
    private int y = -1;

    private boolean isDragged;

    protected final Layout layout;

    private final List<OnClickListener> onClickListeners;


    public View(Element xml, Layout layout){
       rawHeight = xml.getAttributeValue("height");
       rawWidth = xml.getAttributeValue("width");
       rawX = xml.getAttributeValue("x");
       rawY = xml.getAttributeValue("y");
       id = xml.getAttributeValue("id");
       this.layout = layout;
       System.out.println("VIEW: View created "+id);

       onClickListeners = new ArrayList<>(1);
    }

    /*
     * This method is called every time, when the canvas is redrawn
     */
    public abstract void draw(Graphics g);
    /*
     * This method is called every tick
     */
    public void update(){

    }

    public void onClick(MouseEvent e){
        for (OnClickListener listener : onClickListeners) {
            listener.onClick(this);
        }
        System.out.println("VIEW: View clicked: "+id);
    }

    public void onCursorEnter(){
        isDragged = true;
    }

    public void onCursorExit(){
        isDragged = false;
    }

    public void addOnClickListener(OnClickListener listener){
        onClickListeners.add(listener);
    }

    public void removeOnClickListener(OnClickListener listener){
        onClickListeners.remove(listener);
    }

    public String getId(){
        return id;
    }

    public String getRawWidth(){
        return rawWidth;
    }
    public String getRawHeight(){
        return rawHeight;
    }
    public String getRawX(){
        return rawX;
    }
    public String getRawY() {
        return rawY;
    }

    public void setWidth(String rawWidth) {
        this.rawWidth = rawWidth;
        layout.notifyViewsChanged();
    }
    public void setHeight(String rawHeight) {
        this.rawHeight = rawHeight;
        layout.notifyViewsChanged();
    }
    public void setX(String rawX) {
        this.rawX = rawX;
        layout.notifyViewsChanged();
    }
    public void setY(String rawY) {
        this.rawY = rawY;
        layout.notifyViewsChanged();
    }


    public int getAbsoluteWidth() {
        return width;
    }
    public int getAbsoluteHeight() {
        return height;
    }
    public int getAbsoluteX() {
        return x;
    }
    public int getAbsoluteY() {
        return y;
    }

    public void setAbsoluteWidth(int width){
        this.width = width;
    }
    public void setAbsoluteHeight(int height){
        this.height = height;
    }
    public void setAbsoluteX(int x){
        this.x = x;
    }
    public void setAbsoluteY(int y){
        this.y = y;
    }
    public void setAbsolutePosition(int x, int y){
        this.x = x;
        this.y = y;
    }
    public void setAbsoluteSize(int width, int height){
        this.width = width;
        this.height = height;
    }

    public abstract int getContentWidth();
    public abstract int getContentHeight();

    protected void notifyIfNeeded(){
        if(getRawHeight().equals("content") || getRawWidth().equals("content")){
            System.out.println(getClass().getName() + " notifying layout...");
            layout.notifyViewsChanged();
        }
    }


}
