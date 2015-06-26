package ru.dk.gdxGP;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import ru.dk.gdxGP.GameWorld.GameLevels;
import ru.dk.gdxGP.GameWorld.Level;
import ru.dk.gdxGP.Screens.LevelScreen;
import ru.dk.gdxGP.Screens.LoadingScreen;
import ru.dk.gdxGP.Screens.LogoScreen;
import ru.dk.gdxGP.utils.*;
import ru.dk.gdxGP.utils.Graphics;

public class GDXGameGP extends Game implements ApplicationListener {
    public static GDXGameGP currentGame;
    public final InputMultiplexer inputMultiplexer;
    private String levelName;
    private boolean ifBounceSoundMustBePlayed = false;

    /**
     * Creates game from given levelName
     *
     * @param levelName string name of the level
     */
    public GDXGameGP(String levelName) {
        this.levelName = levelName;
        inputMultiplexer = new InputMultiplexer();
        currentGame = this;
    }

    /**
     * Must be called only by libGDX
     */
    @Override
    public void create() {
        Graphics.load();
        AudioPlayer.load();
        this.setScreen(new LogoScreen(1));
        Settings.save();
        AudioPlayer.startPlayBackground();
    }

    public void playBounceSound() {
        this.ifBounceSoundMustBePlayed = true;
    }

    /**
     * Must be called only by libGDX
     */
    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
        if ((getScreen() instanceof LogoScreen) && !((LogoScreen) getScreen()).isActive())
            startGame();
        if (ifBounceSoundMustBePlayed) {
            ifBounceSoundMustBePlayed = !ifBounceSoundMustBePlayed;
            AudioPlayer.playRandomBounce();
        }
    }

    private void startGame() {
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

    /**
     * Must be called only by libGDX
     */
    @Override
    public void resize(int width, int height) {
        if (Gdx.app.getType() != Application.ApplicationType.Android)
            getScreen().resize(width, height);
    }

    /**
     * Must be called only by libGDX
     */
    @Override
    public void pause() {
        getScreen().pause();
        AudioPlayer.pauseBackground();
    }

    /**
     * Must be called only by libGDX
     */
    @Override
    public void resume() {
        getScreen().resume();
        AudioPlayer.resumeBackground();
    }

    /**
     * Must be called only by libGDX
     */
    @Override
    public void dispose() {
        getScreen().dispose();
        super.dispose();
        Settings.save();
        Settings.load();
    }
}