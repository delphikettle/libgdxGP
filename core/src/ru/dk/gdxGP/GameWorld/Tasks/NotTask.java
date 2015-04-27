package ru.dk.gdxGP.GameWorld.Tasks;

import ru.dk.gdxGP.GameWorld.Task;

public class NotTask extends Task {
    private Task notTask;
    public NotTask(Task notTask){
        super(false);
        this.notTask=notTask;
    }
    @Override
    protected boolean check() {
        return !this.notTask.isAchieved();
    }

    @Override
    public void pause() {
        this.notTask.pause();
    }

    @Override
    public void resume() {
        this.notTask.resume();
    }
}
