package ru.dk.gdxGP.GameWorld.Levels;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.*;
import ru.dk.gdxGP.Box2DFactory;
import ru.dk.gdxGP.GameWorld.Fraction;
import ru.dk.gdxGP.GameWorld.Level;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Андрей on 28.02.2015.
 */
public class TestLevel01 extends Level {
    public TestLevel01(int w, int h) {
        super(w, h);
        this.setTimeFactor(0.1f);
    }

    @Override
    public void setSizes() {
        //setYMin(-8);
        //setXMin(-8);
        setXMax(800);
        setYMax(800);
    }

    @Override
    public void createWalls() {
        super.createWalls();
		/* Create the walls */
        /*
        Box2DFactory.createWalls(this.getWorld(), this.getStage().getCamera().viewportWidth,
                this.getStage().getCamera().viewportHeight, 1);
                */
    }

    @Override
    public void setParticles(int w, int h) {
        ArrayList<Body> bodies=new ArrayList<Body>();
        Random rnd=new Random();/*
        bodies.add(this.addFraction(new Fraction(this.getWorld(),
                (rnd.nextInt(this.getXMax() - this.getXMin()) + this.getXMin()),
                (rnd.nextInt(this.getYMax() - this.getYMin()) + this.getYMin()),
                (rnd.nextInt(200) - 100), (rnd.nextInt(200) - 100),
                (rnd.nextInt(10000) + 81) * 0.00025f)).getBody());*/
        for (int i = 0; i < 50; i++) {

            bodies.add(this.addFraction(new Fraction(this.getWorld(),
                    (rnd.nextInt(this.getXMax() - this.getXMin()) + this.getXMin()),
                    (rnd.nextInt(this.getYMax() - this.getYMin()) + this.getYMin()),
                    (rnd.nextInt(200) - 100), (rnd.nextInt(200) - 100),
                    (rnd.nextInt(10000) + 81) * 0.25f)).getBody());


            /*
            Shape shape = Box2DFactory.createCircleShape(1);
            FixtureDef fixtureDef = Box2DFactory.createFixture(shape, 2.5f,
                    0.25f, 1f, false);
            bodies.add(Box2DFactory.createBody(this.getWorld(), BodyDef.BodyType.DynamicBody, fixtureDef,
                    new Vector2(
                            (rnd.nextInt(this.getXMax() - this.getXMin()) + this.getXMin()),
                            (rnd.nextInt(this.getYMax() - this.getYMin()) + this.getYMin()))));*/
            for (int j = 0; j < bodies.size(); j++) {
                if(i==j)continue;
                DistanceJointDef jointDef= new DistanceJointDef();
                jointDef.bodyA=bodies.get(i);
                jointDef.bodyB=bodies.get(j);
                jointDef.collideConnected=true;
                jointDef.frequencyHz=0.025f;
                jointDef.length=
                        (bodies.get(i).getFixtureList().get(0).getShape().getRadius()+bodies.get(j).getFixtureList().get(0).getShape().getRadius())*1.5f*5f;
                Joint joint1=
                        this.getWorld().createJoint(jointDef);
            }
        }
        /*
        if(bodies.get(0)==null)System.out.println("A is null!");
        if(bodies.get(1)==null)System.out.println("B is null!");
        JointDef jointDef=new DistanceJointDef();
        jointDef.bodyA=bodies.get(0);
        jointDef.bodyB=bodies.get(1);
        jointDef.collideConnected=true;
        Joint joint1=
                this.getWorld().createJoint(jointDef);
                */
    }

    @Override
    public void setBorders(int w, int h) {

    }
}
