package gameObjects;

import java.awt.Graphics2D;

import util.Vector;
import client.Camera;

public abstract class DrawableObject {
	protected Vector position = new Vector(0,0);
	protected Vector velocity = new Vector(0,0);
	protected double radius;
	protected boolean moveable = true;
	public Vector getPosition(){
		return new Vector(position);
	}
	public Vector getVelocity(){
		return new Vector(velocity);
	}
	public double getRadius(){
		return radius;
	}
	public double setRadius(){
		return radius;
	}
	public void setPosition(Vector v){
		position.setTo(v);
	}
	public void setVelocity(Vector v){
		if(!moveable){
			System.err.println("Moving an immovable object!");
			return;
		}
		velocity.setTo(v);
	}
	public void move(double elapsedSeconds){
		Vector.add(position, velocity.scale(elapsedSeconds));
	}
	public abstract void update(double elapsedTime);
	public abstract void drawBody(Graphics2D g, double elapsedSeconds, Camera cam);
}