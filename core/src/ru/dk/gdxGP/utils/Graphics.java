package ru.dk.gdxGP.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public final class Graphics {
    public static final ShapeRenderer shapeRenderer;

    static {
        shapeRenderer = new ShapeRenderer();
    }
    private static OrthographicCamera currentCamera;

    private Graphics() {
    }

    public static Camera getCurrentCamera() {
        return currentCamera;
    }

    public static void setCurrentCamera(OrthographicCamera currentCamera) {
        Graphics.currentCamera = currentCamera;
    }

    /**
     * Draws smoothed chain
     * @param vertexes vertexes of chain
     * @param color the color of the chain
     * @param thickness the thickness of the chain
     */
    public static void drawChain(float[] vertexes, Color color, float thickness) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(currentCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        for (int i = 0; i < vertexes.length / 2 - 1; i++) {
            //if(i%2!=0)continue;
            float x1 = vertexes[2 * i], y1 = vertexes[2 * i + 1], x2 = vertexes[2 * i + 2], y2 = vertexes[2 * i + 3];
            shapeRenderer.rectLine(x1, y1, x2, y2, thickness);
            shapeRenderer.circle(x1, y1, thickness / 2, 16);
        }
        shapeRenderer.circle(vertexes[vertexes.length - 2], vertexes[vertexes.length - 1], thickness / 2, 16);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
}
