package ru.dk.gdxGP;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public final class AtlasLoader {
    private static final TextureAtlas textureAtlas;
private AtlasLoader(){}
    static {
        textureAtlas = new TextureAtlas("atlas/textureAtlas.atlas");
    }

    /**
     * Loads and returns textureRegion from the images folder of assets
     * @param name string key of the textureRegion
     * @return loaded textureRegion or null if textureRegion is not exist
     */
    public static TextureRegion getRegion(String name) {
        return textureAtlas.findRegion(name);
    }
}
