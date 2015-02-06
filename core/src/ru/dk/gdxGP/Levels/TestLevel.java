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
		this.setG(0.05f);
        this.setTimeFactor(0.001f);

    }

    @Override
    public void setParticles(int w, int h) {
		Random rnd=new Random();
		for(int i=0;i<250;i++)
        	this.addComponent(new Component(rnd.nextInt(w*2),rnd.nextInt(h*2),rnd.nextInt(2500)+100));
    }
}
