package ru.dk.gdxGP.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import ru.dk.gdxGP.GameWorld.Level;

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
        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(color);
        Gdx.gl.glLineWidth(thickness / currentCamera.zoom);
        shapeRenderer.polyline(vertexes);
        /*
        for (int i = 0; i < vertexes.length/2-1; i++) {
            if(i%2!=0)continue;
            float x1=vertexes[2*i],y1=vertexes[2*i+1]
                    ,x2=vertexes[2*i+2],y2=vertexes[2*i+3],l= Level.getDistance(x1,y1,x2,y2),angle=((true)?0:0)+ MathUtils.radiansToDegrees*(float) Math.asin((y2 - y1) / l);
            shapeRenderer.rect(x1-thickness/2,y1-thickness/2,thickness/2,thickness/2,thickness,l+thickness,1,1, angle);
            System.out.println(angle);
            System.out.println(x1+" "+x2+" "+x2+" "+y2+" "+(x2-x1)+" "+(y2-y1));
        }
        float x1=0,y1=0,x2=4,y2=4,l= Level.getDistance(x1,y1,x2,y2),angle= MathUtils.radiansToDegrees*(float) Math.asin((y2 - y1) / l);
        //System.out.println(angle);
        //shapeRenderer.rect(x1-thickness/2,y1-thickness/2,thickness/2,thickness/2,l+thickness,thickness,1,1,angle);
        //shapeRenderer.rect(-10,-10,20,20);
        //float x=vertexes[vertexes.length-2],y=vertexes[vertexes.length-1];
        */
        shapeRenderer.end();
    }
}
