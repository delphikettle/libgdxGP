package ru.dk.gdxGP.Screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import ru.dk.gdxGP.GameWorld.Border;
import ru.dk.gdxGP.GameWorld.Fraction;
import ru.dk.gdxGP.GameWorld.Level;

public class LevelScreen implements Screen {
    private Box2DDebugRenderer box2DDebugRenderer;
    private OrthographicCamera camera;
    private Stage particlesStage, bordersStage, othersStage;

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
        this.bordersStage=new Stage(this.particlesStage.getViewport(),this.particlesStage.getBatch());
        this.othersStage =new Stage(this.particlesStage.getViewport(),this.particlesStage.getBatch());
    }

    public void addFractionActor(Fraction fraction){
        particlesStage.addActor(fraction);
    }
    public void addBorderActor(Border border){
        bordersStage.addActor(border);
    }
    public void addOtherActor(Actor actor){
        othersStage.addActor(actor);
    }
    public void drawFractions(){
        this.particlesStage.draw();
    }
    public void drawBorders(){
        this.bordersStage.draw();
    }
    public void drawOthers(){
        this.othersStage.draw();
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        this.level.setCameraPosition();
        this.level.preRender();
        this.level.render(delta);
        this.level.afterRender();
        //box2DDebugRenderer.render(world, this.getCamera().combined);
    }

    public Batch getBatch(){
        return this.particlesStage.getBatch();
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

    @Override
    public void dispose() {

    }
}