package ru.dk.gdxGP.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.dk.gdxGP.GDXGameGP;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = (int) (1024 * 0.8f);
        config.height = (int) (846 * 0.8f);
        new LwjglApplication(new GDXGameGP("ChargeYourFriendLevel"), config);
    }
}
