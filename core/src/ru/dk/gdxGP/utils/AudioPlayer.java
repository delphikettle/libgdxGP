package ru.dk.gdxGP.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import ru.dk.gdxGP.Settings;

import java.util.ArrayList;

public final class AudioPlayer {
    private static Music backgroundMusic;
    private static ArrayList<Sound> bounceSounds;

    private AudioPlayer() {
    }

    static {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/back01.mp3"));
        bounceSounds = new ArrayList<Sound>();
        for (int i = 1; i <= 4; i++) {
            bounceSounds.add(Gdx.audio.newSound(Gdx.files.internal("audio/bounce" + i + ".ogg")));
        }
    }

    /**
     * Starts playing background music
     */
    public static void startPlayBackground() {
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(Settings.getCurrentSettings().getMusicVolume());
        backgroundMusic.play();
    }

    /**
     * Pauses playing background music
     */
    public static void pauseBackground() {
        backgroundMusic.pause();
    }

    /**
     * Resumes playing background music
     */
    public static void resumeBackground() {
        backgroundMusic.play();
    }

    /**
     * Plays random bounce sound
     */
    public static void playRandomBounce() {
        try {
            int i = MathUtils.random(0, 3);
            bounceSounds.get(i).play(0.05f*Settings.getCurrentSettings().getSoundVolume());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
