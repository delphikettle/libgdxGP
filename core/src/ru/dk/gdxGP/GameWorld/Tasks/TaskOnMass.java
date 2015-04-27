package ru.dk.gdxGP.GameWorld.Tasks;

import com.badlogic.gdx.math.MathUtils;
import ru.dk.gdxGP.GameWorld.Task;
import ru.dk.gdxGP.GameWorld.WorldElements.Particle;

import java.util.ArrayList;
import java.util.Collections;

public class TaskOnMass extends Task {
    private final ArrayList<Particle> particles = new ArrayList<Particle>();
    private final float mass;
    private float inaccuracy = 0.0f;

    public TaskOnMass(Particle[] particles, float mass, float inaccuracy) {
        super();
        Collections.addAll(this.particles, particles);
        this.mass = mass;
        this.inaccuracy = inaccuracy;
    }

    public TaskOnMass(Particle particle, float mass, float inaccuracy) {
        this(new Particle[]{particle}, mass, inaccuracy);
    }

    @Override
    public boolean check() {
        for (int i = 0; i < particles.size(); i++) {
            Particle particle = particles.get(0);
            if (!(MathUtils.isEqual(particle.getMass(), mass, inaccuracy)))
                return false;
        }
        return true;
    }
}