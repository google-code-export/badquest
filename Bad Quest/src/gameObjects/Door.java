package gameObjects;

import gameObjects.interfaces.Damageable;
import gameObjects.interfaces.Interactive;
import graphics.Camera;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import world.tile.Tile;

public class Door extends DrawableObject implements Interactive, Damageable{
	private boolean open = false;
	private Tile control;
	
	private Color frame = new Color(0x734017);
	private Color door = new Color(0x4A2A12);
	
	public Door(){
		open = true;
		moveable = false;
		solid = false;
		radius = Tile.SIZE/2;
	}
	
	public boolean isOpen(){
		return open;
	}
	
	public void bindToTile(Tile t){
		if(control != null)
			control.setBlocked(false);
		
		control = t;
		
		if(control == null)
			return;
		
		setPosition(control.getCenter());
		control.setBlocked(!open);
		updateControl();
	}
	
	public void updateControl(){
		control.setBlocked(!open);
		getCurrentRoom().updateNodeGraph(control.getRow(), control.getCol());
	}
	
	public void interact(){
		open = !open;
		if(control != null)
			updateControl();
	}
	
	@Override
	public void applyDamage(int amount){
		if(amount > 0)
			interact();
	}
	
	@Override
	public int getCurrentHealth(){
		return 1;
	}
	
	@Override
	public Faction getFaction(){
		return Faction.NEUTRAL;
	}
	
	@Override
	public int getMaxHealth(){
		return 1;
	}
	
	@Override
	public boolean isDamageable(Faction f){
		return true;
	}

	public void update(double elapsedSeconds){
		if(control == null)
			open = true;
		else if(open == control.isBlocked())
			control.setBlocked(!open); //toggle control
	}

	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam){
		AffineTransform prev = g.getTransform();
		
		AffineTransform next = new AffineTransform();
		next.translate(cam.xTranslatePosition(position.x), cam.yTranslatePosition(position.y));
		next.scale(cam.xScale(), cam.yScale());
		g.setTransform(next);
		
		g.setColor(door);
		if(!open)
			g.fillRect(-Tile.SIZE/2, -Tile.SIZE/2, Tile.SIZE, Tile.SIZE);
		g.setColor(frame);
		g.drawRect(-Tile.SIZE/2, -Tile.SIZE/2, Tile.SIZE, Tile.SIZE);
		
		g.setTransform(prev);
	}
}
