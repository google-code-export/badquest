package gameAI.actions;

import gameAI.Node;
import gameAI.Pathfinding;
import gameAI.behaviors.Behavior;
import gameObjects.interfaces.Ambulatory;

import java.util.ArrayDeque;

import util.Vector;
import world.tile.Tile;

public class MoveTo extends Action{
	Vector target;
	ArrayDeque<Node> waypoints = new ArrayDeque<Node>();
	
	double margin = Tile.SIZE*1.25;
	double moveClock = .5;
	double timeToNextMove = Math.random()*moveClock;
	double skipAhead = 4;
	boolean canSkip = false;
	boolean canReach = true;
	
	public MoveTo(Behavior parent, Vector target){
		super(parent);
		this.target = target;
	}
	
	@Override
	public Action transition(){
		if(parent.getPosition().dis2(target) <= margin*margin || !canReach)
			return new Idle(parent);
		return this;
	}
	
	public ArrayDeque<Node> getWaypoints(){
		return waypoints;
	}

	@Override
	public void update(double elapsedSeconds){
		timeToNextMove -= elapsedSeconds;
		
//		if(parent instanceof KeyperClosedBehavior)
//		System.out.println(host + " " + waypoints + " " + host.getInternalVelocity());
		
		if(Pathfinding.isPathClear(host, target)){
			waypoints.clear();
			waypoints.add(new Node(target, -1));
		}else if(timeToNextMove <= 0){
			timeToNextMove = moveClock;
//			waypoints = Pathfinding.routeTo(parent.getPosition(), parent.getRoom().getNodesInTileRadius(parent.getPosition(),2.013), parent.getRoom().getNearestNode(target), parent.getRoom().getNodeGraph());
//			System.out.println(host + " " + parent.getRoom().getAllNodesInTileRadius(target,2.013));
			waypoints = Pathfinding.routeTo(parent.getPosition(), parent.getRoom().getNodesInTileRadius(parent.getPosition(),2.013), parent.getRoom().getAllNodesInTileRadius(target,1.013), parent.getRoom().getNodeGraph());
			canReach &= !waypoints.isEmpty();
		}
		
		if(!waypoints.isEmpty() && canSkip){
			int cnt = 0;
			Node next = null;
			while(waypoints.size() > 1 && Pathfinding.isPathClear(host, waypoints.peek().getPosition()) && cnt++ < skipAhead)
				next = waypoints.remove();
			if(next != null)
				waypoints.addFirst(next);
			canSkip = false;
		}
		
		if(!waypoints.isEmpty() && waypoints.peek().getPosition().dis2(parent.getPosition()) < 10){
			waypoints.remove();
			canSkip = true;
		}
		if(!waypoints.isEmpty()){
			setInternalVelocity(waypoints.peek().getPosition().sub(parent.getPosition()).scaleTo(((Ambulatory)host).getMaxSpeed()));
			host.setLookAt(parent.getPosition().add(parent.getInternalVelocity()));
		}else
			setInternalVelocity(Vector.ZERO);
	}
}
