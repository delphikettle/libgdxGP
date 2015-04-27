package ru.dk.gdxGP.GameWorld;


import com.badlogic.gdx.utils.Timer;


public class MissionChecker extends Timer {

    private final ru.dk.gdxGP.GameWorld.Mission mission;
    private long period;
    private final Timer.Task timerTask = new Timer.Task() {
        @Override
        public void run() {
            if (MissionChecker.this.mission != null) {
                MissionChecker.this.mission.isAchieved();
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