package com.Asteroids.entities;

import com.Asteroids.game.Game;

public class SpaceObject {
	
	protected float x;
	protected float y;
	
	protected float dx;
	protected float dy;
	
	protected float radians;
	protected float speed;
	protected float rotationSpeed;
	
	protected int width;
	protected int height;
	
	protected float[] shapex;
	protected float[] shapey;
	
	public float getx() {return x;}
	public float gety() {return y;}
	
	public float[] getShapeX() {return shapex;}
	public float[] getShapeY() {return shapey;}
	
	/*public boolean contains(float x, float y){
		boolean b = false;
		for(int i = 0, j = shapex.length - 1; i < shapex.length; j = i++){
			if((shapex[i] > y) != (shapey[j] > y) && (x < (shapex[j] - shapex[i])+ shapex[i])){
				
			}
		}
	}*/
	
	
	protected void wrap(){
		if(x < 0) x = Game.WIDTH;
		if(x > Game.WIDTH) x = 0;
		if(y < 0) y = Game.HEIGHT;
		if(y > Game.HEIGHT) y = 0;
	}
	
	protected void boundries(){
		if(x < 10) x = 10;
		if(x > Game.WIDTH) x = Game.WIDTH - 10;
		if(y < 10) y = 10;
		if(y > Game.HEIGHT) y = Game.HEIGHT - 10;
	}
}
