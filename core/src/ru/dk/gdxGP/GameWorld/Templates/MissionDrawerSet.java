package ru.dk.gdxGP.GameWorld.Templates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import ru.dk.gdxGP.GameWorld.Mission;
import ru.dk.gdxGP.GameWorld.InterfacesForActions.MissionDrawer;

/**
 * Created by DK on 15.04.2015.
 */
public final class MissionDrawerSet {
    public static final MissionDrawer standardDrawer = new MissionDrawer() {
        private BitmapFont bitmapFont;

        {
            bitmapFont = new BitmapFont();
            bitmapFont.setColor(Color.BLACK);
            bitmapFont.setScale(2);
        }

        @Override
        public void drawMission(Mission mission, Batch batch) {
            bitmapFont.draw(batch, mission.getTaskText(), Gdx.graphics.getWidth()*0.05f,Gdx.graphics.getHeight()*0.1f);
        }
    };
}
