package ru.dk.gdxGP.GameWorld.Templates;

import ru.dk.gdxGP.GameWorld.InterfacesForActions.ActionForNextStep;
import ru.dk.gdxGP.GameWorld.Level;

public final class ActionForNextStepSet {
    public static final ActionForNextStep moveAction = new ActionForNextStep() {
        @Override
        public void doSomethingOnStep(Level level) {
            level.Move(16);
        }
    };
}
