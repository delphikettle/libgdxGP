package ru.dk.gdxGP.GameWorld;


import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Андрей on 23.01.2015.
 */
public class TaskChecker extends Timer {

    Task task;
    long period;
    TaskChecker( Task task,long period){
        this.task=task;
        this.period=period;
        this.schedule(new TimerTask() {
            @Override
            public void run() {

            }
        },0,this.period);
    }

}