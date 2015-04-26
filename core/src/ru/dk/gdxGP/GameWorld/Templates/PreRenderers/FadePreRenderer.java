package ru.dk.gdxGP.GameWorld.Templates.PreRenderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import ru.dk.gdxGP.GameWorld.Interfaces.PreRenderer;
import ru.dk.gdxGP.GameWorld.Level;

/**
 * Created by DK on 18.04.2015.
 */
public class FadePreRenderer implements PreRenderer {
    final float rStep, gStep, bStep, aStep;
    float rFrom;
    float gFrom;
    float bFrom;
    float aFrom;
    float rTo;
    float gTo;
    float bTo;
    float aTo;
    float r, g, b, a;
    int stepTimes;
    int currentStepTimes = 0;
    boolean rize = true;

    public FadePreRenderer(float rFrom, float gFrom, float bFrom, float aFrom, float rTo, float gTo, float bTo, float aTo, int stepTimes) {
        this.rFrom = rFrom;
        this.gFrom = gFrom;
        this.bFrom = bFrom;
        this.aFrom = aFrom;
        this.rTo = rTo;
        this.gTo = gTo;
        this.bTo = bTo;
        this.aTo = aTo;
        this.r = rFrom;
        this.g = gFrom;
        this.b = bFrom;
        this.a = aFrom;
        this.stepTimes = stepTimes;
        this.rStep = (rTo - rFrom) / stepTimes;
        this.gStep = (gTo - gFrom) / stepTimes;
        this.bStep = (bTo - bFrom) / stepTimes;
        this.aStep = (aTo - aFrom) / stepTimes;
    }

    public FadePreRenderer(Color colorFrom, Color colorTo, int stepTimes) {
        this(colorFrom.r, colorFrom.g, colorFrom.b, colorFrom.a, colorTo.r, colorTo.g, colorTo.b, colorTo.a, stepTimes);
    }

    @Override
    public void preRender(Level level) {
        step();
        Gdx.gl.glClearColor(r, g, b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void step() {
        if (currentStepTimes == stepTimes) {
            rize = !rize;
            currentStepTimes = 0;
        }
        r += rStep * (rize ? 1 : -1);
        g += gStep * (rize ? 1 : -1);
        b += bStep * (rize ? 1 : -1);
        a += aStep * (rize ? 1 : -1);
        currentStepTimes++;
    }

}