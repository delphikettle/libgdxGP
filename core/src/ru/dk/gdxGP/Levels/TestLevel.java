package ru.dk.gdxGP.Levels;

import ru.dk.gdxGP.Component;
import ru.dk.gdxGP.Level;
import java.util.*;

/**
 * Created by Андрей on 12.01.2015.
 */
public class TestLevel extends Level {
    public TestLevel(int w, int h) {
        super(w, h);
		this.setG(1f);
        this.setTimeFactor(1f);

        this.setXMax(this.getXMax()*2);
        this.setYMax(this.getYMax()*2);
        this.setMaxDistance(1000);
    }

    @Override
    public void setParticles(int w, int h) {
		Random rnd=new Random();
        this.addComponent(new Component(100,100,4000));
		for(int i=0;i<1000;i++)
        	this.addComponent(new Component((rnd.nextInt(w*3)-w)*1f,(rnd.nextInt(h*3)-h)*1f,rnd.nextInt(400)+100));
    }
}
