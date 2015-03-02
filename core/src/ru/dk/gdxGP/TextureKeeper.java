package ru.dk.gdxGP;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * TextureKeeper loads TextureRegion once and keeps it for a while.
 */
public  class TextureKeeper {
    static TextureRegion[] textures=new TextureRegion[Character.MAX_VALUE];
    static TextureRegion[][] textureRegions=new TextureRegion[Character.MAX_VALUE][16];
    static int width=256;

    /**
     *
     * @param name marker of texture
     * @param addr no of texture
     * @return TextureRegion with given name and addr
     */
    static public TextureRegion getTextureRegion(char name, char addr){
        if(textureRegions[name][addr]==null)loadTextureRegion(name, addr);
        return textureRegions[name][addr];
    }

    /**
     * loads a texture with given name and addr
     * @param name marker of texture
     * @param addr no of texture
     */
    static public void loadTextureRegion(char name, char addr){
        textureRegions[name][addr]=new TextureRegion(getTexture(name),width*addr/4,width*(addr%4),width,width);
    }

    /**
     *
     * @param name marker of texture
     * @return Texture with given name
     */
    static public TextureRegion getTexture(char name){
        if(textures[name]==null)loadTexture(name);
        return textures[name];
    }

    /**
     * loads a texture with given name
     * @param name marker of texture
     */
    static public void loadTexture(char name){
        textures[name]=new TextureRegion(new Texture("data/images/"+name+".png"));
    }
}
