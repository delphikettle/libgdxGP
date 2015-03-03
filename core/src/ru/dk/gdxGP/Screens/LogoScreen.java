package ru.dk.gdxGP.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import ru.dk.gdxGP.TextureKeeper;

public class LogoScreen extends Stage implements Screen {
    private TextureRegion texture;
    private float width,height;
    private SpriteBatch spriteBatch;
    private float deltaSum;
    private float time;

    private boolean active;

    public LogoScreen(float time){
        this.time=time;;
    }
    @Override
    public void show() {
        texture= TextureKeeper.getTexture('l');
        width=height= 0x0.FAP0f *((Gdx.graphics.getHeight()>Gdx.graphics.getWidth())?Gdx.graphics.getWidth():Gdx.graphics.getHeight());
        spriteBatch=new SpriteBatch();
        deltaSum=0;
        this.render(Gdx.graphics.getDeltaTime());
        this.setActive(true);
    }


    @Override
    public void render(float delta){
        deltaSum+=delta;
        spriteBatch.begin();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.draw(texture,
                (Gdx.graphics.getWidth()-width)/2,(Gdx.graphics.getHeight()-height)/2,
                width,height);
        spriteBatch.end();
        if(deltaSum>=time)this.setActive(false);
    }

    private void setActive(boolean active) {
        this.active=active;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
