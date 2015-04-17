package ru.dk.gdxGP.GameWorld.Levels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import ru.dk.gdxGP.GameWorld.*;
import ru.dk.gdxGP.GameWorld.Tasks.TaskCombination;
import ru.dk.gdxGP.GameWorld.Tasks.TaskOnCoordinate;
import ru.dk.gdxGP.GameWorld.Tasks.TaskOnMass;
import ru.dk.gdxGP.GameWorld.Templates.FractionDrawerSet;
import ru.dk.gdxGP.GameWorld.WorldElements.Fraction;

import java.util.ArrayList;
import java.util.Random;

public class TestLevel01 extends Level {
    private final Random rnd = new Random();
    private Fraction mainFraction;

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
                level.processAccelerometer();
                level.interactAllWithAllFractions();
            }
        });
    }

    @Override
    public void setCameraPosition() {
        this.getStage().getCamera().position.set(
				(mainFraction.getX()+this.getStage().getCamera().position.x*25)/26,
				(mainFraction.getY()+this.getStage().getCamera().position.y*25)/26,
				0);
        this.getStage().setCameraZoom((this.getStage().getZoom()*25+mainFraction.getRadius())/26);
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
                    new Color(MathUtils.random(0.1f, 1), MathUtils.random(0.1f, 1), MathUtils.random(0.1f, 1), MathUtils.random(0.5f, 0.75f)))).getBody());
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

    @Override
    public void tap(final float x, final float y) {
        this.addAction(new ActionForNextStep() {
            @Override
            public void doSomethingOnStep(Level level) {
                System.out.println(level.getFraction(0).getMass());
                float vModule = 25;
                Vector2 v = new Vector2(TestLevel01.this.getFraction(0).getBody().getPosition());
                v.rotate(180);
                v.add(x, y);
                v.setLength(vModule);
                Fraction newFraction = level.getFraction(0).divide(TestLevel01.this.getFraction(0).getMass() * 0.125f, v.x, v.y);
                level.addFraction(
                        newFraction
                );

            }
        });
        //System.out.println("tap "+x+" "+y);
    }
}
