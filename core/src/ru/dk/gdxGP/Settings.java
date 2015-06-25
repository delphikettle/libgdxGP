package ru.dk.gdxGP;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.File;

public class Settings {
    static{
        load();
    }
    public static Settings getCurrentSettings() {
        load();
        return currentSettings;
    }

    public static void setCurrentSettings(Settings currentSettings) {
        Settings.currentSettings = currentSettings;
        save();
    }

    private static Settings currentSettings;
    public float getMusicVolume() {
        return musicVolume;
    }

    public static Settings load(){
        try {
            FileHandle file = Gdx.files.internal("data/settings.json");
            if(file.exists()){
                currentSettings = new Gson().fromJson(file.readString(),Settings.class);
            } else {
                currentSettings = new Settings();
            }
        } catch (NullPointerException e) {
            
        }
        return currentSettings;
    }
    public static void save(){
        FileHandle file = Gdx.files.internal("data/settings.json");
        if(file.exists()){
            file.delete();
        } else {
            file.writeString(currentSettings.toString(),false);
        }
    }
    public void setMusicVolume(float musicVolume) {
        this.musicVolume = MathUtils.clamp(musicVolume,0,1);
        save();
    }

    public Settings(float musicVolume, float soundVolume, boolean isDebug) {
        this.musicVolume = musicVolume;
        this.soundVolume = soundVolume;
        this.isDebug = isDebug;
    }

    public Settings(){
        musicVolume = 1;
        soundVolume = 1;
        isDebug = false;
    }
    public float getSoundVolume() {
        return soundVolume;

    }

    public  void setSoundVolume(float soundVolume) {
        this.soundVolume = MathUtils.clamp(soundVolume,0,1);
        save();
    }

    private float musicVolume, soundVolume;
    private boolean isDebug;

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
        save();
    }


    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

}
