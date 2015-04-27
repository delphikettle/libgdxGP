package ru.dk.gdxGP.GameWorld.Interfaces.Actions;


import ru.dk.gdxGP.GameWorld.WorldElements.Particle;

public interface ParticleOperator {
    public void operateParticle(Particle particle, float deltaTime);
}