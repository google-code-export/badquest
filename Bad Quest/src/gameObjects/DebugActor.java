package gameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import util.Vector;
import client.Camera;

public class DebugActor extends DrawableObject{
	private Vector base,velocity = new Vector(5,0);
	private double R,timeElapsed,period;
	public DebugActor(Vector v){
		R = 75;
		timeElapsed = 0;
		period = 2;
		base = new Vector(v);
		position = base.add(new Vector(R,0));
	}
	
	public void setPosition(Vector v){
		position.setTo(v);
	}
	
	public void update(double elapsedSeconds){
//		timeElapsed = (timeElapsed + elapsedTime)%period;
//		Vector add = new Vector(2*Math.PI*timeElapsed/period).scale(R);
//		position = base.add(add);
		position = position.add(velocity);
	}
	
	public Vector getPosition() {
		return position;
	}
	
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam){
		AffineTransform prev = g.getTransform();
		
		AffineTransform next = new AffineTransform();
		next.translate(cam.xTranslatePosition(position.x), cam.yTranslatePosition(position.y));
		next.scale(cam.xScale(), cam.yScale());
		
		g.setTransform(next);
		g.setColor(Color.ORANGE);
		g.fillOval(-10,-10,20,20);
		
		g.setTransform(prev);
	}
}
