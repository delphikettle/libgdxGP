package ru.dk.gdxGP.GameWorld.WorldElements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import ru.dk.gdxGP.GDXGameGP;
import ru.dk.gdxGP.GameWorld.FractionDrawer;
import ru.dk.gdxGP.GameWorld.FractionOperator;

public class Fraction extends Actor implements FractionDrawer,FractionOperator {
    public enum Condition{Liquid,Solid,Mixed}
    private Condition condition;
    private Body body;
    private static TextureRegion textureRegionFractionSolid;
    private static TextureRegion textureRegionCharge;
    private static TextureRegion textureRegionNullCharge;
    private static TextureRegion textureRegionMinusCharge;
    private static TextureRegion textureRegionPlusCharge;
    private FractionDrawer drawer=null;
    private FractionOperator operator=null;
    private float strength=1;
    private float charge=MathUtils.random(-1f,1f);
    private Color color=new Color(MathUtils.random(0.1f,1),MathUtils.random(0.1f,1),MathUtils.random(0.1f,1),MathUtils.random(0.5f,0.75f));
    static{
        textureRegionFractionSolid = new TextureRegion((Texture) GDXGameGP.assetManager.get("images/FractionSolid01.png"));
        textureRegionCharge = new TextureRegion((Texture) GDXGameGP.assetManager.get("images/charge.png"));
        textureRegionPlusCharge = new TextureRegion((Texture) GDXGameGP.assetManager.get("images/PlusCharge.png"));
        textureRegionNullCharge = new TextureRegion((Texture) GDXGameGP.assetManager.get("images/NullCharge.png"));
        textureRegionMinusCharge = new TextureRegion((Texture) GDXGameGP.assetManager.get("images/MinusCharge.png"));
    }

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
        if(drawer!=null)drawer.drawFraction(this,batch,batch.getColor());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(operator!=null)operator.operateFraction(this, delta);
        //this.getBody().getLinearVelocity().set(this.getBody().getLinearVelocity().x*1.1f,this.getBody().getLinearVelocity().y*1.1f);
        //this.getBody().applyForce(this.getBody().getLinearVelocity().x*0.1f,this.getBody().getLinearVelocity().y*0.1f,this.body.getFixtureList().get(0).getShape().getRadius(),this.body.getFixtureList().get(0).getShape().getRadius(),true);
    }

    @Override
    public void drawFraction(Fraction fraction, Batch batch,Color parentColor) {
        float r=fraction.body.getFixtureList().get(0).getShape().getRadius();
        //batch.draw(fraction.textureRegionFractionSolid, fraction.body.getPosition().x - 1.0f*r, fraction.body.getPosition().y - 1.0f*r ,r,r,r*2.0f, r*2.0f,1,1, MathUtils.radiansToDegrees* fraction.getBody().getAngle());
        switch (fraction.condition){
            case Solid:
                batch.setColor(color);
                batch.draw(textureRegionFractionSolid, fraction.body.getPosition().x - 1.0f * r, fraction.body.getPosition().y - 1.0f * r, r, r, r * 2.0f, r * 2.0f, 1, 1, MathUtils.radiansToDegrees * fraction.getBody().getAngle());
                if(fraction.getCharge()>0)batch.setColor(1,0,0,0.25f);
                else batch.setColor(0,0,1,0.25f);
                float r1=r*(1+Math.abs(fraction.charge*2f));
                if(!(fraction.charge<=0.1f&&fraction.charge>=-0.1f))batch.draw(textureRegionCharge, fraction.body.getPosition().x - 1.0f * r1, fraction.body.getPosition().y - 1.0f * r1, r1, r1, r1 * 2.0f, r1 * 2.0f, 1, 1, MathUtils.radiansToDegrees * fraction.getBody().getAngle());
                float r2=r*0.5f;
                batch.setColor(parentColor);
                if (fraction.charge>0.25f)
                    batch.draw(textureRegionPlusCharge, fraction.body.getPosition().x - 1.0f * r2, fraction.body.getPosition().y - 1.0f * r2, r2 * 2.0f, r2 * 2.0f);
                if (fraction.charge<-0.25f)
                    batch.draw(textureRegionMinusCharge, fraction.body.getPosition().x - 1.0f * r2, fraction.body.getPosition().y - 1.0f * r2, r2 * 2.0f, r2 * 2.0f);
                if (fraction.charge<=0.25f&&fraction.charge>=-0.25f)
                    batch.draw(textureRegionNullCharge, fraction.body.getPosition().x - 1.0f * r2, fraction.body.getPosition().y - 1.0f * r2, r2 * 2.0f, r2 * 2.0f);
                break;
        }
    }

    synchronized public Fraction divide(float mass,float vx,float vy){
        System.out.println("Division started");
        System.out.println("mass="+mass+";vx="+vx+";vy="+vy);
        System.out.println(this.body.getWorld().isLocked());
        if(mass>=this.body.getMass()||mass<=0)//throw new IllegalArgumentException();
            return null;
        float newMass=this.body.getMass()-mass;
        float r = recountRadius(newMass);
        this.body.applyLinearImpulse(-vx*mass,-vy*mass,r,r,true);

        System.out.println("Fraction that was divided:" + this.toString());
        //creating new Fraction
        Fraction fNew = new Fraction(this.body.getWorld(),this.body.getPosition().x+vx*0.01f,this.body.getPosition().y+vy*0.01f,vx,vy,mass);
        System.out.println("Division ended with new Fraction:"+((fNew!=null)?fNew.toString():""));
        return fNew;
    }
    public final float recountRadius(float newMass){
        float newR = (float) Math.sqrt(newMass / Math.PI / this.body.getFixtureList().get(0).getDensity());
        MassData newMassData = new MassData();
        newMassData.mass=newMass;
        newMassData.center.set(newR,newR);
        this.body.setMassData(newMassData);
        this.body.getFixtureList().get(0).getShape().setRadius(newR);
        return newR;
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
