package core;

import components.UpdateReceiver;

import java.util.List;

public class Cycle{
    private boolean isRunning;
    private final JUIXApplication application;

    public Cycle(JUIXApplication application) {
        this.application= application;
        isRunning=true;
    }

    public void start(){
        //initialization
        double nanoPerTick = 1000000000/(float)application.getTicksPerSecond();
        double unprocessedTicks=0;
        double lastNano=System.nanoTime();
        int tickCounter=0;
        //Main loop
        while(isRunning){
            double nowNano=System.nanoTime();
            unprocessedTicks+=(nowNano-lastNano)/nanoPerTick;
            lastNano=nowNano;
            //Ticks processing
            while(unprocessedTicks >1){
                application.update(tickCounter);
                tickCounter++;
                unprocessedTicks--;
            }

            //Drawing
            application.draw();

        }

    }

    public void stop(){
        isRunning=false;
    }
}
