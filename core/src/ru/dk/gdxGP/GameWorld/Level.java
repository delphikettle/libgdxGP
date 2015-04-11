package ru.dk.gdxGP.GameWorld;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Timer;
import ru.dk.gdxGP.GDXGameGP;
import ru.dk.gdxGP.GameWorld.WorldElements.Border;
import ru.dk.gdxGP.GameWorld.WorldElements.Fraction;
import ru.dk.gdxGP.Screens.LevelScreen;

import java.util.*;


public abstract class Level extends Thread implements Runnable,ContactListener
{
	private final World world;
	private final ArrayList<Fraction> particles;
	private final ArrayList<Border> borders;
	private final ArrayList<Actor> otherElements;
	private LevelScreen levelScreen;
	private float prevAccelX;
	private float prevAccelY;
	private final List<ActionForNextStep> actions;
	private Timer stepTimer;
	private int xMin, xMax, yMin, yMax;
	private float G=1;
	private float k=1;

	private float chargingK=1;
	private float timeFactor=1f;
	private static long currentRealTime;
	private float currentGameTime;
	private boolean isMove=false, isEnd=false;
	private float loaded;
	private TaskChecker currentTaskChecker;
	private final static ActionForNextStep moveAction = new ActionForNextStep() {
		@Override
		public void doSomethingOnStep(Level level) {
			level.Move(16);
		}
	};

	public Level() {
        this.world=new World(new Vector2(0.0f,0.0f),true);
        this.world.setContactListener(this);
		particles = new ArrayList<Fraction>();
        borders=new ArrayList<Border>();
		otherElements=new ArrayList<Actor>();
		actions=Collections.synchronizedList(new ArrayList<ActionForNextStep>());
		xMin = yMin =0;
		xMax = Gdx.graphics.getWidth();
		yMax = Gdx.graphics.getHeight();
		this.isMove=true;
		//this.setDaemon(true);
		this.stepTimer=new Timer();
		this.stepTimer.scheduleTask(new Timer.Task() {
			@Override
			public void run() {
				Level.this.addAction(moveAction);
			}
		}, 0,32/1000f);
		this.stepTimer.start();
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
		/*
		if(this.actions.size()>0){
			this.actions.pop().doSomethingOnStep(this);
		}
		*/
	}

