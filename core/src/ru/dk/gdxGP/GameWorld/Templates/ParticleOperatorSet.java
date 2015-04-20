package ru.dk.gdxGP.GameWorld.Templates;

import ru.dk.gdxGP.GameWorld.InterfacesForActions.ParticleOperator;
import ru.dk.gdxGP.GameWorld.WorldElements.Particle;

public final class ParticleOperatorSet {
    public static final ParticleOperator nullOperator = new ParticleOperator() {
        @Override
        public void operateParticle(Particle particle, float deltaTime) {

        }
    };

}
