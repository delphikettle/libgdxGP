package ru.dk.gdxGP;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.dk.gdxGP.Levels.TestLevel;

import java.util.ArrayList;

public class GDXGameGP extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Level lvl;
	//private ArrayList<Component> currentSetOfParticles;

	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		lvl = new TestLevel(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//currentSetOfParticles=lvl.getComponents();
		lvl.start();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		//batch.draw(img, lvl.getComponents().get(0).getX(), lvl.getComponents().get(0).getY(),lvl.getComponents().get(0).getR()*2,lvl.getComponents().get(0).getR()*2);
		this.lvl.getStage().draw();
		batch.end();
	}
}
