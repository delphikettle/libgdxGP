package ru.dk.gdxGP.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;


public class TexturePackerProceed {
    public static void main(String[] arg) {
        System.out.println("starting");
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.maxHeight = settings.maxWidth = 1024;
        settings.paddingX = settings.paddingY = 0;
        TexturePacker.process(settings, "./images", "./atlas", "textureAtlas");
        System.out.println("packed");
    }
}
