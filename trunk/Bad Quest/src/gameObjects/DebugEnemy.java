package gameObjects;

import gameAI.Node;
import gameAI.Pathfinding;
import graphics.Camera;

import java.awt.Graphics2D;
import java.util.ArrayDeque;

import util.Vector;

public class DebugEnemy extends Actor {
	ArrayDeque<Node> waypoints;
	
	double moveSpeed = 75;
	double moveClock = .5;
	double timeToNextMove = Math.random()/2.;
	
	DrawableObject follow; //The target this object will pathfind to
	
	public DebugEnemy(int r){
		super("Pathfinder", r);
		waypoints = new ArrayDeque<Node>();
	}
	
	public void setFollow(DrawableObject d){
		follow = d;
	}
	
	private ArrayDeque<Node> merge(ArrayDeque<Node> next){
		if(waypoints.isEmpty())
			return next;
		ArrayDeque<Node> merge = new ArrayDeque<Node>();
		Node hit = waypoints.getFirst();
		while(!next.isEmpty()){
			if(next.peek().n == hit.n)
				return next;
			merge.add(next.remove());
		}
		return merge;
	}
	
	@Override
	public void update(double elapsedSeconds) {
		if(follow == null){
			setInternalVelocity(new Vector(0,0));
		}else{
			timeToNextMove -= elapsedSeconds;
			if(timeToNextMove <= 0){
				timeToNextMove = moveClock;
				waypoints = merge(Pathfinding.routeTo(currentRoom.getNearestNode(position), currentRoom.getNearestNode(follow.getPosition()), currentRoom.getNodeGraph()));
//				System.out.println(this + " is moving! Path of length " + waypoints.size() + " generated.");
			}
			if(!waypoints.isEmpty() && waypoints.peek().getPosition().dis2(position) < 10)
				waypoints.remove();
			if(!waypoints.isEmpty()){
				setInternalVelocity(waypoints.peek().getPosition().sub(position).scaleTo(moveSpeed));
				setLookAt(position.add(internalVelocity));
			}else
				setInternalVelocity(new Vector(0,0));
		}
		
		super.update(elapsedSeconds);
	}
	
	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam) {
		// TODO Auto-generated method stub
		super.drawBody(g, elapsedSeconds, cam);
	}
}
