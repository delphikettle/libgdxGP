package ru.dk.gdxGP.GameWorld.Levels;

import com.badlogic.gdx.physics.box2d.*;
import ru.dk.gdxGP.GameWorld.Component;
import ru.dk.gdxGP.GameWorld.Fraction;
import ru.dk.gdxGP.GameWorld.Level;
import java.util.*;

/**
 * Created by Андрей on 12.01.2015.
 */
public class TestLevel extends Level {
    public TestLevel(int w, int h) {
        super(w, h);
		//this.setG(1f);
        this.setTimeFactor(1f);
        //this.setMaxDistance(1000);
    }

    @Override
    public void setSizes() {
        this.setXMin(-0*this.getXMax());
        this.setYMin(-0*this.getYMax());
        this.setXMax(2*(this.getXMax()));
        this.setYMax(2*(this.getYMax()));

    }

    @Override
    public void setParticles(int w, int h) {
		Random rnd=new Random();
        this.addComponent(new Component(100,100,4000));
        for (int i = 0; i < 500; i++) {
            this.addFraction(new Fraction(this.getWorld(),
                    (rnd.nextInt(this.getXMax()-this.getXMin())+this.getXMin()),
                    (rnd.nextInt(this.getYMax()-this.getYMin())+this.getYMin()),
                    (rnd.nextInt(200)-100)*10000,(rnd.nextInt(200)-100)*10000,(rnd.nextInt(10000)+81)*0.25f));
        }
        for(int i=0;i<0;i++)
        	this.addComponent(new Component((rnd.nextInt(w*3)-w)*0.2f,(rnd.nextInt(h*3)-h)*0.2f,rnd.nextInt(400)+100));
    }

    @Override
    public void setBorders(int w, int h) {

        BodyDef bodyDef=new BodyDef();
        bodyDef.type= BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0,0);
        bodyDef.fixedRotation=false;
        bodyDef.allowSleep=false;
        System.out.println(getXMax());
        System.out.println(getYMax());
        ChainShape shape=new ChainShape();
        shape.createChain(new float[]{


                getXMin(), getYMin(),
                getXMin(), getYMax(),
                getXMax(), getYMax(),
                getXMax(), getYMin(),
                getXMin(), getYMin()
        });
        FixtureDef fixtureDef=new FixtureDef();
        fixtureDef.shape=shape;
        fixtureDef.friction=1.0f;
        fixtureDef.density=0.5f;
        fixtureDef.restitution=0.0f;
        Body body=this.getWorld().createBody(bodyDef);
        body.createFixture(fixtureDef);
        MassData massData=new MassData();
        massData.mass=Integer.MAX_VALUE;
        massData.center.set(getXMax()-getXMin(),getYMax()-getYMin());
        body.setMassData(massData);
        body.setUserData("border");
/*
        this.addBorder(new Border(this.getWorld(),this.getXMin()-w/2,this.getYMin()-h/2,10,this.getYMax()-this.getYMin(),false));
        this.addBorder(new Border(this.getWorld(),this.getXMin()-w/2,this.getYMax()-h/2,this.getXMax()-this.getXMin(),10,true));
        this.addBorder(new Border(this.getWorld(),this.getXMin()-w/2,this.getYMin()-h/2,this.getXMax()-this.getXMin(),10,true));
        this.addBorder(new Border(this.getWorld(),this.getXMax()-w/2,this.getYMin()-h/2,10,this.getYMax()-this.getYMin(),false));
*/
        //this.addBorder(new Border(this.getWorld(),this.getXMin(),this.getYMin()+(this.getYMax()-this.getYMin())*0,10,this.getYMax()-this.getYMin(),false));
        //this.addBorder(new Border(this.getWorld(),this.getXMin(),this.getYMax(),this.getXMax()-this.getXMin(),10,true));
       // this.addBorder(new Border(this.getWorld(),this.getXMin(),this.getYMin(),this.getXMax()-this.getXMin(),10,true));
        //this.addBorder(new Border(this.getWorld(),this.getXMax(),this.getYMin(),10,this.getYMax()-this.getYMin(),false));

        //this.addBorder(new Border(this.getWorld(),this.getXMin()-w/2,this.getYMin()-h/2,10,10,false));
        //this.addBorder(new Border(this.getWorld(), 0,0,100,100));
    }
}
