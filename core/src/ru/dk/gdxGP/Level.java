package ru.dk.gdxGP;

//import android.util.*;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.math.MathContext;
import java.util.ArrayList;
import java.util.Random;

import static ru.dk.gdxGP.Component.getDistance;

public abstract class Level extends Thread implements Runnable
{
	private ArrayList<Component> particles;
	private LevelStage stage;
	private class LevelStage extends Stage{

		LevelStage(){
		}
		@Override
		public void draw() {
			this.act();
			super.draw();
			this.getCamera().position.set((particles.get(0).getX()+255*this.getCamera().position.x)/256,(particles.get(0).getY()+255*this.getCamera().position.y)/256,0);
			//this.getCamera().lookAt(particles.get(0).getX(),particles.get(0).getY(),-10);
			//this.getCamera().normalizeUp();
			this.getCamera().update();
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

	public long getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(long maxDistance) {
		if(maxDistance>0)this.maxDistance = maxDistance;
	}

	private long maxDistance=10000;
	public Level(int w, int h) {
		stage=new LevelStage();
		particles = new ArrayList<Component>();
		componentArray=particles.toArray();
		xMin = yMin =0;
		xMax = w;
		yMax = h;
		currentRealTime=System.currentTimeMillis();
		currentGameTime=0;
		setParticles(w,h);
		this.isMove=true;
		this.setDaemon(true);
		System.out.println("Time: " + "LevelCreated" + System.currentTimeMillis());
	}
	abstract public void setParticles(int w, int h);

	public Stage getStage(){return this.stage;}

	synchronized final public void addComponent(Component component){
		particles.add(component);
		isComponentsChanged=true;
		stage.addActor(component);
		stage.setScrollFocus(component);
		System.out.println("Time: "+"ComponentAdded "+System.currentTimeMillis());
	}
	long maxOp=0;
	 String maxOpName=""; long time=System.nanoTime(),elTime;
	synchronized final private void Interaction(Component c1, Component c2){
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

	synchronized private void Move(float time){
		for(int i=0;i<particles.size();i++) {
			try {
				for (int j = i + 1; j < particles.size(); j++) {
					this.Interaction(particles.get(i), particles.get(j));
				}
				this.time=System.nanoTime();
				particles.get(i).nextStep(time*0.001f);
				elTime=System.nanoTime()-this.time;
				if(elTime>maxOp){
					maxOp=elTime;
					maxOpName="nextStep";
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
	public void Pause(){

	}
	public void Resume(){

	}

}
