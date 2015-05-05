package ru.dk.gdxGP.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;

public final class Graphics {
    public static final ShapeRenderer shapeRenderer;
    static {
        shapeRenderer=new ShapeRenderer();
    }

    public static Camera getCurrentCamera() {
        return currentCamera;
    }

    public static void setCurrentCamera(OrthographicCamera currentCamera) {
        Graphics.currentCamera = currentCamera;
    }

    private static OrthographicCamera currentCamera;
    private Graphics(){}
    public static void drawChain(float[] vertexes,Color color,float thickness){
        shapeRenderer.setProjectionMatrix(currentCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(color);
        shapeRenderer.polyline(vertexes);
        Gdx.gl.glLineWidth(thickness / currentCamera.zoom);
        shapeRenderer.end();
    }
}
