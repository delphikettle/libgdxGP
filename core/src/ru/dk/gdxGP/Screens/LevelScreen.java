package ru.dk.gdxGP.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.dk.gdxGP.GameWorld.WorldElements.Border;
import ru.dk.gdxGP.GameWorld.WorldElements.Fraction;
import ru.dk.gdxGP.GameWorld.Level;

public class LevelScreen implements Screen {
    private Box2DDebugRenderer box2DDebugRenderer;
    private OrthographicCamera camera;
    private float zoom=1;
    private Stage particlesStage, bordersStage, othersStage;
    private Color startColor;
    public void setInitialScale(float initialScale) {
        this.initialScale = initialScale;
    }

    private float initialScale=1;

    public Level getLevel() {
        return level;
    }

    private Level level;
    public LevelScreen(Level level,float w, float h){
        this.level=level;
        this.box2DDebugRenderer = new Box2DDebugRenderer(true,true,false,true,true,true);
        //this.camera=new OrthographicCamera(level.getWidth(),level.getWidth()*h/w);
        this.camera=new OrthographicCamera(1f,1f*h/w);
        this.particlesStage=new Stage();
        this.particlesStage.getViewport().setCamera(camera);
        this.bordersStage=new Stage();
        this.bordersStage.getViewport().setCamera(camera);
        this.othersStage =new Stage();
        this.othersStage.getViewport().setCamera(camera);
        this.startColor=this.particlesStage.getBatch().getColor();
    }

    public void addFractionActor(Fraction fraction){
        particlesStage.addActor(fraction);
    }
    public void removeFractionActor(Fraction fraction){
        particlesStage.getActors().removeValue(fraction,true);
    }
    public void addBorderActor(Border border){
        bordersStage.addActor(border);
    }
    public void addOtherActor(Actor actor){
        othersStage.addActor(actor);
    }
    public void drawFractions()
    {
        try{
            this.particlesStage.draw();
        }catch (NullPointerException e){
            this.particlesStage.getBatch().end();
            System.out.println("Exception "+e.toString()+" in drawFractions");
        }catch (java.lang.IndexOutOfBoundsException e){
            this.particlesStage.getBatch().end();
            System.out.println("Exception "+e.toString()+" in drawFractions");
        }
    }
    public void drawBorders(){
        try {
            this.bordersStage.draw();
        }catch (NullPointerException e){
            this.bordersStage.getBatch().end();
            System.out.println("Exception " + e.toString() + " in drawBorders");
        }
    }
    public void drawOthers()
    {
        try {
            this.othersStage.draw();
        } catch (NullPointerException e){
            this.othersStage.getBatch().end();
            System.out.println("Exception " + e.toString() + " in drawOthers");
        }

    }
    public void proceed(float deltaTime){
        try {
            this.bordersStage.act(deltaTime);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            this.othersStage.act(deltaTime);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            this.particlesStage.act(deltaTime);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void show() {
        this.box2DDebugRenderer = new Box2DDebugRenderer(true,true,false,true,false,true);
        //this.camera=new OrthographicCamera(level.getWidth(),level.getWidth()*camera.viewportHeight/camera.viewportWidth);
        //this.camera=new OrthographicCamera(20f,20f*camera.viewportHeight/camera.viewportWidth);
        this.camera=new OrthographicCamera(level.getWidth(),level.getWidth()*camera.viewportHeight/camera.viewportWidth);
        //this.particlesStage=new Stage();
        this.particlesStage.getViewport().setCamera(camera);
        //this.bordersStage=new Stage();
        this.bordersStage.getViewport().setCamera(camera);
        //this.othersStage =new Stage();
        this.othersStage.getViewport().setCamera(camera);
        this.startColor=this.particlesStage.getBatch().getColor();
    }

    @Override
    public void render(float delta) {
        this.camera.zoom=zoom;
        this.level.setCameraPosition();
        this.level.preRender();
        this.particlesStage.getBatch().setColor(startColor);
        this.level.render(delta);
        this.particlesStage.getBatch().setColor(startColor);
        this.level.afterRender();
        box2DDebugRenderer.render(this.level.getWorld(), camera.combined);
    }
    public boolean zoom(float initialDistance, float distance) {

        //Clamp range and set zoom
        this.zoom = MathUtils.clamp(initialScale * initialDistance / distance, 0.001f, 1000.0f);
        return true;
    }
    public void drag(float dx,float dy){
        this.camera.position.add(dx*camera.viewportWidth/ Gdx.graphics.getWidth()*zoom,dy*camera.viewportHeight/Gdx.graphics.getHeight()*zoom,0);
    }

    public Batch getBatch(){
        return this.particlesStage.getBatch();
    }
    @Override
    public void resize(int width, int height) {
        /*
        this.camera=new OrthographicCamera(0.1f*width,0.1f* height);
        this.camera.zoom=zoom;
        particlesStage.getViewport().setCamera(camera);
        bordersStage.getViewport().setCamera(camera);
        othersStage.getViewport().setCamera(camera);
        */
    }

    public void tap(float screenX,float screenY){
        Vector3 tapCoords=this.camera.unproject(new Vector3(screenX, screenY, 0));
        this.level.tap(tapCoords.x,tapCoords.y);
        //System.out.println("camera   "+camera.viewportWidth+" "+camera.viewportHeight+" with zoom "+camera.zoom);
        //System.out.println("viewport "+this.particlesStage.getViewport().getScreenWidth()+" "+this.particlesStage.getViewport().getScreenHeight());
    }

    public float getCameraZoom(){
        return this.camera.zoom;
    }

    @Override
    public void pause() {
        this.level.pauseLevel();
    }

    @Override
    public void resume() {
        this.level.resumeLevel();
    }

    @Override
    public void hide() {
        this.level.pauseLevel();
    }

    @Override
    public void dispose() {

    }
}