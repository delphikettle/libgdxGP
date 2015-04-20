package ru.dk.gdxGP.GameWorld.InterfacesForActions;


import ru.dk.gdxGP.GameWorld.WorldElements.Particle;

public interface ParticleOperator {
    public void operateParticle(Particle particle, float deltaTime);
}