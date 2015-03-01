package ru.dk.gdxGP;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Андрей on 01.03.2015.
 */
public class TextureKeeper {
    TextureRegion[][] textureRegions;
    TextureKeeper(boolean load){
        textureRegions=new TextureRegion[Character.MAX_VALUE][16];
    }
}
