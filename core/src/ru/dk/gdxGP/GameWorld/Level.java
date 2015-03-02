package ru.dk.gdxGP.GameWorld;

//import android.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;


public abstract class Level extends Thread implements Runnable,ContactListener
{
	private ArrayList<Fraction> particles;
    private World world;
	private LevelStage stage;
    private ArrayList<Border> borders;
	private float prevAccelX;
	private float prevAccelY;

	public class LevelStage extends Stage{
        private Box2DDebugRenderer box2DDebugRenderer;
        private OrthographicCamera camera;
		private Level level;
		LevelStage(Level level){
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
	}


	//borders:
	private int xMin, xMax, yMin, yMax;
	private float G=1;
	private float timeFactor=1;
	private static long currentRealTime;
	private float currentGameTime;
	private boolean isMove=false, isEnd=false;
	private volatile boolean isComponentsChanged=true;
	private Object[] componentArray=null;
    private Texture borderTexture;
    private Fraction testParticle;

    private float timeFromLastRecount=0;
	public Level(int w, int h) {
        System.out.println(w+";"+h);
		stage=new LevelStage(this);
        this.world=new World(new Vector2(0.0f,0.0f),false);
        this.world.setContactListener(this);
		particles = new ArrayList<Fraction>();
        borders=new ArrayList<Border>();
		componentArray=particles.toArray();
		xMin = yMin =0;
		xMax = w;
		yMax = h;
        setSizes();
        System.out.println(this.getXMax()+":xMax");
        setParticles(w,h);
        setBorders(w,h);
        createWalls();
        borderTexture=new Texture("border01.png");
		currentRealTime=System.currentTimeMillis();
		currentGameTime=0;
		this.isMove=true;
		this.setDaemon(true);
        //this.testParticle=new Fraction(this.world,0,0,100,50,400);
		System.out.println("Time: " + "LevelCreated" + System.currentTimeMillis());
	}

	abstract public void setCameraPosition();
	abstract public void preRender();
	abstract public void render();
	public void afterRender(){
		this.getStage().act();
	}

    abstract public void setSizes();
	abstract public void setParticles(int w, int h);
    abstract public void setBorders(int w, int h);
    public void createWalls(){
        BodyDef bodyDef=new BodyDef();
        bodyDef.type= BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0,0);
        bodyDef.active=true;

        ChainShape shape=new ChainShape();
        shape.createChain(new float[]{

                getXMin(), getYMin(),
                getXMin(), getYMax(),
                getXMax(), getYMax(),
                getXMax(), getYMin(),
                getXMin(), getYMin()
        });
        FixtureDef fixtureDef=new FixtureDef();
        fixtureDef.shape=shape;

        fixtureDef.friction=1.0f;
        fixtureDef.density = 0.5f;
        fixtureDef.restitution=1.0f;
        /*fixtureDef.isSensor=false;*/
        Body body=world.createBody(bodyDef);
        Fixture fixture=body.createFixture(fixtureDef);
		fixture.setFriction(1.0f);
		fixture.setDensity(1.0f);
		fixture.setRestitution(1.0f);
        body.setUserData("border");
    }

	final public LevelStage getStage(){return this.stage;}

    public World getWorld() {
        return world;
    }

    synchronized final public Fraction addFraction(Fraction f){
        particles.add(f);
        isComponentsChanged=true;
        stage.addActor(f);
        return f;
    }
	public long maxOp=0;
	public String maxOpName=""; long time=System.nanoTime(),elTime;

    public synchronized void drawBorders(Batch b){

        b.begin();
        b.draw(borderTexture, this.getXMin()-5, getYMin()-5, getXMax() - getXMin()+10, 10);
        b.draw(borderTexture,this.getXMin()-5,getYMin()-5,10,getYMax()-getYMin()+10);
        b.draw(borderTexture,this.getXMin()-5,getYMax()-5,getXMax()-getXMin()+10,10);
        b.draw(borderTexture,this.getXMax()-5,getYMin()-5,10,getYMax()-getYMin()+10);
        b.end();

    }
	synchronized private void Move(float time){
		world.step(time/60f, 1, 1);
		processAccelerometer();
	}

	synchronized final private float getNextStepTime(){
		return -currentGameTime+ (currentGameTime=(this.timeFactor*1.0f*(-currentRealTime+(currentRealTime=System.currentTimeMillis()))+currentGameTime));
		
	}

	@Override
	final public void run() {
		super.run();
		currentRealTime=System.currentTimeMillis();
		currentGameTime=0;
		while (!isEnd)
			if(isMove) {
                this.Move(this.getNextStepTime());
			}
	}
	final public float getG(){
		return G;
	}
	final public float setG(float newG){
		return this.G=newG;
	}
	final public float getTimeFactor(){
		return this.timeFactor;
	}
	final public float setTimeFactor(float newTimeFactor){
		return newTimeFactor > 0 ? (this.timeFactor = newTimeFactor) : this.timeFactor;
	}
	final public int getXMin(){
		return this.xMin;
	}
	final public int getYMin(){
		return this.yMin;
	}
	final public int getXMax(){
		return this.xMax;
	}
	final public int getYMax(){
		return this.yMax;
	}
	final public int setXMin(int newXMin){
		return this.xMin=newXMin;
	}
	final public int setYMin(int newYMin){
		return this.yMin=newYMin;
	}
	final public int setXMax(int newXMax){
		return this.xMax=newXMax;
	}
	final public int setYMax(int newYMax){
		return this.yMax=newYMax;
	}
	final public float getCurrentGameTime(){
		return this.currentGameTime;
	}

	public ArrayList<Fraction> getParticles() {
		return particles;
	}

	public void Pause(){

	}
	public void Resume(){

	}
    @Override
    public void endContact(Contact contact) {

    }
    @Override
    public void beginContact(Contact contact) {
    }
    @Override
    public void preSolve (Contact contact, Manifold oldManifold){
    }
    @Override
    public void postSolve (Contact contact, ContactImpulse impulse){
    }


	private void processAccelerometer() {

		/* Get accelerometer values */
		float y = Gdx.input.getAccelerometerY();
		float x = Gdx.input.getAccelerometerX();

		/*
		 * If accelerometer values have changed since previous processing,
		 * change world gravity.
		 */
		if (prevAccelX != x || prevAccelY != y) {

			/* Negative on the x axis but not in the y */
			world.setGravity(new Vector2(y, -x));

			/* Store new accelerometer values */
			prevAccelX = x;
			prevAccelY = y;
		}
	}
}
