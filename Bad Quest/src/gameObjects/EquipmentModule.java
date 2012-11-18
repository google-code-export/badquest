package gameObjects;

import graphics.Camera;

import java.awt.Graphics2D;

import util.SpringDampHelper;
import util.Vector;

public class EquipmentModule {
	private double radius; //Distance from center of the host body
	private double angleOffset; //Angle offset of position relative to the host's angle
	private double tilt;
	private Actor host;
	private Equipment cur;
	
	private SpringDampHelper spring = new SpringDampHelper(2.9, .08, 5);
	private double maxDist = 20;
	
	public EquipmentModule(Actor host){
		this.host = host;
		radius = 0;
		angleOffset = 0;
		tilt = 0;
		maxDist = 0;
	}
	
	public EquipmentModule(Actor host, double radius, double angleOffset, double tilt){
		this.host = host;
		this.radius = radius;
		this.angleOffset = angleOffset;
		this.tilt = tilt;
	}
	
	public Actor getActor(){
		return host;
	}
	
	public double getRadius(){
		return radius;
	}
	
	public double getAngleOffset(){
		return angleOffset;
	}
	
	public double getTilt(){
		return tilt;
	}
	
	public void setRadius(double nextRadius){
		radius = nextRadius;
	}
	
	public void setAngleOffset(double angle){
		angleOffset = angle;
	}
	
	/**
	 * Currently discards active piece of equipment, because FUCK IT.
	 * @param equip
	 */
	public void loadEquipment(Equipment equip){
		if(cur != null)
			cur.unregister();
		cur = equip;
		if(cur != null)
			cur.registerWithModule(this);
	}
	
	public void use(){
		//activate the equipment!
		if(cur != null)
			cur.activate();
	}
	
	public void move(double elapsedSeconds){
		Vector expectedPosition = host.getPosition().add(new Vector(host.getAngle()+angleOffset).scale(radius));
		if(expectedPosition.dis2(cur.getPosition()) > maxDist*maxDist){
			Vector clamp = cur.getPosition().sub(expectedPosition).norm().scale(maxDist);
			cur.setPosition(expectedPosition.add(clamp));
		}
		Vector velocity = spring.getVelocity(expectedPosition, cur.getPosition());
		cur.setInternalVelocity(velocity);
		cur.setAngle(host.getAngle()+tilt);
	}
	
	public void update(double elapsedSeconds){
		if(cur != null){
			move(elapsedSeconds);
			cur.update(elapsedSeconds);
		}
	}
	
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam){
		if(cur != null)
			cur.drawBody(g, elapsedSeconds, cam);
	}
}