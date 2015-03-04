package ru.dk.gdxGP.Screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import ru.dk.gdxGP.GDXGameGP;
import ru.dk.gdxGP.GameWorld.Level;


public class LoadingScreen extends Stage implements Screen {
    private String[] textures;

    public LogoScreen getLogoScreen() {
        return logoScreen;
    }

    private LogoScreen logoScreen;
    private boolean active;
    private byte type;
    private Level level;

    public Screen getNextScreen() {
        return nextScreen;
    }

    private Screen nextScreen;
    public LoadingScreen(LogoScreen logoScreen, String[] texturesForLoad){
        this.logoScreen=logoScreen;
        this.textures=texturesForLoad;
        this.type=0;
        this.nextScreen=null;
    }
    public LoadingScreen(LogoScreen logoScreen, Level level){
        this.type=1;
        this.level=level;
        this.logoScreen=logoScreen;
        this.nextScreen=level.getStage();
    }
    @Override
    public void show() {
        this.setActive(true);
        switch (type){
            case 0:
                for (int i = 0; i < textures.length; i++) {
                    GDXGameGP.assetManager.load(textures[i],Texture.class);
                }
                break;
            case 1:
                System.out.println("levelStarted");
                level.start();
                break;
        }
    }

    @Override
    public void render(float delta) {
        switch (type){
            case 0:
                System.out.println("renderLoadScreen");
                if(logoScreen!=null){
                    logoScreen.render(delta);
                    logoScreen.setRotation((-360*GDXGameGP.assetManager.getProgress()+25*logoScreen.getRotation())/26);
                }
                if(GDXGameGP.assetManager.update())this.setActive(false);
                break;
            case 1:
                System.out.println("renderLevelLoading");
                if(logoScreen!=null){
                    logoScreen.render(delta);
                    logoScreen.setRotation((-360*level.getLoaded()+255*logoScreen.getRotation())/256);
                }
                if(level.getLoaded()>=1f)this.setActive(false);
                break;
        }
    }

    private void setActive(boolean active) {
        this.active = active;
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
