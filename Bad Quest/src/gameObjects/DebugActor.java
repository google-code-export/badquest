package gameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import util.Vector;
import client.Camera;

public class DebugActor extends DrawableObject{
	public DebugActor(Vector v){
		setPosition(v);
	}

	public void update(double elapsedSeconds){
		move(elapsedSeconds);
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
