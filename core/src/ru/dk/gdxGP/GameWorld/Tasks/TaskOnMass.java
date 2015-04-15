package ru.dk.gdxGP.GameWorld.Tasks;

import com.badlogic.gdx.math.MathUtils;
import ru.dk.gdxGP.GameWorld.Task;
import ru.dk.gdxGP.GameWorld.WorldElements.Fraction;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by DK on 15.04.2015.
 */
public class TaskOnMass extends Task {
    private final ArrayList<Fraction> fractions = new ArrayList<Fraction>();
    private float inaccuracy = 0.0f;
    private final float mass;

    public TaskOnMass(Fraction[] fractions, float mass, float inaccuracy) {
        super();
        Collections.addAll(this.fractions, fractions);
        this.mass = mass;
        this.inaccuracy = inaccuracy;
    }

    public TaskOnMass(Fraction fraction, float mass, float inaccuracy) {
        this(new Fraction[]{fraction}, mass, inaccuracy);
    }

    @Override
    public boolean check() {
        for (int i = 0; i < fractions.size(); i++) {
            Fraction fraction = fractions.get(0);
            if (!(MathUtils.isEqual(fraction.getMass(), mass, inaccuracy)))
                return false;
        }
        return true;
    }
}