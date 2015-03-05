package ru.dk.gdxGP.GameWorld;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Random;

/**
 * Created by DK on 21.02.15.
 */
public class Border extends Actor {
    Body body;
    private TextureRegion textureRegion;
    public  Border(World world,int x,int y,int w, int h,Shape shape){
        BodyDef bodyDef=new BodyDef();
        bodyDef.type= BodyDef.BodyType.StaticBody;
        bodyDef.bullet=true;
        bodyDef.angularVelocity=0.0f;
        bodyDef.fixedRotation=false;
        bodyDef.active=true;
        bodyDef.position.set(x,y);
        bodyDef.linearVelocity.set(0,0);
        bodyDef.active=true;
        bodyDef.bullet=true;
        bodyDef.fixedRotation=true;
        body = world.createBody(bodyDef);
        FixtureDef fixtureDef=new FixtureDef();
        fixtureDef.friction=1.0f;
        fixtureDef.shape=shape;
        fixtureDef.density=1.0f;
        fixtureDef.restitution=10.0f;
        fixtureDef.isSensor=false;
        body.createFixture(fixtureDef);
        body.setUserData(this);
    }
    public Body getBody() {
        return body;
    }
}
