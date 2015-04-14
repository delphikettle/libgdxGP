package ru.dk.gdxGP.GameWorld;

import java.util.LinkedList;

/**
 * Created by DK on 14.04.2015.
 */
public class Mission extends Task {
    private Task currentTask;
    private final LinkedList<Task> taskList=new LinkedList<Task>();
    public Mission(String title){
        super(true,title);
    }
    public void addTask(Task task){
        this.taskList.add(task);
    }
    @Override
    public boolean check() {
        if(currentTask==null) {
            if (taskList.size() > 0)
                this.currentTask = taskList.pop();
        }
        if(currentTask!=null){
            if(currentTask.isAchieved())
                currentTask=null;
        }

        return (this.taskList.size()==0&&taskList==null);
    }
}
