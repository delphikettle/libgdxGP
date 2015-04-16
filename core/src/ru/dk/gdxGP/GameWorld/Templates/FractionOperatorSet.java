package ru.dk.gdxGP.GameWorld.Templates;

import ru.dk.gdxGP.GameWorld.FractionOperator;
import ru.dk.gdxGP.GameWorld.WorldElements.Fraction;

public final class FractionOperatorSet {
    public static final FractionOperator nullDrawer = new FractionOperator() {
        @Override
        public void operateFraction(Fraction fraction, float deltaTime) {

        }
    };

}
