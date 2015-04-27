package ru.dk.gdxGP.GameWorld.Levels;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import ru.dk.gdxGP.GameWorld.Interfaces.Actions.*;
import ru.dk.gdxGP.GameWorld.Level;
import ru.dk.gdxGP.GameWorld.Mission;
import ru.dk.gdxGP.GameWorld.ParticleDef;
import ru.dk.gdxGP.GameWorld.Task;
import ru.dk.gdxGP.GameWorld.Tasks.*;
import ru.dk.gdxGP.GameWorld.Templates.ParticleDrawerSet;
import ru.dk.gdxGP.GameWorld.Templates.PreRenderers.FadePreRenderer;
import ru.dk.gdxGP.GameWorld.WorldElements.Particle;
import ru.dk.gdxGP.Screens.LevelScreen;

import java.util.ArrayList;
import java.util.List;

public class ChargeAllLevel extends Level {
    private CustomTask subTask01;
    private TimeTask subTask02;
    private TaskCombination task01;
    private TimeTask task00;
    private Particle mainParticle;
    private List<Particle> particleList=new ArrayList<Particle>();

    @Override
    protected void setSizes() {
        this.setXMin(-5);
        this.setXMax(5);
        this.setYMin(-5);
        this.setYMax(5);
    }

    @Override
    protected void setParticles() {
        ParticleDef particleDef=new ParticleDef(0,0,0,0);
        particleDef.rMax=particleDef.rMin=particleDef.aMin=particleDef.aMax=1;
        particleDef.gMax=particleDef.gMin=particleDef.bMin=particleDef.bMax=0;
        particleDef.minMass=particleDef.maxMass=1f;
        particleDef.minCharge=particleDef.maxCharge=5f;
        this.mainParticle=this.generateRandomParticle(particleDef);
        this.mainParticle.setUnderCoulomb(true);
        this.mainParticle.setDrawer(ParticleDrawerSet.mainDrawer);
        this.addParticle(mainParticle);

        particleDef=new ParticleDef(getXMin(),getXMax(),getYMin(),getYMax());
        particleDef.minMass=particleDef.maxMass=0.05f;
        particleDef.minCharge=-0.5f;
        particleDef.maxCharge=0.0f;
        Particle bufParticle;
        for (int i = 0; i < 25; i++) {
            bufParticle=this.generateRandomParticle(particleDef);
            bufParticle.setUnderCoulomb(true);
            particleList.add(bufParticle);
            this.addParticle(bufParticle);
        }
    }

    @Override
    protected void setParameters() {
        this.setK(25);
        this.setChargingK(1f);
        this.getStage().setCameraZoom(2);
        this.setCameraPositionChanger(new CameraPositionChanger() {
            @Override
            public void changeCameraPosition(Level level, Camera camera, LevelScreen screen) {
                level.moveCamera(mainParticle.getX() * 0, mainParticle.getY() * 0, 25);
            }
        });
        this.setPreRenderer(new FadePreRenderer(new Color(1,1,1,1),new Color(1,1,0.75f,1),100 ));
    }

    @Override
    public void setOtherElements() {

    }

    @Override
    protected Mission createMission() {
        final Mission mission=new Mission("");
        task00=new TimeTask(7000);
        task00.start();
        task00.setMainTaskText("You must charge all particles around, because of your big positive charge.");
        task00.setSecondaryTaskText("You can control your particle via tapping the point you must move to");
        task00.setActionAfterAchievedTask(new ActionAfterAchievedTask() {
            @Override
            public void actionAfterAchievedTask(Task task) {
                ChargeAllLevel.this.setLevelProceeder(new LevelProceeder() {
                    @Override
                    public void proceed(Level level, float delta) {
                        level.interactAllWithAllParticles();
                    }
                });
                ChargeAllLevel.this.setLevelTapper(new LevelTapper() {
                    @Override
                    public void tapLevel(Level level, float x, float y) {
                        level.moveOnTap(mainParticle,1f,x,y);
                    }
                });
                subTask02.start();
            }
        });
        subTask01 =new CustomTask(new CheckerForTask() {
            @Override
            public boolean checkTask(Task task) {
                task01.setSecondaryTaskText("Hurry up! You have "+MathUtils.round(subTask02.getTimeToFinish()/1000)+"sec");
                for (int i = 0; i < particleList.size(); i++) {
                    if(particleList.get(i).getCharge()<0.0005f)
                        return false;
                }
                return true;
            }
        });
        subTask02=new TimeTask(30000);
        subTask02.setActionAfterAchievedTask(new ActionAfterAchievedTask() {
            @Override
            public void actionAfterAchievedTask(Task task) {
                NullTask task03=new NullTask();
                task03.setMainTaskText("You failed! You must be more quickly!");
                task03.setSecondaryTaskText("Tap 'back' button to exit");
                mission.setCurrentTask(task03);
            }
        });
        task01=new TaskCombination(new Task[]{
                new NotTask(subTask02),
                subTask01
        }, TaskCombination.TC_AND, true);
        task01.setMainTaskText("Charge all particles around during 30sec.");
        task01.setActionAfterAchievedTask(new ActionAfterAchievedTask() {
            @Override
            public void actionAfterAchievedTask(Task task) {
                NullTask task03=new NullTask();
                task03.setMainTaskText("My congratulations! You've completed your first mission at "+MathUtils.round(subTask02.getTimeToFinish()/100)/10f+"sec before fail!");
                task03.setSecondaryTaskText("Tap 'back' button to exit");
                mission.addTask(task03);
            }
        });
        mission.addTask(task00);
        mission.addTask(task01);
        return mission;
    }
}
