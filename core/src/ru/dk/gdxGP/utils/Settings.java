package ru.dk.gdxGP.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Settings {
    private static Settings currentSettings;
    static{
        if(currentSettings==null){
            currentSettings=new Settings();
        }
    }
    public static Settings getCurrentSettings() {
        return currentSettings;
    }

    public static void setCurrentSettings(Settings currentSettings) {
        Settings.currentSettings = currentSettings;
        save();
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public static Settings load(){
        FileHandle file = Gdx.files.local("settings.json");
        if(file.exists()){
            currentSettings = new Gson().fromJson(file.readString(),Settings.class);
        } else {
            currentSettings = new Settings();
        }
        return currentSettings;
    }
    public static void save(){
        FileHandle file = Gdx.files.local("settings.json");
        if(file.exists()){
            file.delete();
        }
        file.writeString(currentSettings.toString(),false);
    }
    public void setMusicVolume(float musicVolume) {
        this.musicVolume = MathUtils.clamp(musicVolume,0,1);
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
    }

    private float musicVolume, soundVolume;
    private boolean isDebug;

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }


    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

}
