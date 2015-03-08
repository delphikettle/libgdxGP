package ru.dk.gdxGP.GameWorld.WorldElements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import ru.dk.gdxGP.GDXGameGP;
import ru.dk.gdxGP.GameWorld.FractionDrawer;
import ru.dk.gdxGP.GameWorld.FractionOperator;
import ru.dk.gdxGP.TextureKeeper;

import java.util.Random;

public class Fraction extends Actor implements FractionDrawer,FractionOperator {
    private Body body;
    private TextureRegion textureRegion;
    private FractionDrawer drawer=null;
    private FractionOperator operator=null;

    public Fraction(World world,float x,float y,float vx, float vy,float mass){
        Random rnd=new Random();
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
        massData.I=0.0f;
        body.setMassData(massData);

        FixtureDef fixtureDef=new FixtureDef();
        fixtureDef.shape=circleShape;
        fixtureDef.friction=1.0f;
        fixtureDef.density=0.25f;
        fixtureDef.restitution=1.0f;
        fixtureDef.isSensor=false;
        body.createFixture(fixtureDef);
        body.setUserData(this);
        this.textureRegion= new TextureRegion((Texture) GDXGameGP.assetManager.get("images/circle.png"));
        this.setDrawer(this);
        this.setOperator(this);
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

    @Override
    public void operateFraction(Fraction fraction, float deltaTime) {
        this.setX(this.getBody().getPosition().x);
        this.setY(this.getBody().getPosition().y);

    }

}
