package ru.dk.gdxGP.GameWorld;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import ru.dk.gdxGP.GameWorld.WorldElements.Fraction;

public interface FractionDrawer{
    public void drawFraction(Fraction fraction,Batch batch,Color parentColor);
}