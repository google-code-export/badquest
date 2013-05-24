package gameAI.states;

import util.Vector;
import gameAI.behaviors.Behavior;
import gameObjects.Actor;

public abstract class State {
	protected Behavior parent;
	protected Actor host;
	public State(Behavior parent){
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
	public abstract State transition();
	public abstract void update(double elapsedSeconds);
}
