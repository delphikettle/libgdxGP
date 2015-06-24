package ru.dk.gdxGP;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Settings {
    private float mV,sV;
    private Settings(){
        this.mV=musicVolume;
        this.sV=soundVolume;
    }
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

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
    public static void save(){
        System.out.println(new Settings().toString());
    }
}
