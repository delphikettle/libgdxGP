package ru.dk.gdxGP.GameWorld.Templates;

import ru.dk.gdxGP.GameWorld.Level;
import ru.dk.gdxGP.GameWorld.LevelProceeder;

/**
 * Created by DK on 16.04.2015.
 */
public final class LevelProceederSet {
    static final public LevelProceeder noneProceed=new LevelProceeder() {
        @Override
        public void proceed(Level level, float delta) {

        }
    };
}