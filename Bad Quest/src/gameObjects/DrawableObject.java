package gameObjects;

import java.awt.Graphics2D;

import util.Vector;
import client.Camera;

public abstract class DrawableObject {
	protected Vector position = new Vector(0,0);
	protected Vector velocity = new Vector(0,0);
	protected double radius,angle;
	protected boolean moveable = true, solid = true;
	
	protected final int OID;
	
	public DrawableObject(){
		OID = register();
	}
	
	public DrawableObject(int ID){
		OID = register(ID);
	}
	
	private final int register(){
		return ObjectManager.register(this);
	}
	
	private final int register(int ID){
		return ObjectManager.register(this, ID);
	}
	
	//Gets
	public int getOID(){
		return OID;
	}
	public Vector getPosition(){
		return new Vector(position);
	}
	public Vector getVelocity(){
		return new Vector(velocity);
	}
	public double getRadius(){
		return radius;
	}
	public double getAngle() {
		return angle;
	}
	public boolean isSolid() {
		return solid;
	}
	
	//Sets
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
	public void setRadius(double radius){
		this.radius = radius;
	}
	public void setAngle(double angle){
		this.angle = angle;
	}
	public void setSolid(boolean solid) {
		this.solid = solid;
	}
	
	//Update stuff
	public void move(double elapsedSeconds){
		Vector.add(position, velocity.scale(elapsedSeconds));
	}
	public abstract void update(double elapsedSeconds);
	public abstract void drawBody(Graphics2D g, double elapsedSeconds, Camera cam);
	
	public String toString(){
		return "Object "+OID;
	}
}