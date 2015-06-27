package ru.dk.gdxGP.GameWorld;

import ru.dk.gdxGP.GameWorld.Levels.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameLevels {
    @SuppressWarnings("unchecked")
    public static final List<Class<? extends Level>> LEVELS = new ArrayList<Class<? extends Level>>(
            Arrays.asList(
                    ChargeAllLevel.class,
                    ChargeYourFriendLevel.class
            )
    );
    public static final List<Class<? extends Level>> TUTORIALS = new ArrayList<Class<? extends Level>>(
            Arrays.asList(
                    ControlTutorialLevel.class,
                    ForcesTutorialLevel.class,
                    InteractionTutorialLevel.class
            )
    );

    /**
     * Returns the list of levels
     *
     * @param sorted if levels must be sorted by names
     * @return {@link java.util.ArrayList} of String with levels' names
     */
    public static List<String> getLevelsNames(boolean sorted) {
        List<String> names = new ArrayList<String>(GameLevels.LEVELS.size());

        for (Class<? extends Level> level : GameLevels.LEVELS) {
            names.add(level.getSimpleName());
        }

        if (sorted) {
            Collections.sort(names);
        }

        return names;
    }
    /**
     * Returns the list of levels
     *
     * @param sorted if levels must be sorted by names
     * @return {@link java.util.ArrayList} of String with levels' names
     */
    public static List<String> getTutorialsNames(boolean sorted) {
        List<String> names = new ArrayList<String>(GameLevels.TUTORIALS.size());

        for (Class<? extends Level> level : GameLevels.TUTORIALS) {
            names.add(level.getSimpleName());
        }

        if (sorted) {
            Collections.sort(names);
        }

        return names;
    }

    /**
     * Return the instance of {@link ru.dk.gdxGP.GameWorld.Level} from given name
     *
     * @param levelName name of the level that must be given
     * @return instance of level or null if level with given name is not exist
     */
    public static Level instantiateLevel(String levelName) {
        try {
            return forName(GameLevels.LEVELS, levelName).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            return forName(GameLevels.TUTORIALS, levelName).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Return the class extends {@link ru.dk.gdxGP.GameWorld.Level} from given name
     *
     * @param levels    list of levels' classes
     * @param levelName name of the level' class that must be given
     * @return class of level
     */
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
