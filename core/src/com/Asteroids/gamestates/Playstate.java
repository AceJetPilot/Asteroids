package com.Asteroids.gamestates;

import java.util.ArrayList;

import com.Asteroids.entities.Asteroid;
import com.Asteroids.entities.Bullet;
import com.Asteroids.entities.Player;
import com.Asteroids.game.Game;
import com.Asteroids.managers.GameKeys;
import com.Asteroids.managers.GameStateManager;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class Playstate extends GameState{
	
	private ShapeRenderer sr;
	
	private Player player;
	private ArrayList<Bullet> bullets;
	private ArrayList<Asteroid> asteroids;
	
	private int level;
	private int totalAsteroids;
	private int numAsteroidsLeft;
	
	public Playstate(GameStateManager gsm){
		super(gsm);
	}

	public void init() {
		
		sr = new ShapeRenderer();
		
		bullets = new ArrayList<Bullet>();
		
		player = new Player(bullets); 
		
		asteroids = new ArrayList<Asteroid>();
		asteroids.add(new Asteroid(100, 100, Asteroid.LARGE));
		asteroids.add(new Asteroid(200, 100, Asteroid.MEDIUM));
		asteroids.add(new Asteroid(300, 100, Asteroid.SMALL));
		
		level = 1;
		spawnAsteroids();
		
	}
	
	private void spawnAsteroids(){
		asteroids.clear();
		
		int numToSpawn = 4 + level - 1;
		totalAsteroids = numToSpawn * 7;
		numAsteroidsLeft = totalAsteroids;
		
		for(int i = 0; i < numToSpawn; i++){
			float x = MathUtils.random(Game.WIDTH);
			float y = MathUtils.random(Game.HEIGHT);
			
			float dx = x * player.getx();
			float dy = y - player.gety();
			float dist = (float) Math.sqrt(dx * dx + dy * dy);
			
			while(dist < 100){
				x = MathUtils.random(Game.WIDTH);
				y = MathUtils.random(Game.HEIGHT);
				dx = x * player.getx();
				dy = y - player.gety();
				dist = (float) Math.sqrt(dx * dx + dy * dy);
			}
			
			asteroids.add(new Asteroid(x, y , Asteroid.LARGE));
		}
	}

	public void update(float dt) {
		//System.out.println("play state updating");
		handleInput();
		
		player.update(dt);
		
		for(int i = 0; i < bullets.size(); i++){
			bullets.get(i).update(dt);
			if(bullets.get(i).shouldRemove()){
				bullets.remove(i);
				i--;
			}
		}
		
		for(int i = 0; i < asteroids.size(); i++){
			asteroids.get(i).update(dt);
			if(asteroids.get(i).shouldRemove()){
				asteroids.remove(i);
				i--;
			}
		}
		
		checkCollisions();
		
		
	}
	
	private void checkCollisions(){
		//bullet-asteroid collision
		for(int i = 0; i < bullets.size(); i++){
			Bullet b = bullets.get(i);
			for(int j = 0; j < asteroids.size(); j++){
				Asteroid a = asteroids.get(j);
				//if a contains b
			}
		}
	}

	public void draw() {
		//System.out.println("play state drawing");
		player.draw(sr);
		
		for(int i = 0; i < bullets.size(); i++){
			bullets.get(i).draw(sr);
		}
		
		for(int i = 0; i < asteroids.size(); i++){
			asteroids.get(i).draw(sr);
		}
	}

	public void handleInput() {
		player.setLeft(GameKeys.isDown(GameKeys.LEFT));
		player.setRight(GameKeys.isDown(GameKeys.RIGHT));
		player.setUp(GameKeys.isDown(GameKeys.UP));
		if(GameKeys.isPressed(GameKeys.SPACE)){
			player.shoot();
		}
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
}
