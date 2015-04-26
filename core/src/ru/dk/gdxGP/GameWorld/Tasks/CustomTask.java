package ru.dk.gdxGP.GameWorld.Tasks;

import ru.dk.gdxGP.GameWorld.InterfacesForActions.CheckerForTask;
import ru.dk.gdxGP.GameWorld.Task;

/**
 * Created by STUDENT_7 on 23.04.2015.
 */
public class CustomTask extends Task {
    public CheckerForTask getChecker() {
        return checker;
    }

    public void setChecker(CheckerForTask checker) {
        this.checker = checker;
    }

    private CheckerForTask checker;
    public CustomTask(CheckerForTask checker){
        this.checker=checker;
    }
    @Override
    protected boolean check() {
        return checker.checkTask(this);
    }
}