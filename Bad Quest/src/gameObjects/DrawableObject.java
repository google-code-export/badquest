package gameObjects;

import graphics.Camera;

import java.awt.Graphics2D;

import util.Geometry;
import util.Vector;
import world.Room;

public abstract class DrawableObject {
	protected String name = "default";
	protected Vector position = new Vector(0,0);
	protected Vector internalVelocity = new Vector(0,0);
	protected Vector externalVelocity = new Vector(0,0);
	protected double radius,angle,dragPerSecond = 1200,mass=1;
	protected boolean moveable = true, solid = true, alive = true;
	
	protected Room currentRoom;
	protected final int OID;
	
	public DrawableObject(){
		if(this instanceof Player)
			OID = registerWithOffset(1000000000);
		else if(this instanceof Actor)
			OID = registerWithOffset(1000000);
		else
			OID = register();
	}
	
	public DrawableObject(int ID){
		OID = registerSpecific(ID);
	}
	
	private final int register(){
		return ObjectManager.register(this);
	}
	
	private final int registerSpecific(int ID){
		return ObjectManager.registerWithID(this, ID);
	}
	
	private final int registerWithOffset(int ID){
		return ObjectManager.registerWithOffset(this, ID);
	}
	
	//Gets
	public int getOID(){
		return OID;
	}
	public Vector getPosition(){
		return new Vector(position);
	}
	public Vector getVelocity(){
		return internalVelocity.add(externalVelocity);
	}
	public Vector getInternalVelocity(){
		return new Vector(internalVelocity);
	}
	public Vector getExternalVelocity(){
		return new Vector(externalVelocity);
	}
	public double getRadius(){
		return radius;
	}
	public double getAngle() {
		return angle;
	}
	public double getMass(){
		return mass;
	}
	public String getName(){
		return name;
	}
	public boolean isSolid() {
		return solid;
	}
	public Room getCurrentRoom(){
		return currentRoom;
	}
	
	//Sets
	public void setPosition(Vector v){
		position = new Vector(v);
	}
	public void setInternalVelocity(Vector v){
		if(!moveable){
			System.err.println("Moving an immovable object!");
			return;
		}
		internalVelocity = new Vector(v);
	}
	public void setExternalVelocity(Vector v){
		if(!moveable){
			System.err.println("Moving an immovable object!");
			return;
		}
		externalVelocity = new Vector(v);
	}
	public void setRadius(double radius){
		this.radius = radius;
	}
	public void setAngle(double angle){
		this.angle = angle;
	}
	public void setMass(double mass){
		this.mass = mass;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setSolid(boolean solid) {
		this.solid = solid;
	}
	public void setCurrentRoom(Room next){
		currentRoom = next;
	}
	
	public void applyExternalVelocity(Vector vel){
		externalVelocity = externalVelocity.add(vel);
	}
	
	public void stopMoving(){
		internalVelocity = new Vector(0,0);
		externalVelocity = new Vector(0,0);
	}
	
	public void stopMovingInDirection(Vector dir){
		Vector iproj = internalVelocity.project(dir);
		Vector eproj = externalVelocity.project(dir);
		if(Geometry.findScale(new Vector(0,0), dir, iproj) >= 0)
			internalVelocity = internalVelocity.sub(iproj);
		if(Geometry.findScale(new Vector(0,0), dir, eproj) >= 0)
			externalVelocity = externalVelocity.sub(eproj);
	}
	
	public void kill(){
		alive = false;
	}
	
	public boolean isDead(){
		return !alive;
	}
	
	/**
	 * Unregister this object, get rid of all references to it.
	 */
	public final void delete(){
		if(currentRoom != null)
			currentRoom.removeEntity(getOID());
		ObjectManager.removeObjectByID(getOID());
	}
	
	/**
	 * Transfers this drawable object to a specified room. Deletes this object from the current room's object map,
	 * then adds this object to the next room's object map. Note that the object retains its position.
	 * @param next
	 * 			The room this object is moving to. Set to null if removing this object from the game world.
	 */
	public void changeCurrentRoom(Room next) {
		if(currentRoom != null)
			currentRoom.getEntityMap().remove(getOID());
		
		if(next != null)
			next.addEntity(this);
	
		setCurrentRoom(next);
	}
	
	/**
	 * Transfers this drawable object to a specified room, at the specified coordinates. Deletes this object from 
	 * the current room's object map, then adds this object to the next room's object map.
	 * @param next
	 * 			The room this object is moving to. Set to null if removing this object from the game world.
	 * @param pos
	 * 			The room-relative location at which to place this object.
	 */
	public void changeCurrentRoom(Room next, Vector pos) {
		if(currentRoom != null)
			currentRoom.getEntityMap().remove(getOID());
		
		if(next != null)
			next.addEntityAt(this, pos);
	
		setCurrentRoom(next);
	}
	
	/**
	 * Round off an array of doubles to integral values
	 * @param x
	 * @return
	 */
	protected int[] roundOff(double[] x){
		int[] ret = new int[x.length];
		for(int i = 0; i < ret.length; i++)
			ret[i] = (int)Math.round(x[i]);
		return ret;
	}
	
	/**
	 * Draw the outline of a polygon using floating point vertices and the current graphics context.
	 */
	protected void drawPoly(Vector[] p, Graphics2D g){
		double[] x = new double[p.length];
		double[] y = new double[p.length];
		for(int i = 0; i < p.length; i++){
			x[i] = p[i].x;
			y[i] = p[i].y;
		}
		drawPoly(x,y,g);
	}
	
	/**
	 * Draw the outline of a polygon using floating point vertices and the current graphics context.
	 */
	protected void drawPoly(double[] x, double[] y, Graphics2D g){
		int[] px = roundOff(x);
		int[] py = roundOff(y);
		
		g.drawPolygon(px, py, x.length);
	}
	
	/**
	 * Fill a polygon using floating point vertices and the current graphics context.
	 */
	protected void fillPoly(Vector[] p, Graphics2D g){
		double[] x = new double[p.length];
		double[] y = new double[p.length];
		for(int i = 0; i < p.length; i++){
			x[i] = p[i].x;
			y[i] = p[i].y;
		}
		fillPoly(x,y,g);
	}
	
	/**
	 * Fill a polygon using floating point vertices and the current graphics context.
	 */
	protected void fillPoly(double[] x, double[] y, Graphics2D g){
		int[] px = roundOff(x);
		int[] py = roundOff(y);
		
		g.fillPolygon(px, py, x.length);
	}
	
	//Update stuff
	public void move(double elapsedSeconds){
		double mag = externalVelocity.mag();
		setExternalVelocity(externalVelocity.scale(Math.max((mag-mass*dragPerSecond*elapsedSeconds)/mag,0)));
		
		if(solid && currentRoom != null)
			currentRoom.collideWithSolids(this, elapsedSeconds);
		
		position = position.add(getVelocity().scale(elapsedSeconds));
	}
	public abstract void update(double elapsedSeconds);
	public abstract void drawBody(Graphics2D g, double elapsedSeconds, Camera cam);
	
	public String toString(){
		return name + " (object "+OID+")";
	}
}