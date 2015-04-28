package ru.dk.gdxGP;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

public class AudioPlayer {
    private static Music backgroundMusic;
    private static ArrayList<Sound> bounceSounds;
    static{
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/back01.mp3"));
        bounceSounds=new ArrayList<Sound>();
        for (int i = 1; i <= 4; i++) {
            bounceSounds.add(Gdx.audio.newSound(Gdx.files.internal("audio/bounce" + i + ".ogg")));
        }
    }
    public static void startPlayBackground(){
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }
    public static void pauseBackground(){
        backgroundMusic.pause();
    }
    public static void resumeBackground(){
        backgroundMusic.play();
    }
    public static void playRandomBounce(){
        try {
            int i=MathUtils.random(0, 3);
            long id=  bounceSounds.get(i).play();
            bounceSounds.get(i).setVolume(id,0.25f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
