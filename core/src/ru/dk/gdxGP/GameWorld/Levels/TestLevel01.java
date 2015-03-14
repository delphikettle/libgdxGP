package ru.dk.gdxGP.GameWorld.Levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import ru.dk.gdxGP.GameWorld.WorldElements.Fraction;
import ru.dk.gdxGP.GameWorld.Level;

import java.util.ArrayList;
import java.util.Random;

public class TestLevel01 extends Level {
    public TestLevel01(int w, int h) {
        super(w, h);
        this.setTimeFactor(1f);
        this.setG(10);
        this.setK(10);
    }

    @Override
    public void setCameraPosition() {
		/*this.getStage().getCamera().position.set(
				(this.getParticles().get(0).getX()+this.getStage().getCamera().position.x*255)/256,
				(this.getParticles().get(0).getY()+this.getStage().getCamera().position.y*255)/256,
				0);*/
    }

    @Override
    public void preRender() {

    }

    @Override
    public void afterRender() {

    }

    @Override
    public void setSizes() {
        setYMin(0);
        setXMin(0);
        //setXMax(800);
        //setYMax(800);
    }


    @Override
    public void setParticles() {
        ArrayList<Body> bodies=new ArrayList<Body>();
        Random rnd=new Random();/*
        bodies.add(this.addFraction(new Fraction(this.getWorld(),
                (rnd.nextInt(this.getXMax() - this.getXMin()) + this.getXMin()),
                (rnd.nextInt(this.getYMax() - this.getYMin()) + this.getYMin()),
                (rnd.nextInt(200) - 100), (rnd.nextInt(200) - 100),
                (rnd.nextInt(10000) + 81) * 0.00025f)).getBody());*/
        for (int i = 0;
             i < 10/*Gdx.graphics.getHeight()*Gdx.graphics.getWidth()/(50000)*/;
             i++) {

            bodies.add(this.addFraction(new Fraction(this.getWorld(),
                    (rnd.nextInt(this.getXMax() - this.getXMin()) + this.getXMin()),
                    (rnd.nextInt(this.getYMax() - this.getYMin()) + this.getYMin()),
                    (rnd.nextInt(200) - 100)*0f, (rnd.nextInt(200) - 100)*0f,
                    (rnd.nextInt(2500) + 4))).getBody());

            /*for (int j = 0; j < bodies.size(); j++) {
                if(i==j)continue;
                DistanceJointDef jointDef= new DistanceJointDef();
                jointDef.bodyA=bodies.get(i);
                jointDef.bodyB=bodies.get(j);
                jointDef.collideConnected=true;
                jointDef.dampingRatio=0.1f;
                jointDef.frequencyHz=0.005f;
                jointDef.length=
                        (bodies.get(i).getFixtureList().get(0).getShape().getRadius()+bodies.get(j).getFixtureList().get(0).getShape().getRadius())*0f;
                Joint joint1=
                        this.getWorld().createJoint(jointDef);
            }*/
        }
    }

    /*
    @Override
    public void renderBorders() {
        super.drawBorders(this.getStage().getBatch());
    }
    */

    @Override
    public void setOtherElements() {

    }
}
