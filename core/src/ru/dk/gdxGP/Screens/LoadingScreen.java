package ru.dk.gdxGP.Screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import ru.dk.gdxGP.TextureKeeper;

public class LoadingScreen extends Stage implements Screen {
    private LogoScreen logoScreen;
    private Thread thread;
    private char[] textures;
    private boolean active;
    public LoadingScreen(LogoScreen logoScreen, char[] textures){
        this.logoScreen=logoScreen;
        this.textures=textures;
    }
    private void setActive(boolean active) {
        this.active=active;
    }
    public boolean isActive() {
        return active;
    }
    @Override
    public void show() {
        this.setActive(true);
        this.thread=new Thread(new Runnable() {
            @Override
            public void run() {
                TextureKeeper.loadTextures(textures);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setActive(false);
            }
        });
        thread.start();
    }

    @Override
    public void render(float delta) {
        this.logoScreen.render(delta);
        this.logoScreen.rotate(-1);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
