package gameAI.behaviors;

import gameAI.Pathfinding;
import gameAI.actions.Action;
import gameAI.actions.Follow;
import gameAI.actions.Idle;
import gameAI.actions.Wander;
import gameObjects.Actor;
import gameObjects.DrawableObject;
import gameObjects.Player;
import gameObjects.interfaces.Damageable;

import java.util.ArrayDeque;

import util.Vector;
import world.Room;
import world.tile.Tile;

public class FollowerBehavior extends Behavior{
	public DrawableObject follow = null;
	
	double followRange = 250;
	double visionRadius = 100;
	double fov = Math.toRadians(135);
	
	public FollowerBehavior(Actor host){
		super(host);
	}
	
	public double getInterestLevel(){
		if(follow == null)
			return 0.;
		return 1 - follow.getPosition().dis(getPosition()) / followRange;
	}
	
	@Override
	protected void init(){
		setCurrentAction(new Wander(this));
	}
	
	@Override
	public void setCurrentAction(Action nextAction){
		super.setCurrentAction(nextAction);
	}

	@Override
	public void checkTransitions(double elapsedSeconds){
		if(follow != null && follow.isDead()){
			follow = null;
			setCurrentAction(new Wander(this));
		}
		
		setCurrentAction(currentAction.transition());
		
		Room room = getRoom();
		Vector view = new Vector(visionRadius,0);
		view = view.rot(getAngle()-fov/2);
		ArrayDeque<DrawableObject> inSight = room.getEntitiesIntersectingArc(getPosition(), view.add(getPosition()), fov);
		
		if(currentAction instanceof Idle){
			if(follow == null){
				for(DrawableObject d : inSight)
					if(d.getOID() != host.getOID() && d instanceof Player && ((Damageable)d).isDamageable(host.getFaction())){
						follow = d;
						setCurrentAction(new Follow(this, follow));
					}
			}else if(follow.getPosition().dis2(getPosition()) > 2.5*Tile.SIZE*Tile.SIZE){
				if(Pathfinding.routeTo(getPosition(), getRoom().getNodesInTileRadius(getPosition(),2.013), getRoom().getAllNodesInTileRadius(follow.getPosition(),1.013), getRoom().getNodeGraph()).isEmpty()){
					follow = null;
					setCurrentAction(new Idle(this,1,new Wander(this)));
				}else
					setCurrentAction(new Follow(this, follow));
			}
		}else if(currentAction instanceof Wander){
			if(follow == null){
				for(DrawableObject d : inSight)
					if(d.getOID() != host.getOID() && d instanceof Player && ((Damageable)d).isDamageable(host.getFaction())){
						follow = d;
						setCurrentAction(new Follow(this, follow));
					}
			}
		}else if(currentAction instanceof Follow){
			if(follow.getPosition().dis2(getPosition()) > followRange*followRange){
				follow = null;
				setCurrentAction(new Wander(this));
			}
		}
	}

	@Override
	public void update(double elapsedSeconds){
		currentAction.update(elapsedSeconds);
		checkTransitions(elapsedSeconds);
	}
}
