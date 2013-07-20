package gameObjects;

import gameAI.behaviors.FollowerBehavior;
import gameObjects.equipment.DebugSword;
import gameObjects.equipment.EquipmentModule;
import gameObjects.equipment.HornedHelmet;
import graphics.Camera;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import util.Vector;

public class DebugEnemy extends Actor {
	EquipmentModule[] equip;
	
	double moveSpeed = 75;
	double moveClock = .5;
	double timeToNextMove = Math.random()*moveClock;
	double skipAhead = 4;
	boolean canSkip = false;
	
	FollowerBehavior brain;
	
	public DebugEnemy(){
		super("Hunter", 10);
		
		equip = new EquipmentModule[3];
		equip[0] = new EquipmentModule(this, radius*1.5, Math.PI/2.5, Math.PI/6);
		equip[0].loadEquipment(new DebugSword());
		
		equip[1] = new EquipmentModule(this, radius*1.5, -Math.PI/2.5, -Math.PI/6);
//		equip[1] = new EquipmentModule(this, radius*1.4, -Math.PI/2, -Math.PI/1.9, 4);
		equip[1].loadEquipment(new DebugSword());
		
		equip[2] = new EquipmentModule(this);
		equip[2].loadEquipment(new HornedHelmet());
		
		
		brain = new FollowerBehavior(this);
	}
	
	@Override
	public void kill() {
		for(EquipmentModule eqm:equip)
			eqm.loadEquipment(null);
		super.kill();
	}
	
	@Override
	public void setPosition(Vector v){
		super.setPosition(v);
		for(EquipmentModule eqm:equip)
			eqm.move(0);
	}
	
	@Override
	public void update(double elapsedSeconds) {
		brain.update(elapsedSeconds);
		
		super.update(elapsedSeconds);
		for(EquipmentModule eqm:equip)
			eqm.update(elapsedSeconds);
	}
	
	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam) {
		AffineTransform prev = g.getTransform();
		
		super.drawBody(g, elapsedSeconds, cam);
		
		AffineTransform next = new AffineTransform();
		next.translate(cam.xTranslatePosition(position.x), cam.yTranslatePosition(position.y));
		next.scale(cam.xScale(), cam.yScale());
		g.setTransform(next);
		
		
		
		g.setTransform(prev);
		
		for(EquipmentModule eqm:equip)
			eqm.drawBody(g, elapsedSeconds, cam);
	}
}
