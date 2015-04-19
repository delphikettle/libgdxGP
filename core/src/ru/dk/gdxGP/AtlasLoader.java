package ru.dk.gdxGP;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by DK on 16.04.2015.
 */
public class AtlasLoader {
    private static final TextureAtlas textureAtlas;

    static {
        textureAtlas = new TextureAtlas("atlas/textureAtlas.atlas");
    }

    public static TextureRegion getRegion(String name) {
        return textureAtlas.findRegion(name);
    }
}
