package ru.dk.gdxGP.GameWorld;

//import android.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public abstract class Level extends Thread implements Runnable,ContactListener
{
	private ArrayList<Actor> particles;
    private World world;
	private LevelStage stage;
    private ArrayList<Border> borders;
	private class LevelStage extends Stage{
        private Box2DDebugRenderer box2DDebugRenderer;
        private OrthographicCamera camera;
		LevelStage(){
            box2DDebugRenderer = new Box2DDebugRenderer();
            /*camera = new OrthographicCamera(20,
                    20 * (Gdx.graphics.getHeight() / (float) Gdx.graphics
                            .getWidth()));
			this.getViewport().setCamera(camera);*/
            //camera=new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
            //camera.position.set(particles.get(0).getX(),particles.get(0).getY(),0);
            //camera.far=0;
		}
		@Override
		public void draw() {
			/*
			this.getCamera().position.set(
					(particles.get(0).getX()+this.getCamera().position.x*255)/256,
					(particles.get(0).getY()+this.getCamera().position.y*255)/256,
					0);
					*/
			super.draw();
            drawBorders(this.getBatch());
            this.act();
			world.step(getNextStepTime() / 60f, 6, 2);
            //box2DDebugRenderer.render(world, this.getCamera().combined);
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
	public long getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(long maxDistance) {
		if(maxDistance>0)this.maxDistance = maxDistance;
	}

	private long maxDistance=10000;
	public Level(int w, int h) {
        System.out.println(w+";"+h);
		stage=new LevelStage();
        this.world=new World(new Vector2(0.0f,-1f),false);
        this.world.setContactListener(this);
		particles = new ArrayList<Actor>();
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
    synchronized public void addBorder(Border border){
        stage.addActor(border);
        borders.add(border);
    }
	final public Stage getStage(){return this.stage;}

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

    synchronized private void drawBorders(Batch b){

        b.begin();
        b.draw(borderTexture, this.getXMin()-5, getYMin()-5, getXMax() - getXMin()+10, 10);
        b.draw(borderTexture,this.getXMin()-5,getYMin()-5,10,getYMax()-getYMin()+10);
        b.draw(borderTexture,this.getXMin()-5,getYMax()-5,getXMax()-getXMin()+10,10);
        b.draw(borderTexture,this.getXMax()-5,getYMin()-5,10,getYMax()-getYMin()+10);
        b.end();

    }
	synchronized private void Move(float time){
        this.world.step(time,10, 10);
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
    public void CollisionWithBorder(Fraction fraction){
        fraction.getBody().getLinearVelocity().set(-fraction.getBody().getLinearVelocity().x,-fraction.getBody().getLinearVelocity().y);
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
}
