package ru.dk.gdxGP.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import ru.dk.gdxGP.GameWorld.Level;
import ru.dk.gdxGP.GameWorld.WorldElements.Border;
import ru.dk.gdxGP.GameWorld.WorldElements.Particle;

public class LevelScreen implements Screen {
    private final Stage particlesStage;
    private final Stage bordersStage;
    private final Stage othersStage;
    private final Level level;
    private Box2DDebugRenderer box2DDebugRenderer;
    private OrthographicCamera camera;
    private float zoom = 1;
    private Color startColor;
    private float initialScale = 1;
    private Batch missionBatch = new SpriteBatch();

    public LevelScreen(Level level, float w, float h) {
        this.level = level;
        this.box2DDebugRenderer = new Box2DDebugRenderer(true, true, false, true, true, true);
        this.camera = new OrthographicCamera(1f, 1f * h / w);
        this.particlesStage = new Stage();
        this.particlesStage.getViewport().setCamera(camera);
        this.bordersStage = new Stage();
        this.bordersStage.getViewport().setCamera(camera);
        this.othersStage = new Stage();
        this.othersStage.getViewport().setCamera(camera);
        this.startColor = this.particlesStage.getBatch().getColor();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public float getZoom() {
        return zoom;
    }

    public void setInitialScale(float initialScale) {
        this.initialScale = initialScale;
    }

    public Level getLevel() {
        return level;
    }

    public void addParticleActor(Particle particle) {
        particlesStage.addActor(particle);
    }

    public void removeParticleActor(Particle particle) {
        particlesStage.getActors().removeValue(particle, true);
    }

    public void addBorderActor(Border border) {
        bordersStage.addActor(border);
    }

    public void addOtherActor(Actor actor) {
        othersStage.addActor(actor);
    }

    public void drawParticles() {
        try {
            this.particlesStage.draw();
        } catch (NullPointerException e) {
            this.particlesStage.getBatch().end();
            System.out.println("Exception " + e.toString() + " in drawParticles");
        } catch (IndexOutOfBoundsException e) {
            this.particlesStage.getBatch().end();
            System.out.println("Exception " + e.toString() + " in drawParticles");
        }
    }

    public void drawBorders() {
        try {
            this.bordersStage.draw();
        } catch (NullPointerException e) {
            this.bordersStage.getBatch().end();
            System.out.println("Exception " + e.toString() + " in drawBorders");
        }
    }

    public void drawOthers() {
        try {
            this.othersStage.draw();
        } catch (NullPointerException e) {
            this.othersStage.getBatch().end();
            System.out.println("Exception " + e.toString() + " in drawOthers");
        }

    }

    public void proceed(float deltaTime) {
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
        this.box2DDebugRenderer = new Box2DDebugRenderer(true, true, false, true, false, true);
        this.camera = new OrthographicCamera(level.getWidth(), level.getWidth() * camera.viewportHeight / camera.viewportWidth);
        this.particlesStage.getViewport().setCamera(camera);
        this.bordersStage.getViewport().setCamera(camera);
        this.othersStage.getViewport().setCamera(camera);
        this.startColor = this.particlesStage.getBatch().getColor();
    }

    @Override
    public void render(float delta) {
        this.camera.zoom = zoom;
        this.level.setCameraPosition();
        this.level.preRender();
        this.particlesStage.getBatch().setColor(startColor);
        this.level.render(delta);
        this.particlesStage.getBatch().setColor(startColor);
        this.level.afterRender();
        missionBatch.begin();
        if (this.level.getMission() != null)
            this.level.getMission().render(missionBatch);
        missionBatch.end();
        float xTo=MathUtils.clamp(camera.position.x,level.getXMin(),level.getXMax()),
                yTo=MathUtils.clamp(camera.position.y,level.getYMin(),level.getYMax()),delay=25;
        this.camera.position.set((delay * camera.position.x + xTo) / (delay + 1), (delay * camera.position.y + yTo) / (delay + 1), 0);
        //box2DDebugRenderer.render(this.level.getWorld(), camera.combined);
        this.setCameraZoom((this.getZoom() * delay + MathUtils.clamp(this.getZoom(),0.2f,5f)) / (delay+1));
    }

    public boolean zoom(float initialDistance, float distance) {
        this.zoom = MathUtils.clamp(initialScale * initialDistance / distance, 0.001f, 1000.0f);
        return true;
    }

    public void drag(float dx, float dy) {
        this.camera.position.add(dx * camera.viewportWidth / Gdx.graphics.getWidth() * zoom, dy * camera.viewportHeight / Gdx.graphics.getHeight() * zoom, 0);
    }

    public Batch getBatch() {
        return this.particlesStage.getBatch();
    }

    @Override
    public void resize(int width, int height) {
    }

    public void tap(float screenX, float screenY) {
        Vector3 tapCoords = this.camera.unproject(new Vector3(screenX, screenY, 0));
        this.level.tap(tapCoords.x, tapCoords.y);
    }

    public float getCameraZoom() {
        return this.camera.zoom;
    }

    public void setCameraZoom(float zoom) {
        this.zoom = zoom;
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