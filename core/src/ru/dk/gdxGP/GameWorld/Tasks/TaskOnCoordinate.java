package ru.dk.gdxGP.GameWorld.Tasks;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.dk.gdxGP.GameWorld.WorldElements.Fraction;
import ru.dk.gdxGP.GameWorld.Task;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Андрей on 07.02.2015.
 */
public class TaskOnCoordinate extends Task{
    private ArrayList<Fraction> fractions=new ArrayList<Fraction>();
    private float inaccuracy=0.0f;
    private Vector2 coordinate;

    public TaskOnCoordinate(Fraction[] fractions,Vector2 coordinate,float inaccuracy){
        super();
        Collections.addAll(this.fractions, fractions);
        this.coordinate=coordinate;
        this.inaccuracy=inaccuracy;
    }
    public TaskOnCoordinate(Fraction fraction,Vector2 coordinate,float inaccuracy){
        this(new Fraction[]{fraction}, coordinate, inaccuracy);
    }

    @Override
    public boolean check() {
        for (int i = 0; i < fractions.size(); i++) {
            Fraction fraction=fractions.get(0);
            if(!(MathUtils.isEqual(fraction.getPosition().x, coordinate.x, inaccuracy) && MathUtils.isEqual(fraction.getPosition().y, coordinate.y, inaccuracy)))
                return false;
        }
        return true;
    }

}
