package ru.dk.gdxGP.GameWorld;

/**
 * Created by Андрей on 11.01.2015.
 */
public abstract class Task extends Throwable {
    private boolean isAchieved =false,once;
    private String title="";

    private String taskText="";
    public Task(){
        this(true,"");
    }
    protected Task(boolean once, String title){
        this.once = once;
        this.title = title;
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

    public String getTaskText() {
        return taskText;
    }

    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }

    public boolean isOnce() {
        return once;
    }

    public void setOnce(boolean once) {
        this.once = once;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
