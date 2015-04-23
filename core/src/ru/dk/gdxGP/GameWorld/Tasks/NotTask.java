package ru.dk.gdxGP.GameWorld.Tasks;

import ru.dk.gdxGP.GameWorld.Task;

/**
 * Created by STUDENT_7 on 23.04.2015.
 */
public class NotTask extends Task {
    private Task notTask;
    public NotTask(Task notTask){
        super(false);
        this.notTask=notTask;
    }
    @Override
    protected boolean check() {
        System.out.println("checking "+this.notTask.isAchieved());
        return !this.notTask.isAchieved();
    }
}
