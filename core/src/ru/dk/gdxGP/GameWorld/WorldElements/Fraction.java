package ru.dk.gdxGP.GameWorld.WorldElements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import javafx.scene.control.SplitPane;
import ru.dk.gdxGP.GDXGameGP;
import ru.dk.gdxGP.GameWorld.FractionDrawer;
import ru.dk.gdxGP.GameWorld.FractionOperator;
import ru.dk.gdxGP.TextureKeeper;

import java.util.Random;

public class Fraction extends Actor implements FractionDrawer,FractionOperator {
    public enum Condition{Liquid,Solid,Mixed}
    private Condition condition;
    private Body body;
    private TextureRegion textureRegion;
    private FractionDrawer drawer=null;
    private FractionOperator operator=null;
    private float strength=1;
    private float charge=MathUtils.random(-1f,1f);

    public float getCharge() {
        return charge;
    }

    public void setCharge(float charge) {
        this.charge = charge;
    }

    public float getStrength() {
        return strength;
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }


    public Fraction(World world,float x,float y,float vx, float vy,float mass){
        BodyDef bodyDef=new BodyDef();
        bodyDef.active=true;
        bodyDef.bullet=false;
        bodyDef.fixedRotation=false;
        bodyDef.linearVelocity.set(vx,vy);
        bodyDef.position.set(x,y);
        bodyDef.type= BodyDef.BodyType.DynamicBody;
        bodyDef.allowSleep=false;
        body=world.createBody(bodyDef);

        CircleShape circleShape=new CircleShape();
        circleShape.setRadius((float) Math.sqrt(mass / Math.PI / 1.0f));

        MassData massData=new MassData();
        massData.mass=mass;
        massData.center.set(circleShape.getRadius(),circleShape.getRadius());
        body.setMassData(massData);

        FixtureDef fixtureDef=new FixtureDef();
        fixtureDef.shape=circleShape;
        fixtureDef.friction=1.0f;
        fixtureDef.density=1.0f;
        fixtureDef.restitution=1.0f;
        fixtureDef.isSensor=false;
        body.createFixture(fixtureDef);
        body.setUserData(this);
        this.textureRegion= new TextureRegion((Texture) GDXGameGP.assetManager.get("images/circle.png"));
        this.setDrawer(this);
        this.setOperator(this);
        condition=Condition.Solid;
    }

    public void setOperator(FractionOperator operator) {
        this.operator = operator;
    }

    public void setDrawer(FractionDrawer drawer) {
        this.drawer = drawer;
    }

    public Body getBody() {
        return body;
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if(drawer!=null)drawer.drawFraction(this,batch);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(operator!=null)operator.operateFraction(this, delta);
        //this.getBody().getLinearVelocity().set(this.getBody().getLinearVelocity().x*1.1f,this.getBody().getLinearVelocity().y*1.1f);
        //this.getBody().applyForce(this.getBody().getLinearVelocity().x*0.1f,this.getBody().getLinearVelocity().y*0.1f,this.body.getFixtureList().get(0).getShape().getRadius(),this.body.getFixtureList().get(0).getShape().getRadius(),true);
    }

    @Override
    public void drawFraction(Fraction fraction, Batch batch) {
        float r=fraction.body.getFixtureList().get(0).getShape().getRadius();
        batch.draw(fraction.textureRegion, fraction.body.getPosition().x - 1.0f*r, fraction.body.getPosition().y - 1.0f*r ,r,r,r*2.0f, r*2.0f,1,1, MathUtils.radiansToDegrees* fraction.getBody().getAngle());

    }

    synchronized public Fraction divide(float mass,float vx,float vy){
        System.out.println("Division started");
        System.out.println("mass="+mass+";vx="+vx+";vy="+vy);
        System.out.println(this.body.getWorld().isLocked());
        if(mass>=this.body.getMass()||mass<=0)//throw new IllegalArgumentException();
            return null;
        MassData newMassData=new MassData();
        newMassData.mass=this.body.getMass()-mass;
        float r=this.body.getFixtureList().get(0).getShape().getRadius();
        newMassData.center.set(r,r);
        this.body.getLinearVelocity().add(-vx*mass/newMassData.mass,-vy*mass/newMassData.mass);
        this.body.setMassData(newMassData);
        this.body.getFixtureList().get(0).getShape().setRadius((float) Math.sqrt(newMassData.mass / Math.PI / 1.0f));

        System.out.println("Fraction that was divided:"+this.toString());
        //creating new Fraction
        Fraction fNew =null;
        new Fraction(this.body.getWorld(),this.body.getPosition().x+vx*0.1f,this.body.getPosition().y+vy*0.1f,vx,vy,mass);
        //System.out.println("Division ended with new Fraction:"+((fNew!=null)?fNew.toString():""));
        return fNew;
    }
    @Override
    public void operateFraction(Fraction fraction, float deltaTime) {
        //fraction.setX(fraction.getBody().getPosition().x);
        //fraction.setY(fraction.getBody().getPosition().y);
        //if(MathUtils.random.nextInt(1024)==MathUtils.random.nextInt(1024))System.out.println(this.toString());
    }

    @Override
    public String toString() {
        String s="Fraction:mass="+this.getBody().getMass()+";vx="+this.getBody().getLinearVelocity().x+";vy="+this.getBody().getLinearVelocity().y;
        return s;
    }
}
