package com.Asteroids.game;

import com.Asteroids.managers.GameInputProcessor;
import com.Asteroids.managers.GameKeys;
import com.Asteroids.managers.GameStateManager;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;


public class Game implements ApplicationListener {
	
	public static int WIDTH;
	public static int HEIGHT;

	public static OrthographicCamera cam;
	
	private GameStateManager gsm;
	
	public void create(){
		
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		
		cam = new OrthographicCamera(WIDTH, HEIGHT);
		cam.translate(WIDTH / 2, HEIGHT / 2);
		cam.update();
		
		Gdx.input.setInputProcessor(new GameInputProcessor());
		
		gsm = new GameStateManager();
	}
	
	public void render(){
		
		// clear screen to black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.draw();
		
		GameKeys.update();
		
	}
	
	public void resize(int width, int height) {}
	public void pause() {}
	public void resume() {}
	public void dispose() {}

}
