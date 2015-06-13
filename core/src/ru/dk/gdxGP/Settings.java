package ru.dk.gdxGP;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

public class Settings {
    public static float getMusicVolume() {
        return musicVolume;
    }

    public static void setMusicVolume(float musicVolume) {
        Settings.musicVolume = MathUtils.clamp(musicVolume,0,1);
    }

    public static float getSoundVolume() {
        return soundVolume;
    }

    public static void setSoundVolume(float soundVolume) {
        Settings.soundVolume = MathUtils.clamp(soundVolume,0,1);
    }

    private static float musicVolume=1, soundVolume=1;
}
