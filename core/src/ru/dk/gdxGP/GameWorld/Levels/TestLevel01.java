package ru.dk.gdxGP.GameWorld.Levels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ru.dk.gdxGP.GameWorld.ActionForNextStep;
import ru.dk.gdxGP.GameWorld.WorldElements.Fraction;
import ru.dk.gdxGP.GameWorld.Level;

import java.util.ArrayList;
import java.util.Random;

public class TestLevel01 extends Level {
    public TestLevel01() {
        super();
        this.setTimeFactor(0.1f);
        this.setG(-1);
        this.setK(1);
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
    public void loadAssets() {
        this.loadAssets(standardAssetsPaths);
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
             i < 5/*Gdx.graphics.getHeight()*Gdx.graphics.getWidth()/(50000)*/;
             i++) {

            bodies.add(this.addFraction(new Fraction(this.getWorld(),
                    (rnd.nextInt(this.getXMax() - this.getXMin()) + this.getXMin()),
                    (rnd.nextInt(this.getYMax() - this.getYMin()) + this.getYMin()),
                    (rnd.nextInt(200) - 100) * 0f, (rnd.nextInt(200) - 100) * 0f,
                    (rnd.nextInt(2500) + 400) * 2.0f, (float)(Math.pow(-1,i)*MathUtils.random(0f, 1f)), 1, 1, 1, Fraction.Condition.Liquid,
                    new Color(MathUtils.random(0.1f, 1), MathUtils.random(0.1f, 1), MathUtils.random(0.1f, 1), MathUtils.random(0.5f, 0.75f)))).getBody());
            if(MathUtils.random.nextBoolean())((Fraction)bodies.get(i).getUserData()).setCondition(Fraction.Condition.Liquid);
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

    @Override
    public void tap(final float x, final float y) {

        this.addAction(new ActionForNextStep() {
            @Override
            public void doSomethingOnStep(Level level) {
                Random rnd = new Random();
                float vModule=1000;
                Vector2 v=new Vector2(TestLevel01.this.getFraction(0).getBody().getPosition());
                v.rotate(180);
                v.add(x,y);
                v.setLength(vModule);
                level.addFraction(
                        level.getFraction(0).divide(TestLevel01.this.getFraction(0).getBody().getMass() * 0.125f, v.x, v.y)
                );

            }
        });
    }
}
