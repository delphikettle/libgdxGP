package ru.dk.gdxGP.GameWorld.Levels;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.*;
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
    }

    @Override
    public void setSizes() {
        setYMin(0);
        setXMin(0);
        setXMax(800);
        setYMax(800);
    }

    @Override
    public void setParticles(int w, int h) {
        ArrayList<Body> bodies=new ArrayList<Body>();
        Random rnd=new Random();
        for (int i = 0; i < 50; i++) {
            bodies.add(this.addFraction(new Fraction(this.getWorld(),
                    (rnd.nextInt(this.getXMax() - this.getXMin()) + this.getXMin()),
                    (rnd.nextInt(this.getYMax() - this.getYMin()) + this.getYMin()),
                    (rnd.nextInt(200) - 100), (rnd.nextInt(200) - 100), (rnd.nextInt(10000) + 81) * 0.25f)).getBody());
            for (int j = 0; j < bodies.size(); j++) {
                if(i==j)continue;
                DistanceJointDef jointDef= new DistanceJointDef();
                jointDef.bodyA=bodies.get(i);
                jointDef.bodyB=bodies.get(j);
                jointDef.collideConnected=true;
                jointDef.frequencyHz=0.0001f;
                jointDef.length=(bodies.get(i).getFixtureList().get(0).getShape().getRadius()+bodies.get(j).getFixtureList().get(0).getShape().getRadius())*1.5f*10;
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
