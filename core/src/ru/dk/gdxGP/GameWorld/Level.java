package ru.dk.gdxGP.GameWorld;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Timer;
import ru.dk.gdxGP.GDXGameGP;
import ru.dk.gdxGP.GameWorld.Interfaces.Actions.ActionForNextStep;
import ru.dk.gdxGP.GameWorld.Interfaces.Actions.CameraPositionChanger;
import ru.dk.gdxGP.GameWorld.Interfaces.Actions.LevelProceeder;
import ru.dk.gdxGP.GameWorld.Interfaces.Actions.LevelTapper;
import ru.dk.gdxGP.GameWorld.Interfaces.Drawers.AfterRenderer;
import ru.dk.gdxGP.GameWorld.Interfaces.Drawers.PreRenderer;
import ru.dk.gdxGP.GameWorld.Interfaces.ParticleSender;
import ru.dk.gdxGP.GameWorld.Templates.ActionForNextStepSet;
import ru.dk.gdxGP.GameWorld.Templates.LevelProceederSet;
import ru.dk.gdxGP.GameWorld.Templates.ParticleDrawerSet;
import ru.dk.gdxGP.GameWorld.WorldElements.Border;
import ru.dk.gdxGP.GameWorld.WorldElements.Particle;
import ru.dk.gdxGP.Screens.LevelScreen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public abstract class Level extends Thread implements Runnable {
    private static Vector2 dist = new Vector2();
    public final ContactMultiListener multiListener;
    private final World world;
    private final ArrayList<Particle> particles;
    private final ArrayList<Border> borders;
    private final ArrayList<Actor> otherElements;
    private final List<ActionForNextStep> actions;
    private final Timer stepTimer;
    private LevelScreen levelScreen;
    private float prevAccelX;
    private float prevAccelY;
    private float xMin;
    private float xMax;
    private float yMin;
    private float yMax;
    private float G = 1;
    private float k = 1;
    private float chargingK = 1;
    private float massFlowingK = 1;
    private MissionChecker currentMissionChecker = new MissionChecker(new Mission("Null"), 1000);
    private boolean isMove = false, isEnd = false;
    private float loaded;
    private AfterRenderer afterRenderer;
    private PreRenderer preRenderer;
    private LevelTapper levelTapper;
    private CameraPositionChanger cameraPositionChanger;
    private LevelProceeder levelProceeder = LevelProceederSet.noneProceed;
    private Vector2 buf = new Vector2(), d = new Vector2();

    protected Level() {
        this.world = new World(new Vector2(0.0f, 0.0f), true);
        this.multiListener = new ContactMultiListener();
        this.multiListener.addContactListener(new ContactListener() {
            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void beginContact(Contact contact) {
                GDXGameGP.currentGame.playBounceSound();
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                if (contact.getFixtureA().getBody().getUserData() instanceof Particle && contact.getFixtureB().getBody().getUserData() instanceof Particle) {
                    Particle f1 = (Particle) contact.getFixtureA().getBody().getUserData(), f2 = (Particle) contact.getFixtureB().getBody().getUserData();
                    contactParticles(f1, f2, contact);
                }
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });
        this.world.setContactListener(multiListener);
        particles = new ArrayList<Particle>();
        borders = new ArrayList<Border>();
        otherElements = new ArrayList<Actor>();
        actions = Collections.synchronizedList(new ArrayList<ActionForNextStep>());
        xMin = yMin = 0;
        xMax = Gdx.graphics.getWidth();
        yMax = Gdx.graphics.getHeight();
        this.isMove = true;
        this.stepTimer = new Timer();
        this.stepTimer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                Level.this.addAction(ActionForNextStepSet.moveAction);
            }
        }, 0, 32 / 1000f);
        this.stepTimer.start();
    }

    public static float getDistance(float x1, float y1, float x2, float y2) {
        dist.set(x2, y2);
        dist.add(-x1, -y1);
        return dist.len();
    }

    public int getActionsCount() {
        return actions.size();
    }

    public int getParticlesCount() {
        return particles.size();
    }

    public AfterRenderer getAfterRenderer() {
        return afterRenderer;
    }

    public void setAfterRenderer(AfterRenderer afterRenderer) {
        this.afterRenderer = afterRenderer;
    }

    public PreRenderer getPreRenderer() {
        return preRenderer;
    }

    public void setPreRenderer(PreRenderer preRenderer) {
        this.preRenderer = preRenderer;
    }

    public LevelTapper getLevelTapper() {
        return levelTapper;
    }

    public void setLevelTapper(LevelTapper levelTapper) {
        this.levelTapper = levelTapper;
    }

    public CameraPositionChanger getCameraPositionChanger() {
        return cameraPositionChanger;
    }

    public void setCameraPositionChanger(CameraPositionChanger cameraPositionChanger) {
        this.cameraPositionChanger = cameraPositionChanger;
    }

    public LevelProceeder getLevelProceeder() {
        return levelProceeder;
    }

    public void setLevelProceeder(LevelProceeder levelProceeder) {
        this.levelProceeder = levelProceeder;
    }

    public final void setCameraPosition() {
        if (this.cameraPositionChanger != null)
            this.cameraPositionChanger.changeCameraPosition(this, this.getScreen().getCamera(), this.getScreen());
    }

    public final void preRender() {
        ParticleDrawerSet.updateStateTime();
        if (this.preRenderer != null)
            this.preRenderer.preRender(this);
    }

    public final void render(float delta) {
        if (this.levelScreen != null) {
            renderBorders();
            renderOthers();
            renderParticles();
        }
    }

    public void renderBorders() {
        levelScreen.drawBorders();
    }

    public void renderOthers() {
        levelScreen.drawOthers();
    }

    public void renderParticles() {
        levelScreen.drawParticles();
    }

    public final void afterRender() {
        if (this.afterRenderer != null) {
            this.afterRenderer.afterRender(this);
        }
    }

    public void proceedParticles(float delta) {
        this.levelScreen.proceed(delta);
    }

    /**
     * Loads level. Creates particles, borders etc. Must be called after creating level.
     *
     * @param screen LevelScreen where level must be displayed
     */
    public final void load(final LevelScreen screen) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(MathUtils.random(0, 100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setLoaded(0.0f / 7);
                setSizes();
                try {
                    Thread.sleep(MathUtils.random(0, 100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setLoaded(1.0f / 7);
                setLevelScreen(screen);
                try {
                    Thread.sleep(MathUtils.random(0, 100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setLoaded(2.0f / 7);
                createWalls();
                try {
                    Thread.sleep(MathUtils.random(0, 100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setLoaded(3.0f / 7);
                setParticles();
                try {
                    Thread.sleep(MathUtils.random(0, 100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setLoaded(4.0f / 7);
                setOtherElements();
                try {
                    Thread.sleep(MathUtils.random(0, 100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setLoaded(5.0f / 7);
                setParameters();
                try {
                    Thread.sleep(MathUtils.random(0, 100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setLoaded(6.0f / 7);
                Level.this.addAction(new ActionForNextStep() {
                    @Override
                    public void doSomethingOnStep(Level level) {
                        Level.this.currentMissionChecker = new MissionChecker(Level.this.createMission(), 100);
                    }
                });
                try {
                    Thread.sleep(MathUtils.random(0, 100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setLoaded(7.0f / 7);
            }
        }).start();
    }

    public void setLevelScreen(LevelScreen screen) {
        this.levelScreen = screen;
    }

    public float getLoaded() {
        return loaded;
    }

    private void setLoaded(float loaded) {
        this.loaded = loaded;
    }

    abstract protected void setSizes();

    protected abstract void setParticles();

    public void setOtherElements() {
    }

    protected abstract Mission createMission();

    protected void setParameters() {
    }

    public void createWalls() {
        this.addBorder(new Border(this, this.getWorld(), 0, 0, getRectWorldBorderShape()));
    }

    /**
     * Creates and returns the shape of standard world border. This border is a rectangle with sizes according level sizes
     *
     * @return world border shape
     */
    public final ChainShape getRectWorldBorderShape() {
        ChainShape shape = new ChainShape();
        shape.createLoop(new float[]{
                getXMin(), getYMin(),
                getXMin(), getYMax(),
                getXMax(), getYMax(),
                getXMax(), getYMin()
        });
        return shape;
    }

    public final void tap(float x, float y) {
        if (this.levelTapper != null)
            this.levelTapper.tapLevel(this, x, y);
    }

    final public LevelScreen getScreen() {
        return this.levelScreen;
    }

    public World getWorld() {
        return world;
    }

    synchronized final public Particle addParticle(Particle f) {
        if (f == null) return null;
        particles.add(f);
        if (levelScreen != null)
            levelScreen.addParticleActor(f);
        return f;
    }

    synchronized final public void removeParticle(Particle particle) {
        particles.remove(particle);
        this.world.destroyBody(particle.getBody());
        if (levelScreen != null)
            levelScreen.removeParticleActor(particle);
    }

    synchronized final public Border addBorder(Border border) {
        borders.add(border);
        if (levelScreen != null)
            levelScreen.addBorderActor(border);
        return border;
    }

    synchronized final public Actor addActor(Actor actor) {
        otherElements.add(actor);
        if (levelScreen != null)
            levelScreen.addOtherActor(actor);
        return actor;
    }

    public final Particle getParticle(int index) {
        return this.particles.get(index);
    }

    public void Move(float time) {
        if (this.levelProceeder != null) {
            levelProceeder.proceed(this, time);
        }
        world.step(1 / 100f, 10, 10);
    }

    @Override
    final public void run() {
        super.run();
        while (!isEnd) {
            if (isMove) {
                if (!this.actions.isEmpty()) {
                    this.actions.remove(0).doSomethingOnStep(this);
                }
            }
        }
    }

    private synchronized boolean isMoveActionPresent() {
        synchronized (this.actions) {
            return this.actions.contains(ActionForNextStepSet.moveAction);
        }
    }

    /**
     * Adds action to the queue of actions in level thread
     *
     * @param action action that must be added
     */
    protected synchronized final void addAction(ActionForNextStep action) {
        synchronized (this.actions) {

            if (action == ActionForNextStepSet.moveAction && isMoveActionPresent()) {
                return;
            }
            this.actions.add(action);
        }
    }

    public float getChargingK() {
        return chargingK;
    }

    protected void setChargingK(float chargingK) {
        this.chargingK = chargingK;
    }

    public float getK() {
        return k;
    }

    protected void setK(float k) {
        this.k = 0.125f*k;
    }

    final public float getG() {
        return G;
    }

    protected final float setG(float newG) {
        return this.G = newG;
    }

    public float getWidth() {
        return getXMax() - getXMin();
    }

    public float getHeight() {
        return getYMax() - getYMin();
    }

    public float getMassFlowingK() {
        return massFlowingK;
    }

    public void setMassFlowingK(float massFlowingK) {
        this.massFlowingK = massFlowingK;
    }

    public final float getXMin() {
        return this.xMin;
    }

    public final float getYMin() {
        return this.yMin;
    }

    public final float getXMax() {
        return this.xMax;
    }

    public final float getYMax() {
        return this.yMax;
    }

    /**
     * Sets a new value to xMin
     *
     * @param newXMin new value of xMin
     * @return newXMin
     */
    protected final float setXMin(float newXMin) {
        return this.xMin = newXMin;
    }

    /**
     * Sets a new value to yMin
     *
     * @param newYMin new value of yMin
     * @return newYMin
     */
    protected final float setYMin(float newYMin) {
        return this.yMin = newYMin;
    }

    /**
     * Sets a new value to xMax
     *
     * @param newXMax new value of xMax
     * @return newXMax
     */
    protected final float setXMax(float newXMax) {
        return this.xMax = newXMax;
    }

    /**
     * Sets a new value to yMax
     *
     * @param newYMax new value of yMax
     * @return newYMax
     */
    protected final float setYMax(float newYMax) {
        return this.yMax = newYMax;
    }

    final public Mission getMission() {
        return this.currentMissionChecker.getMission();
    }

    public void contactParticles(final Particle f1, final Particle f2, Contact contact) {
        flowMass(f1, f2);
    }

    /**
     * Pauses level for a while
     */
    public void pauseLevel() {
        this.isMove = false;
        this.stepTimer.stop();
        this.currentMissionChecker.pause();
    }

    /**
     * Resumes level
     */
    public void resumeLevel() {
        this.isMove = true;
        this.stepTimer.start();
        this.stepTimer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                Level.this.addAction(ActionForNextStepSet.moveAction);
            }
        }, 0, 32 / 1000f);
        this.currentMissionChecker.resume();
    }

    //additional methods for management of the world and the particles

    /**
     * Proceeds world gravity changing accordingly to accelerometer
     *
     * @param factor factor of accelerometer acceleration
     */
    public void processAccelerometer(float factor) {
        float y = Gdx.input.getAccelerometerY();
        float x = Gdx.input.getAccelerometerX();
        if (prevAccelX != x || prevAccelY != y) {
            world.setGravity(new Vector2(factor * y, -factor * x));
            prevAccelX = x;
            prevAccelY = y;
        }
    }

    /**
     * Proceeds interact between two particles
     *
     * @param f1 first particle
     * @param f2 second particle
     */
    public void interactionBetweenParticles(Particle f1, Particle f2) {
        d.set(f2.getBody().getPosition());
        d.add(-f1.getBody().getPosition().x, -f1.getBody().getPosition().y);
        float F = ((this.G * f1.getBody().getMass() * f2.getBody().getMass()-this.k * f1.getCharge() * f2.getCharge() )
                / (d.len() * d.len()));
        buf.set(f2.getBody().getPosition().x - f1.getBody().getPosition().x, f2.getBody().getPosition().y - f1.getBody().getPosition().y);
        buf.setLength(F);
        if (F < 0) buf.rotate(180);
        f1.getBody().applyForceToCenter(buf, true);
        f2.getBody().applyForceToCenter(buf.rotate(180), true);
        chargeBetweenParticle(f1, f2, d.len(), this.chargingK);
    }

    /**
     * Proceeds charge changing between two particles
     *
     * @param f1        first particle
     * @param f2        second particle
     * @param d         distance between particles
     * @param chargingK charging factor, usually equals to the level's chargingK
     */
    private void chargeBetweenParticle(Particle f1, Particle f2, float d, float chargingK) {
        if (chargingK == 0) return;
        float q1 = f1.getCharge(), q2 = f2.getCharge();
        float deltaCharge = chargingK * ((q1 * 1 + q2 * 1) / 2 - q2) *0.0005f/ (d * d);

        if (!Float.isNaN(deltaCharge) && !Float.isInfinite(deltaCharge))
            try {
                f1.moveParameters(f2, 0, deltaCharge, 0, new Vector2(0, 0));
            } catch (Particle.NullMassException e) {
            }
    }

    /**
     * Proceeds charge changing between two particles
     *
     * @param f1        first particle
     * @param f2        second particle
     * @param chargingK charging factor, usually equals to the level's chargingK
     */
    public void chargeBetweenParticle(Particle f1, Particle f2, float chargingK) {
        d.set(f2.getBody().getPosition());
        d.add(-f1.getBody().getPosition().x, -f1.getBody().getPosition().y);
        chargeBetweenParticle(f1, f2, d.len(), chargingK);
    }

    /**
     * Proceeds interacts all particles between others
     */
    public void interactAllWithAllParticles() {
        for (int i = 0; i < particles.size(); i++) {
            Particle f1 = particles.get(i);
            if (f1 != null) {
                for (int j = i + 1; j < particles.size(); j++) {
                    interactionBetweenParticles(f1, particles.get(j));
                }
            }
        }
    }

    /**
     * Proceeds the flowing of mass between two particles
     *
     * @param f1 first particle
     * @param f2 second particle
     */
    public void flowMass(final Particle f1, final Particle f2) {
        if (!(this.particles.contains(f1) && this.particles.contains(f2)))
            return;
        if (this.massFlowingK == 0) return;
        this.addAction(new ActionForNextStep() {
            @Override
            public void doSomethingOnStep(Level level) {
                Particle from = f1, to = f2;
                if (f1.getMass() > f2.getMass()) {
                    from = f2;
                    to = f1;
                }
                float deltaMass = (float) (Level.this.massFlowingK * (((from.getMass() + to.getMass()) / 2 + 1024 * from.getMass()) / 1025 - from.getMass()) * 0.005f / (Math.pow((from.getRadius() + to.getRadius()), 2)));
                try {
                    from.moveParameters(to, deltaMass, 0, 0, new Vector2(0, 0));
                } catch (final Particle.NullMassException e) {
                    Level.this.addAction(new ActionForNextStep() {
                        @Override
                        public void doSomethingOnStep(Level level) {
                            Level.this.removeParticle(e.getParticle());
                        }
                    });
                }

            }
        });
    }

    /**
     * Must be used in {@link ru.dk.gdxGP.GameWorld.Interfaces.Actions.LevelTapper} for main particle moving
     *
     * @param particle particle that must be moved
     * @param speed    particle speed
     * @param x        x coordinate of a point particle must move to
     * @param y        y coordinate of a point particle must move to
     */
    public void moveOnTap(final Particle particle, final float speed, final float x, final float y) {
        this.addAction(new ActionForNextStep() {
            @Override
            public void doSomethingOnStep(Level level) {
                Vector2 v = new Vector2(particle.getPosition());
                v.rotate(180);
                v.add(x, y);
                v.setLength(speed);
                particle.getBody().applyLinearImpulse(v.x, v.y, particle.getMassCenter().x, particle.getMassCenter().y, true);
            }
        });
    }

    /**
     * Must be used in {@link ru.dk.gdxGP.GameWorld.Interfaces.Actions.LevelTapper} for main particle division
     *
     * @param particle       particle that must be divided
     * @param speed          speed of the new particle
     * @param piece          a part of particle that must be divided
     * @param x              x coordinate of a point new particle must move to
     * @param y              y coordinate of a point new particle must move to
     * @param particleSender object to which particle can be send
     */
    public void divideOnTap(final Particle particle, final float speed, final float piece, final float x, final float y, final ParticleSender particleSender) {
        this.addAction(new ActionForNextStep() {
            @Override
            public void doSomethingOnStep(Level level) {
                Vector2 v = new Vector2(particle.getPosition());
                v.rotate(180);
                v.add(x, y);
                v.setLength(speed);
                if (speed < 0) v.rotate(180);
                Particle newParticle = particle.divide(Level.this.getParticle(0).getMass() * piece, v.x, v.y);
                level.addParticle(newParticle);
                if (particleSender != null)
                    particleSender.sendParticle(newParticle);

            }
        });
    }

    /**
     * Must be used in{@link ru.dk.gdxGP.GameWorld.Interfaces.Actions.CameraPositionChanger} for smooth moving
     *
     * @param xTo   target x position
     * @param yTo   target y position
     * @param delay smooth of move
     */
    public void moveCamera(float xTo, float yTo, float delay) {
        this.getScreen().getCamera().position.set((delay * this.getScreen().getCamera().position.x + xTo) / (delay + 1), (delay * this.getScreen().getCamera().position.y + yTo) / (delay + 1), 0);
    }

    /**
     * Must be used in{@link ru.dk.gdxGP.GameWorld.Interfaces.Actions.CameraPositionChanger} for smooth zooming
     *
     * @param zoomTo target zoom
     * @param delay  smooth of zoom
     */
    public void zoomCamera(float zoomTo, float delay) {
        this.getScreen().setCameraZoom((this.getScreen().getZoom() * delay + zoomTo) / (delay + 1));
    }

    //methods for generating

    /**
     * Adds particles generated by {@link #generateRandomParticle} method
     *
     * @param particleDef definition of particle that must be generated
     * @param count       number of particles that must be generated
     */
    public void addRandomParticles(final ParticleDef particleDef, final int count) {
        this.addAction(new ActionForNextStep() {
            @Override
            public void doSomethingOnStep(Level level) {
                for (int i = 0; i < count; i++) {
                    Level.this.addParticle(Level.this.generateRandomParticle(particleDef));
                }
            }
        });
    }

    /**
     * Generates and returns random particle from given ParticleDef
     *
     * @param particleDef definition of particle that must be generated
     * @return generated {@link Particle}
     */
    public Particle generateRandomParticle(ParticleDef particleDef) {
        Particle particle = new Particle(this, this.getWorld(),
                MathUtils.random(particleDef.minX, particleDef.maxX),
                MathUtils.random(particleDef.minY, particleDef.maxY),
                MathUtils.random(particleDef.minVX, particleDef.maxVX), MathUtils.random(particleDef.minVY, particleDef.maxVY),
                MathUtils.random(particleDef.minMass, particleDef.maxMass), (MathUtils.random(particleDef.minCharge, particleDef.maxCharge)), 1, 1, 1, Particle.Condition.Liquid,
                new Color(MathUtils.random(particleDef.rMin, particleDef.rMax),
                        MathUtils.random(particleDef.gMin, particleDef.gMax),
                        MathUtils.random(particleDef.bMin, particleDef.bMax),
                        MathUtils.random(particleDef.aMin, particleDef.aMax)));
        return particle;
    }
}