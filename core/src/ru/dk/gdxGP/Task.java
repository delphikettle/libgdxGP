package ru.dk.gdxGP;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Андрей on 11.01.2015.
 */
public abstract class Task extends Throwable {
    boolean isEnded=false;
    public Task(){
        isEnded=false;
    }
    public final boolean isEnded(){
        if(!isEnded)isEnded=check();
        return isEnded;
    }
    public abstract boolean check();

}