	public final void load(final LevelScreen screen){
		loadAssets();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(getAssetsLoaded()<1){
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				setLoaded(0.0f / 5);
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
		System.out.println(loaded);
		this.loaded = loaded;
	}

	public void setLevelScreen(LevelScreen screen){
		this.levelScreen=screen;
		//здесь нужно добавить частицы, границы и прочее к стейджам скрина
	}
	public void loadAssets(String[] assetsPaths){
		for (int i = 0; i < assetsPaths.length; i++) {
			GDXGameGP.assetManager.load(assetsPaths[i],Texture.class);
		}
	}

	public float getLoaded() {
		GDXGameGP.assetManager.update();
		return 0.6f*loaded+0.4f*GDXGameGP.assetManager.getProgress();
	}
	public static String[] standardAssetsPaths = new String[]{
			"images/PlusCharge.png",
			"images/NullCharge.png",
			"images/MinusCharge.png",
			"images/FractionSolid01.png",
			"images/FractionSolid.png",
			"images/FractionLiquid.png",
			"images/charge.png",
			"border01.png"
	};
	abstract public void loadAssets();
	public float getAssetsLoaded(){
		System.out.println("assetsLoaded="+GDXGameGP.assetManager.getProgress());
		return GDXGameGP.assetManager.getProgress();
	}
	public boolean areAssetsLoaded(){
		return GDXGameGP.assetManager.update();
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
		if(f==null)return null;
        particles.add(f);
        if(levelScreen!=null)
			levelScreen.addFractionActor(f);
        return f;
    }
	synchronized final public void removeFraction(Fraction fraction){
		System.out.println("removing fraction");
		particles.remove(fraction);
		fraction.getBody().setActive(false);
		this.world.destroyBody(fraction.getBody());
		if(levelScreen!=null)
			levelScreen.removeFractionActor(fraction);
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
	private void Move(float time){
		this.proceed(time);
		world.step(1/100f,16,16);
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
		/*
		float stepTime=time+lostTime;
		int normalStepTime=(int)stepTime;
		lostTime=stepTime-(float)normalStepTime;
		this.proceed(time);
		for (int i = 0; i < normalStepTime; i++) {
			world.step(1 / 60f, 10, 10);
		}
		*/
		//if(MathUtils.random.nextInt(1024)==MathUtils.random.nextInt(1024))System.out.println(time);

		//this.proceed(time);
		//this.world.step(1/100f,10,10);
	}
	 final private float getNextStepTime(){

		return (-currentGameTime+(currentGameTime=(this.timeFactor*1.0f*(-currentRealTime+(currentRealTime=System.currentTimeMillis()))+currentGameTime)));
		
	}

	@Override
	final public void run() {
		super.run();
		while (!isEnd) {
			Gdx.app.log("run", "!!! running "+isMove);
			if (isMove) {
				if(this.getLoaded()>=1.0f) {
						if(!this.actions.isEmpty()){
							Gdx.app.log("run", "!!! "+this.actions.size());
							this.actions.remove(0).doSomethingOnStep(this);
						}
					Gdx.app.log("run", "!!! running3 "+isMove);
				}
				Gdx.app.log("run", "!!! running2 "+isMove);
			}
			Gdx.app.log("run", "!!! running1 "+isMove);
		}
		Gdx.app.log("run", "!!! ended");
	}
	private synchronized boolean isMoveActionPresent(){
		synchronized (this.actions) {
			return this.actions.contains(moveAction);
		}
	}
	public synchronized final void addAction(ActionForNextStep action){
		synchronized (this.actions) {

			if(action==moveAction&&isMoveActionPresent()) {
				//System.err.println();
				//Gdx.app.log("addAction","action was not added");
				return;
			}
			//System.out.println("action added");
			//Gdx.app.log("addAction","action was added "+(action==moveAction));
			this.actions.add(action);
		}
	}
	public float getChargingK() {
		return chargingK;
	}
	public void setChargingK(float chargingK) {
		this.chargingK = chargingK;
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
	public int getWidth(){
		return getXMax()-getXMin();
	}
	public int getHeight(){
		return getYMax()-getYMin();
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

	public void contactFractions(Fraction f1, Fraction f2,Contact contact){
		if(f1.getCondition()!= Fraction.Condition.Solid||f2.getCondition()!= Fraction.Condition.Solid){
			//contact.setEnabled(false);
		}
		if(f1.getCondition()== Fraction.Condition.Liquid&&f2.getCondition()== Fraction.Condition.Liquid){
			//moving mass
			flowMass(f1,f2);
			//System.out.println("flowing mass:");
		}
		if((f1.getCondition()==Fraction.Condition.Liquid&&f2.getCondition()!=Fraction.Condition.Liquid)||
				(f2.getCondition()==Fraction.Condition.Liquid&&f1.getCondition()!=Fraction.Condition.Liquid)){
			//jointing bodies
		}
	}

	public void pauseLevel(){
		this.isMove=false;
		currentRealTime=System.currentTimeMillis();
		this.stepTimer.stop();

	}
	public void resumeLevel(){
		currentRealTime=System.currentTimeMillis();
		this.isMove=true;
		this.stepTimer.start();
		this.stepTimer.scheduleTask(new Timer.Task() {
			@Override
			public void run() {
				Level.this.addAction(moveAction);
			}
		}, 0, 32 / 1000f);
	}
    @Override
    public void endContact(Contact contact) {

    }
    @Override
    public void beginContact(Contact contact) {
    }
    @Override
    public void preSolve (Contact contact, Manifold oldManifold){
		if(contact.getFixtureA().getBody().getUserData() instanceof Fraction && contact.getFixtureB().getBody().getUserData() instanceof Fraction){
			Fraction f1= (Fraction) contact.getFixtureA().getBody().getUserData(), f2= (Fraction) contact.getFixtureB().getBody().getUserData();
			//contactFractions(f1,f2,contact);
		}

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
			world.setGravity(new Vector2(y, -x));

			/* Store new accelerometer values */
			prevAccelX = x;
			prevAccelY = y;
		}
	}

	private Vector2 buf=new Vector2(),d=new Vector2();


	public void interactionBetweenFractions(Fraction f1,Fraction f2){
		d.set(f2.getBody().getPosition());
		d.add(-f1.getBody().getPosition().x,-f1.getBody().getPosition().y);
		float F=  ( ((-this.k*f1.getCharge()*f2.getCharge()+this.G)*f1.getBody().getMass()*f2.getBody().getMass())/(d.len()*d.len()));
                        ///((f1.getBody().getPosition().x - f2.getBody().getPosition().x)*(f1.getBody().getPosition().x - f2.getBody().getPosition().x)+(f1.getBody().getPosition().y - f2.getBody().getPosition().y)*(f1.getBody().getPosition().y - f2.getBody().getPosition().y)));
		buf.set(f2.getBody().getPosition().x-f1.getBody().getPosition().x,f2.getBody().getPosition().y-f1.getBody().getPosition().y);
		buf.setLength(F);
		if(F<0)buf.rotate(180);
		f1.getBody().applyForceToCenter(buf, true);
		f2.getBody().applyForceToCenter(buf.rotate(180),true);

		float q1=f1.getCharge(),q2=f2.getCharge(),m1=f1.getMass(),m2=f2.getMass();
		float deltaCharge=this.chargingK*(((q1*m1+q2*m2)/(m1+m2)+1024*q2)/1025-q2)/(d.len()*d.len());
				//((f1.getBody().getPosition().x - f2.getBody().getPosition().x)*(f1.getBody().getPosition().x - f2.getBody().getPosition().x)
				//+(f1.getBody().getPosition().y - f2.getBody().getPosition().y)*(f1.getBody().getPosition().y - f2.getBody().getPosition().y));
		//if(MathUtils.random.nextInt(1024)==MathUtils.random.nextInt(1024))System.out.println(deltaCharge);

		if(!Float.isNaN(deltaCharge)&&!Float.isInfinite(deltaCharge))
			try {
				//System.out.println("moving parameters with deltaCharge"+deltaCharge);
				f1.moveParameters(f2,0,deltaCharge,0,new Vector2(0,0));
			} catch (Fraction.NullMassException e) {
				System.out.println("NullException "+e.toString());
				this.removeFraction(e.getFraction());
			}

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

	public void flowMass(Fraction f1, Fraction f2){
		d.set(f2.getBody().getPosition());
		d.add(-f1.getBody().getPosition().x,-f1.getBody().getPosition().y);
		Fraction from=f1,to=f2;
		if(f1.getMass()>f2.getMass()){
			from=f2;
			to=f1;
		}
		if(from.getMass()==to.getMass())return;
		float d=this.d.len();
		if(d>from.getRadius()+to.getRadius())return;
		float a=to.getMass(),b=from.getMass();
		float sqrt=(float)(Math.PI*(2*a*d*d+2*b*d*d-Math.PI*Math.pow(d,4)));
		if(sqrt<=0)return;
		final float mass=(float)(Math.sqrt(sqrt)-a+b)/2;
		if(mass<=0)return;
		final Fraction finalFrom = from;
		final Fraction finalTo = to;

		this.addAction(new ActionForNextStep() {
			@Override
			public void doSomethingOnStep(Level level) {
				try {
					finalFrom.moveParameters(finalTo, mass, 0, 0, new Vector2(0, 0));
				} catch (Fraction.NullMassException e) {
					Level.this.removeFraction(e.getFraction());
				}
			}
		});

	}

	//methods for generating

	public Fraction generateRandomFraction(FractionDef fractionDef){
		Random rnd=new Random();
		return new Fraction(this.getWorld(),
				MathUtils.random(fractionDef.minX, fractionDef.maxX),
				MathUtils.random(fractionDef.minY, fractionDef.maxY),
				MathUtils.random(fractionDef.minVX,fractionDef.maxVX),MathUtils.random(fractionDef.minVY,fractionDef.maxVY) ,
				MathUtils.random(fractionDef.minMass,fractionDef.masMass), (MathUtils.random(fractionDef.minCharge, fractionDef.maxCharge)), 1, 1, 1, Fraction.Condition.Liquid,
				new Color(MathUtils.random(0.1f, 1), MathUtils.random(0.1f, 1), MathUtils.random(0.1f, 1), MathUtils.random(0.5f, 0.75f)));
	}
}