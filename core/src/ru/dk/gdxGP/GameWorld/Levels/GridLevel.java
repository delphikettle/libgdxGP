package ru.dk.gdxGP.GameWorld.Levels;

import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import ru.dk.gdxGP.GameWorld.Interfaces.Actions.LevelProceeder;
import ru.dk.gdxGP.GameWorld.Level;
import ru.dk.gdxGP.GameWorld.Mission;
import ru.dk.gdxGP.GameWorld.ParticleDef;
import ru.dk.gdxGP.GameWorld.WorldElements.Particle;

public class GridLevel extends Level {
    @Override
    protected void setSizes() {
        this.setXMin(-40);
        this.setXMax(40);
        this.setYMin(-5f);
        this.setYMax(5f);
    }

    @Override
    protected void setParticles() {
        ParticleDef plusDef=new ParticleDef(getXMin(),getXMax(),getYMin(), getYMax());
        plusDef.minCharge=plusDef.maxCharge=10;
        plusDef.minMass=plusDef.maxMass=0.5f;
        ParticleDef minusDef=new ParticleDef(getXMin(),getXMax(),getYMin(), getYMax());
        minusDef.minCharge=minusDef.maxCharge=-5;
        minusDef.minMass=minusDef.maxMass=0.01f;
        for (int i = 0; i < 100; i++) {
            Particle plusParticle = this.generateRandomParticle(plusDef);
            Particle minusParticle = this.generateRandomParticle(minusDef);
            DistanceJointDef jointDef=new DistanceJointDef();
            jointDef.length=5*(plusParticle.getRadius()+minusParticle.getRadius());
            jointDef.bodyA=plusParticle.getBody();
            jointDef.bodyB=minusParticle.getBody();
            jointDef.frequencyHz=1f;
            this.getWorld().createJoint(jointDef);
            this.addParticle(plusParticle);
            this.addParticle(minusParticle);
        }
        this.setLevelProceeder(new LevelProceeder() {
            @Override
            public void proceed(Level level, float delta) {
                GridLevel.this.interactAllWithAllParticles();
            }
        });
    }

    @Override
    protected void setParameters() {
        this.setK(4);
        this.setChargingK(0);
    }

    @Override
    protected Mission createMission() {
        return null;
    }
}
