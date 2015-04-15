package ru.dk.gdxGP.GameWorld.Templates;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import ru.dk.gdxGP.GameWorld.Mission;
import ru.dk.gdxGP.GameWorld.MissionDrawer;

/**
 * Created by DK on 15.04.2015.
 */
public final class MissionDrawerSet {
    public static final MissionDrawer standardDrawer=new MissionDrawer() {
        private BitmapFont bitmapFont;
        {
            bitmapFont=new BitmapFont();
            bitmapFont.setColor(Color.BLACK);
            bitmapFont.setScale(0.5f);
        }
        @Override
        public void drawMission(Mission mission,Batch batch) {
            bitmapFont.draw(batch, mission.getTaskText(), 0, 0);
        }
    };
}
