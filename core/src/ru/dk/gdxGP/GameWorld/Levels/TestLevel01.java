package ru.dk.gdxGP.GameWorld.Levels;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import ru.dk.gdxGP.GameWorld.*;
import ru.dk.gdxGP.GameWorld.InterfacesForActions.CameraPositionChanger;
import ru.dk.gdxGP.GameWorld.InterfacesForActions.LevelProceeder;
import ru.dk.gdxGP.GameWorld.InterfacesForActions.LevelTapper;
import ru.dk.gdxGP.GameWorld.Tasks.TaskCombination;
import ru.dk.gdxGP.GameWorld.Tasks.TaskOnCoordinate;
import ru.dk.gdxGP.GameWorld.Tasks.TaskOnMass;
import ru.dk.gdxGP.GameWorld.Templates.FractionDrawerSet;
import ru.dk.gdxGP.GameWorld.Templates.PreRenderers.FadePreRenderer;
import ru.dk.gdxGP.GameWorld.WorldElements.Fraction;
import ru.dk.gdxGP.Screens.LevelScreen;

import java.util.ArrayList;
import java.util.Random;

public class TestLevel01 extends Level {
    private final Random rnd = new Random();
    private Fraction mainFraction;
    private float initialScale=1;
    public TestLevel01() {
        super();
    }

    @Override
    public void setParameters() {
        this.setG(100f);
        this.setK(10f);
        this.setChargingK(0.1f);
        this.setLevelProceeder(new LevelProceeder() {
            @Override
            public void proceed(Level level, float delta) {
                level.proceedParticles(delta);
                level.processAccelerometer(10);
                level.interactAllWithAllFractions();
            }
        });
        this.setCameraPositionChanger(new CameraPositionChanger() {
            @Override
            public void changeCameraPosition(Level level,Camera camera,LevelScreen screen) {
                camera.position.set(
                        (mainFraction.getX()+camera.position.x*255)/256,
                        (mainFraction.getY()+camera.position.y*255)/256,
                        0);
                screen.setCameraZoom((screen.getZoom() * 25 + initialScale) / 26);

            }
        });
        this.setLevelTapper(new LevelTapper() {
            @Override
            public void tapLevel(Level level, float x, float y) {
                TestLevel01.super.divideOnTap(mainFraction,25,0.125f,x, y);
            }
        });
        this.setPreRenderer(new FadePreRenderer(new Color(1f,1f,0.75f,0),new Color(1,1,0.5f,1f),60));
    }

    @Override
    public void setSizes() {
        setYMin(0);
        setXMin(0);
        setXMax((int) (getXMax() * 0.01f));
        setYMax((int) (getYMax() * 0.01f));
    }

    @Override
    public void setParticles() {
        ArrayList<Body> bodies = new ArrayList<Body>();
        mainFraction = new Fraction(this,this.getWorld(),
                (MathUtils.random(this.getXMin() + this.getWidth() * 0.1f, this.getXMax() - this.getWidth() * 0.1f)),
                (MathUtils.random(this.getYMin() + this.getHeight() * 0.1f, this.getYMax() - this.getHeight() * 0.1f)),
                (rnd.nextInt(200) - 100) * 0f, (rnd.nextInt(200) - 100) * 0f,
                (rnd.nextInt(2500) + 400) * 0.0005f, MathUtils.random(-1f, 1f), 1, 1, 1, Fraction.Condition.Liquid,
                new Color(MathUtils.random(0.1f, 1), MathUtils.random(0.1f, 1), MathUtils.random(0.1f, 1), MathUtils.random(0.5f, 0.75f)));
        mainFraction.setDrawer(FractionDrawerSet.mainDrawer);
        this.addFraction(mainFraction);
        for (int i = 0;
             i < 5/*Gdx.graphics.getHeight()*Gdx.graphics.getWidth()/(50000)*/;
             i++) {

            bodies.add(this.addFraction(new Fraction(this,this.getWorld(),
                    (MathUtils.random(this.getXMin() + this.getWidth() * 0.1f, this.getXMax() - this.getWidth() * 0.1f)),
                    (MathUtils.random(this.getYMin() + this.getHeight() * 0.1f, this.getYMax() - this.getHeight() * 0.1f)),
                    (rnd.nextInt(200) - 100) * 0f, (rnd.nextInt(200) - 100) * 0f,
                    (rnd.nextInt(2500) + 400) * 0.00001f, MathUtils.random(-5f, 5f), 1, 1, 1, Fraction.Condition.Liquid,
                    new Color(MathUtils.random(0.1f, 1), MathUtils.random(0.1f, 1), MathUtils.random(0.1f, 1), MathUtils.random(0.75f, 1f)))).getBody());
        }
    }

    @Override
    public void setOtherElements() {

    }

    @Override
    public Mission createMission() {
        final Mission mission = new Mission("");
        Task task01 = new TaskOnCoordinate(mainFraction, new Vector2(0, 0), 1);
        task01.setOnce(true);
        Task task02 = new TaskOnMass(mainFraction, 0.5f, 1f);
        task02.setOnce(true);
        final TaskCombination taskComb = new TaskCombination(new Task[]{task01, task02}, TaskCombination.TC_AND, true);
        taskComb.setTaskText("Make your particle be approximately in coordinates 0,0 with mass 0.5");

        mission.addTask(taskComb);

        Task task03=new TaskOnCoordinate(mainFraction,new Vector2(getWidth(),getHeight()), 1);
        task03.setTaskText("you've completed it");
        mission.addTask(task03);
        return mission;
    }
}
