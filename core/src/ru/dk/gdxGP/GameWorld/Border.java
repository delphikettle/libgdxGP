package ru.dk.gdxGP.GameWorld;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Random;

/**
 * Created by DK on 21.02.15.
 */
public class Border extends Actor {
    Body body;
    public  Border(World world,int x,int y,int w, int h,boolean horizontal){
        BodyDef bodyDef=new BodyDef();
        bodyDef.type= BodyDef.BodyType.StaticBody;
        bodyDef.bullet=true;
        bodyDef.angularVelocity=0.0f;
        bodyDef.fixedRotation=false;
        bodyDef.active=true;
        bodyDef.position.set(x - (horizontal ? 0.0f : 0.5f) * w, y - (horizontal ? 0.5f : 0.0f) * h);
        bodyDef.linearVelocity.set(0,0);
        bodyDef.active=true;
        bodyDef.bullet=true;
        bodyDef.fixedRotation=true;
        body = world.createBody(bodyDef);
        PolygonShape polygonShape=new PolygonShape();
        polygonShape.setAsBox(w * (horizontal ?0.5f : 0.5f),h*(horizontal ? 0.5f : 0.5f));
        FixtureDef fixtureDef=new FixtureDef();
        fixtureDef.friction=1.0f;
        fixtureDef.shape=polygonShape;
        fixtureDef.density=1.0f;
        fixtureDef.restitution=10.0f;
        fixtureDef.isSensor=false;
        body.createFixture(fixtureDef);
        body.setUserData(this);
    }
}
