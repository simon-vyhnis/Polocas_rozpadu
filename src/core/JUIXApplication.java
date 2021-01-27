package core;

import com.sun.istack.internal.NotNull;
import components.*;
import exceptions.InvalidPartException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class JUIXApplication implements MouseListener, KeyListener, MouseMotionListener {
    private JFrame frame;
    private JPanel panel;
    private Canvas canvas;

    private Part currentPart;

    private final double ticksPerSecond = 0.6;

    private List<UpdateReceiver> receivers;
    private JUIXColors colors;

    /**
     * If you do not need anything else to to define than name and start components.Part than use this constructor.
     */
    public JUIXApplication(@NotNull String name, @NotNull Class<?> startPartClass ){
        frame = new JFrame();
        panel = new JPanel();
        canvas = new Canvas();
        receivers = new ArrayList<>(0);
        colors = new JUIXColors(getClass().getClassLoader().getResource("configFiles/colors.xml"));
        colors.changeColor("primaryColor", Color.red);
        frame.setTitle(name);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        canvas.addMouseListener(this);
        canvas.addKeyListener(this);
        canvas.addMouseMotionListener(this);
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                if(currentPart!=null)
                    currentPart.notifyWindowResized();
                canvas.setBounds(0,0,panel.getWidth(),panel.getHeight());
            }
        });
        panel.setPreferredSize(new Dimension(800,500));
        panel.setLayout(null);
        panel.setVisible(true);
        frame.setContentPane(panel);
        frame.add(canvas);
        canvas.setBounds(0,0,panel.getWidth(),panel.getHeight());
        Color background = colors.getColor("backgroundColor");
        if(background != null)
            canvas.setBackground(background);
        setIcon();

        panel.validate();
        frame.pack();
        frame.setVisible(true);
        try {
            currentPart = createPart(startPartClass);
        } catch (InvalidPartException e) {
            e.printStackTrace();
        }
        createLooper();

    }

    void draw(){
        BufferStrategy strategy = canvas.getBufferStrategy();
        if(strategy==null){
            canvas.createBufferStrategy(2);
            return;
        }
        Graphics g = strategy.getDrawGraphics();
        canvas.paint(g);
        currentPart.draw(g);
        g.dispose();
        strategy.show();
    }

    void update(int tick){
        currentPart.update(tick);
        for(UpdateReceiver receiver:receivers){
            if(receiver==null){
                receivers.remove(receiver);
            }else{
                receiver.onReceive();
            }
        }
    }

    private void createLooper(){
        Thread thread = new Thread(() -> {
                    Cycle cycle = new Cycle(this);
                    cycle.start();
                }, "JUIX Thread");
     thread.start();
    }

    /**
     * Use this constructor if you want to build JUIX application with config file.
     * A config file have to contain at least name and start part. Learn more about config files <a>here<a/>.
     */
    public JUIXApplication(JUIXFile configFile){
        //TODO: create this
    }

    /*
     * Returns the JFrame, that the app is displayed on.
     * You should not access the frame manually, but if you are sure, that there isn't other way, than use this method.
     */
    public JFrame getFrame(){
        return frame;
    }
    public JPanel getPanel(){
        return panel;
    }
    public Canvas getCanvas(){
        return canvas;
    }
    public JUIXColors getColors(){
        return colors;
    }
    /*
     * Returns tick per second, what is the app currently running.
     */
    public double getTicksPerSecond() {
        return ticksPerSecond;
    }

    public void switchPart(Class<?> destination){


    }

    public void registerUpdateReceiver(UpdateReceiver receiver){
        receivers.add(receiver);
    }

    private Part createPart(Class<?> partClass) throws InvalidPartException {
        try {
            Constructor<?> constructor =  partClass.getConstructor(JUIXApplication.class);
            return (Part) constructor.newInstance(this);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            throw new InvalidPartException();
        }
    }

    private void setIcon(){
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png")));
    }

    public void setCursor(Cursor cursor){
        frame.setCursor(cursor);
    }

    public int getWindowWidth(){
        return panel.getWidth();
    }
    public int getWindowHeight(){
        return panel.getHeight();
    }

    //MouseListener methods
    @Override
    public void mouseClicked(MouseEvent e) {
        if(currentPart!=null)
            currentPart.onClick(e);
    }
    @Override
    public void mousePressed(MouseEvent e) {

    }
    @Override
    public void mouseReleased(MouseEvent e) {

    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mouseExited(MouseEvent e) {

    }

    //KeyListener methods
    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if(currentPart!=null)
            currentPart.keyTyped(e);

    }
    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }
    @Override
    public void mouseMoved(MouseEvent e) {
        if(currentPart!=null)
            currentPart.mouseMove(e);
    }

}