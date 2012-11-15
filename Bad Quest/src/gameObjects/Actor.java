package gameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import util.Vector;
import client.Camera;

public class Actor extends DrawableObject {
	private String name = "default";
	protected Color color = Color.ORANGE;
	
	private Vector lookAt = new Vector(0,0);
	
	public Actor(){
	}
	
	public Actor(String name){
		this.name = name;
	}
	
	public Actor(String name, int r){
		this.name = name;
		radius = r;
	}
	
	public Actor(String name, int r, Vector position){
		this.name = name;
		radius = r;
		this.position.setTo(position);
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public Vector getLookAt(){
		return lookAt;
	}
	
	public void setLookAt(Vector look){
		if(look == null)
			lookAt = null;
		else
			lookAt = new Vector(look);
	}

	@Override
	public void update(double elapsedSeconds){
		if(lookAt != null)
			angle = lookAt.sub(position).ang();
		move(elapsedSeconds);
	}

	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam){
		AffineTransform prev = g.getTransform();
		
		AffineTransform next = new AffineTransform();
		next.translate(cam.xTranslatePosition(position.x), cam.yTranslatePosition(position.y));
		next.scale(cam.xScale(), cam.yScale());
		
		g.setTransform(next);
		g.setColor(color);
		int drawRadius = (int)radius;
		g.fillOval(-drawRadius,-drawRadius,2*drawRadius,2*drawRadius);
		
		g.setTransform(prev);
	}
}
