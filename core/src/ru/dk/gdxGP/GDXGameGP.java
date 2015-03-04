package ru.dk.gdxGP;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import ru.dk.gdxGP.GameWorld.Level;
import ru.dk.gdxGP.GameWorld.Levels.TestLevel01;
import ru.dk.gdxGP.Screens.LevelScreen;
import ru.dk.gdxGP.Screens.LoadingScreen;
import ru.dk.gdxGP.Screens.LogoScreen;

public class GDXGameGP extends ApplicationAdapter implements GestureDetector.GestureListener, InputProcessor {
	SpriteBatch batch;
	Texture img;
	Level lvl;
	State state=State.logo;
	Screen screen;
	static public final AssetManager assetManager=new AssetManager();

	public enum State{
		logo,loading,MainMenu,SelectLevel,Game,Pause
	}


	@Override
	public void create() {
		this.screen=new LogoScreen(1);
		screen.show();

		batch = new SpriteBatch();
		/*
		img = new Texture("badlogic.jpg");
		Gdx.input.setInputProcessor(this);
		Gdx.input.setInputProcessor(new GestureDetector(this));
		lvl = new TestLevel01(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		lvl.start();
		Stage stage=new Stage();
		*/
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(screen!=null)screen.render(Gdx.graphics.getDeltaTime());
		switch (state){
			case logo:
				assert (screen != null);
				if(!((LogoScreen) screen).isActive()){
					this.state=State.loading;
					this.screen=new LoadingScreen((LogoScreen) this.screen,new String[]{
							"badlogic.jpg",
							"border01.png",
							"circle01.png",
							"images/circle.png",
							"images/logo.png",
							"data/images/c.png",
							"data/images/l.png",
							"data/images/soccerBall.png",
							"data/images/molecule.png"
					});
					this.screen.show();
				}
				break;
			case loading:
				assert ((LoadingScreen)screen) != null;
				if(!((LoadingScreen)screen).isActive()){
					//this.state=State.MainMenu;
					if(((LoadingScreen)screen).getNextScreen()==null){
						((LoadingScreen)screen).getLogoScreen().setRotation(0);
						this.screen=new LoadingScreen(((LoadingScreen)screen).getLogoScreen(),new TestLevel01(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
						this.screen.show();
					}else{
						this.screen=((LoadingScreen)screen).getNextScreen();
						this.state=State.Game;
					}
				}
				break;
			case MainMenu:
				break;
			case SelectLevel:
				break;
			case Game:
				break;
		}
		//временно, потом удалить!!!

		/*
		if(state!=State.logo&&state!=State.loading) {
			Gdx.gl.glClearColor(1, 1, 1, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			batch.begin();

			this.screen.render(Gdx.graphics.getDeltaTime());
			batch.end();
		}
		*/
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		System.out.println("GestureListener");
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

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		System.out.println("InputProcessor");
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
