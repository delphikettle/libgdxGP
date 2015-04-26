package ru.dk.gdxGP.GameWorld.Levels;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import ru.dk.gdxGP.GameWorld.ParticleDef;
import ru.dk.gdxGP.GameWorld.InterfacesForActions.*;
import ru.dk.gdxGP.GameWorld.Level;
import ru.dk.gdxGP.GameWorld.Mission;
import ru.dk.gdxGP.GameWorld.Task;
import ru.dk.gdxGP.GameWorld.Tasks.TaskOnAction;
import ru.dk.gdxGP.GameWorld.Tasks.TimeTask;
import ru.dk.gdxGP.GameWorld.Templates.ParticleDrawerSet;
import ru.dk.gdxGP.GameWorld.Templates.PreRenderers.FadePreRenderer;
import ru.dk.gdxGP.GameWorld.WorldElements.Particle;
import ru.dk.gdxGP.Screens.LevelScreen;

/**
 * Created by DK on 19.04.2015.
 */
public class ForcesTutorialLevel extends Level {
    private TimeTask task01, task02, task03, task04, task05, task06, task07;
    private TaskOnAction task08;
    private Particle mainParticle, secondParticle;

    @Override
    protected void setSizes() {
        this.setXMin(-5);
        this.setYMin(-5);
        this.setXMax(5);
        this.setYMax(5);
    }

    @Override
    protected void setParameters() {
        ForcesTutorialLevel.this.setCameraPositionChanger(new CameraPositionChanger() {
            @Override
            public void changeCameraPosition(Level level, Camera camera, LevelScreen screen) {
                ForcesTutorialLevel.this.moveCamera(mainParticle.getX(), mainParticle.getY(), 25);
                screen.setCameraZoom((screen.getZoom() * 25 + 1) / 26);
            }
        });
        this.setG(0);
        this.setK(0);
        this.setChargingK(0);
        this.setMassFlowingK(0);
        this.setPreRenderer(new FadePreRenderer(new Color(1, 1, 1, 1), new Color(0.75f, 1f, 0.75f, 1), 250));
    }

    @Override
    protected void setParticles() {
        ParticleDef particleDef = new ParticleDef(-2.5f, -2.5f, 0, 0);
        particleDef.minMass = particleDef.maxMass = 0.5f;
        particleDef.minCharge = particleDef.maxCharge = 0;
        mainParticle = super.generateRandomParticle(particleDef);
        mainParticle.setDrawer(ParticleDrawerSet.mainDrawer);
        this.addParticle(mainParticle);
        particleDef = new ParticleDef(2.5f, 2.5f, 0, 0);
        particleDef.minMass = particleDef.maxMass = 0.25f;
        particleDef.minCharge = particleDef.maxCharge = 0;
        secondParticle = super.generateRandomParticle(particleDef);

    }

    @Override
    public void setOtherElements() {

    }

    @Override
    protected Mission createMission() {
        Mission mission = new Mission("");
        task01 = new TimeTask(7000);
        task01.start();
        task01.setMainTaskText("In this world there are many other particles, which has its own characteristics.");
        task01.setSecondaryTaskText("E.g. mass, electric charge, color etc.");
        task01.setActionAfterAchievedTask(new ActionAfterAchievedTask() {
            @Override
            public void actionAfterAchievedTask(Task task) {
                ForcesTutorialLevel.this.addAction(new ActionForNextStep() {
                    @Override
                    public void doSomethingOnStep(Level level) {
                        ForcesTutorialLevel.this.addParticle(secondParticle);
                    }
                });
                ForcesTutorialLevel.this.setCameraPositionChanger(new CameraPositionChanger() {
                    @Override
                    public void changeCameraPosition(Level level, Camera camera, LevelScreen screen) {
                        ForcesTutorialLevel.this.moveCamera(secondParticle.getX(), secondParticle.getY(), 25);
                        screen.setCameraZoom((screen.getZoom() * 25 + 1) / 26);
                    }
                });
                task02.start();
            }
        });
        task02 = new TimeTask(7000);
        task02.setMainTaskText("For example, this one has twice lower mass than your particle");
        task02.setActionAfterAchievedTask(new ActionAfterAchievedTask() {
            @Override
            public void actionAfterAchievedTask(Task task) {
                task03.start();
                ForcesTutorialLevel.this.setCameraPositionChanger(new CameraPositionChanger() {
                    @Override
                    public void changeCameraPosition(Level level, Camera camera, LevelScreen screen) {
                        ForcesTutorialLevel.this.moveCamera(0 * (secondParticle.getX() + mainParticle.getX()) / 2f, 0 * (secondParticle.getY() + mainParticle.getY()) / 2f, 25);
                        screen.setCameraZoom((screen.getZoom() * 25 + 2) / 26);
                    }
                });
            }
        });
        task03 = new TimeTask(7000);
        task03.setMainTaskText("In some levels there is Newton's law of universal gravitation");
        task03.setActionAfterAchievedTask(new ActionAfterAchievedTask() {
            @Override
            public void actionAfterAchievedTask(Task task) {
                ForcesTutorialLevel.this.addAction(new ActionForNextStep() {
                    @Override
                    public void doSomethingOnStep(Level level) {
                        secondParticle.getBody().setLinearVelocity(0, 2.5f);
                    }
                });
                ForcesTutorialLevel.this.setLevelProceeder(new LevelProceeder() {
                    @Override
                    public void proceed(Level level, float delta) {
                        level.interactAllWithAllParticles();
                    }
                });
                ForcesTutorialLevel.this.setG(100);
                task04.start();
            }
        });
        task04 = new TimeTask(15000);
        task04.setMainTaskText("And every particle can be influenced by this law");
        task04.setActionAfterAchievedTask(new ActionAfterAchievedTask() {
            @Override
            public void actionAfterAchievedTask(Task task) {
                ForcesTutorialLevel.this.setK(100);
                mainParticle.setCharge(2);
                secondParticle.setCharge(2);
                task05.start();
            }
        });
        task05 = new TimeTask(10000);
        task05.setMainTaskText("But what happens if I make both particles positive?");
        task05.setActionAfterAchievedTask(new ActionAfterAchievedTask() {
            @Override
            public void actionAfterAchievedTask(Task task) {
                secondParticle.setCharge(-2);
                task06.start();
            }
        });
        task06 = new TimeTask(10000);
        task06.setMainTaskText("And what if one became negative?");
        task06.setActionAfterAchievedTask(new ActionAfterAchievedTask() {
            @Override
            public void actionAfterAchievedTask(Task task) {
                task07.start();
            }
        });
        task07 = new TimeTask(10000);
        task07.setMainTaskText("Yes, they both forced by Coulomb's law");
        task07.setActionAfterAchievedTask(new ActionAfterAchievedTask() {
            @Override
            public void actionAfterAchievedTask(Task task) {
                ForcesTutorialLevel.this.setLevelTapper(new LevelTapper() {
                    @Override
                    public void tapLevel(Level level, float x, float y) {
                        task08.setCompleted(true);
                    }
                });
            }
        });
        task08 = new TaskOnAction();
        task08.setMainTaskText("You've finished ForcesTutorialLevel. Tap to exit.");
        task08.setActionAfterAchievedTask(new ActionAfterAchievedTask() {
            @Override
            public void actionAfterAchievedTask(Task task) {
                System.exit(0);
            }
        });

        mission.addTask(task01);
        mission.addTask(task02);
        mission.addTask(task03);
        mission.addTask(task04);
        mission.addTask(task05);
        mission.addTask(task06);
        mission.addTask(task07);
        mission.addTask(task08);
        return mission;
    }
}
