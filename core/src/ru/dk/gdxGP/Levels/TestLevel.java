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
        this.setTimeFactor(0.01f);

        this.setMaxDistance(1000);
    }

    @Override
    public void setParticles(int w, int h) {
		Random rnd=new Random();
		for(int i=0;i<500;i++)
        	this.addComponent(new Component(rnd.nextInt(w*3)-w,rnd.nextInt(h*3)-h,rnd.nextInt(400)+100));
    }
}
