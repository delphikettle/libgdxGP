package ru.dk.gdxGP.GameWorld.Templates;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import ru.dk.gdxGP.AtlasLoader;
import ru.dk.gdxGP.GameWorld.InterfacesForActions.ParticleDrawer;
import ru.dk.gdxGP.GameWorld.WorldElements.Particle;

public final class ParticleDrawerSet {
    private static final TextureRegion textureRegionParticleSolid;
    private static final TextureRegion textureRegionCharge;
    private static final TextureRegion textureRegionNullCharge;
    private static final TextureRegion textureRegionMinusCharge;
    private static final TextureRegion textureRegionPlusCharge;

    static {
        textureRegionParticleSolid = AtlasLoader.getRegion("ParticleSolid01");
        textureRegionCharge = AtlasLoader.getRegion("charge");
        textureRegionPlusCharge = AtlasLoader.getRegion("PlusCharge");
        textureRegionNullCharge = AtlasLoader.getRegion("NullCharge");
        textureRegionMinusCharge = AtlasLoader.getRegion("MinusCharge");
    }

    static final public ParticleDrawer solidDrawer = new ParticleDrawer() {
        @Override
        public void drawParticle(Particle particle, Batch batch, Color parentColor) {
            float r = particle.getRadius();
            batch.setColor(particle.getColor());
            batch.draw(textureRegionParticleSolid, particle.getPosition().x - 1.0f * r, particle.getPosition().y - 1.0f * r, r, r, r * 2.0f, r * 2.0f, 1, 1, MathUtils.radiansToDegrees * particle.getBody().getAngle());
            if (particle.getCharge() > 0) batch.setColor(1, 0, 0, 0.25f);
            else batch.setColor(0, 0, 1, 0.25f);
            float r1 = (float) (r * (1 + Math.sqrt(Math.abs(particle.getCharge()) * 2f)));
            //if (!(particle.charge <= 0.1f && particle.charge >= -0.1f))
            batch.draw(textureRegionCharge, particle.getPosition().x - 1.0f * r1, particle.getPosition().y - 1.0f * r1, r1, r1, r1 * 2.0f, r1 * 2.0f, 1, 1, MathUtils.radiansToDegrees * particle.getBody().getAngle());
            float r2 = r * 0.5f;
            batch.setColor(0, 0, 0, 1);
            if (particle.getCharge() > 0.125f)
                batch.draw(textureRegionPlusCharge, particle.getPosition().x - 1.0f * r2, particle.getPosition().y - 1.0f * r2, r2, r2, r2 * 2.0f, r2 * 2.0f, 1, 1, MathUtils.radiansToDegrees * particle.getBody().getAngle());
            if (particle.getCharge() < -0.125f)
                batch.draw(textureRegionMinusCharge, particle.getPosition().x - 1.0f * r2, particle.getPosition().y - 1.0f * r2, r2, r2, r2 * 2.0f, r2 * 2.0f, 1, 1, MathUtils.radiansToDegrees * particle.getBody().getAngle());
            if (particle.getCharge() <= 0.125f && particle.getCharge() >= -0.125f)
                batch.draw(textureRegionNullCharge, particle.getPosition().x - 1.0f * r2, particle.getPosition().y - 1.0f * r2, r2, r2, r2 * 2.0f, r2 * 2.0f, 1, 1, MathUtils.radiansToDegrees * particle.getBody().getAngle());
            batch.setColor(parentColor);
        }
    };
    public static final ParticleDrawer mainDrawer = new ParticleDrawer() {
        @Override
        public void drawParticle(Particle particle, Batch batch, Color parentColor) {
            float r = particle.getRadius();
            batch.setColor(particle.getColor());
            batch.draw(textureRegionParticleSolid, particle.getPosition().x - 1.0f * r, particle.getPosition().y - 1.0f * r, r, r, r * 2.0f, r * 2.0f, 1, 1, MathUtils.radiansToDegrees * particle.getBody().getAngle());
            if (particle.getCharge() > 0) batch.setColor(1, 0, 0, 0.25f);
            else batch.setColor(0, 0, 1, 0.25f);
            float r1 = (float) (r * (1 + Math.sqrt(Math.abs(particle.getCharge()) * 2f)));
            //if (!(particle.charge <= 0.1f && particle.charge >= -0.1f))
            batch.draw(textureRegionCharge, particle.getPosition().x - 1.0f * r1, particle.getPosition().y - 1.0f * r1, r1, r1, r1 * 2.0f, r1 * 2.0f, 1, 1, MathUtils.radiansToDegrees * particle.getBody().getAngle());
            float r2 = r * 0.5f;
            batch.setColor(0, 1, 0, 1f);
            if (particle.getCharge() > 0.125f)
                batch.draw(textureRegionPlusCharge, particle.getPosition().x - 1.0f * r2, particle.getPosition().y - 1.0f * r2, r2, r2, r2 * 2.0f, r2 * 2.0f, 1, 1, MathUtils.radiansToDegrees * particle.getBody().getAngle());
            if (particle.getCharge() < -0.125f)
                batch.draw(textureRegionMinusCharge, particle.getPosition().x - 1.0f * r2, particle.getPosition().y - 1.0f * r2, r2, r2, r2 * 2.0f, r2 * 2.0f, 1, 1, MathUtils.radiansToDegrees * particle.getBody().getAngle());
            if (particle.getCharge() <= 0.125f && particle.getCharge() >= -0.125f)
                batch.draw(textureRegionNullCharge, particle.getPosition().x - 1.0f * r2, particle.getPosition().y - 1.0f * r2, r2, r2, r2 * 2.0f, r2 * 2.0f, 1, 1, MathUtils.radiansToDegrees * particle.getBody().getAngle());
            batch.setColor(parentColor);
        }
    };
    
}
