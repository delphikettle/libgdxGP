package ru.dk.gdxGP;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import ru.dk.gdxGP.GameWorld.Level;
import ru.dk.gdxGP.GameWorld.Levels.TestLevel;

public class GDXGameGP extends ApplicationAdapter implements com.badlogic.gdx.input.GestureDetector.GestureListener {
	SpriteBatch batch;
	Texture img;
	Level lvl;
	//private ArrayList<Component> currentSetOfParticles;

	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");

		lvl = new TestLevel(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		lvl.start();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		this.lvl.getStage().draw();
		batch.end();
	}

	@Override
	public void dispose() {
		super.dispose();
		System.out.println(lvl.maxOpName+lvl.maxOp);
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		return false;
	}
}
