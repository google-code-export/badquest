package gameObjects;

import gameAI.Node;
import gameAI.Pathfinding;
import graphics.Camera;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
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
			
			if(position.dis2(follow.getPosition()) < Math.pow(2.5 * (radius+follow.getRadius())/2, 2))
				waypoints.clear();
			else if(Pathfinding.isPathClear(this, follow.getPosition())){
				waypoints.clear();
				waypoints.add(new Node(follow.getPosition(), -1));
			}else if(timeToNextMove <= 0){
				timeToNextMove = moveClock;
				waypoints = merge(Pathfinding.routeTo(currentRoom.getNearestNode(position), currentRoom.getNearestNode(follow.getPosition()), currentRoom.getNodeGraph()));
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
		AffineTransform prev = g.getTransform();
		
		super.drawBody(g, elapsedSeconds, cam);
		
		AffineTransform next = new AffineTransform();
		next.translate(cam.xTranslatePosition(position.x), cam.yTranslatePosition(position.y));
		next.scale(cam.xScale(), cam.yScale());
		g.setTransform(next);
		
		if(!waypoints.isEmpty()){
			g.setColor(Color.cyan);
			g.drawLine(0, 0, (int)(waypoints.peek().getPosition().x-position.x), (int)(waypoints.peek().getPosition().y-position.y));
		}
		
		g.setTransform(prev);
	}
}
