package ru.dk.gdxGP.GameWorld;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Timer;
import ru.dk.gdxGP.GameWorld.InterfacesForActions.*;
import ru.dk.gdxGP.GameWorld.Templates.LevelProceederSet;
import ru.dk.gdxGP.GameWorld.WorldElements.Border;
import ru.dk.gdxGP.GameWorld.WorldElements.Fraction;
import ru.dk.gdxGP.Screens.LevelScreen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public abstract class Level extends Thread implements Runnable, ContactListener {
    private final static ActionForNextStep moveAction = new ActionForNextStep() {
        @Override
        public void doSomethingOnStep(Level level) {
            level.Move(16);
        }
    };
    protected static final String[] standardAssetsPaths = new String[]{
            "images/PlusCharge.png",
            "images/NullCharge.png",
            "images/MinusCharge.png",
            "images/FractionSolid01.png",
            "images/FractionSolid.png",
            "images/FractionLiquid.png",
            "images/charge.png",
            "border01.png"
    };
    private final World world;
    private final ArrayList<Fraction> particles;
    private final ArrayList<Border> borders;
    private final ArrayList<Actor> otherElements;
    private final List<ActionForNextStep> actions;
    private LevelScreen levelScreen;
    private float prevAccelX;
    private float prevAccelY;
    private final Timer stepTimer;
    private float xMin;
    private float xMax;
    private float yMin;
    private float yMax;
    private float G = 1;
    private float k = 1;
    private float chargingK = 1;

    private float massFlowingK=1;
    private MissionChecker currentMissionChecker = new MissionChecker(new Mission("Null"), 1000);
    private boolean isMove = false, isEnd = false;
    private float loaded;

    public AfterRenderer getAfterRenderer() {
        return afterRenderer;
    }

    public void setAfterRenderer(AfterRenderer afterRenderer) {
        this.afterRenderer = afterRenderer;
    }

    private AfterRenderer afterRenderer;

    public PreRenderer getPreRenderer() {
        return preRenderer;
    }

    public void setPreRenderer(PreRenderer preRenderer) {
        this.preRenderer = preRenderer;
    }

    private PreRenderer preRenderer;

    public LevelTapper getLevelTapper() {
        return levelTapper;
    }

    public void setLevelTapper(LevelTapper levelTapper) {
        this.levelTapper = levelTapper;
    }

    private LevelTapper levelTapper;

    public CameraPositionChanger getCameraPositionChanger() {
        return cameraPositionChanger;
    }

    public void setCameraPositionChanger(CameraPositionChanger cameraPositionChanger) {
        this.cameraPositionChanger = cameraPositionChanger;
    }

    private CameraPositionChanger cameraPositionChanger;

    public LevelProceeder getLevelProceeder() {
        return levelProceeder;
    }

    public void setLevelProceeder(LevelProceeder levelProceeder) {
        this.levelProceeder = levelProceeder;
    }

    private LevelProceeder levelProceeder= LevelProceederSet.noneProceed;
    private Vector2 buf = new Vector2(), d = new Vector2();

    protected Level() {
        this.world = new World(new Vector2(0.0f, 0.0f), true);
        this.world.setContactListener(this);
        particles = new ArrayList<Fraction>();
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
                Level.this.addAction(moveAction);
            }
        }, 0, 32 / 1000f);
        this.stepTimer.start();
    }

    public final void setCameraPosition(){
        if(this.cameraPositionChanger!=null)
            this.cameraPositionChanger.changeCameraPosition(this,this.getStage().getCamera(),this.getStage());
    }

    public final void preRender(){
        if(this.preRenderer!=null)
            this.preRenderer.preRender(this);
    }

    public final void render(float delta) {
        if (this.levelScreen != null) {
            renderBorders();
            renderOthers();
            renderFractions();
        }
    }

    public void renderBorders() {
        levelScreen.drawBorders();
    }

    public void renderOthers() {
        levelScreen.drawOthers();
    }

    public void renderFractions() {
        levelScreen.drawFractions();
    }

    public final void afterRender(){
        if(this.afterRenderer!=null){
            this.afterRenderer.afterRender(this);
        }
    }
    public void proceedParticles(float delta){
        this.levelScreen.proceed(delta);
    }

    public final void load(final LevelScreen screen) {
        //loadAssets();
        new Thread(new Runnable() {
            @Override
            public void run() {
                /*
                while (getAssetsLoaded() < 1) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }*/
                try {
                    Thread.sleep(MathUtils.random(0,100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setLoaded(0.0f / 7);
                setSizes();
                try {
                    Thread.sleep(MathUtils.random(0,100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setLoaded(1.0f / 7);
                setLevelScreen(screen);
                try {
                    Thread.sleep(MathUtils.random(0,100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setLoaded(2.0f / 7);
                createWalls();
                try {
                    Thread.sleep(MathUtils.random(0,100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setLoaded(3.0f / 7);
                setParticles();
                try {
                    Thread.sleep(MathUtils.random(0,100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setLoaded(4.0f / 7);
                setOtherElements();
                try {
                    Thread.sleep(MathUtils.random(0,100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setLoaded(5.0f / 7);
                setParameters();
                try {
                    Thread.sleep(MathUtils.random(0,100));
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
                    Thread.sleep(MathUtils.random(0,100));
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
        System.out.println(loaded);
        this.loaded = loaded;
    }

    abstract protected void setSizes();

    protected abstract void setParticles();

    abstract public void setOtherElements();

    protected abstract Mission createMission();

    protected void setParameters() {
    }

    public void createWalls() {
        ChainShape shape = new ChainShape();
        shape.createChain(new float[]{

                getXMin(), getYMin(),
                getXMin(), getYMax(),
                getXMax(), getYMax(),
                getXMax(), getYMin(),
                getXMin(), getYMin()
        });

        this.addBorder(new Border(this.getWorld(), 0, 0, shape, true));
    }

    public final void tap(float x, float y){
        if(this.levelTapper!=null)
            this.levelTapper.tapLevel(this, x, y);
    }

    final public LevelScreen getStage() {
        return this.levelScreen;
    }

    public World getWorld() {
        return world;
    }

    synchronized final public Fraction addFraction(Fraction f) {
        if (f == null) return null;
        particles.add(f);
        if (levelScreen != null)
            levelScreen.addFractionActor(f);
        return f;
    }

    synchronized final public void removeFraction(Fraction fraction) {
        System.out.println("removing fraction");
        particles.remove(fraction);
        fraction.getBody().setActive(false);
        this.world.destroyBody(fraction.getBody());
        if (levelScreen != null)
            levelScreen.removeFractionActor(fraction);
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

    public final Fraction getFraction(int index) {
        return this.particles.get(index);
    }

    public void Move(float time) {
        if(this.levelProceeder!=null){
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
                if(currentMissionChecker.isFinished()) {
                    System.out.println(currentMissionChecker.isFinished());
                    System.exit(0);
                }
            }
        }
    }

    private synchronized boolean isMoveActionPresent() {
        synchronized (this.actions) {
            return this.actions.contains(moveAction);
        }
    }

    protected synchronized final void addAction(ActionForNextStep action) {
        synchronized (this.actions) {

            if (action == moveAction && isMoveActionPresent()) {
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
        this.k = k;
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

    protected final float getXMin() {
        return this.xMin;
    }

    protected final float getYMin() {
        return this.yMin;
    }

    protected final float getXMax() {
        return this.xMax;
    }

    protected final float getYMax() {
        return this.yMax;
    }

    protected final float setXMin(float newXMin) {
        return this.xMin = newXMin;
    }

    protected final float setYMin(float newYMin) {
        return this.yMin = newYMin;
    }

    protected final float setXMax(float newXMax) {
        return this.xMax = newXMax;
    }

    protected final float setYMax(float newYMax) {
        return this.yMax = newYMax;
    }

    final public Mission getMission() {
        return this.currentMissionChecker.getMission();
    }

    void contactFractions(Fraction f1, Fraction f2, Contact contact) {
        flowMass(f1, f2);
    }

    public void pauseLevel() {
        this.isMove = false;
        this.stepTimer.stop();
        this.currentMissionChecker.pause();
    }

    public void resumeLevel() {
        this.isMove = true;
        this.stepTimer.start();
        this.stepTimer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                Level.this.addAction(moveAction);
            }
        }, 0, 32 / 1000f);
        this.currentMissionChecker.resume();
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void beginContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        if (contact.getFixtureA().getBody().getUserData() instanceof Fraction && contact.getFixtureB().getBody().getUserData() instanceof Fraction) {
            Fraction f1 = (Fraction) contact.getFixtureA().getBody().getUserData(), f2 = (Fraction) contact.getFixtureB().getBody().getUserData();
            contactFractions(f1, f2, contact);
        }

    }


    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
    //additional methods for management of the world and the particles

    public void processAccelerometer(float factor) {
        float y = Gdx.input.getAccelerometerY();
        float x = Gdx.input.getAccelerometerX();
        if (prevAccelX != x || prevAccelY != y) {
            world.setGravity(new Vector2(factor * y, -factor * x));
            prevAccelX = x;
            prevAccelY = y;
        }
    }

    public void interactionBetweenFractions(Fraction f1, Fraction f2) {
        d.set(f2.getBody().getPosition());
        d.add(-f1.getBody().getPosition().x, -f1.getBody().getPosition().y);
        float F = (((-this.k * f1.getCharge() * f2.getCharge() + this.G) * f1.getBody().getMass() * f2.getBody().getMass()) / (d.len() * d.len()));
        buf.set(f2.getBody().getPosition().x - f1.getBody().getPosition().x, f2.getBody().getPosition().y - f1.getBody().getPosition().y);
        buf.setLength(F);
        if (F < 0) buf.rotate(180);
        f1.getBody().applyForceToCenter(buf, true);
        f2.getBody().applyForceToCenter(buf.rotate(180), true);

        float q1 = f1.getCharge(), q2 = f2.getCharge(), m1 = f1.getMass(), m2 = f2.getMass();
        float deltaCharge = this.chargingK * (((q1 * m1 + q2 * m2) / (m1 + m2) + 1024 * q2) / 1025 - q2) / (d.len() * d.len());

        if (!Float.isNaN(deltaCharge) && !Float.isInfinite(deltaCharge))
            try {
                f1.moveParameters(f2, 0, deltaCharge, 0, new Vector2(0, 0));
            } catch (Fraction.NullMassException e) {
                System.out.println("NullException " + e.toString());
                this.removeFraction(e.getFraction());
            }

    }

    public void interactAllWithAllFractions() {
        for (int i = 0; i < particles.size(); i++) {
            Fraction f1 = particles.get(i);
            if (f1 != null) {
                for (int j = i + 1; j < particles.size(); j++) {
                    interactionBetweenFractions(f1, particles.get(j));
                }
            }
        }
    }

    public void flowMass(final Fraction f1, final Fraction f2) {
        this.addAction(new ActionForNextStep() {
            @Override
            public void doSomethingOnStep(Level level) {
                Fraction from = f1, to = f2;
                if (f1.getMass() > f2.getMass()) {
                    from = f2;
                    to = f1;
                }
                float deltaMass = MathUtils.clamp(from.getMass() * to.getMass() * Level.this.massFlowingK * 0.001f, 0.000001f, 1f);
                try {
                    from.moveParameters(to, deltaMass, 0, 0, new Vector2(0, 0));
                } catch (Fraction.NullMassException e) {
                    Level.this.removeFraction(e.getFraction());
                }

            }
        });
    }
     public void divideOnTap(final Fraction fraction, final float speed, final float piece, final float x, final float y){
         this.addAction(new ActionForNextStep() {
             @Override
             public void doSomethingOnStep(Level level) {
                 Vector2 v = new Vector2(fraction.getPosition());
                 v.rotate(180);
                 v.add(x, y);
                 v.setLength(speed);
                 Fraction newFraction = fraction.divide(Level.this.getFraction(0).getMass() * piece, v.x, v.y);
                 level.addFraction(
                         newFraction
                 );

             }
         });
     }
    public void moveCamera(float xTo,float yTo,float delay){
        this.getStage().getCamera().position.set((delay*this.getStage().getCamera().position.x+xTo)/(delay+1),(delay*this.getStage().getCamera().position.y+yTo)/(delay+1),0);
    }

    //methods for generating
    public void addRandomFractions(final FractionDef fractionDef, final int count){
        this.addAction(new ActionForNextStep() {
            @Override
            public void doSomethingOnStep(Level level) {
                for (int i = 0; i < count; i++) {
                    Level.this.addFraction(Level.this.generateRandomFraction(fractionDef));
                }
            }
        });
    }

    public Fraction generateRandomFraction(FractionDef fractionDef) {
        return new Fraction(this,this.getWorld(),
                MathUtils.random(fractionDef.minX, fractionDef.maxX),
                MathUtils.random(fractionDef.minY, fractionDef.maxY),
                MathUtils.random(fractionDef.minVX, fractionDef.maxVX), MathUtils.random(fractionDef.minVY, fractionDef.maxVY),
                MathUtils.random(fractionDef.minMass, fractionDef.maxMass), (MathUtils.random(fractionDef.minCharge, fractionDef.maxCharge)), 1, 1, 1, Fraction.Condition.Liquid,
                new Color(MathUtils.random(fractionDef.rMin, fractionDef.rMax),
                        MathUtils.random(fractionDef.gMin, fractionDef.gMax),
                        MathUtils.random(fractionDef.bMin, fractionDef.bMax),
                        MathUtils.random(fractionDef.aMin, fractionDef.aMax)));
    }
}