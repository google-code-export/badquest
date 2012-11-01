package gameObjects;

import java.awt.Graphics2D;

import util.SpringDampHelper;
import util.Vector;
import client.Camera;

public class EquipmentModule {
	private Vector position; //The expected position of his piece of equipment
	private Equipment cur;
	
	private SpringDampHelper spring = new SpringDampHelper(2.9, .08, 5);
	
	public EquipmentModule(Vector position){
		this.position = new Vector(position);
	}
	
	/**
	 * Currently discards active piece of equipment, because FUCK IT.
	 * @param equip
	 */
	public void loadEquipment(Equipment equip){
		cur = equip;
	}
	
	public void setPosition(Vector v){
		position.setTo(v);
	}
	
	public void setAngle(double a){
		cur.setAngle(a);
	}
	
	public void move(double elapsedSeconds){
		if(cur != null){
			Vector velocity = spring.getVelocity(position, cur.getPosition());
			cur.setVelocity(velocity);
			cur.update(elapsedSeconds);
		}
	}
	
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam){
		if(cur != null)
			cur.drawBody(g, elapsedSeconds, cam);
	}
}
