package ru.dk.gdxGP.GameWorld.Interfaces.Drawers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import ru.dk.gdxGP.GameWorld.WorldElements.Border;

public interface BorderDrawer {
    public void drawBorder(Border border, Batch batch, Color parentColor);
}
