package ru.dk.gdxGP.GameWorld;

/**
 * Created by Андрей on 11.01.2015.
 */
public abstract class Task {
    private boolean isAchieved = false, once;
    private String title = "";
    private String taskText = "";

    private ActionAfterAchievedTask actionAfterAchievedTask;

    protected Task() {
        this(true, "");
    }

    protected Task(boolean once, String title) {
        this.once = once;
        this.title = title;
    }

    public ActionAfterAchievedTask getActionAfterAchievedTask() {
        return actionAfterAchievedTask;
    }

    public void setActionAfterAchievedTask(ActionAfterAchievedTask actionAfterAchievedTask) {
        this.actionAfterAchievedTask = actionAfterAchievedTask;
    }

    public final boolean isAchieved() {
        //if(!isAchieved) isAchieved =check();
        if (once) {
            if (!isAchieved) {
                isAchieved = check();
                if (isAchieved&&this.actionAfterAchievedTask!=null)
                    this.actionAfterAchievedTask.actionAfterAchievedTask(this);
            }
        } else {
            isAchieved = check();
        }
        return isAchieved;
    }

    protected abstract boolean check();

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
