package ru.dk.gdxGP.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import ru.dk.gdxGP.AtlasLoader;
import ru.dk.gdxGP.GDXGameGP;


public class LoadingScreen extends Stage implements Screen {

    private final LogoScreen logoScreen;
    private final Screen nextScreen;
    private final LoaderForLoadingScreen loader;
    private final SpriteBatch spriteBatch;
    private boolean active;
    private TextureRegion texture;
    private float rotation;
    private float width, height;

    public LoadingScreen(LogoScreen logoScreen, LoaderForLoadingScreen loader, Screen nextScreen) {
        this.logoScreen = logoScreen;
        this.loader = loader;
        this.nextScreen = nextScreen;
        this.spriteBatch = new SpriteBatch();
        texture = AtlasLoader.getRegion("loadBall");
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
        texture = AtlasLoader.getRegion("loadBall");
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
        if (loader.isLoaded()) {
            this.setActive(false);
            GDXGameGP.currentGame.setScreen(nextScreen);
        }
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
