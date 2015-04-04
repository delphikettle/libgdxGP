package ru.dk.gdxGP.GameWorld;

/**
 * Created by Андрей on 11.01.2015.
 */
public abstract class Task extends Throwable {
    private boolean isAchieved =false,once;
    private String taskText;
    public Task(){
        this(true,"");
    }
    protected Task(boolean once, String taskText){
        this.once = once;
        this.taskText = taskText;
    }
    public final boolean isAchieved(){
        //if(!isAchieved) isAchieved =check();
        if(once){
            if (!isAchieved){
                isAchieved=check();
            }
        }else{
            isAchieved=check();
        }
        return isAchieved;
    }
    public abstract boolean check();
}
