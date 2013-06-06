package gameAI.behaviors;

import java.util.ArrayDeque;

import util.Vector;
import world.Room;
import world.tile.Tile;
import gameAI.actions.*;
import gameObjects.*;

public class KeyperOpenBehavior extends Behavior{
	Keyper host;
	double visionRadius = 80;
	double fov = Math.toRadians(135);
	
	Door target;
	public KeyperOpenBehavior(Keyper host){
		super(host);
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
			if(target == null || target.isOpen()){
				target = null;
				setCurrentAction(new Wander(this));
			}
		}else if(currentAction instanceof Wander){
			for(DrawableObject d:inSight)
				if(d instanceof Door){
					Door door = (Door)d;
					if(!door.isOpen()){
						target = door;
						break;
					}
				}
			
			if(target != null)
				setCurrentAction(new MoveTo(this, target.getPosition()));
		}else if(currentAction instanceof Idle){
			if(target != null && target.getPosition().dis2(getPosition()) > 2.5*Tile.SIZE*Tile.SIZE){
				setCurrentAction(new MoveTo(this, target.getPosition()));
			}else if(target != null && target.isOpen()){
				setCurrentAction(new Wander(this));
			}else if(target != null){
				setCurrentAction(new Interact(this, target));
			}
		}else if(currentAction instanceof Interact){
			if(target.getPosition().dis2(getPosition()) > 2.5*Tile.SIZE*Tile.SIZE)
				setCurrentAction(new MoveTo(this, target.getPosition()));
		}
	}

	@Override
	public void update(double elapsedSeconds){
		currentAction.update(elapsedSeconds);
		checkTransitions(elapsedSeconds);
	}
}
