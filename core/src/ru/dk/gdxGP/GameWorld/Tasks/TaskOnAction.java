package ru.dk.gdxGP.GameWorld.Tasks;

import ru.dk.gdxGP.GameWorld.Task;

public class TaskOnAction extends Task {
    private boolean completed = false;

    public TaskOnAction() {
        super();
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    protected boolean check() {
        return completed;
    }
}
