package ru.dk.gdxGP.GameWorld.Interfaces.Drawers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import ru.dk.gdxGP.GameWorld.WorldElements.Particle;

public interface ParticleDrawer {
    public void drawParticle(Particle particle, Batch batch, Color parentColor);
}