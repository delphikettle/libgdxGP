package ru.dk.gdxGP;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Андрей on 01.03.2015.
 */
public final class TextureKeeper {
    Texture[] textures;
    TextureRegion[][] textureRegions;
    static int width=256;
    TextureKeeper(){
        textures=new Texture[Character.MAX_VALUE];
        textureRegions=new TextureRegion[Character.MAX_VALUE][16];
    }

    public TextureRegion getTextureRegion(char name, char addr){
        if(textureRegions[name][addr]==null)loadTextureRegion(name, addr);
        return textureRegions[name][addr];
    }
    public void loadTextureRegion(char name, char addr){
        textureRegions[name][addr]=new TextureRegion(getTexture(name),width*addr/4,width*addr%4,width,width);
    }

    public Texture getTexture(char name){
        if(textures[name]==null)loadTexture(name);
        return textures[name];
    }
    public void loadTexture(char name){
        textures[name]=new Texture("/data/images/"+new String(new char[]{name})+".png");
    }
}
