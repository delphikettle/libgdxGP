package ru.dk.gdxGP.GameWorld;


import com.badlogic.gdx.utils.Timer;


/**
 * Created by Андрей on 23.01.2015.
 */
public class MissionChecker extends Timer {

    private final ru.dk.gdxGP.GameWorld.Mission mission;
    private long period;
    private boolean finished = false;
    private final Timer.Task timerTask = new Timer.Task() {
        @Override
        public void run() {
            if (MissionChecker.this.mission != null) {
                if (MissionChecker.this.mission.isAchieved()) {
                    MissionChecker.this.stop();
                    MissionChecker.this.finished = true;
                }
            }
        }
    };

    public MissionChecker(ru.dk.gdxGP.GameWorld.Mission mission, long period) {
        this.mission = mission;
        this.period = period;
        scheduleTask();
    }

    private void scheduleTask() {
        if(!timerTask.isScheduled())
            schedule(timerTask, 0, this.period / 1000.0f);
    }

    public boolean isFinished() {
        return finished;
    }

    public void pause() {
        this.mission.pause();
        timerTask.cancel();
    }

    public void resume() {
        this.mission.resume();
        scheduleTask();
    }

    public Mission getMission() {
        return mission;
    }
}