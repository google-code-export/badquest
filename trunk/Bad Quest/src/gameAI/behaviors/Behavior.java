package gameAI.behaviors;

import util.Vector;
import world.Room;
import gameAI.actions.Action;
import gameAI.actions.Idle;
import gameObjects.Actor;

public abstract class Behavior{
	protected Actor host;
	protected Action currentAction;
	
	public Behavior(Actor host){
		this.host = host;
		init();
	}
	
	//Actor getters
	public Vector getPosition(){
		return host.getPosition();
	}
	
	public double getAngle(){
		return host.getAngle();
	}
	
	public Vector getInternalVelocity(){
		return host.getInternalVelocity();
	}
	
	public Vector getExternalVelocity(){
		return host.getExternalVelocity();
	}
	
	public Vector getVelocity(){
		return host.getVelocity();
	}
	
	public Room getRoom(){
		return host.getCurrentRoom();
	}
	
	//Actor setters
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
	
	public Action getCurrentAction(){
		return currentAction;
	}
	
	public void setCurrentAction(Action nextAction){
		currentAction = nextAction;
		if(currentAction == null)
			currentAction = new Idle(this);
	}
	
	protected abstract void init();
	public abstract void checkTransitions(double elapsedSeconds);
	public abstract void update(double elapsedSeconds);
}
