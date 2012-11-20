package gameObjects;

import graphics.Camera;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;

import util.Vector;
import world.Room;
import world.tile.Tile;

public class Portal extends DrawableObject {
	private Room owner;
	
	private Room to;
	private Vector exitPos;
	
	private State state;
	
	private int n = 6;
	private int[] x,y;
	private double angularVelocity = Math.PI/2;
	
	public Portal(Room owner, Vector position){
		moveable = false;
		solid = false;
		this.owner = owner;
		state = State.OFF;
		radius = Tile.SIZE*.8;
		setPosition(position);
		name = "Portal";
		init();
	}
	
	public void init(){
		x = new int[n];
		y = new int[n];
		for(int i = 0; i < n; i++){
			x[i] = (int)((radius*.9) * Math.cos(2*Math.PI/n * i));
			y[i] = (int)((radius*.9) * Math.sin(2*Math.PI/n * i));
		}
	}
	
	public void setState(State s){
		state = s;
	}	
	
	public void setExit(Room to, Vector pos){
		this.to = to;
		exitPos = new Vector(pos);
	}
	
	public State getState(){
		return state;
	}
	
	public Room getOwner(){
		return owner;
	}
	
	public Room getExitRoom(){
		return to;
	}
	
	public Vector getExitPosition(){
		return exitPos;
	}
	
	public void link(Portal p){
		setExit(p.getOwner(), p.getPosition().sub(to.getPosition()));
		p.setExit(owner, getPosition().sub(owner.getPosition()));
	}
	
	public void linkAndSetActive(Portal p){
		setExit(p.getOwner(), p.getPosition().sub(p.getOwner().getPosition()));
		p.setExit(owner, getPosition().sub(owner.getPosition()));
		
		setState(State.ACTIVE);
		p.setState(State.ACTIVE);
	}
	
	@Override
	public void update(double elapsedSeconds) {
		if(state == State.INACTIVE && !owner.actorWithinCircle(position, radius+Tile.SIZE/2))
			state = State.ACTIVE;
//		if(state == State.ACTIVE)
//			angle = (angle + angularVelocity*elapsedSeconds)%(2*Math.PI);
	}
	
	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam) {
		if(state == State.ACTIVE)
		angle = (angle + angularVelocity*elapsedSeconds)%(2*Math.PI);
		
		AffineTransform prev = g.getTransform();
		
		g.translate(cam.xTranslatePosition(position.x), cam.yTranslatePosition(position.y));
		g.scale(cam.scale(), cam.scale());
		g.rotate(angle);
		
		Polygon draw = new Polygon(x,y,n);
		
		int drawRadius = (int)radius;
		g.setColor(Color.gray.darker());
		g.fillOval(-drawRadius, -drawRadius, 2*drawRadius, 2*drawRadius);
		
		if(state == State.ACTIVE){
			g.setColor(Color.green.brighter());
		}else
			g.setColor(Color.DARK_GRAY);
		
		g.drawPolygon(draw);
		
		
		if(state == State.OFF)
			g.setColor(Color.DARK_GRAY.darker());
		else
			g.setColor(Color.green);
			
		g.drawOval(-drawRadius, -drawRadius, 2*drawRadius, 2*drawRadius);
		
//		if(state == State.ACTIVE)
//			g.fillOval(-drawRadius+1, -drawRadius+1, 2*drawRadius-2, 2*drawRadius-2);
		
		g.setTransform(prev);
	}
	
	public enum State{
		ACTIVE,
		INACTIVE,
		OFF;
	}
}
