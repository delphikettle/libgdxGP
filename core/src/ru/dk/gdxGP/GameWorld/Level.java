package ru.dk.gdxGP.GameWorld;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.utils.Box2DBuild;
import com.badlogic.gdx.scenes.scene2d.Actor;
import ru.dk.gdxGP.GameWorld.WorldElements.Border;
import ru.dk.gdxGP.GameWorld.WorldElements.Fraction;
import ru.dk.gdxGP.Screens.LevelScreen;

import java.util.ArrayList;
import java.util.LinkedList;


public abstract class Level extends Thread implements Runnable,ContactListener
{
	private final World world;
	private final ArrayList<Fraction> particles;
	private final ArrayList<Border> borders;
	private final ArrayList<Actor> otherElements;
	private LevelScreen levelScreen;
	private float prevAccelX;
	private float prevAccelY;
	private LinkedList<ActionForNextStep> actions;

	private int xMin, xMax, yMin, yMax;

	private float G=1;
	private float k=10;
	private float timeFactor=1f;
	private static long currentRealTime;
	private float currentGameTime;

	private boolean isMove=false, isEnd=false;

	private float loaded;

	public Level() {
        this.world=new World(new Vector2(0.0f,0.0f),true);
        this.world.setContactListener(this);
		particles = new ArrayList<Fraction>();
        borders=new ArrayList<Border>();
		otherElements=new ArrayList<Actor>();
		actions=new LinkedList<ActionForNextStep>();
		actions.add(new ActionForNextStep() {
			@Override
			public void doSomethingOnStep(Level level) {
				System.out.println("Action System works fine!");
			}
		});
		xMin = yMin =0;
		xMax = Gdx.graphics.getWidth();
		yMax = Gdx.graphics.getHeight();
		this.isMove=true;
		//this.setDaemon(true);
	}

	abstract public void setCameraPosition();
	abstract public void preRender();
	public void render(float delta){
		if(this.levelScreen!=null){
			renderBorders();
			renderOthers();
			renderFractions();
		}
	}
	public void renderBorders(){
		levelScreen.drawBorders();
	}
	public void renderOthers(){
		levelScreen.drawOthers();
	}
	public void renderFractions(){
		levelScreen.drawFractions();
	}
	abstract public void afterRender();
	public void proceed(float deltaTime){
		//this.levelScreen.proceed(deltaTime);
		processAccelerometer();
		interactAllWithAllFractions();
		if(this.actions.size()>0){
			this.actions.pop().doSomethingOnStep(this);
		}
	}

	public void load(final LevelScreen screen){
		new Thread(new Runnable() {
			@Override
			public void run() {
				setSizes();
				setLoaded(1.0f / 5);
				setLevelScreen(screen);
				setLoaded(2.0f / 5);
				createWalls();
				setLoaded(3.0f / 5);
				setParticles();
				setLoaded(4.0f / 5);
				setOtherElements();
				setLoaded(5.0f / 5);
			}
		}).start();
	}


	private void setLoaded(float loaded) {
		this.loaded = loaded;
	}

	public void setLevelScreen(LevelScreen screen){
		this.levelScreen=screen;
		//здесь нужно добавить частицы, границы и прочее к стейджам скрина
	}
    abstract public void setSizes();
	abstract public void setParticles();
    abstract public void setOtherElements();
    public void createWalls(){
        ChainShape shape=new ChainShape();
        shape.createChain(new float[]{

                getXMin(), getYMin(),
                getXMin(), getYMax(),
                getXMax(), getYMax(),
                getXMax(), getYMin(),
                getXMin(), getYMin()
        });

		this.addBorder(new Border(this.getWorld(),0,0,shape,true));
    }
	abstract public void tap(float x, float y);
	final public LevelScreen getStage(){return this.levelScreen;}

    public World getWorld() {
        return world;
    }

    synchronized final public Fraction addFraction(Fraction f){
        particles.add(f);
        if(levelScreen!=null)
			levelScreen.addFractionActor(f);
        return f;
    }
	synchronized final public Border addBorder(Border border){
		borders.add(border);
		if(levelScreen!=null)
			levelScreen.addBorderActor(border);
		return border;
	}
	synchronized final public Actor addActor(Actor actor){
		otherElements.add(actor);
		if(levelScreen!=null)
			levelScreen.addOtherActor(actor);
		return actor;
	}
	final public Fraction getFraction(int index){
		return this.particles.get(index);
	}
	public long maxOp=0;
	public String maxOpName=""; long time=System.nanoTime(),elTime;

