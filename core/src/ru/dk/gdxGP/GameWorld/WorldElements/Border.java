package ru.dk.gdxGP.GameWorld.WorldElements;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import ru.dk.gdxGP.GameWorld.Interfaces.Drawers.BorderDrawer;
import ru.dk.gdxGP.GameWorld.Interfaces.LevelElement;
import ru.dk.gdxGP.GameWorld.Level;
import ru.dk.gdxGP.GameWorld.Templates.BorderDrawerSet;
import ru.dk.gdxGP.utils.AtlasLoader;

public class Border extends Actor implements LevelElement {
    private final Level level;
    private Body body;
    private float[] vertexes;
    private BorderDrawer drawer = BorderDrawerSet.standardDrawer;

    public Border(Level level, World world, int x, int y, Shape shape) {
        this.level = level;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.bullet = true;
        bodyDef.angularVelocity = 0.0f;
        bodyDef.active = true;
        bodyDef.position.set(x, y);
        bodyDef.linearVelocity.set(0, 0);
        bodyDef.active = true;
        bodyDef.bullet = true;
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 1.0f;
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.restitution = 1.0f;
        fixtureDef.isSensor = false;
        body.createFixture(fixtureDef);
        body.setUserData(this);
    }

    public void updateVertexes() {
        Shape shape = this.body.getFixtureList().get(0).getShape();
        if (shape instanceof ChainShape) {
            ChainShape chainShape = (ChainShape) shape;
            vertexes = new float[chainShape.getVertexCount() * 2];
            for (int i = 0; i < vertexes.length / 2; i++) {
                Vector2 vector2 = new Vector2();
                chainShape.getVertex(i, vector2);
                vertexes[2 * i] = vector2.x;
                vertexes[2 * i + 1] = vector2.y;
            }
        }
    }

    public float[] getVertexes() {
        if (vertexes == null)
            updateVertexes();
        return vertexes;
    }

    public Body getBody() {
        return body;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (drawer != null)
            drawer.drawBorder(this, batch, batch.getColor());
    }

    public Shape getShape() {
        return this.body.getFixtureList().get(0).getShape();
    }

    @Override
    public Level getLevel() {
        return this.level;
    }
}
