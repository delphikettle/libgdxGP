package ru.dk.gdxGP;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import ru.dk.gdxGP.GameWorld.GameLevels;
import ru.dk.gdxGP.GameWorld.Level;
import ru.dk.gdxGP.Screens.LevelScreen;
import ru.dk.gdxGP.Screens.LoadingScreen;
import ru.dk.gdxGP.Screens.LogoScreen;

public class GDXGameGP extends Game implements GestureDetector.GestureListener, InputProcessor, ApplicationListener {
    static public final AssetManager assetManager = new AssetManager();
    private SpriteBatch batch;
    Texture img;
    Level lvl;
    private State state = State.logo;
    private Screen screen;
    private String levelName;

    public GDXGameGP(String levelName) {
        this.levelName = levelName;
    }

    @Override
    public void create() {
        this.screen = new LogoScreen(1);
        screen.show();

        batch = new SpriteBatch();
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(this);
        inputMultiplexer.addProcessor(new GestureDetector(this));
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (screen != null) screen.render(Gdx.graphics.getDeltaTime());
        switch (state) {
            case logo:
                assert (screen != null);
                if (!((LogoScreen) screen).isActive()) {
                    this.state = State.loading;
                    final Level level = GameLevels.instantiateLevel(levelName);
                    final LevelScreen levelScreen = new LevelScreen(level, 0.1f * Gdx.graphics.getWidth(), 0.1f * Gdx.graphics.getHeight());
                    this.screen = new LoadingScreen((LogoScreen) screen, new LoadingScreen.LoaderForLoadingScreen() {

                        @Override
                        public void startLoad() {
                            level.load(levelScreen);
                        }

                        @Override
                        public boolean isLoaded() {
                            if (level.getLoaded() >= 1.0f) level.start();
                            return level.getLoaded() >= 1.0f;
                        }

                        @Override
                        public float getProgress() {
                            return level.getLoaded();
                        }

                        @Override
                        public boolean ifProgressMustBeShown() {
                            return true;
                        }
                    }, levelScreen);
                    this.screen.show();
                }
                break;
            case loading:
                if (this.screen instanceof LoadingScreen)
                    if (!((LoadingScreen) this.screen).isActive()) {
                        this.screen = ((LoadingScreen) screen).getNextScreen();
                        this.screen.show();
                    }

                break;
            case Game:
                break;
        }
    }

    @Override
    public void resize(int width, int height) {
        if (Gdx.app.getType() != Application.ApplicationType.Android)
            screen.resize(width, height);
    }

    @Override
    public void pause() {
        screen.pause();
    }

    @Override
    public void resume() {
        screen.resume();
    }

    @Override
    public void dispose() {
        screen.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if (screen instanceof LevelScreen) {
            ((LevelScreen) screen).tap(x, y);
        }

        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return screen instanceof LevelScreen && ((LevelScreen) screen).zoom(initialDistance, distance);
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (screen instanceof LevelScreen) {
            ((LevelScreen) screen).setInitialScale(((LevelScreen) screen).getCameraZoom());
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (screen instanceof LevelScreen) {
            ((LevelScreen) screen).drag(-Gdx.input.getDeltaX(pointer), Gdx.input.getDeltaY(pointer));
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        if (screen instanceof LevelScreen) {
            ((LevelScreen) screen).setInitialScale(((LevelScreen) screen).getCameraZoom());
            ((LevelScreen) screen).zoom(((LevelScreen) screen).getZoom(), (float) (((LevelScreen) screen).getZoom() * Math.pow(1.1f, -amount)));
        }
        return false;
    }

    public enum State {
        logo, loading, MainMenu, SelectLevel, Game, Pause
    }


}
