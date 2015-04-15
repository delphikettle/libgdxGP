package ru.dk.gdxGP.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;


public class LoadingScreen extends Stage implements Screen {

    private final LogoScreen logoScreen;
    private boolean active;
    private final Screen nextScreen;
    private final LoaderForLoadingScreen loader;
    private TextureRegion texture;
    private float rotation;
    private float width, height;
    private final SpriteBatch spriteBatch;
    public LoadingScreen(LogoScreen logoScreen, LoaderForLoadingScreen loader, Screen nextScreen) {
        this.logoScreen = logoScreen;
        this.loader = loader;
        this.nextScreen = nextScreen;
        this.spriteBatch = new SpriteBatch();
        texture = new TextureRegion(new Texture("images/loadBall.png"));
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    public Screen getNextScreen() {
        return nextScreen;
    }

    public LogoScreen getLogoScreen() {
        return logoScreen;
    }

    @Override
    public void show() {
        this.setActive(true);
        loader.startLoad();
        if (logoScreen != null) {
            width = height = 1.1f * logoScreen.getHeight();
        } else {
            width = height = 0x0.FAP0f * ((Gdx.graphics.getHeight() > Gdx.graphics.getWidth()) ? Gdx.graphics.getWidth() : Gdx.graphics.getHeight());
        }
        texture = new TextureRegion(new Texture("images/loadBall.png"));
    }

    @Override
    public void render(float delta) {
        if (logoScreen != null)
            logoScreen.render(delta);

        spriteBatch.begin();
        spriteBatch.draw(texture,
                (Gdx.graphics.getWidth() - width) / 2, (Gdx.graphics.getHeight() - height) / 2,
                width / 2, height / 2,
                width, height, 1, 1, rotation);
        spriteBatch.end();
        if (loader.ifProgressMustBeShown()) {
            rotation = (-360 * loader.getProgress() + 5 * rotation) / 6;
        } else {
            rotation += -1f;
        }
        if (loader.isLoaded()) this.setActive(false);
        //System.out.println("LoadingScreen drawn");
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

    public interface LoaderForLoadingScreen {
        public void startLoad();

        public boolean isLoaded();

        public float getProgress();

        public boolean ifProgressMustBeShown();
    }

}
