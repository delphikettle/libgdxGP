package ru.dk.gdxGP.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.dk.gdxGP.GDXGameGP;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width=1024;
        config.height=846;
		new LwjglApplication(new GDXGameGP(), config);
	}
}
