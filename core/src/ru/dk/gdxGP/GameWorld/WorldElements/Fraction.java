package ru.dk.gdxGP.GameWorld.WorldElements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import ru.dk.gdxGP.GameWorld.InterfacesForActions.FractionDrawer;
import ru.dk.gdxGP.GameWorld.InterfacesForActions.FractionOperator;
import ru.dk.gdxGP.GameWorld.Level;
import ru.dk.gdxGP.GameWorld.Templates.FractionDrawerSet;
import ru.dk.gdxGP.GameWorld.Templates.FractionOperatorSet;

public class Fraction extends Actor {
    private static final FixtureDef blankFixtureDef;
    static {
        blankFixtureDef = new FixtureDef();
        blankFixtureDef.density = 1;
        blankFixtureDef.isSensor = true;
        blankFixtureDef.friction = 1;
        blankFixtureDef.restitution = 1;
        blankFixtureDef.shape = new CircleShape();
        blankFixtureDef.shape.setRadius(0.00001f);
    }
    /*
    private static final TextureRegion textureRegionFractionSolid;
    private static final TextureRegion textureRegionCharge;
    private static final TextureRegion textureRegionNullCharge;
    private static final TextureRegion textureRegionMinusCharge;
    private static final TextureRegion textureRegionPlusCharge;
    */
    private final Body body;
    private final Level level;
    private Condition condition;
    private FractionDrawer drawer = null;
    private FractionOperator operator = null;
    private float charge;
    private Color color;
    private float density = 1;

    public Fraction(Level level,World world, float x, float y, float vx, float vy, float mass, float charge, float friction, float density, float restitution, Condition condition, Color color) {
        this.level=level;

        BodyDef bodyDef = new BodyDef();
        bodyDef.active = true;
        bodyDef.bullet = true;
        bodyDef.fixedRotation = false;
        bodyDef.linearVelocity.set(vx, vy);
        bodyDef.position.set(x, y);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.allowSleep = false;
        body = world.createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius((float) Math.sqrt(mass / Math.PI / density));

        MassData massData = new MassData();
        massData.mass = mass;
        massData.center.set(circleShape.getRadius(), circleShape.getRadius());
        body.setMassData(massData);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.friction = friction;
        fixtureDef.density = density;
        fixtureDef.restitution = restitution;
        fixtureDef.isSensor = false;
        body.createFixture(fixtureDef);
        body.setUserData(this);
        this.setDrawer(FractionDrawerSet.solidDrawer);
        this.setOperator(FractionOperatorSet.nullDrawer);
        this.condition = condition;
        this.charge = charge;
        this.color = color;
        this.body.resetMassData();
    }

    public float getMass() {
        return this.body.getMass();
    }

    public float getDensity() {
        try {
            this.density = this.body.getFixtureList().get(0).getDensity();
        } catch (IndexOutOfBoundsException e) {
        }
        return this.density;
    }

    public float getRestitution() {
        return this.body.getFixtureList().get(0).getRestitution();
    }

    public float getFriction() {
        return body.getFixtureList().get(0).getFriction();
    }

    public float getCharge() {
        return charge;
    }

    public void setCharge(float charge) {
        this.charge = charge;
    }

    public float getRadius() {
        return this.body.getFixtureList().get(0).getShape().getRadius();
    }

    public Vector2 getVelocity() {
        return this.body.getLinearVelocity();
    }

    public Vector2 getMassCenter() {
        return this.body.getWorldCenter();
    }

    public Vector2 getPosition() {
        return this.body.getPosition();
    }

    @Override
    public float getX() {
        return this.getPosition().x;
    }
    @Override
    public float getY() {
        return this.getPosition().y;
    }

    @Override
    public Color getColor() {
        return color;
    }
    public Body getBody() {
        return body;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public void setOperator(FractionOperator operator) {
        this.operator = operator;
    }

    public void setDrawer(FractionDrawer drawer) {
        this.drawer = drawer;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (drawer != null) drawer.drawFraction(this, batch, batch.getColor());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (operator != null) operator.operateFraction(this, delta);
    }

    synchronized public Fraction divide(float mass, float vx, float vy) {
        if (mass >= this.body.getMass() || mass <= 0)//throw new IllegalArgumentException();
            return null;
        float newMass = this.body.getMass() - mass;
        float r = recountRadius(newMass);
        this.body.applyLinearImpulse(-vx * mass, -vy * mass, getMassCenter().x, getMassCenter().y, true);
        Vector2 coords = new Vector2(vx, vy);
        coords.setLength((float) (r + Math.sqrt(mass / Math.PI / getDensity())));
        coords.add(this.body.getPosition().x, this.body.getPosition().y);
        return new Fraction(this.level,this.body.getWorld(), coords.x, coords.y, vx, vy, mass,
                this.getCharge(), this.getFriction(), this.getDensity(), this.getRestitution(), this.getCondition(), this.getColor());
    }

    public void moveParameters(Fraction to, float mass, float charge, float density, Vector2 velocity) throws NullMassException {
        synchronized (this.body) {
            if (mass == 0) {
                to.charge += charge;
                this.charge -= charge * to.getMass() / this.getMass();
                this.body.applyLinearImpulse(-velocity.x * to.getMass(), -velocity.y * to.getMass(), getMassCenter().x, getMassCenter().y, true);
                to.body.applyLinearImpulse(velocity.x, velocity.y, to.getMassCenter().x, to.getMassCenter().y, true);
                if (density != 0) {
                    this.body.getFixtureList().get(0).setDensity(this.getDensity() - density * to.getMass() / this.getMass());
                    to.body.getFixtureList().get(0).setDensity(to.getDensity() + density);
                    this.recountRadius(this.getMass());
                    to.recountRadius(to.getMass());
                }
            } else {
                if (this.getMass() - mass <= 0) throw new NullMassException(this);
                if (to.getMass() + mass <= 0) throw new NullMassException(to);
                to.charge = (to.getCharge() * to.getMass() + this.getCharge() * mass) / (to.getMass() + mass);
                to.body.applyLinearImpulse(this.getVelocity().x * mass, this.getVelocity().y * mass, to.getMassCenter().x, to.getMassCenter().y, true);
                to.body.getFixtureList().get(0).setDensity((to.getDensity() * to.getMass() + mass * this.getDensity()) / (to.getMass() + mass));
                this.recountRadius(this.getMass() - mass);
                to.recountRadius(to.getMass() + mass);
                this.moveParameters(to, 0, charge, density, velocity);
            }
        }
    }

    public final float recountRadius(float newMass) {
        float newR = (float) Math.sqrt(newMass / Math.PI / this.getDensity());
        MassData newMassData = new MassData();
        newMassData.mass = newMass;
        newMassData.center.set(newR, newR);
        this.body.setMassData(newMassData);
        synchronized (this.body) {
            //while(this.body.getFixtureList().size==0) {}
            this.body.getFixtureList().get(0).getShape().setRadius(newR);
            this.body.destroyFixture(this.body.createFixture(blankFixtureDef));
        }
        return newR;
    }

    @Override
    public String toString() {
        return "Fraction:mass=" + this.getBody().getMass() + ";vx=" + this.getBody().getLinearVelocity().x + ";vy=" + this.getBody().getLinearVelocity().y;
    }

    public enum Condition {Liquid, Solid, Mixed}

    public class NullMassException extends Exception {
        private final Fraction fraction;

        NullMassException(Fraction fraction) {
            this.fraction = fraction;
        }

        public Fraction getFraction() {
            return fraction;
        }
    }
}
