package gameObjects;

import graphics.Camera;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import util.Vector;

public class DebugBall extends DrawableObject {
	BufferedImage moon = null;
	public DebugBall(Vector position, double radius){
		this.position = new Vector(position);
		this.radius = radius/2;
		solid = true;
		mass = .001;
		name = "Moon";
		try{
			moon = ImageIO.read(this.getClass().getResource("/resources/img/Moon.png"));
		}catch(Exception e){
			System.out.println("Moon.png not found!");
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(double elapsedSeconds) {
		move(elapsedSeconds);
	}

	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam) {
		AffineTransform prev = g.getTransform();
		
		AffineTransform next = new AffineTransform();
		next.translate(cam.xTranslatePosition(position.x), cam.yTranslatePosition(position.y));
		next.scale(cam.xScale(), cam.yScale());
		g.setTransform(next);
		
		try {
			g.drawImage(moon, -(int)radius, -(int)radius, (int)radius, (int)radius, 0, 0, moon.getWidth(), moon.getHeight(), null);
		} catch (Exception e) {
			g.setPaint(new GradientPaint(-10, -10, Color.black, 10, 10, Color.orange));
			g.fillOval(-(int)radius, -(int)radius, 2*(int)radius, 2*(int)radius);	
			e.printStackTrace();
		}	
		
		g.setTransform(prev);
	}
}
