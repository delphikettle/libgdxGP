package ru.dk.gdxGP.GameWorld.Tasks;

import com.badlogic.gdx.math.Vector2;
import ru.dk.gdxGP.GameWorld.Component;
import ru.dk.gdxGP.GameWorld.Task;

/**
 * Created by Андрей on 07.02.2015.
 */
public class TaskOnCoordinate extends Task{
    TaskOnCoordinate(Component c,Vector2 vector2){

    }

    @Override
    public boolean check() {
        return false;
    }
}