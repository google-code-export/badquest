package gameObjects;

import gameAI.Node;
import gameAI.actions.MoveTo;
import gameAI.actions.Wander;
import gameAI.behaviors.Behavior;
import gameAI.behaviors.FollowerBehavior;
import gameObjects.equipment.EquipmentModule;
import gameObjects.equipment.HornedHelmet;
import graphics.Camera;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayDeque;

import util.Vector;

public class Follower extends Actor{
	EquipmentModule helmet;
	FollowerBehavior brain;
	
	double moveSpeed = 75;

	public Follower(){
		super("Follower", 10);
		brain = new FollowerBehavior(this);
		helmet = new EquipmentModule(this);
		helmet.loadEquipment(new HornedHelmet());
	}
	
	public Behavior getBrain(){
		return brain;
	}
	
	@Override
	public void kill() {
		helmet.loadEquipment(null);
		super.kill();
	}
	
	@Override
	public void setPosition(Vector v){
		super.setPosition(v);
		helmet.move(0);
	}
	
	@Override
	public void update(double elapsedSeconds) {
		if(brain != null)
			brain.update(elapsedSeconds);
		super.update(elapsedSeconds);
		helmet.update(elapsedSeconds);
	}
	
	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam) {
		AffineTransform prev = g.getTransform();
		
		super.drawBody(g, elapsedSeconds, cam);
		
		AffineTransform next = new AffineTransform();
		next.translate(cam.xTranslatePosition(position.x), cam.yTranslatePosition(position.y));
		next.scale(cam.xScale(), cam.yScale());
		g.setTransform(next);

		g.setColor(Color.green);
		if(brain.getCurrentAction() instanceof MoveTo){
			MoveTo action = (MoveTo)(brain.getCurrentAction());
			ArrayDeque<Node> wp = action.getWaypoints();
			
			Vector p = Vector.ZERO;
			for(Node x:wp){
				Vector pos = x.getPosition().sub(getPosition());
				g.drawLine((int)p.x, (int)p.y, (int)pos.x, (int)pos.y);
				p = pos;
			}
		}else if(brain.getCurrentAction() instanceof Wander){
			g.setColor(Color.red);
			Wander action = (Wander)(brain.getCurrentAction());
			if(action.target() != null){
				Vector draw = action.target().sub(getPosition());
				g.drawLine(0,0,(int)draw.x,(int)draw.y);
			}
		}
		
		g.setColor(Color.orange);
		g.fillRect(-(int)radius, -(int)(radius+4), (int)(2*radius*brain.getInterestLevel()), 3);
		g.setColor(Color.black);
		g.drawRect(-(int)radius, -(int)(radius+4), (int)(2*radius), 3);
		
		g.setTransform(prev);
		
		helmet.drawBody(g, elapsedSeconds, cam);
	}
}
