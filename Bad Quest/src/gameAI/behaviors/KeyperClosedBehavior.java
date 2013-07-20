package gameAI.behaviors;

import gameAI.actions.Idle;
import gameAI.actions.Interact;
import gameAI.actions.MoveTo;
import gameAI.actions.Wander;
import gameObjects.Door;
import gameObjects.DrawableObject;
import gameObjects.Keyper;

import java.util.ArrayDeque;

import util.Vector;
import world.Room;
import world.tile.Tile;

public class KeyperClosedBehavior extends Behavior{
	Keyper host;
	double visionRadius = 80;
	double fov = Math.toRadians(135);
	
	public Door target;
	
	public KeyperClosedBehavior(Keyper host){
		super(host);
		this.host = host;
	}
	
	@Override
	protected void init(){
		setCurrentAction(new Wander(this));
	}
	
	@Override
	public void checkTransitions(double elapsedSeconds){
		setCurrentAction(currentAction.transition());
		
		Room room = getRoom();
		Vector view = new Vector(visionRadius,0);
		view = view.rot(getAngle()-fov/2);
		ArrayDeque<DrawableObject> inSight = room.getEntitiesIntersectingArc(getPosition(), view.add(getPosition()), fov);
		
		if(currentAction instanceof MoveTo){
			if(target == null || !target.isOpen()){
				target = null;
				setCurrentAction(new Wander(this));
			}
		}else if(currentAction instanceof Wander){
			for(DrawableObject d:inSight)
				if(d instanceof Door){
					Door door = (Door)d;
					if(door.isOpen()){
						target = door;
						break;
					}
				}
			
			if(target != null)
				setCurrentAction(new MoveTo(this, target.getPosition()));
		}else if(currentAction instanceof Idle){
			if(target == null || !target.isOpen()){
				target = null;
				if(!((Idle)currentAction).hasNext())
					setCurrentAction(new Wander(this));
			}else if(target != null && target.getPosition().dis2(getPosition()) > 2*Tile.SIZE*Tile.SIZE){
				setCurrentAction(new MoveTo(this, target.getPosition()));
			}else if(target != null){
				setCurrentAction(new Interact(this, target));
			}
		}else if(currentAction instanceof Interact){
			if(!target.isOpen()){
				target = null;
				setCurrentAction(new Wander(this));
			}else if(target.getPosition().dis2(getPosition()) > 2.5*Tile.SIZE*Tile.SIZE)
				setCurrentAction(new MoveTo(this, target.getPosition()));
		}
	}
	
	@Override
	public void update(double elapsedSeconds){
		currentAction.update(elapsedSeconds);
		checkTransitions(elapsedSeconds);
	}
}
