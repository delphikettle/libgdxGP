package ru.dk.gdxGP.GameWorld.Tasks;

import com.badlogic.gdx.utils.Timer;
import ru.dk.gdxGP.GameWorld.Task;

public class TimeTask extends Task {
    private boolean finished = false;
    private final Timer.Task timeTask = new Timer.Task() {
        @Override
        public void run() {
            TimeTask.this.finished = true;
        }
    };
    private float time;
    private Timer timeTimer;
    private float timeToFinish;

    public TimeTask(float time) {
        super();
        this.time = time;
    }

    public float getTimeToFinish() {
        return timeToFinish = timeTask.getExecuteTimeMillis() - System.nanoTime() / 1000000;
    }

    public void start() {
        timeTimer = new Timer();
        timeTimer.start();
        timeTimer.scheduleTask(timeTask, time / 1000f);
        this.timeToFinish = timeTask.getExecuteTimeMillis() - System.nanoTime() / 1000000;
    }

    @Override
    protected boolean check() {
        updateTimeToFinish();
        return finished;
    }

    @Override
    public void pause() {
        updateTimeToFinish();
        timeTask.cancel();
    }

    public void updateTimeToFinish() {
        if (timeTask.isScheduled())
            this.timeToFinish = timeTask.getExecuteTimeMillis() - System.nanoTime() / 1000000;
    }

    @Override
    public void resume() {
        timeTask.cancel();
        timeTimer.scheduleTask(timeTask, timeToFinish / 1000f);
        updateTimeToFinish();
    }
}
