package com.Asteroids.managers;

import com.Asteroids.gamestates.GameState;
import com.Asteroids.gamestates.Playstate;

public class GameStateManager {
	
	//current gamestate
	private GameState gameState;
	
	public static final int MENU = 0;
	public static final int PLAY = 420;
	
	public GameStateManager(){
		setState(PLAY);
	}
	
	public void setState(int state){
		if(gameState != null){
			gameState.dispose();
		}
		if(state == MENU){
			//switch to menu state
		}
		if(state == PLAY){
			gameState = new Playstate(this);
		}
	}
	
	public void update(float dt){
		gameState.update(dt);
	}
	
	public void draw(){
		gameState.draw();
	}
	
}
