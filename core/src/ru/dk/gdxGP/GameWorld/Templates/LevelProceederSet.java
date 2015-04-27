package ru.dk.gdxGP.GameWorld.Templates;

import ru.dk.gdxGP.GameWorld.Interfaces.Actions.LevelProceeder;
import ru.dk.gdxGP.GameWorld.Level;

public final class LevelProceederSet {
    static final public LevelProceeder noneProceed = new LevelProceeder() {
        @Override
        public void proceed(Level level, float delta) {

        }
    };
}
