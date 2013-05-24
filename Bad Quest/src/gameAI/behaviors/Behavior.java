package gameAI.behaviors;

import util.Vector;
import world.Room;
import gameAI.states.IdleState;
import gameAI.states.State;
import gameObjects.Actor;

public abstract class Behavior{
	protected Actor host;
	protected State currentState;
	
	public Behavior(Actor host){
		this.host = host;
		init();
	}
	
	//Actor modifiers
	public void setAngle(double angle){
		host.setAngle(angle);
	}
	
	public void setInternalVelocity(Vector v){
		host.setInternalVelocity(v);
	}	
	
	//Behavior stuff
	public Actor getHost(){
		return host;
	}
	
	public Room getRoom(){
		if(host == null)
			return null;
		return host.getCurrentRoom();
	}
	
	public State getCurrentState(){
		return currentState;
	}
	
	public void setCurrentState(State nextState){
		currentState = nextState;
		if(currentState == null)
			currentState = new IdleState(this);
	}
	
	protected abstract void init();
	public abstract void update(double elapsedSeconds);
}
