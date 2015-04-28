package ru.dk.gdxGP.GameWorld.Levels;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import ru.dk.gdxGP.GameWorld.Interfaces.Actions.ActionAfterAchievedTask;
import ru.dk.gdxGP.GameWorld.Interfaces.Actions.CameraPositionChanger;
import ru.dk.gdxGP.GameWorld.Interfaces.Actions.LevelProceeder;
import ru.dk.gdxGP.GameWorld.Interfaces.Actions.LevelTapper;
import ru.dk.gdxGP.GameWorld.Level;
import ru.dk.gdxGP.GameWorld.Mission;
import ru.dk.gdxGP.GameWorld.ParticleDef;
import ru.dk.gdxGP.GameWorld.Task;
import ru.dk.gdxGP.GameWorld.Tasks.TaskOnAction;
import ru.dk.gdxGP.GameWorld.Tasks.TimeTask;
import ru.dk.gdxGP.GameWorld.Templates.ParticleDrawerSet;
import ru.dk.gdxGP.GameWorld.Templates.PreRenderers.FadePreRenderer;
import ru.dk.gdxGP.GameWorld.WorldElements.Particle;
import ru.dk.gdxGP.Screens.LevelScreen;

public class InteractionTutorialLevel extends Level {
    private TimeTask task01;
    private TimeTask task02;
    private TimeTask task03;
    private TimeTask task04;
    private TaskOnAction task05;
    private Particle mainParticle;

    @Override
    protected void setSizes() {
        this.setXMin(-5);
        this.setYMin(-5);
        this.setXMax(5);
        this.setYMax(5);
    }

    @Override
    protected void setParameters() {
        InteractionTutorialLevel.this.setCameraPositionChanger(new CameraPositionChanger() {
            @Override
            public void changeCameraPosition(Level level, Camera camera, LevelScreen screen) {
                InteractionTutorialLevel.this.moveCamera(mainParticle.getX(), mainParticle.getY(), 25);
                screen.setCameraZoom((screen.getZoom() * 25 + 0.5f) / 26);
            }
        });
        this.setG(0);
        this.setK(0);
        this.setChargingK(2.5f);
        this.setMassFlowingK(0);
        this.setPreRenderer(new FadePreRenderer(new Color(1, 1, 1, 1), new Color(0.75f, 0.75f, 1f, 1), 250));
    }

    @Override
    protected void setParticles() {
        ParticleDef particleDef = new ParticleDef(0, 0, 0, 0);
        particleDef.minMass = particleDef.maxMass = 1f;
        particleDef.minCharge = particleDef.maxCharge = 5f;
        mainParticle = super.generateRandomParticle(particleDef);
        mainParticle.setDrawer(ParticleDrawerSet.mainDrawer);
        this.addParticle(mainParticle);
    }

    @Override
    public void setOtherElements() {

    }

    @Override
    protected Mission createMission() {
        Mission mission = new Mission("");
        task01 = new TimeTask(5000);
        task01.setMainTaskText("This particle has very positive charge.");
        task01.setSecondaryTaskText("");
        task01.start();
        task01.setActionAfterAchievedTask(new ActionAfterAchievedTask() {
            @Override
            public void actionAfterAchievedTask(Task task) {
                InteractionTutorialLevel.this.setCameraPositionChanger(new CameraPositionChanger() {
                    @Override
                    public void changeCameraPosition(Level level, Camera camera, LevelScreen screen) {
                        InteractionTutorialLevel.this.moveCamera(0, 0, 25);
                        screen.setCameraZoom((screen.getZoom() * 25 + 1.75f) / 26);
                    }
                });
                InteractionTutorialLevel.this.setLevelProceeder(new LevelProceeder() {
                    @Override
                    public void proceed(Level level, float delta) {
                        level.interactAllWithAllParticles();
                    }
                });
                ParticleDef particleDef = new ParticleDef(0.75f * getXMin(), 0.75f * getXMax(), 0.75f * getYMin(), 0.75f * getYMax());
                particleDef.minCharge = -2f;
                particleDef.maxCharge = -1f;
                particleDef.minMass = particleDef.maxMass = 0.05f;
                InteractionTutorialLevel.this.addRandomParticles(particleDef, 25);

                task02.start();
            }
        });
        task02 = new TimeTask(15000);
        task02.setMainTaskText("And it can share its charge to other particles");
        task02.setSecondaryTaskText("As you can see, nearest particles became more positive than furthest");
        task02.setActionAfterAchievedTask(new ActionAfterAchievedTask() {
            @Override
            public void actionAfterAchievedTask(Task task) {
                task03.start();
                InteractionTutorialLevel.this.setK(5);
                InteractionTutorialLevel.this.setChargingK(1f);
            }
        });
        task03 = new TimeTask(7500);
        task03.setMainTaskText("And if I turn on Coulomb's law, particles will move");
        task03.setActionAfterAchievedTask(new ActionAfterAchievedTask() {
            @Override
            public void actionAfterAchievedTask(Task task) {
                InteractionTutorialLevel.this.setK(50);
                InteractionTutorialLevel.this.setChargingK(0.5f);
                InteractionTutorialLevel.this.setMassFlowingK(250);
                InteractionTutorialLevel.this.setCameraPositionChanger(new CameraPositionChanger() {
                    @Override
                    public void changeCameraPosition(Level level, Camera camera, LevelScreen screen) {
                        InteractionTutorialLevel.this.moveCamera(mainParticle.getX(), mainParticle.getY(), 25);
                        screen.setCameraZoom((screen.getZoom() * 25 + 0.75f) / 26);
                    }
                });
                task04.start();
            }
        });
        task04 = new TimeTask(15000);
        task04.setMainTaskText("When particles collide big particle take some mass from small particles");
        task04.setSecondaryTaskText("And some of them even were absorbed by big particle");
        task04.setActionAfterAchievedTask(new ActionAfterAchievedTask() {
            @Override
            public void actionAfterAchievedTask(Task task) {
                InteractionTutorialLevel.this.setLevelTapper(new LevelTapper() {
                    @Override
                    public void tapLevel(Level level, float x, float y) {
                        task05.setCompleted(true);
                    }
                });
                InteractionTutorialLevel.this.setCameraPositionChanger(new CameraPositionChanger() {
                    @Override
                    public void changeCameraPosition(Level level, Camera camera, LevelScreen screen) {
                        InteractionTutorialLevel.this.moveCamera(0, 0, 25);
                        screen.setCameraZoom((screen.getZoom() * 25 + 2f) / 26);
                    }
                });
            }
        });
        task05 = new TaskOnAction();
        task05.setMainTaskText("Now you know practically all about interactions between particles in this game");
        task05.setSecondaryTaskText("You've finished all tutorials in this game. Now it's time to try out real level.");

        task05.setActionAfterAchievedTask(new ActionAfterAchievedTask() {
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
        return mission;
    }
}
