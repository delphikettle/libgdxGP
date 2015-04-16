package ru.dk.gdxGP.GameWorld.Tasks;

import com.badlogic.gdx.utils.Timer;
import ru.dk.gdxGP.GameWorld.Task;

/**
 * Created by STUDENT_7 on 16.04.2015.
 */
public class TimeTask extends Task {
    private boolean finished=false;
    private float time;
    private Timer stepTimer;
    public TimeTask(float time){
        super();
        this.time=time;
    }
    public void start(){
        stepTimer=new Timer();
        stepTimer.start();
        stepTimer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                TimeTask.this.finished=true;
            }
        }, time/1000f);
    }
    @Override
    protected boolean check() {
        return finished;
    }
}
