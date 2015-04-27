package ru.dk.gdxGP.GameWorld.Tasks;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.dk.gdxGP.GameWorld.Task;
import ru.dk.gdxGP.GameWorld.WorldElements.Particle;

import java.util.ArrayList;
import java.util.Collections;

public class TaskOnCoordinate extends Task {
    private ArrayList<Particle> particles = new ArrayList<Particle>();
    private float inaccuracy = 0.0f;
    private Vector2 coordinate;

    public TaskOnCoordinate(Particle[] particles, Vector2 coordinate, float inaccuracy) {
        super();
        Collections.addAll(this.particles, particles);
        this.coordinate = coordinate;
        this.inaccuracy = inaccuracy;
    }

    public TaskOnCoordinate(Particle particle, Vector2 coordinate, float inaccuracy) {
        this(new Particle[]{particle}, coordinate, inaccuracy);
    }

    @Override
    public boolean check() {
        for (int i = 0; i < particles.size(); i++) {
            Particle particle = particles.get(0);
            if (!(MathUtils.isEqual(particle.getPosition().x, coordinate.x, inaccuracy) && MathUtils.isEqual(particle.getPosition().y, coordinate.y, inaccuracy)))
                return false;
        }
        return true;
    }

}
