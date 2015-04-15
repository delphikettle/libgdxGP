package ru.dk.gdxGP.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.Timer;
import java.util.TimerTask;

public class LogoScreen extends Stage implements Screen {
    private TextureRegion texture;
    private float width, height;
    private SpriteBatch spriteBatch;
    private final float time;
    private float rotation;
    private boolean active;
    public LogoScreen(float time) {
        this.time = time;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void show() {
        texture = new TextureRegion(new Texture("images/logo.png"));
        width = height = 0x0.FAP0f * ((Gdx.graphics.getHeight() > Gdx.graphics.getWidth()) ? Gdx.graphics.getWidth() : Gdx.graphics.getHeight());
        spriteBatch = new SpriteBatch();
        this.render(Gdx.graphics.getDeltaTime());
        this.setActive(true);
        Timer timer2 = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                setActive(false);
            }
        };
        timer2.schedule(task, (long) (time * 1000));
    }


    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    @Override
    public void render(float delta) {
        if (active) this.width = this.height = this.height * 0.99f;
        spriteBatch.begin();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.draw(texture,
                (Gdx.graphics.getWidth() - width) / 2, (Gdx.graphics.getHeight() - height) / 2,
                width / 2, height / 2,
                width, height, 1, 1, rotation);
        spriteBatch.end();
    }

    public void rotate(float rotation) {
        this.rotation += rotation;
    }

    public boolean isActive() {
        return active;
    }

    private void setActive(boolean active) {
        this.active = active;
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