	/*
    public synchronized void drawBorders(Batch b){
        b.begin();
        b.draw(borderTexture, this.getXMin()-5, getYMin()-5, getXMax() - getXMin()+10, 10);
        b.draw(borderTexture,this.getXMin()-5,getYMin()-5,10,getYMax()-getYMin()+10);
        b.draw(borderTexture,this.getXMin()-5,getYMax()-5,getXMax()-getXMin()+10,10);
        b.draw(borderTexture,this.getXMax()-5,getYMin()-5,10,getYMax()-getYMin()+10);
        b.end();
    }
    */
	private float lostTime=0.0f;
	private void Move(float time){
		 ///if(time!=0.0f) {
		//	 System.out.println(((double)(time)));
		 //}
		 //world.step(time/60f, 1, 1);
		/*
		 if(time>0){
			 lastTime=MathUtils.clamp(time,lastTime/2.0f,lastTime*1.5f);
			 this.proceed(lastTime);
			 world.step(lastTime/60.0f, 10, 10);
		 }*/
		float stepTime=time+lostTime;
		int normalStepTime=(int)stepTime;
		lostTime=stepTime-(float)normalStepTime;
		this.proceed(time);
		for (int i = 0; i < normalStepTime; i++) {
			world.step(1 / 60f, 10, 10);
		}
		if(MathUtils.random.nextInt(1024)==MathUtils.random.nextInt(1024))System.out.println(time);

	}
	 final private float getNextStepTime(){

		return (-currentGameTime+(currentGameTime=(this.timeFactor*1.0f*(-currentRealTime+(currentRealTime=System.currentTimeMillis()))+currentGameTime)));
		
	}

	public float getLoaded() {
		return loaded;
	}

	@Override
	final public void run() {
		super.run();
		currentRealTime=System.currentTimeMillis();
		currentGameTime=0;
		while (!isEnd) {
			if (isMove) {
				if(this.getLoaded()>=1.0f) {
					//if(MathUtils.random.nextInt(1024)==MathUtils.random.nextInt(1024))System.out.println(getNextStepTime());
					this.Move(this.getNextStepTime());
				}
			}
		}
	}
	public final void addAction(ActionForNextStep action){
		this.actions.add(action);
	}
	public float getK() {
		return k;
	}
	public void setK(float k) {
		this.k = k;
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


	public void pauseLevel(){
		this.isMove=false;
		currentRealTime=System.currentTimeMillis();
	}
	public void resumeLevel(){
		currentRealTime=System.currentTimeMillis();
		this.isMove=true;
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

	//additional methods for management of the world and the particles

	public void processAccelerometer() {

		/* Get accelerometer values */
		float y = Gdx.input.getAccelerometerY();
		float x = Gdx.input.getAccelerometerX();

		/*
		 * If accelerometer values have changed since previous processing,
		 * change world gravity.
		 */
		if (prevAccelX != x || prevAccelY != y) {

			/* Negative on the x axis but not in the y */
			world.setGravity(new Vector2(1.0f*y, -1.0f*x));

			/* Store new accelerometer values */
			prevAccelX = x;
			prevAccelY = y;
		}
	}

	Vector2 buf=new Vector2();


	public void interactionBetweenFractions(Fraction f1,Fraction f2){
		float F= (float) ( ((-this.k*f1.getCharge()*f2.getCharge()+this.G)*f1.getBody().getMass()*f2.getBody().getMass())
                        /((f1.getBody().getPosition().x - f2.getBody().getPosition().x)*(f1.getBody().getPosition().x - f2.getBody().getPosition().x)+(f1.getBody().getPosition().y - f2.getBody().getPosition().y)*(f1.getBody().getPosition().y - f2.getBody().getPosition().y)));
		buf.set(f2.getBody().getPosition().x-f1.getBody().getPosition().x,f2.getBody().getPosition().y-f1.getBody().getPosition().y);
		buf.setLength(F);
		if(F<0)buf.rotate(180);
		f1.getBody().applyForceToCenter(buf, true);
		f2.getBody().applyForceToCenter(buf.rotate(180),true);
	}

	public void interactAllWithAllFractions(){
		for(int i=0;i<particles.size();i++){
			Fraction f1=particles.get(i);
			if(f1!=null){
				for (int j = i+1; j < particles.size(); j++) {
					interactionBetweenFractions(f1,particles.get(j));
				}
			}
		}
	}
}