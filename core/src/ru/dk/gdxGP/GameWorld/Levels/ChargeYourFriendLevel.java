package ru.dk.gdxGP.GameWorld.Levels;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import ru.dk.gdxGP.GameWorld.InterfacesForActions.*;
import ru.dk.gdxGP.GameWorld.Level;
import ru.dk.gdxGP.GameWorld.Mission;
import ru.dk.gdxGP.GameWorld.ParticleDef;
import ru.dk.gdxGP.GameWorld.Task;
import ru.dk.gdxGP.GameWorld.Tasks.*;
import ru.dk.gdxGP.GameWorld.Templates.ParticleDefSet;
import ru.dk.gdxGP.GameWorld.Templates.ParticleDrawerSet;
import ru.dk.gdxGP.GameWorld.Templates.PreRenderers.FadePreRenderer;
import ru.dk.gdxGP.GameWorld.WorldElements.Particle;
import ru.dk.gdxGP.Screens.LevelScreen;

import java.util.TimerTask;

/**
 * Created by DK on 25.04.2015.
 */
public class ChargeYourFriendLevel extends Level {
    private Particle mainParticle, friendParticle, particleWithCharge;
    private TimeTask task01;
    private TimeTask task02;
    private TaskCombination task03;
    private TimeTask task04;
    private TaskOnContact subTask02;
    private CustomTask task05;
    private NullTask task06;
    @Override
    protected void setSizes() {
        setXMin(-10);
        setXMax(10);
        setYMin(-10);
        setYMax(10);
    }

    @Override
    protected void setParticles() {
        ParticleDef particleDef=new ParticleDef(getXMin(), getXMax(), getYMin(), getYMax());
        particleDef.minMass=0.15f;
        particleDef.maxMass=0.25f;
        particleDef.minCharge=particleDef.maxCharge=0;
        mainParticle=this.generateRandomParticle(particleDef);
        mainParticle.setDrawer(ParticleDrawerSet.mainDrawer);

        particleDef=new ParticleDef(getXMin(), getXMax(), getYMin(), getYMax());
        particleDef.minMass=0.15f;
        particleDef.maxMass=0.25f;
        particleDef.minCharge=particleDef.maxCharge=0;
        friendParticle=this.generateRandomParticle(particleDef);
        friendParticle.setDrawer(new ParticleDrawer() {
            @Override
            public void drawParticle(Particle particle, Batch batch, Color parentColor) {
                ParticleDrawerSet.baseParticleDrawer.drawParticle(particle, batch, parentColor);
                ParticleDrawerSet.chargeCloudDrawer.drawParticle(particle, batch, parentColor);
                batch.setColor(1,1, 0, 1f);
                ParticleDrawerSet.chargeDrawer.drawParticle(particle, batch, parentColor);
                batch.setColor(parentColor);
            }
        });

        particleDef=new ParticleDef(-10,0,0,10);
        particleDef.minMass=0.15f;
        particleDef.maxMass=0.25f;
        particleDef.minCharge=particleDef.maxCharge=0;
        particleWithCharge=this.generateRandomParticle(particleDef);

        particleDef=new ParticleDef(getXMin(), getXMax(), getYMin(), getYMax());
        particleDef.minMass= 0.1f;
        particleDef.maxMass=0.3f;
        particleDef.minCharge=-0.01f;
        particleDef.maxCharge=0.01f;
        this.addRandomParticles(particleDef, 35);

        this.addParticle(mainParticle);
        this.addParticle(friendParticle);
        this.addParticle(particleWithCharge);
    }

    @Override
    protected void setParameters() {
        this.setK(100);
        this.setG(0);
        this.setChargingK(10);
        this.setMassFlowingK(0);
        this.setCameraPositionChanger(new CameraPositionChanger() {
            @Override
            public void changeCameraPosition(Level level, Camera camera, LevelScreen screen) {
                level.moveCamera(mainParticle.getX(), mainParticle.getY(), 25);
                level.zoomCamera(0.1f,25);
            }
        });
        this.setPreRenderer(new FadePreRenderer(new Color(0.75f,1,0.75f,1),new Color(0.75f,0.75f,1,1),100 ));
        this.setLevelProceeder(new LevelProceeder() {
            @Override
            public void proceed(Level level, float delta) {
                level.interactAllWithAllParticles();
            }
        });
    }

