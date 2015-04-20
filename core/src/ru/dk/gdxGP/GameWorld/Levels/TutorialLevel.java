package ru.dk.gdxGP.GameWorld.Levels;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import ru.dk.gdxGP.GameWorld.ParticleDef;
import ru.dk.gdxGP.GameWorld.InterfacesForActions.ActionAfterAchievedTask;
import ru.dk.gdxGP.GameWorld.InterfacesForActions.CameraPositionChanger;
import ru.dk.gdxGP.GameWorld.InterfacesForActions.LevelProceeder;
import ru.dk.gdxGP.GameWorld.InterfacesForActions.LevelTapper;
import ru.dk.gdxGP.GameWorld.Level;
import ru.dk.gdxGP.GameWorld.Mission;
import ru.dk.gdxGP.GameWorld.Task;
import ru.dk.gdxGP.GameWorld.Tasks.NullTask;
import ru.dk.gdxGP.GameWorld.Tasks.TaskOnAction;
import ru.dk.gdxGP.GameWorld.Tasks.TimeTask;
import ru.dk.gdxGP.GameWorld.Templates.ParticleDrawerSet;
import ru.dk.gdxGP.GameWorld.Templates.LevelProceederSet;
import ru.dk.gdxGP.GameWorld.Templates.PreRenderers.FadePreRenderer;
import ru.dk.gdxGP.GameWorld.WorldElements.Particle;
import ru.dk.gdxGP.Screens.LevelScreen;

/**
 * Created by STUDENT_7 on 16.04.2015.
 */
public class TutorialLevel extends Level {
    private Particle mainParticle;
    private TaskOnAction task01, task04, task03;
    private TimeTask task02;
    private NullTask task05;


    @Override
    protected void setSizes() {
        setXMax(10);
        setYMax(10);
    }

    @Override
    protected void setParameters() {
        TutorialLevel.this.setCameraPositionChanger(new CameraPositionChanger() {
            @Override
            public void changeCameraPosition(Level level, Camera camera, LevelScreen screen) {
                TutorialLevel.this.moveCamera(mainParticle.getX(), mainParticle.getY(), 25);
                screen.setCameraZoom((screen.getZoom() * 25 + 1) / 26);
            }
        });
        this.setPreRenderer(new FadePreRenderer(new Color(1, 1, 1, 1), new Color(1f, 1f, 0.9f, 1), 600));
    }

    @Override
    protected void setParticles() {
        ParticleDef particleDef = new ParticleDef(5, 5, 5, 5);
        particleDef.minMass = particleDef.maxMass = 0.5f;
        particleDef.minCharge = particleDef.maxCharge = 0;
        mainParticle = super.generateRandomParticle(particleDef);
        mainParticle.setDrawer(ParticleDrawerSet.mainDrawer);
        this.addParticle(mainParticle);
    }

    @Override
    public void setOtherElements() {

    }

    @Override
    protected Mission createMission() {
        Mission mission = new Mission("Tutorial");
        task01 = new TaskOnAction();
        task01.setMainTaskText("Hello! This is you! Your particle green circled! Tap on the screen!");
        this.setLevelTapper(new LevelTapper() {
            @Override
            public void tapLevel(Level level, float x, float y) {
                TutorialLevel.this.task01.setCompleted(true);
            }
        });
        task02 = new TimeTask(5000);
        task02.setMainTaskText("After 5 sec I'll turn on Gravitation");
        task01.setActionAfterAchievedTask(new ActionAfterAchievedTask() {
            @Override
            public void actionAfterAchievedTask(Task task) {
                task02.start();
            }
        });
        task02.setActionAfterAchievedTask(new ActionAfterAchievedTask() {
            @Override
            public void actionAfterAchievedTask(Task task) {
                TutorialLevel.this.setLevelProceeder(new LevelProceeder() {
                    @Override
                    public void proceed(Level level, float delta) {
                        level.processAccelerometer(5);
                    }
                });
                TutorialLevel.this.setCameraPositionChanger(new CameraPositionChanger() {
                    @Override
                    public void changeCameraPosition(Level level, Camera camera, LevelScreen screen) {
                        TutorialLevel.this.moveCamera(mainParticle.getX(), mainParticle.getY(), 255);
                        screen.setCameraZoom((screen.getZoom() * 25 + 1) / 26);
                    }
                });
                TutorialLevel.this.setLevelTapper(new LevelTapper() {
                    @Override
                    public void tapLevel(Level level, float x, float y) {
                        task03.setCompleted(true);
                    }
                });
            }
        });
        task03 = new TaskOnAction();
        task03.setMainTaskText("You can rotate your device to move your particle. Tap to stop it!");
        task03.setActionAfterAchievedTask(new ActionAfterAchievedTask() {
            @Override
            public void actionAfterAchievedTask(Task task) {
                TutorialLevel.this.setLevelProceeder(LevelProceederSet.noneProceed);
                TutorialLevel.this.getWorld().setGravity(new Vector2(0, 0));
                mainParticle.getBody().setLinearVelocity(0, 0);
                mainParticle.getBody().setAngularVelocity(0);
                TutorialLevel.this.setLevelTapper(new LevelTapper() {
                    private int times = 0;

                    @Override
                    public void tapLevel(Level level, float x, float y) {
                        level.divideOnTap(mainParticle, 10, 0.1f, x, y);
                        times++;
                        if (times >= 5) task04.setCompleted(true);
                    }
                });
            }
        });
        task04 = new TaskOnAction();
        task04.setMainTaskText("Also you can control your particle via tapping your screen. Tap 5 times!");
        task04.setActionAfterAchievedTask(new ActionAfterAchievedTask() {
            @Override
            public void actionAfterAchievedTask(Task task) {
                TutorialLevel.this.setLevelProceeder(new LevelProceeder() {
                    @Override
                    public void proceed(Level level, float delta) {
                        level.processAccelerometer(1);
                    }
                });
            }
        });
        task05 = new NullTask();
        task05.setMainTaskText("You've finished first tutorial. Enjoy this level or try out another level!");
        mission.addTask(task01);
        mission.addTask(task02);
        mission.addTask(task03);
        mission.addTask(task04);
        mission.addTask(task05);
        return mission;
    }
}
