package gameAI.actions;

import java.util.ArrayDeque;

import util.Vector;
import world.tile.Tile;
import gameAI.Node;
import gameAI.Pathfinding;
import gameAI.behaviors.Behavior;
import gameObjects.interfaces.Ambulatory;

public class MoveTo extends Action{
	Vector target;
	ArrayDeque<Node> waypoints = new ArrayDeque<Node>();
	
	double margin = Tile.SIZE*1.25;
	double moveClock = .5;
	double timeToNextMove = Math.random()*moveClock;
	
	public MoveTo(Behavior parent, Vector target){
		super(parent);
		this.target = target;
	}
	
	@Override
	public Action transition(){
		if(parent.getPosition().dis2(target) <= margin*margin)
			return new Idle(parent);
		return this;
	}

	@Override
	public void update(double elapsedSeconds){
		timeToNextMove -= elapsedSeconds;
		
		if(Pathfinding.isPathClear(host, target)){
			waypoints.clear();
			waypoints.add(new Node(target, -1));
		}else if(timeToNextMove <= 0){
			timeToNextMove = moveClock;
			waypoints = Pathfinding.routeTo(parent.getPosition(), parent.getRoom().getNodesInTileRadius(parent.getPosition(),2.013), parent.getRoom().getNearestNode(target), parent.getRoom().getNodeGraph());
		}
		
		if(!waypoints.isEmpty() && waypoints.peek().getPosition().dis2(parent.getPosition()) < 10){
			waypoints.remove();
		}
		if(!waypoints.isEmpty()){
			setInternalVelocity(waypoints.peek().getPosition().sub(parent.getPosition()).scaleTo(((Ambulatory)host).getMaxSpeed()));
			host.setAngle(host.getInternalVelocity().ang());
//			host.setLookAt(parent.getPosition().add(parent.getInternalVelocity()));
		}else
			setInternalVelocity(Vector.ZERO);
	}
}
