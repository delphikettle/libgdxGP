package ru.dk.gdxGP.GameWorld;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class Component extends Actor
{
	private float vx,vy;
	private float x,y;
	private float m,r,density;
	private Particle owner;
	private boolean isNeedRecount=true;
	private float lastF;
	private boolean isRedrawNeeded=true;
    private boolean isUnderGravitation=false;
	private static boolean isReloadTextureNeeded=true;
	private static TextureRegion textureRegion;
	private static Texture texture;
    private Body body;

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public Component(float x, float y,float m) throws IllegalArgumentException {
		this.x=x;
		this.y=y;
        Random rnd=new Random();
		this.vx=(rnd.nextInt(200)-100)*10;
		this.vy=(rnd.nextInt(200)-100)*10;
		if (m>0)this.m=m; else throw new IllegalArgumentException();
		if(texture==null)texture=getTexture();
		this.density=1;
		this.r=(float)Math.sqrt(this.m/Math.PI/this.density);
		this.owner=null;
		this.textureRegion=loadTextureRegion();
	}
    public void setBody(@NotNull Body body){
        this.body=body;
        this.body.setLinearVelocity(this.vx,this.vy);
    }
    public Component(Particle p){
        x=0;
        y=0;
        this.owner=p;
    }
	private Texture reloadTexture(){isReloadTextureNeeded=false; return texture=new Texture("circle01.png");}
	private Texture getTexture(){return texture=isReloadTextureNeeded?( reloadTexture()):texture;}
	static public TextureRegion getTextureRegion(){return textureRegion;}
	public TextureRegion loadTextureRegion(){
		return textureRegion=new TextureRegion(getTexture());
	}
	public void nextStep(float time){
		if(owner==null){
			//x += vx * time;
			//y += vy * time;
		}else{
			
		}
        if(body!=null){
            this.x=body.getPosition().x;
            this.y=body.getPosition().y;
        }
		this.setX(this.x);
		this.setY(this.y);
		//x += vx *0.000005f;
		//y += vy *0.000005f;
		//if(time==0f) throw new IllegalArgumentException(""+time);
		//Log.i("Component",""+this.getX()+" ; "+this.getY());
	}

    public float getVx() {
        return vx;
    }

    public float getVy() {
        return vy;
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

	@Override
	public void setX(float x) {
		if(this.getX()!=x){
			super.setX(x);
            this.x=x;
            this.body.getPosition().set(x,y);
			if(onCoordinateChangedListener != null) onCoordinateChangedListener.onCoordinateChanged(this);
		}

	}
	@Override
	public void setY(float y) {
		if(this.getY()!=y){
			super.setY(y);
            this.y=y;
            this.body.getPosition().set(x, y);
			if(onCoordinateChangedListener != null) onCoordinateChangedListener.onCoordinateChanged(this);
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

    public void setVx(float vx) {
        this.vx = vx;
        this.body.setLinearVelocity(this.vx,this.vy);
    }

    public void setVy(float vy) {
        this.vy = vy;
        this.body.setLinearVelocity(this.vx,this.vy);
    }

    public void onBorderContact(Integer l,Integer r,Integer t,Integer b){

        //left
        if(l!=null){
            this.setVx(-this.vx*0.99f);
            this.setX(l.intValue()+this.r+1000);
        }

        //right
        if(r!=null){
            this.setVx(-this.vx*0.99f);
            this.setX(r.intValue()-this.r-1000);
        }

        //top
        if(t!=null){
            this.setVy(-this.vy*0.99f);
            this.setY(t.intValue()+this.r+1000);
        }

        //bottom
        if(b!=null){
            this.setVy(-this.vy*0.99f);
            this.setY(b.intValue()-this.r-1000);
        }
        if(this.onBorderContactListener!=null)this.onBorderContactListener.onBorderContact(l,r,t,b);
    }

    public void updateActorPosition(){
        this.setX(this.body.getPosition().x);
        this.setY(this.body.getPosition().y);
    }
    public void interactWithAnotherComponent(Component c){

    }
    public boolean ifUnderGravitation(){return isUnderGravitation;}
	@Override
	public void draw(Batch batch, float parentAlpha) {
		//super.draw(batch, parentAlpha);
		batch.draw(this.getTextureRegion(),this.getX()-this.getR(),this.getY()-this.getR(),this.getR()*2,this.getR()*2);
	}
	OnCoordinateChangedListener onCoordinateChangedListener =null;
	OnMassChangedListener onMassChangedListener=null;
	OnEachStepListener onEachStepListener=null;
    OnBorderContactListener onBorderContactListener=null;

    public interface OnBorderContactListener{
        public void onBorderContact(Integer l,Integer r,Integer t,Integer b);
    }
	public interface OnCoordinateChangedListener {
		public void onCoordinateChanged(Component c);
	}
	public interface OnMassChangedListener {
		public void onMassChanged(Component c);
	}
	public interface OnEachStepListener {
		public void onEachStep(Component c);
	}
}
