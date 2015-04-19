package ru.dk.gdxGP.GameWorld.Tasks;

import com.badlogic.gdx.utils.Timer;
import ru.dk.gdxGP.GameWorld.Task;

/**
 * Created by STUDENT_7 on 16.04.2015.
 */
public class TimeTask extends Task {
    private boolean finished=false;
    private float time;
    private Timer timeTimer;
    private float timeToFinish;
    private final Timer.Task timeTask= new Timer.Task() {
        @Override
        public void run() {
            TimeTask.this.finished=true;
        }
    };
    public TimeTask(float time){
        super();
        this.time=time;
    }
    public void start(){
        timeTimer = new Timer();
        timeTimer.start();
        timeTimer.scheduleTask(timeTask, time / 1000f);
        this.timeToFinish=timeTask.getExecuteTimeMillis()-System.nanoTime() / 1000000;
    }
    @Override
    protected boolean check() {
        this.timeToFinish=timeTask.getExecuteTimeMillis()-System.nanoTime() / 1000000;
        return finished;
    }

    @Override
    public void pause() {
        this.timeToFinish=timeTask.getExecuteTimeMillis()-System.nanoTime() / 1000000;
        timeTask.cancel();
    }

    @Override
    public void resume() {
        timeTimer.scheduleTask(timeTask, timeToFinish / 1000f);
    }
}
