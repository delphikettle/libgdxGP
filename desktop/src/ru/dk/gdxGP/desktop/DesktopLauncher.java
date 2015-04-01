package ru.dk.gdxGP.desktop;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import ru.dk.gdxGP.GDXGameGP;
import ru.dk.gdxGP.GameWorld.Levels.TestLevel01;

public class DesktopLauncher {
	public static void main (String[] arg) {
		GDXGameGP.assetManager.load("images/logo.png", Texture.class);
		GDXGameGP.assetManager.load("images/loadBall.png", Texture.class);

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width=1024;
        config.height=846;
		new LwjglApplication(new GDXGameGP("TestLevel01"), config);
	}
}
