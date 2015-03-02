package ru.dk.gdxGP.GameWorld.Levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import ru.dk.gdxGP.GameWorld.Fraction;
import ru.dk.gdxGP.GameWorld.Level;

import java.util.ArrayList;
import java.util.Random;

public class TestLevel01 extends Level {
    public TestLevel01(int w, int h) {
        super(w, h);
        this.setTimeFactor(1f);
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
    public void render() {
        this.getStage().getBatch().enableBlending();
        //this.getStage().getBatch().setColor(0,0,1,0.5f);
        this.getStage().superDraw();
        //this.getStage().getBatch().disableBlending();
        this.drawBorders(this.getStage().getBatch());
    }

    @Override
    public void setSizes() {
        //setYMin(-8);
        //setXMin(-8);
        //setXMax(800);
        //setYMax(800);
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
        for (int i = 0;
             i < Gdx.graphics.getHeight()*Gdx.graphics.getWidth()/(10000)                            ;
             i++) {

            bodies.add(this.addFraction(new Fraction(this.getWorld(),
                    (rnd.nextInt(this.getXMax() - this.getXMin()) + this.getXMin()),
                    (rnd.nextInt(this.getYMax() - this.getYMin()) + this.getYMin()),
                    (rnd.nextInt(200) - 100)*0.1f, (rnd.nextInt(200) - 100)*0.1f,
                    (rnd.nextInt(2500) + 4) * 1f)).getBody());

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

    @Override
    public void setBorders(int w, int h) {

    }
}
