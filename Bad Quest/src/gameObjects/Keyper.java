package gameObjects;

import java.awt.Color;
import java.awt.Graphics2D;

import gameAI.behaviors.Behavior;
import gameAI.behaviors.KeyperClosedBehavior;
import gameAI.behaviors.KeyperOpenBehavior;
import gameObjects.equipment.DebugHelmet;
import gameObjects.equipment.EquipmentModule;
import gameObjects.equipment.HornedHelmet;
import gameObjects.interfaces.Ambulatory;
import graphics.Camera;

public class Keyper extends Actor implements Ambulatory{
	private Behavior brain;
	private EquipmentModule head;
	
	private double moveSpeed = 50;
	
	public Keyper(){
		super("Key-per of... what?", 8);
		brain = new KeyperClosedBehavior(this);
		head = new EquipmentModule(this);
		head.loadEquipment(new DebugHelmet(Color.gray));
	}
	
	public Keyper(String name){
		super(name, 10);
		brain = new KeyperClosedBehavior(this);
		head = new EquipmentModule(this);
		head.loadEquipment(new DebugHelmet(Color.gray));
	}
	
	public Keyper(String name, int type){
		super(name, 10);
		
		head = new EquipmentModule(this);
		if(type == 0){
			head.loadEquipment(new DebugHelmet(new Color(167,80,0)));
			brain = new KeyperClosedBehavior(this);
		}else{
			head.loadEquipment(new HornedHelmet());
			brain = new KeyperOpenBehavior(this);
		}
	}
	
	public Behavior getBrain(){
		return brain;
	}
	
	@Override
	public double getMaxSpeed(){
		return moveSpeed;
	}
	
	@Override
	public void update(double elapsedSeconds) {
		if(brain != null)
			brain.update(elapsedSeconds);
		super.update(elapsedSeconds);
		head.update(elapsedSeconds);
	}
	
	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam){
		super.drawBody(g, elapsedSeconds, cam);
		head.drawBody(g, elapsedSeconds, cam);
	}
}
