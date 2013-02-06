package gameObjects;

import gameAI.Node;
import gameAI.Pathfinding;
import graphics.Camera;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayDeque;

import util.Vector;

public class DebugEnemy extends Actor {
	ArrayDeque<Node> waypoints;
	
	double moveSpeed = 75;
	double moveClock = .5;
	double timeToNextMove = Math.random()*moveClock;
	double skipAhead = 4;
	boolean canSkip = false;
	
	DrawableObject follow; //The target this object will pathfind to
	
	public DebugEnemy(int r){
		super("Pathfinder", r);
		waypoints = new ArrayDeque<Node>();
	}
	
	public void setFollow(DrawableObject d){
		follow = d;
	}
	
	@Override
	public void update(double elapsedSeconds) {
		if(follow != null && follow.isDead())
			follow = null;
		
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
				waypoints = Pathfinding.routeTo(position, currentRoom.getNodesInTileRadius(position,2.013), currentRoom.getNearestNode(follow.getPosition()), currentRoom.getNodeGraph());
				canSkip = true;
			}
			
			if(!waypoints.isEmpty() && canSkip){
				int cnt = 0;
				Node next = null;
				while(waypoints.size() > 1 && Pathfinding.isPathClear(this, waypoints.peek().getPosition()) && cnt++ < skipAhead)
					next = waypoints.remove();
				if(next != null)
					waypoints.addFirst(next);
				canSkip = false;
			}
			
			if(!waypoints.isEmpty() && waypoints.peek().getPosition().dis2(position) < 10){
				waypoints.remove();
				canSkip = true;
			}
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
//			g.setColor(Color.cyan);
//			g.drawLine(0, 0, (int)(waypoints.peek().getPosition().x-position.x), (int)(waypoints.peek().getPosition().y-position.y));
//			for(Node w:waypoints)
//				g.drawOval((int)(w.getPosition().x-position.x), (int)(w.getPosition().y-position.y),5,5);
		}
		
		g.setTransform(prev);
	}
}
