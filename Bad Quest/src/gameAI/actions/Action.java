package gameAI.actions;

import util.Vector;
import gameAI.behaviors.Behavior;
import gameObjects.Actor;

public abstract class Action {
	protected Behavior parent;
	protected Actor host;
	public Action(Behavior parent){
		this.parent = parent;
		this.host = parent.getHost();
	}
	
	protected void setAngle(double angle){
		parent.setAngle(angle);
	}
	
	protected void setInternalVelocity(Vector v){
		parent.setInternalVelocity(v);
	}
	
	/**
	 * Called before update, checks conditions to determine whether the parent behavior should transition to a new State
	 * @return
	 */
	public abstract Action transition();
	public abstract void update(double elapsedSeconds);
}
