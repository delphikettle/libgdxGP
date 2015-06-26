package ru.dk.gdxGP;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import ru.dk.gdxGP.Screens.LogoScreen;
import ru.dk.gdxGP.utils.AtlasLoader;
import ru.dk.gdxGP.utils.Settings;

public class GDXLogo extends Game {
    @Override
    public void create() {
        AtlasLoader.load();
        this.setScreen(new LogoScreen(1));
        Settings.load();
    }

    @Override
    public void render() {
        super.render();
        if ((getScreen() instanceof LogoScreen) && !((LogoScreen) getScreen()).isActive())
            Gdx.app.exit();
    }

    /**
     * Must be called only by libGDX
     */
    @Override
    public void dispose() {
        getScreen().dispose();
        super.dispose();
    }
}
