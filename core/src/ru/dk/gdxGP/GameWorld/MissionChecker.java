package ru.dk.gdxGP.GameWorld;


import com.badlogic.gdx.utils.Timer;


/**
 * Created by Андрей on 23.01.2015.
 */
public class MissionChecker extends Timer {

    private final ru.dk.gdxGP.GameWorld.Mission task;
    private long period;
    private final Timer.Task timerTask=new Timer.Task() {
        @Override
        public void run() {
            if(MissionChecker.this.task.isAchieved()) {
                MissionChecker.this.stop();
                MissionChecker.this.finished=true;
            }
        }
    };

    private boolean finished=false;
    public MissionChecker(ru.dk.gdxGP.GameWorld.Mission task, long period){
        this.task=task;
        this.period=period;
        this.schedule(timerTask, 0, this.period);
    }


    public boolean isFinished() {
        return finished;
    }

}