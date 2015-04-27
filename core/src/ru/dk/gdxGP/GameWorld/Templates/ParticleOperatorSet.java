package ru.dk.gdxGP.GameWorld.Templates;

import com.sun.org.apache.bcel.internal.generic.POP;
import ru.dk.gdxGP.GameWorld.Interfaces.Actions.ParticleOperator;
import ru.dk.gdxGP.GameWorld.WorldElements.Particle;

public final class ParticleOperatorSet {
    public static final ParticleOperator nullOperator = new ParticleOperator() {
        @Override
        public void operateParticle(Particle particle, float deltaTime) {

        }
    };

}
