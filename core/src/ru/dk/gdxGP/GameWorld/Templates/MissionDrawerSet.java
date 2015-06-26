package ru.dk.gdxGP.GameWorld.Templates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import ru.dk.gdxGP.GameWorld.Interfaces.Drawers.MissionDrawer;
import ru.dk.gdxGP.GameWorld.Mission;

public final class MissionDrawerSet {
    private static  BitmapFont englishBitmapFont;
    public static final MissionDrawer mainTextDrawer = new MissionDrawer() {
        @Override
        public void drawMission(Mission mission, Batch batch) {
            englishBitmapFont.draw(batch, mission.getMainTaskText(),
                    Gdx.graphics.getWidth() * 0.05f,
                    Gdx.graphics.getHeight() * 0.1f);
        }
    };
    private static BitmapFont russianBitmapFont;
    static {
        load();
    }

    public static void load() {
        englishBitmapFont = new BitmapFont();
        englishBitmapFont.setColor(Color.BLACK);
        englishBitmapFont.setScale(3* Gdx.graphics.getDensity()*160/455);
        russianBitmapFont = new BitmapFont();
        russianBitmapFont.setColor(0.9f, 0.9f, 0.9f, 1);
        russianBitmapFont.setScale(2f*Gdx.graphics.getDensity()*160/455);
    }

    public static final MissionDrawer secondaryTextDrawer = new MissionDrawer() {
        @Override
        public void drawMission(Mission mission, Batch batch) {
            russianBitmapFont.draw(batch, mission.getSecondaryTaskText(), Gdx.graphics.getWidth() * 0.05f, Gdx.graphics.getHeight() * 0.05f);
        }
    };
    public static final MissionDrawer standardDrawer = new MissionDrawer() {
        @Override
        public void drawMission(Mission mission, Batch batch) {
            mainTextDrawer.drawMission(mission, batch);
            secondaryTextDrawer.drawMission(mission, batch);
        }
    };
}
