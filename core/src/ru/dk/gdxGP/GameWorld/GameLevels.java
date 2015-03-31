package ru.dk.gdxGP.GameWorld;

import ru.dk.gdxGP.GameWorld.Levels.TestLevel;
import ru.dk.gdxGP.GameWorld.Levels.TestLevel01;
import ru.dk.gdxGP.GameWorld.Levels.UnnamedLevel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by DK on 31.03.2015.
 */
public class GameLevels {
    @SuppressWarnings("unchecked")
    public static final List<Class<? extends Level>> LEVELS = new ArrayList<Class<? extends Level>>(
            Arrays.asList(
                    TestLevel.class,
                    TestLevel01.class,
                    UnnamedLevel.class
            )
    );
    public static List<String> getNames(List<Class<? extends Level>> levels,
                                          boolean sorted) {
        List<String> names = new ArrayList<String>(levels.size());

        for (Class<? extends Level> level : levels) {
            names.add(level.getSimpleName());
        }

        if (sorted) {
            Collections.sort(names);
        }

        return names;
    }
    public static Level instantiateLevel(List<Class<? extends Level>> levels,
                                         String levelName) {
        try {
            return forName(levels, levelName).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<? extends Level> forName(
            List<Class<? extends Level>> levels, String levelName) {

        Class<? extends Level> requestedClass = null;

        for (Class<? extends Level> level : levels) {
            if (level.getSimpleName().equals(levelName)) {
                requestedClass = level;
            }
        }

        return requestedClass;
    }

}