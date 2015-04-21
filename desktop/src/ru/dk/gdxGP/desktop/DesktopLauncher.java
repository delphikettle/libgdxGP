package ru.dk.gdxGP.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import ru.dk.gdxGP.GDXGameGP;

public class DesktopLauncher {
    public static void main(String[] arg) {
        System.out.println("starting");
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.maxHeight = settings.maxWidth = 1024;
        settings.paddingX = settings.paddingY = 0;
        //TexturePacker.process(settings, "./images", "./atlas", "textureAtlas");
        //GDXGameGP.assetManager.load("images/logo.png", Texture.class);
        //GDXGameGP.assetManager.load("images/loadBall.png", Texture.class);
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1024;
        config.height = 846;
        new LwjglApplication(new GDXGameGP("InteractionTutorialLevel"), config);
    }
}
