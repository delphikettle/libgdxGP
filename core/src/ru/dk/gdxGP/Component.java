package ru.dk.gdxGP;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import ru.dk.gdxGP.*;

public class Component extends Actor
{
	private float vx,vy;
	private float x,y;
	private float m,r,density;
	private Particle owner;
	private boolean isNeedRecount=true;
	private float lastF;
	private boolean isRedrawNeeded=true;
	private static boolean isReloadTextureNeeded=true;
	private TextureRegion textureRegion;
	private static Texture texture;
	public Component(float x, float y,float m) throws IllegalArgumentException {
		this.x=x;
		this.y=y;
		this.vx=10;
		this.vy=1;
		if (m>0)this.m=m; else throw new IllegalArgumentException();
		if(texture==null)texture=reloadTexture();
		this.density=1;
		this.r=(float)Math.sqrt(this.m/Math.PI/this.density);
		this.owner=null;

		this.textureRegion=loadTextureRegion();
	}
	private Texture reloadTexture(){return texture=new Texture("circle01.png");}
	private Texture getTexture(){return texture=isReloadTextureNeeded?reloadTexture():texture;}
	private TextureRegion getTextureRegion(){return this.textureRegion;}
	public TextureRegion loadTextureRegion(){
		return textureRegion=new TextureRegion(getTexture());
	}
	public Component(Particle p){
		x=0;
		y=0;
		this.owner=p;
	}
	public void nextStep(float time){
		if(owner==null){
			x += vx * time;
			y += vy * time;
		}else{
			
		}
		this.setX(this.x);
		this.setY(this.y);
		//x += vx *0.000005f;
		//y += vy *0.000005f;
		//if(time==0f) throw new IllegalArgumentException(""+time);
		//Log.i("Component",""+this.getX()+" ; "+this.getY());
	}
	final public float getF(Component cAnother){
		if(isNeedRecount)return lastF=recountF(cAnother);else
			return lastF;
	}
	private float recountF(Component cAnother){
		isNeedRecount=false;
		return m;
	}
	final public static float getDistance(Component c1, Component c2) throws IllegalArgumentException {
		if(c1.owner==null&&c2.owner==null)return (float)(Math.sqrt(Math.pow(c1.x-c2.x,2)+Math.pow(c1.y-c2.y,2)));else {
			throw new IllegalArgumentException("c1 and c2 are must not have owners");
		}
	}

	public float getM() {
		return m;
	}
	public float informXAcceleration(float ax){
		return vx+=ax;
	}
	public float informYAcceleration(float ay){
		return vy+=ay;
	}
	final public static float getXDiff(Component c1, Component c2) throws IllegalArgumentException{
		if(c1.owner==null&&c2.owner==null)return c2.x-c1.x; else{
			throw new IllegalArgumentException("c1 and c2 are must not have owners");
		}
	}
	final public static float getYDiff(Component c1, Component c2) throws IllegalArgumentException{
		if(c1.owner==null&&c2.owner==null)return c2.y-c1.y; else{
			throw new IllegalArgumentException("c1 and c2 are must not have owners");
		}
	}
	final public float getX(){
		return this.x;
	}
	final public float getY(){
		return this.y;
	}
	final public float getR(){
		return this.r;
	}
	final public void setOwner(Particle owner){
		this.owner=owner;
		this.x=this.y=0;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(this.getTextureRegion(),this.getX()-this.getR(),this.getY()-this.getR(),this.getR()*2,this.getR()*2);
	}
}
