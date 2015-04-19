package ru.dk.gdxGP.GameWorld.Templates;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import ru.dk.gdxGP.AtlasLoader;
import ru.dk.gdxGP.GameWorld.InterfacesForActions.FractionDrawer;
import ru.dk.gdxGP.GameWorld.WorldElements.Fraction;

public final class FractionDrawerSet {
    private static final TextureRegion textureRegionFractionSolid;
    private static final TextureRegion textureRegionCharge;
    private static final TextureRegion textureRegionNullCharge;
    private static final TextureRegion textureRegionMinusCharge;
    private static final TextureRegion textureRegionPlusCharge;

    static{
        textureRegionFractionSolid = AtlasLoader.getRegion("FractionSolid01");
        textureRegionCharge = AtlasLoader.getRegion("charge");
        textureRegionPlusCharge = AtlasLoader.getRegion("PlusCharge");
        textureRegionNullCharge = AtlasLoader.getRegion("NullCharge");
        textureRegionMinusCharge = AtlasLoader.getRegion("MinusCharge");
    }
static final public FractionDrawer solidDrawer=new FractionDrawer() {
    @Override
    public void drawFraction(Fraction fraction, Batch batch, Color parentColor) {
        float r=fraction.getRadius();
        batch.setColor(fraction.getColor());
        batch.draw(textureRegionFractionSolid, fraction.getPosition().x - 1.0f * r, fraction.getPosition().y - 1.0f * r, r, r, r * 2.0f, r * 2.0f, 1, 1, MathUtils.radiansToDegrees * fraction.getBody().getAngle());
        if (fraction.getCharge() > 0) batch.setColor(1, 0, 0, 0.25f);
        else batch.setColor(0, 0, 1, 0.25f);
        float r1 = (float) (r * (1 + Math.sqrt(Math.abs(fraction.getCharge()) * 2f)));
        //if (!(fraction.charge <= 0.1f && fraction.charge >= -0.1f))
        batch.draw(textureRegionCharge, fraction.getPosition().x - 1.0f * r1, fraction.getPosition().y - 1.0f * r1, r1, r1, r1 * 2.0f, r1 * 2.0f, 1, 1, MathUtils.radiansToDegrees * fraction.getBody().getAngle());
        float r2 = r * 0.5f;
        batch.setColor(0,0,0,1);
        if (fraction.getCharge() > 0.125f)
            batch.draw(textureRegionPlusCharge, fraction.getPosition().x - 1.0f * r2, fraction.getPosition().y - 1.0f * r2, r2, r2, r2 * 2.0f, r2 * 2.0f, 1, 1, MathUtils.radiansToDegrees * fraction.getBody().getAngle());
        if (fraction.getCharge() < -0.125f)
            batch.draw(textureRegionMinusCharge, fraction.getPosition().x - 1.0f * r2, fraction.getPosition().y - 1.0f * r2, r2, r2, r2 * 2.0f, r2 * 2.0f, 1, 1, MathUtils.radiansToDegrees * fraction.getBody().getAngle());
        if (fraction.getCharge() <= 0.125f && fraction.getCharge() >= -0.125f)
            batch.draw(textureRegionNullCharge, fraction.getPosition().x - 1.0f * r2, fraction. getPosition().y - 1.0f * r2, r2, r2, r2 * 2.0f, r2 * 2.0f, 1, 1, MathUtils.radiansToDegrees * fraction.getBody().getAngle());
        batch.setColor(parentColor);
    }
};
    public static final FractionDrawer mainDrawer=new FractionDrawer() {
        @Override
        public void drawFraction(Fraction fraction, Batch batch, Color parentColor) {
            float r=fraction.getRadius();
            batch.setColor(fraction.getColor());
            batch.draw(textureRegionFractionSolid, fraction.getPosition().x - 1.0f * r, fraction.getPosition().y - 1.0f * r, r, r, r * 2.0f, r * 2.0f, 1, 1, MathUtils.radiansToDegrees * fraction.getBody().getAngle());
            if (fraction.getCharge() > 0) batch.setColor(1, 0, 0, 0.25f);
            else batch.setColor(0, 0, 1, 0.25f);
            float r1 = (float) (r * (1 + Math.sqrt(Math.abs(fraction.getCharge()) * 2f)));
            //if (!(fraction.charge <= 0.1f && fraction.charge >= -0.1f))
            batch.draw(textureRegionCharge, fraction.getPosition().x - 1.0f * r1, fraction.getPosition().y - 1.0f * r1, r1, r1, r1 * 2.0f, r1 * 2.0f, 1, 1, MathUtils.radiansToDegrees * fraction.getBody().getAngle());
            float r2 = r * 0.5f;
            batch.setColor(0,1,0,1f);
            if (fraction.getCharge() > 0.125f)
                batch.draw(textureRegionPlusCharge, fraction.getPosition().x - 1.0f * r2, fraction.getPosition().y - 1.0f * r2, r2, r2, r2 * 2.0f, r2 * 2.0f, 1, 1, MathUtils.radiansToDegrees * fraction.getBody().getAngle());
            if (fraction.getCharge() < -0.125f)
                batch.draw(textureRegionMinusCharge, fraction.getPosition().x - 1.0f * r2, fraction.getPosition().y - 1.0f * r2, r2, r2, r2 * 2.0f, r2 * 2.0f, 1, 1, MathUtils.radiansToDegrees * fraction.getBody().getAngle());
            if (fraction.getCharge() <= 0.125f && fraction.getCharge() >= -0.125f)
                batch.draw(textureRegionNullCharge, fraction.getPosition().x - 1.0f * r2, fraction. getPosition().y - 1.0f * r2, r2, r2, r2 * 2.0f, r2 * 2.0f, 1, 1, MathUtils.radiansToDegrees * fraction.getBody().getAngle());
            batch.setColor(parentColor);
        }
    };
}
