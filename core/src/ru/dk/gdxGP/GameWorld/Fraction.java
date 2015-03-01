package ru.dk.gdxGP.GameWorld;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Random;

/**
 * Created by DK on 20.02.15.
 */
public class Fraction extends Actor {
    private Body body;

    public static boolean isIsReloadTextureNeeded() {
        return isReloadTextureNeeded;
    }

    public static void setIsReloadTextureNeeded(boolean isReloadTextureNeeded) {
        Fraction.isReloadTextureNeeded = isReloadTextureNeeded;
    }

    private static boolean isReloadTextureNeeded=true;
    private TextureRegion textureRegion;
    private static Texture texture;

    public Fraction(World world,float x,float y,float vx, float vy,float mass){
        Random rnd=new Random();
        BodyDef bodyDef=new BodyDef();
        bodyDef.bullet=true;
        bodyDef.active=true;
        bodyDef.linearVelocity.set(vx*mass,vy*mass);
        bodyDef.position.set(x,y);
        bodyDef.type= BodyDef.BodyType.DynamicBody;
        bodyDef.allowSleep=false;
        bodyDef.awake=true;
        bodyDef.linearDamping=0.0f;
        bodyDef.angularDamping=0.001f;
        bodyDef.angularVelocity=0*(rnd.nextFloat()-rnd.nextFloat())*500.0f;
        body=world.createBody(bodyDef);
        FixtureDef fixtureDef=new FixtureDef();
        CircleShape circleShape=new CircleShape();
        circleShape.setRadius((float) Math.sqrt(mass / Math.PI / 1.0f));
        circleShape.setPosition(new Vector2(0, 0));
        fixtureDef.shape=circleShape;
        fixtureDef.friction=2.5f;
        fixtureDef.density=0.25f;
        fixtureDef.restitution=0.75f;
        fixtureDef.isSensor=false;
        MassData massData=new MassData();
        massData.mass=mass;
        massData.center.set(circleShape.getRadius(),circleShape.getRadius());
        massData.I=0.0f;
        body.createFixture(fixtureDef);
        body.setUserData(this);
        body.setBullet(true);
        body.setFixedRotation(false);
        this.textureRegion=loadTextureRegion();
    }

    public Body getBody() {
        return body;
    }

    private Texture reloadTexture(){isReloadTextureNeeded=false; return texture=new Texture("circle01.png");}
    private Texture getTexture(){return texture=isReloadTextureNeeded?( reloadTexture()):texture;}
    public TextureRegion getTextureRegion(){return this.textureRegion;}
    public TextureRegion loadTextureRegion(){
        return textureRegion=new TextureRegion(getTexture());
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        float r=this.body.getFixtureList().get(0).getShape().getRadius();
        batch.draw(this.getTextureRegion(), this.body.getPosition().x - 1.0f*r, this.body.getPosition().y - 1.0f*r ,r,r,r*2.0f, r*2.0f,1,1, MathUtils.radiansToDegrees* this.getBody().getAngle());


    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.setX(this.getBody().getPosition().x);
        this.setY(this.getBody().getPosition().y);
        //this.getBody().getLinearVelocity().set(this.getBody().getLinearVelocity().x*1.1f,this.getBody().getLinearVelocity().y*1.1f);
        //this.getBody().applyForce(this.getBody().getLinearVelocity().x*0.1f,this.getBody().getLinearVelocity().y*0.1f,this.body.getFixtureList().get(0).getShape().getRadius(),this.body.getFixtureList().get(0).getShape().getRadius(),true);
    }
}