    @Override
    protected Mission createMission() {
        final Mission mission=new Mission("Charge your friend");
        task01 = new TimeTask(7000);
        task01.setMainTaskText("Hello! Today you need charge you friend.");
        task01.setSecondaryTaskText("Your friend is yellow circled");
        task01.setActionAfterAchievedTask(new ActionAfterAchievedTask() {
            @Override
            public void actionAfterAchievedTask(Task task) {
                ChargeYourFriendLevel.this.setCameraPositionChanger(new CameraPositionChanger() {
                    @Override
                    public void changeCameraPosition(Level level, Camera camera, LevelScreen screen) {
                        level.moveCamera(MathUtils.clamp(mainParticle.getX(),mainParticle.getX()-0.1f,mainParticle.getX()+0.1f), MathUtils.clamp(mainParticle.getY(),mainParticle.getY()-0.1f,mainParticle.getY()+0.1f), 25);
                        level.zoomCamera(MathUtils.clamp(screen.getCameraZoom(),0.2f,1f), 255);
                    }
                });
                ChargeYourFriendLevel.this.setLevelTapper(new LevelTapper() {
                    @Override
                    public void tapLevel(Level level, float x, float y) {
                        level.moveOnTap(mainParticle, 0.1f, x, y);
                    }
                });
                task02.start();
            }
        });

        task02 = new TimeTask(7000);
        task02.setMainTaskText("But firstly you need get charge");
        task02.setSecondaryTaskText("But how? Try to collide with other fractions.");

        TaskOnContact subTask01;
        subTask01 = new TaskOnContact(mainParticle,friendParticle);
        subTask01.setActionAfterAchievedTask(new ActionAfterAchievedTask() {
            @Override
            public void actionAfterAchievedTask(Task task) {
                mission.addTask(subTask02);
            }
        });
        subTask02 = new TaskOnContact(mainParticle,particleWithCharge);
        subTask02.setMainTaskText("In this world there is one special Particle");
        subTask02.setSecondaryTaskText("It can share its charge specially. Find it!");
        subTask02.setActionAfterAchievedTask(new ActionAfterAchievedTask() {
            @Override
            public void actionAfterAchievedTask(Task task) {
                ChargeYourFriendLevel.this.setPreRenderer(new FadePreRenderer(new Color(0, 1, 0, 1), new Color(1, 1, 0, 1), 20));
                particleWithCharge.setDrawer(new ParticleDrawer() {
                    @Override
                    public void drawParticle(Particle particle, Batch batch, Color parentColor) {
                        ParticleDrawerSet.baseParticleDrawer.drawParticle(particle, batch, parentColor);
                        ParticleDrawerSet.chargeCloudDrawer.drawParticle(particle, batch, parentColor);
                        batch.setColor(1, 0, 0, 1f);
                        ParticleDrawerSet.chargeDrawer.drawParticle(particle, batch, parentColor);
                        batch.setColor(parentColor);
                    }
                });
                mission.addTask(task04);
                task04.start();
                ChargeYourFriendLevel.super.multiListener.addContactListener(new ContactListener() {
                    @Override
                    public void beginContact(Contact contact) {

                    }

                    @Override
                    public void endContact(Contact contact) {

                    }

                    @Override
                    public void preSolve(Contact contact, Manifold oldManifold) {

                    }

                    @Override
                    public void postSolve(Contact contact, ContactImpulse impulse) {
                        if ((contact.getFixtureA().getBody().getUserData() == mainParticle && contact.getFixtureB().getBody().getUserData() == particleWithCharge)
                                || (contact.getFixtureA().getBody().getUserData() == particleWithCharge && contact.getFixtureB().getBody().getUserData() == mainParticle))
                            try {
                                mainParticle.moveParameters(particleWithCharge, 0, 0.01f, 0, new Vector2(0, 0));
                            } catch (Particle.NullMassException e) {
                            }
                    }
                });
            }
        });

        task03=new TaskCombination(new Task[]{subTask01, subTask02}, TaskCombination.TC_OR, true);
        task03.setMainTaskText("Find your friend particle");
        task03.setSecondaryTaskText("Or maybe another...");
        task04=new TimeTask(5000);
        task04.setMainTaskText("-What do you need from me?!");
        task04.setSecondaryTaskText("-If you collide me one more time I'll make you negative");
        task04.setActionAfterAchievedTask(new ActionAfterAchievedTask() {
            @Override
            public void actionAfterAchievedTask(Task task) {
                ChargeYourFriendLevel.this.setPreRenderer(new FadePreRenderer(new Color(0.75f,1,0.75f,1),new Color(0.75f,0.75f,1,1),100 ));
                mission.addTask(task05);
                mission.addTask(task06);
                ChargeYourFriendLevel.this.multiListener.addContactListener(new ContactListener() {
                    @Override
                    public void beginContact(Contact contact) {

                    }

                    @Override
                    public void endContact(Contact contact) {

                    }

                    @Override
                    public void preSolve(Contact contact, Manifold oldManifold) {

                    }

                    @Override
                    public void postSolve(Contact contact, ContactImpulse impulse) {
                        if ((contact.getFixtureA().getBody().getUserData() == mainParticle && contact.getFixtureB().getBody().getUserData() == friendParticle)
                                || (contact.getFixtureA().getBody().getUserData() == friendParticle && contact.getFixtureB().getBody().getUserData() == mainParticle))
                            ChargeYourFriendLevel.this.chargeBetweenParticle(mainParticle,friendParticle,50);
                    }
                });
            }
        });
        task05=new CustomTask(new CheckerForTask() {
            @Override
            public boolean checkTask(Task task) {
                task05.setSecondaryTaskText("You need charge it "+(-0.125f-friendParticle.getCharge())+" more");
                return friendParticle.getCharge()<=-0.125f;
            }
        });
        task05.setMainTaskText("Charge you friend");

        task06=new NullTask();
        task06.setMainTaskText("You've charged your friend as needed");
        task06.setSecondaryTaskText("Tap 'exit' button to exit");

        mission.addTask(task01);
        mission.addTask(task02);
        mission.addTask(task03);
        task01.start();
        return mission;
    }
}
