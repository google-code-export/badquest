package gameObjects;

import java.awt.Graphics2D;

import client.Camera;

public abstract class Equipment extends DrawableObject {
	
	public Equipment(){
		solid = false;
	}
	
	@Override
	public void update(double elapsedSeconds){
		move(elapsedSeconds);
	}

	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam){
	}
	
	/**
	 * Do stuff, equipment specific
	 */
	public abstract void use();
}
