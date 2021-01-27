package components;

import core.JUIXApplication;
import core.LayoutParser;
import exceptions.InvalidViewReferenceException;
import exceptions.MissingAttributeException;
import org.jdom2.Attribute;
import org.jdom2.Element;
import views.TextInputView;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Layout extends View {
    protected Map<String, View> views;
    protected JUIXApplication application;
    private TextInputView activeInput;
    private View draggedView;

    public abstract void notifyViewsChanged();

    public Layout(Element xml, Layout layout, LayoutParser parser, JUIXApplication application) {
        super(xml, layout);
        this.application = application;
        try {
            views = parser.parseLayout(xml, this);
        } catch (MissingAttributeException | InvalidViewReferenceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics g) {
        views.forEach((id, view) -> view.draw(g));
    }

    @Override
    public void onClick(MouseEvent e){
        super.onClick(e);
        System.out.println("LAYOUT: "+"Layout clicked");
        if(draggedView != null){
            draggedView.onClick(e);
        }
        if(activeInput != null && activeInput != draggedView) {
            activeInput.stopBeingActive();
            activeInput = null;
        }
    }
    public void keyTyped(KeyEvent e){
        if(activeInput != null){
            activeInput.keyTyped(e);
        }else{
            views.forEach((id, view)->{
               if(view instanceof Layout){
                   ((Layout) view).keyTyped(e);
               }
            });
        }
    }
    public void mouseMove(MouseEvent e){
        if(draggedView != null && !isViewDragged(draggedView ,e)){
            draggedView.onCursorExit();
        }
        draggedView = null;
        views.forEach((id, view)->{
            if(isViewDragged(view,e)){
                draggedView = view;
                draggedView.onCursorEnter();
            }
        });
        if(draggedView == null){
            application.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }

    }

    public boolean isViewDragged(View view, MouseEvent e){
        return view.getAbsoluteX() <= e.getX() &&
                view.getAbsoluteX()+view.getAbsoluteWidth() >= e.getX() &&
                view.getAbsoluteY() <= e.getY() &&
                view.getAbsoluteY()+view.getAbsoluteHeight() >= e.getY();
    }


    public Map<String, View> getViews() {
        return views;
    }

    public View getView(String id) {
        return views.get(id);
    }

    public void setActiveInput(TextInputView view){
        activeInput = view;
    }

    @Override
    public void setX(String rawX) {
        super.setX(rawX);
        notifyViewsChanged();
    }

    @Override
    public void setY(String rawY) {
        super.setY(rawY);
        notifyViewsChanged();
    }
    public JUIXApplication getApplication(){
        return application;
    }
}
