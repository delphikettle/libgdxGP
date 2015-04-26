package ru.dk.gdxGP.GameWorld.Interfaces;


import com.badlogic.gdx.graphics.Camera;
import ru.dk.gdxGP.GameWorld.Level;
import ru.dk.gdxGP.Screens.LevelScreen;

/**
 * Created by DK on 17.04.2015.
 */
public interface CameraPositionChanger {
    public void changeCameraPosition(Level level, Camera camera, LevelScreen screen);
}
