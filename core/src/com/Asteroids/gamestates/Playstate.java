package com.Asteroids.gamestates;

import java.util.ArrayList;

import com.Asteroids.entities.Asteroid;
import com.Asteroids.entities.Bullet;
import com.Asteroids.entities.FlyingSaucer;
import com.Asteroids.entities.Particle;
import com.Asteroids.entities.Player;
import com.Asteroids.game.Game;
import com.Asteroids.managers.GameKeys;
import com.Asteroids.managers.GameStateManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class Playstate extends GameState{
	
	private SpriteBatch sb;
	private ShapeRenderer sr;
	
	private BitmapFont font;
	
	private Player player;
	
	private ArrayList<Bullet> bullets;
	private ArrayList<Asteroid> asteroids;
	private ArrayList<Particle> particles;
	private ArrayList<FlyingSaucer> flyingsaucer;
	
	private int level;
	private int totalAsteroids;
	private int numAsteroidsLeft;
	
	public Playstate(GameStateManager gsm){
		super(gsm);
	}

	public void init() {
		
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		
		//setfont
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Hyperspace Bold.ttf"));
		font = gen.generateFont(20);
		
		bullets = new ArrayList<Bullet>();
		
		player = new Player(bullets); 
		
		asteroids = new ArrayList<Asteroid>();
		//asteroids.add(new Asteroid(100, 100, Asteroid.LARGE));
		//asteroids.add(new Asteroid(200, 100, Asteroid.MEDIUM));
		//asteroids.add(new Asteroid(300, 100, Asteroid.SMALL));
		
		particles = new ArrayList<Particle>();
		
		level = 1;
		spawnAsteroids();
		
	}
	
	private void createParticles(float x, float y){
		for(int i = 0; i < 6; i++){
			particles.add(new Particle(x, y));
		}
	}
	
	private void splitAsteroids(Asteroid a){
		createParticles(a.getx(), a.gety());
		numAsteroidsLeft--;
		if(a.getType() == Asteroid.LARGE){
			asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.MEDIUM));
			asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.MEDIUM));
		}
		if(a.getType() == Asteroid.MEDIUM){
			asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.SMALL));
			asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.SMALL));
			//asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.SMALL));
		}
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
		
		//next level
		if(asteroids.size() == 0){
			level++;
			spawnAsteroids();
		}
		//resets player
		player.update(dt);
		if(player.isDead()){
			player.reset();
			player.loseLife();
			return;
		}
		//updates bullets
		for(int i = 0; i < bullets.size(); i++){
			bullets.get(i).update(dt);
			if(bullets.get(i).shouldRemove()){
				bullets.remove(i);
				i--;
			}
		}
		//udates asteroids
		for(int i = 0; i < asteroids.size(); i++){
			asteroids.get(i).update(dt);
			if(asteroids.get(i).shouldRemove()){
				asteroids.remove(i);
				i--;
			}
		}
		
		//updates particles
		for(int i = 0; i < particles.size(); i++){
			particles.get(i).update(dt);
			if(particles.get(i).shouldRemove()){
				particles.remove(i);
				i--;
			}
			
		}
		
		
		
		checkCollisions();
		
		
	}
	
	private void checkCollisions(){
		//player-asteroid collision
		if(!player.isHit()){
			for(int i = 0; i < asteroids.size(); i++){
				Asteroid a = asteroids.get(i);
				if(a.intersects(player)){
					player.hit();
					asteroids.remove(i);
					i--;
					splitAsteroids(a);
					break;
				}
			}
		}
		//bullet-asteroid collision
		for(int i = 0; i < bullets.size(); i++){
			Bullet b = bullets.get(i);
			for(int j = 0; j < asteroids.size(); j++){
				Asteroid a = asteroids.get(j);
				//if a contains b
				if(a.contains(b.getx(), b.gety())){
					bullets.remove(i);
					i--;
					asteroids.remove(j);
					j--;
					splitAsteroids(a);
					//increment score
					player.incrementScore(a.getScore());
					break;
				}
			}
		}
	}

	public void draw() {
		//System.out.println("play state drawing");
		player.draw(sr);
		
		//draws bullets
		for(int i = 0; i < bullets.size(); i++){
			bullets.get(i).draw(sr);
		}
		
		//draws asteroids
		for(int i = 0; i < asteroids.size(); i++){
			asteroids.get(i).draw(sr);
		}
		
		//draw particles
		for(int i = 0; i < particles.size(); i++){
			particles.get(i).draw(sr);
		}
		
		//draw score
		sb.setColor(1, 1, 1, 1);
		sb.begin();
		font.draw(sb, Long.toString(player.getScore()), 40, 390 );
		sb.end();
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
