package gameObjects;

import graphics.Camera;

import java.awt.Graphics2D;


public abstract class Equipment extends DrawableObject {
	protected double cycleTimer;
	protected double cooldown;
	
	protected EquipmentModule host = null;
	
	public Equipment(){
		solid = false;
	}
	
	protected void triggerCycleCooldown(){
		cooldown = cycleTimer;
	}
	
	protected final void registerWithModule(EquipmentModule host){
		if(this.host != null){
			System.err.println("Double-registering a piece of equipment: " + this);
			return;
		}
		this.host = host;
	}
	
	protected final void unregister(){
		host = null;
	}
	
	@Override
	public void update(double elapsedSeconds){
		move(elapsedSeconds);
		if(cooldown > 0)
			cooldown -= elapsedSeconds;
	}

	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam){
		
	}
	
	/**
	 * Do stuff, equipment specific
	 */
	public abstract void activate();
}
