package ru.dk.gdxGP.GameWorld.Levels;

import com.badlogic.gdx.math.MathUtils;
import ru.dk.gdxGP.GameWorld.InterfacesForActions.ActionAfterAchievedTask;
import ru.dk.gdxGP.GameWorld.InterfacesForActions.CheckerForTask;
import ru.dk.gdxGP.GameWorld.InterfacesForActions.LevelProceeder;
import ru.dk.gdxGP.GameWorld.InterfacesForActions.LevelTapper;
import ru.dk.gdxGP.GameWorld.Level;
import ru.dk.gdxGP.GameWorld.Mission;
import ru.dk.gdxGP.GameWorld.ParticleDef;
import ru.dk.gdxGP.GameWorld.Task;
import ru.dk.gdxGP.GameWorld.Tasks.*;
import ru.dk.gdxGP.GameWorld.WorldElements.Particle;

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
        particleDef.minMass=particleDef.maxMass=5f;
        particleDef.minCharge=particleDef.maxCharge=5f;
        this.mainParticle=this.generateRandomParticle(particleDef);
        this.mainParticle.setUnderCoulomb(true);
        this.addParticle(mainParticle);

        particleDef=new ParticleDef(getXMin(),getXMax(),getYMin(),getYMax());
        particleDef.minMass=particleDef.maxMass=0.05f;
        particleDef.minCharge=-0.1f;
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
        this.setK(50);
        this.setChargingK(2.5f);
    }

    @Override
    public void setOtherElements() {

    }

    @Override
    protected Mission createMission() {
        final Mission mission=new Mission("");
        task00=new TimeTask(3000);
        task00.start();
        task00.setMainTaskText("You have a very positive charge.");
        task00.setSecondaryTaskText("So you must charge all particles around.");
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
                        level.divideOnTap(mainParticle, 50, 0.01f, x, y,false,true);
                    }
                });
                System.out.print("firstFinished");
                subTask02.start();
            }
        });
        subTask01 =new CustomTask(new CheckerForTask() {
            @Override
            public boolean checkTask(Task task) {
                task01.setSecondaryTaskText("Hurry up! You have "+subTask02.getTimeToFinish()+"ms");
                for (int i = 0; i < particleList.size(); i++) {
                    if(particleList.get(i).getCharge()<0.5f)
                        return false;
                }
                return true;
            }
        });
        subTask02=new TimeTask(10000);
        subTask02.setActionAfterAchievedTask(new ActionAfterAchievedTask() {
            @Override
            public void actionAfterAchievedTask(Task task) {
                System.out.println("firstFinished");
                subTask01.setChecker(new CheckerForTask() {
                    @Override
                    public boolean checkTask(Task task) {
                        return false;
                    }
                });
                ChargeAllLevel.this.setLevelTapper(new LevelTapper() {
                    @Override
                    public void tapLevel(Level level, float x, float y) {
                        System.exit(0);
                    }
                });
                task01.setMainTaskText("You failed! You must be more quickly!");
                task01.setSecondaryTaskText("Tap to exit!");
            }
        });
        task01=new TaskCombination(new Task[]{
                new NotTask(subTask02),
                subTask01
        }, TaskCombination.TC_AND, true);
        task01.setMainTaskText("Charge all particles around during 10sec.");
        task01.setSecondaryTaskText("Hurry up! You have 10000ms");


        task01.setActionAfterAchievedTask(new ActionAfterAchievedTask() {
            @Override
            public void actionAfterAchievedTask(Task task) {
                NullTask task03=new NullTask();
                task03.setMainTaskText("My congratulations! You've completed your first mission!");
                task03.setSecondaryTaskText("Tap 'back' button to exit");
                ChargeAllLevel.this.setLevelTapper(new LevelTapper() {
                    @Override
                    public void tapLevel(Level level, float x, float y) {
                    }
                });
                mission.addTask(task03);
            }
        });
        mission.addTask(task00);
        mission.addTask(task01);
        return mission;
    }
}
