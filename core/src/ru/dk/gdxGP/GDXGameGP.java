package ru.dk.gdxGP;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import ru.dk.gdxGP.GameWorld.GameLevels;
import ru.dk.gdxGP.GameWorld.Level;
import ru.dk.gdxGP.Screens.LevelScreen;
import ru.dk.gdxGP.Screens.LoadingScreen;
import ru.dk.gdxGP.Screens.LogoScreen;

public class GDXGameGP extends Game implements GestureDetector.GestureListener, InputProcessor, ApplicationListener {
    public static GDXGameGP currentGame;
    public final InputMultiplexer inputMultiplexer;
    private String levelName;
    private Music backgroundMusic;

    public GDXGameGP(String levelName) {
        this.levelName = levelName;
        inputMultiplexer = new InputMultiplexer();
        currentGame = this;
    }

    @Override
    public void create() {
        this.setScreen(new LogoScreen(1));
        inputMultiplexer.addProcessor(this);
        inputMultiplexer.addProcessor(new GestureDetector(this));
        this.backgroundMusic=Gdx.audio.newMusic(Gdx.files.internal("audio/background01.mp3"));
        this.backgroundMusic.setLooping(true);
        this.backgroundMusic.play();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
        if ((getScreen() instanceof LogoScreen) && !((LogoScreen) getScreen()).isActive())
            startGame();
    }

    public void startGame() {
        System.out.println("starting game");
        final Level level = GameLevels.instantiateLevel(levelName);
        final LevelScreen levelScreen = new LevelScreen(level, 0.1f * Gdx.graphics.getWidth(), 0.1f * Gdx.graphics.getHeight());
        this.setScreen(new LoadingScreen((LogoScreen) screen, new LoadingScreen.LoaderForLoadingScreen() {

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
        }, levelScreen));
        Gdx.input.setInputProcessor(inputMultiplexer);

    }

    @Override
    public void resize(int width, int height) {
        if (Gdx.app.getType() != Application.ApplicationType.Android)
            getScreen().resize(width, height);
    }

    @Override
    public void pause() {
        getScreen().pause();
        this.backgroundMusic.pause();
    }

    @Override
    public void resume() {
        getScreen().resume();
        this.backgroundMusic.play();
    }

    @Override
    public void dispose() {
        getScreen().dispose();
        super.dispose();
        System.exit(0);
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if (getScreen() instanceof LevelScreen) {
            ((LevelScreen) getScreen()).tap(x, y);
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
        return getScreen() instanceof LevelScreen && ((LevelScreen) getScreen()).zoom(initialDistance, distance);
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
        if (getScreen() instanceof LevelScreen) {
            ((LevelScreen) getScreen()).setInitialScale(((LevelScreen) getScreen()).getCameraZoom());
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (getScreen() instanceof LevelScreen) {
            ((LevelScreen) getScreen()).drag(-Gdx.input.getDeltaX(pointer), Gdx.input.getDeltaY(pointer));
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        if (getScreen() instanceof LevelScreen) {
            ((LevelScreen) getScreen()).setInitialScale(((LevelScreen) getScreen()).getCameraZoom());
            ((LevelScreen) getScreen()).zoom(((LevelScreen) getScreen()).getZoom(), (float) (((LevelScreen) getScreen()).getZoom() * Math.pow(1.1f, -amount)));
        }
        return false;
    }


}
