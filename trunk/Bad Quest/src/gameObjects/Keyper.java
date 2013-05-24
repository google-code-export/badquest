package gameObjects;

import java.awt.Color;
import java.awt.Graphics2D;

import gameAI.behaviors.Behavior;
import gameAI.behaviors.KeyperClosedBehavior;
import gameObjects.equipment.DebugHelmet;
import gameObjects.equipment.EquipmentModule;
import graphics.Camera;

public class Keyper extends Actor {
	private Behavior brain;
	private EquipmentModule head;
	public Keyper(){
		super("Key-per of... what?", 8);
		brain = new KeyperClosedBehavior(this);
		head = new EquipmentModule(this);
		head.loadEquipment(new DebugHelmet(Color.gray));
	}
	
	public Keyper(String name){
		super(name, 8);
		brain = new KeyperClosedBehavior(this);
		head = new EquipmentModule(this);
		head.loadEquipment(new DebugHelmet(Color.gray));
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
