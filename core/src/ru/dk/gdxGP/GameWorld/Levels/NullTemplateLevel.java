package ru.dk.gdxGP.GameWorld.Levels;

import ru.dk.gdxGP.GameWorld.Level;
import ru.dk.gdxGP.GameWorld.Mission;

public class NullTemplateLevel extends Level {


    @Override
    public void setSizes() {

    }

    @Override
    public void setParticles() {

    }

    @Override
    public Mission createMission() {
        return null;
    }

}
