package ru.dk.gdxGP.GameWorld.Templates;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Shape;
import ru.dk.gdxGP.GameWorld.Interfaces.Drawers.BorderDrawer;
import ru.dk.gdxGP.GameWorld.WorldElements.Border;
import ru.dk.gdxGP.utils.Graphics;

public class BorderDrawerSet {
    public static final BorderDrawer standardDrawer = new BorderDrawer() {
        @Override
        public void drawBorder(Border border, Batch batch, Color parentColor) {
            Shape shape = border.getShape();
            if (shape instanceof ChainShape) {
                Graphics.drawChain(border.getVertexes(), new Color(0.1f, 1, 0.5f, 0.75f), 0.2f);
            }

        }
    };
}
