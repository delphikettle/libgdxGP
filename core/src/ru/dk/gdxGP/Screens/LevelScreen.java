package ru.dk.gdxGP.Screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import ru.dk.gdxGP.GameWorld.Level;

public class LevelScreen extends Stage implements Screen {
    private Box2DDebugRenderer box2DDebugRenderer;
    private OrthographicCamera camera;
    private Stage particlesStage, bordersStage, otherStage;

    public Level getLevel() {
        return level;
    }

    private Level level;
    public LevelScreen(Level level,float w, float h){
        this.level=level;
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        this.camera=new OrthographicCamera(w,h);
        this.particlesStage=new Stage();
        this.particlesStage.getViewport().setCamera(camera);
    }
    @Override
    final public void draw() {
        this.level.setCameraPosition();
        this.level.preRender();
        this.level.render();
        this.level.afterRender();
        //box2DDebugRenderer.render(world, this.getCamera().combined);
    }
    final public void superDraw(){
        super.draw();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        draw();
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