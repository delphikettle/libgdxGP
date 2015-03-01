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

import static ru.dk.gdxGP.GameWorld.Component.getDistance;

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
            box2DDebugRenderer.SHAPE_AWAKE.set(Color.GREEN);
            camera=new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
            //camera.position.set(particles.get(0).getX(),particles.get(0).getY(),0);
            camera.far=0;
		}
		@Override
		public void draw() {
			super.draw();
            //this.getBatch().begin();
            //testParticle.draw(this.getBatch(),1);
            //this.getBatch().draw(Component.getTextureRegion(),0,0,50,50);
            //this.getBatch().end();

            //this.getCamera().position.set((testParticle.getBody().getPosition().x+25*this.getCamera().position.x)/26,
                    //(testParticle.getBody().getPosition().y+25*this.getCamera().position.y)/26,0);
            //this.getCamera().position.set((getXMin()+25*this.getCamera().position.x)/26,
                    //(getYMin()+25*this.getCamera().position.y)/26,0);

            /*
            this.getCamera().position.set(
                    (particles.get(0).getX()+255*this.getCamera().position.x)/256,
                    (particles.get(0).getY()+255*this.getCamera().position.y)/256,0);
                    */
			//this.getCamera().lookAt(particles.get(0).getX(),particles.get(0).getY(),0);
			//this.getCamera().normalizeUp();
			//this.getCamera().update();
            drawBorders(this.getBatch());
            this.act();
            getWorld().step(getNextStepTime(),6,2);
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
        this.world=new World(new Vector2(0.0f,0.0f),false);
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
        bodyDef.fixedRotation=false;
        bodyDef.allowSleep=false;
        System.out.println(getXMax());
        System.out.println(getYMax());
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
        fixtureDef.density=0.5f;
        fixtureDef.restitution=0.0f;
        Body body=world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        MassData massData=new MassData();
        massData.mass=Integer.MAX_VALUE;
        massData.center.set(getXMax()-getXMin(),getYMax()-getYMin());
        body.setMassData(massData);
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

	synchronized final public void addComponent(Component component){
		particles.add(component);
		isComponentsChanged=true;
		stage.addActor(component);
        component.setBody(addBodyByComponent(component));
		System.out.println("Time: "+"ComponentAdded "+System.currentTimeMillis());
	}
    synchronized final public Body addBodyByComponent(@NotNull Component c){

        BodyDef bodyDef=new BodyDef();
        bodyDef.type= BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(c.getX(),c.getY());
        bodyDef.linearVelocity.set(c.getVx(),c.getVy());
        bodyDef.fixedRotation=true;
        bodyDef.bullet=true;
        bodyDef.active=true;
        Body body= world.createBody(bodyDef);

        CircleShape circleShape=new CircleShape();
        circleShape.setRadius(c.getR());
        FixtureDef fixtureDef=new FixtureDef();
        fixtureDef.shape=circleShape;
        fixtureDef.friction=-1;
        fixtureDef.density=c.getDensity();
        body.createFixture(fixtureDef).setFriction(-1f);

        MassData massData=new MassData();
        massData.mass=c.getM();
        massData.I=0;
        massData.center.set(c.getR(), c.getR());
        body.setMassData(massData);
        body.setBullet(true);
        body.getPosition().set(c.getX(), c.getY());
        body.setLinearVelocity(c.getVx(), c.getVy());
        body.setUserData(c);
        for(int i=0;i<body.getFixtureList().size;i++) {
            body.getFixtureList().get(i).setRestitution(0f);
            body.getFixtureList().get(i).setFriction(0f);
        }
        return body;

    }
	public long maxOp=0;
	public String maxOpName=""; long time=System.nanoTime(),elTime;
	synchronized final private void Interaction(Component c1, Component c2){
        if(!(c1.ifUnderGravitation()&&c2.ifUnderGravitation()))
            return;
		time=System.nanoTime();
		float d=getDistance(c1,c2);
		elTime=System.nanoTime()-time;
		if(elTime>maxOp){
			//maxOp=elTime;
			//maxOpName="detDistance";
		}
		time=System.nanoTime();
		if(d==0) return;
		//if(d>this.maxDistance)return;
		elTime=System.nanoTime()-time;
		if(elTime>maxOp){
			//maxOp=elTime;
			//maxOpName="if...if";
		}
		time=System.nanoTime();
		float F= (float) (G*c1.getF(c2)*c2.getF(c1)/(d));
		elTime=System.nanoTime()-time;
		if(elTime>maxOp){
			//maxOp=elTime;
			//maxOpName="F=...";
		}
		time=System.nanoTime();
		float F1=F/c1.getM(),F2=F/c2.getM();
		elTime=System.nanoTime()-time;
		if(elTime>maxOp){
			//maxOp=elTime;
			//maxOpName="F1=..., F2...";
		}
		time=System.nanoTime();
		c1.informXAcceleration(F1 * Component.getXDiff(c1,c2) / d);
		c2.informXAcceleration(F2 * Component.getXDiff(c2,c1) / d);
		c1.informYAcceleration(F1 * Component.getYDiff(c1, c2) / d);
		c2.informYAcceleration(F2 * Component.getYDiff(c2, c1) / d);
		elTime=System.nanoTime()-time;
		if(elTime>maxOp){
			//maxOp=elTime;
			//maxOpName="InformAcceleration";
		}
		if(d<=c1.getR()+c2.getR()){
			//collision
		}

		//Log.i("Level.Interaction",F1+"");
	}

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
        if(9==9)return;
		for(int i=0;i<particles.size();i++) {
			try {
                if(!(particles.get(i) instanceof  Component)) {

                    continue;
                }
				//this.time=System.nanoTime();
				if(particles.get(i) instanceof  Component)((Component) (particles.get(i))).updateActorPosition();
                //CollisionWithBorders(particles.get(i));
				//elTime=System.nanoTime()-this.time;
				//if(elTime>maxOp){
				//	maxOp=elTime;
				//	maxOpName="nextStep";
				//}

                //for (int j = i + 1; j < particles.size(); j++) {
                 //   this.Interaction(particles.get(i), particles.get(j));
                //}
			}catch (NullPointerException e){particles.remove(i);}
		}
        //this.timeFromLastRecount+=time;
        //if(this.timeFromLastRecount<100||true)return;
        //this.timeFromLastRecount=0;
        if(true)return;
        for(int i=0;i<particles.size();i++) {
            if(!(particles.get(i) instanceof  Component))continue;
            try {
                for (int j = i + 1; j < particles.size(); j++) {
                    if(!(particles.get(j) instanceof  Component))continue;
                    this.Interaction((Component)particles.get(i), (Component)particles.get(j));
                }
            }catch (NullPointerException e){particles.remove(i);}
        }
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
    public boolean CollisionWithBorders(Component c)
    {
        boolean f=false,l,r,t,b;
        l=r=t=b=false;

        //left
        if(c.getX()-c.getR()<=getXMin()){
            //this.vx=-this.vx*0.99f;
            //c.informXAcceleration(-1.99f*c.getVx());
            //this.x=getXMin()+this.r+1;
            //c.setX(getXMin()+c.getR()+1);
            l=true;
            f=true;
        }

        //right
        if(c.getX()+c.getR()>=getXMax()){
            //this.vx=-this.vx*0.99f;
            //this.x=getXMax()-this.r-1;
            r=true;
            f=true;
        }

        //top
        if(c.getY()-c.getR()<=getYMin()){
            //this.vy=-this.vy*0.99f;
            //this.y=getYMin()+this.r+1;
            t=true;
            f=true;
        }

        //bottom
        if(c.getY()+c.getR()>=getYMax()){
            //this.vy=-this.vy*0.99f;
            //this.y=getYMax()-this.r-1;
            b=true;
            f=true;
        }
        if(f)c.onBorderContact(l?getXMin():null,r?getXMax():null,t?getYMin():null,b?getYMax():null);
        return f;
    }
    public void CollisionWithBorder(Fraction fraction){
        //System.out.println(fraction.getBody().getLinearVelocity());
        fraction.getBody().getLinearVelocity().set(-fraction.getBody().getLinearVelocity().x,-fraction.getBody().getLinearVelocity().y);
    }
	public void Pause(){

	}
	public void Resume(){

	}
    @Override
    public void endContact(Contact contact) {
        //contact.setFriction(-1f);

    }
    @Override
    public void beginContact(Contact contact) {
        //contact.setFriction(-1f);
    }
    @Override
    public void preSolve (Contact contact, Manifold oldManifold){

        //if(contact.getFixtureA().getBody().getUserData() instanceof Fraction)System.out.println("fraction found");
        //contact.setFriction(-1f);
    }
    @Override
    public void postSolve (Contact contact, ContactImpulse impulse){
        //contact.setFriction(-1f);
        /*
        if(contact.getFixtureA().getBody().getUserData().equals("border")&&contact.getFixtureB().getBody().getUserData() instanceof Fraction) {
            CollisionWithBorder((Fraction) contact.getFixtureB().getBody().getUserData());
        }
        if(contact.getFixtureB().getBody().getUserData().equals("border")&&contact.getFixtureA().getBody().getUserData() instanceof Fraction) {
            CollisionWithBorder((Fraction) contact.getFixtureA().getBody().getUserData());
        }
        */
    }
}
