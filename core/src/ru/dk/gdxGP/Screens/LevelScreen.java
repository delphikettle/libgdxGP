package ru.dk.gdxGP.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import ru.dk.gdxGP.GDXGameGP;
import ru.dk.gdxGP.GameWorld.Level;
import ru.dk.gdxGP.GameWorld.WorldElements.Border;
import ru.dk.gdxGP.GameWorld.WorldElements.Particle;
import ru.dk.gdxGP.utils.Settings;
import ru.dk.gdxGP.utils.Graphics;

public class LevelScreen implements GestureDetector.GestureListener, InputProcessor, Screen {
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
    private BitmapFont bitmapFont = new BitmapFont();
    private SpriteBatch fontBatch = new SpriteBatch();

    {
        bitmapFont.setColor(new Color(1, 1, 1, 1));
        bitmapFont.setScale(1);
    }

    private float xMin, xMax, yMin, yMax, zoomMin = 0.1f, zoomMax = 2f, coordsDelay = 25, cameraDelay = 25;

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
        } catch (IndexOutOfBoundsException e) {
            this.particlesStage.getBatch().end();
        }
    }

    public void drawBorders() {
        try {
            this.bordersStage.draw();
        } catch (NullPointerException e) {
            this.bordersStage.getBatch().end();
        }
    }

    public void drawOthers() {
        try {
            this.othersStage.draw();
        } catch (NullPointerException e) {
            this.othersStage.getBatch().end();
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
        this.xMin = level.getXMin();
        this.xMax = level.getXMax();
        this.yMin = level.getYMin();
        this.yMax = level.getYMax();
        Graphics.setCurrentCamera(camera);
        this.box2DDebugRenderer.SHAPE_AWAKE.set(0, 0, 0, 0.5f);

        GDXGameGP.currentGame.inputMultiplexer.addProcessor(this);
        GDXGameGP.currentGame.inputMultiplexer.addProcessor(new GestureDetector(this));
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

        if (this.level.getMission() != null) {
            missionBatch.begin();
            this.level.getMission().render(missionBatch);
            missionBatch.end();
        }

        float xTo = MathUtils.clamp(camera.position.x, xMin, xMax),
                yTo = MathUtils.clamp(camera.position.y, yMin, yMax);
        this.camera.position.set((coordsDelay * camera.position.x + xTo) / (coordsDelay + 1), (coordsDelay * camera.position.y + yTo) / (coordsDelay + 1), 0);
        this.setCameraZoom((this.getZoom() * cameraDelay + MathUtils.clamp(this.getZoom(), zoomMin, zoomMax)) / (cameraDelay + 1));

        if(Settings.getCurrentSettings().isDebug())
            drawDebug();
    }

    private void drawDebug() {
        box2DDebugRenderer.render(this.level.getWorld(), camera.combined);
        this.fontBatch.begin();
        bitmapFont.draw(this.fontBatch, "FPS:" + Gdx.graphics.getFramesPerSecond() + "; actions count:" + this.level.getActionsCount() + ";" + " particles count:" + this.level.getParticlesCount() + ";", 0, Gdx.graphics.getHeight() - 10);
        this.fontBatch.end();
    }

    public float getCameraDelay() {
        return cameraDelay;
    }

    public void setCameraDelay(float cameraDelay) {
        this.cameraDelay = cameraDelay;
    }

    public float getXMin() {
        return xMin;
    }

    public void setXMin(float xMin) {
        this.xMin = xMin;
    }

    public float getXMax() {
        return xMax;
    }

    public void setXMax(float xMax) {
        this.xMax = xMax;
    }

    public float getYMin() {
        return yMin;
    }

    public void setYMin(float yMin) {
        this.yMin = yMin;
    }

    public float getYMax() {
        return yMax;
    }

    public void setYMax(float yMax) {
        this.yMax = yMax;
    }

    public float getZoomMin() {
        return zoomMin;
    }

    public void setZoomMin(float zoomMin) {
        this.zoomMin = zoomMin;
    }

    public float getZoomMax() {
        return zoomMax;
    }

    public void setZoomMax(float zoomMax) {
        this.zoomMax = zoomMax;
    }

    public float getCoordsDelay() {
        return coordsDelay;
    }

    public void setCoordsDelay(float coordsDelay) {
        this.coordsDelay = coordsDelay;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        this.setInitialScale(this.getCameraZoom());
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 tapCoords = this.camera.unproject(new Vector3(x, y, 0));
        this.level.tap(tapCoords.x, tapCoords.y);
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }


    public Batch getBatch() {
        return this.particlesStage.getBatch();
    }

    @Override
    public void resize(int width, int height) {
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

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        float dx = -Gdx.input.getDeltaX(pointer), dy = Gdx.input.getDeltaY(pointer);
        this.camera.position.add(dx * camera.viewportWidth / Gdx.graphics.getWidth() * zoom, dy * camera.viewportHeight / Gdx.graphics.getHeight() * zoom, 0);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }


    public boolean zoom(float initialDistance, float distance) {
        this.zoom = MathUtils.clamp(initialScale * initialDistance / distance, 0.001f, 1000.0f);
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        this.setInitialScale(this.getCameraZoom());
        this.zoom(this.getZoom(), (float) (this.getZoom() * Math.pow(1.1f, -amount)));
        return false;
    }
}