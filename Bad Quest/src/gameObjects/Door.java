package gameObjects;

import gameObjects.interfaces.Interactive;
import graphics.Camera;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import world.tile.Tile;

public class Door extends DrawableObject implements Interactive{
	private boolean open = false;
	private Tile control;
	
	private Color frame = new Color(0x734017);
	private Color door = new Color(0x4A2A12);
	
	public Door(){
		open = false;
		moveable = false;
		solid = false;
	}
	
	public void interact(){
		open = !open;
	}

	public void update(double elapsedSeconds){
		if(control == null)
			open = true;
		else if(open == control.isSolid())
			; //toggle control
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
