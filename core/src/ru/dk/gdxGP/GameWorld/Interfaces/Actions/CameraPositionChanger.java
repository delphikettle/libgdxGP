package ru.dk.gdxGP.GameWorld.Interfaces.Actions;


import com.badlogic.gdx.graphics.Camera;
import ru.dk.gdxGP.GameWorld.Level;
import ru.dk.gdxGP.Screens.LevelScreen;

public interface CameraPositionChanger {
    public void changeCameraPosition(Level level, Camera camera, LevelScreen screen);
}
