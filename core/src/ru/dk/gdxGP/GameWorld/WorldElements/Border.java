package ru.dk.gdxGP.GameWorld.WorldElements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import ru.dk.gdxGP.GDXGameGP;

import java.util.Random;

/**
 * Created by DK on 21.02.15.
 */
public class Border extends Actor {
    Body body;
    private boolean contour;
    private TextureRegion textureRegion;
    public  Border(World world,int x,int y,Shape shape,boolean contour){
        BodyDef bodyDef=new BodyDef();
        bodyDef.type= BodyDef.BodyType.StaticBody;
        bodyDef.bullet=true;
        bodyDef.angularVelocity=0.0f;
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
        this.contour=(contour&&shape instanceof ChainShape);
        fixtureDef.density=1.0f;
        fixtureDef.restitution=1.0f;
        fixtureDef.isSensor=false;
        body.createFixture(fixtureDef);
        body.setUserData(this);
        this.textureRegion=new TextureRegion(GDXGameGP.assetManager.get("border01.png",Texture.class));
    }
    public Body getBody() {
        return body;
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (this.contour){
            ChainShape chainShape= (ChainShape) this.body.getFixtureList().get(0).getShape();
            float xMin,yMin,xMax,yMax;
            Vector2 vector2=new Vector2();
            chainShape.getVertex(0,vector2);
            xMin=vector2.x;
            yMin=vector2.y;
            chainShape.getVertex(2,vector2);
            xMax=vector2.x;
            yMax=vector2.y;
            batch.draw(textureRegion, xMin-0.5f, yMin-0.5f, xMax - xMin+1, 0.5f);
            batch.draw(textureRegion,xMin-0.5f,yMin-0.5f,0.5f,yMax-yMin+1);
            batch.draw(textureRegion,xMin,yMax,xMax-xMin+0.5f,0.5f);
            batch.draw(textureRegion,xMax,yMin,0.5f,yMax-yMin+0.5f);
        }
    }
}
