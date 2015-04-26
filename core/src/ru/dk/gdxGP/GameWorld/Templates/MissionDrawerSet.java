package ru.dk.gdxGP.GameWorld.Templates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import ru.dk.gdxGP.GameWorld.Interfaces.MissionDrawer;
import ru.dk.gdxGP.GameWorld.Mission;

/**
 * Created by DK on 15.04.2015.
 */
public final class MissionDrawerSet {
    public static final MissionDrawer standardDrawer = new MissionDrawer() {
        private BitmapFont englishBitmapFont, russianBitmapFont;

        {
            englishBitmapFont = new BitmapFont();
            englishBitmapFont.setColor(Color.BLACK);
            englishBitmapFont.setScale(3);
            russianBitmapFont = new BitmapFont();
            russianBitmapFont.setColor(0.9f, 0.9f, 0.9f, 1);
            russianBitmapFont.setScale(2f);
        }

        @Override
        public void drawMission(Mission mission, Batch batch) {
            englishBitmapFont.draw(batch, mission.getMainTaskText(), Gdx.graphics.getWidth() * 0.05f, Gdx.graphics.getHeight() * 0.1f);
            russianBitmapFont.draw(batch, mission.getSecondaryTaskText(), Gdx.graphics.getWidth() * 0.05f, Gdx.graphics.getHeight() * 0.05f);
        }
    };
}
