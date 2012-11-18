package gameObjects;

import graphics.Camera;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import util.Vector;

public class DebugBullet extends DrawableObject {
	private double timeAlive = 2;
	
	public DebugBullet(Vector position, Vector velocity){
		setPosition(position);
		internalVelocity.setTo(velocity);
//		setVelocity(velocity);
		radius = 1;
	}
	
	@Override
	public void update(double elapsedSeconds) {
		timeAlive -= elapsedSeconds;
		move(elapsedSeconds);
		
		if(timeAlive <= 0)
			kill();
	}

	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam) {
		AffineTransform prev = g.getTransform();
		
		g.translate(cam.xTranslatePosition(position.x), cam.yTranslatePosition(position.y));
		g.scale(cam.scale(), cam.scale());
		
		g.setColor(Color.yellow);
		g.fillOval(-(int)radius,-(int)radius, 2*(int)radius, 2*(int)radius);
		
		g.setTransform(prev);
	}

}
