package ru.dk.gdxGP.Screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import ru.dk.gdxGP.GameWorld.Level;

public class LevelScreen extends Stage implements Screen {
    private Box2DDebugRenderer box2DDebugRenderer;
    private OrthographicCamera camera;

    public Level getLevel() {
        return level;
    }

    private Level level;
    public LevelScreen(Level level){
        this.level=level;
        box2DDebugRenderer = new Box2DDebugRenderer();
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