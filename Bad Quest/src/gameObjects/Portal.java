package gameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import util.Vector;

import world.Room;
import world.Tile;

import client.Camera;

public class Portal extends DrawableObject {
	private Room owner;
	
	private Room to;
	private Portal exit;
	
	private State state;
	
	public Portal(Room owner, Vector position){
		moveable = false;
		solid = false;
		this.owner = owner;
		state = State.OFF;
		radius = Tile.SIZE/2;
		setPosition(position);
	}
	
	public void setPortal(Room r, Portal e){
		to = r;
		exit = e;
		state = State.ACTIVE;
	}
	
	@Override
	public void update(double elapsedTime) {
		if(state == State.INACTIVE && !owner.actorWithinCircle(position, radius+Tile.SIZE/2))
			state = State.ACTIVE;
	}
	
	public void setState(State s){
		state = s;
	}
	
	public State getState(){
		return state;
	}
	
	public Room getExitRoom(){
		return to;
	}
	
	public Portal getExitPortal(){
		return exit;
	}
	
	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam) {
		AffineTransform prev = g.getTransform();
		
		g.translate(cam.xTranslatePosition(position.x), cam.yTranslatePosition(position.y));
		g.scale(cam.scale(), cam.scale());
		
		int drawRadius = (int)radius;
		g.setColor(Color.green.darker());
		if(state == State.OFF)
			g.drawOval(-drawRadius, -drawRadius, 2*drawRadius, 2*drawRadius);
		else
			g.fillOval(-drawRadius, -drawRadius, 2*drawRadius, 2*drawRadius);
		
		g.setColor(Color.green.brighter());
		if(state == State.ACTIVE)
			g.fillOval(-drawRadius+1, -drawRadius+1, 2*drawRadius-2, 2*drawRadius-2);
		
		g.setTransform(prev);
	}
	
	public enum State{
		ACTIVE,
		INACTIVE,
		OFF;
	}
}
