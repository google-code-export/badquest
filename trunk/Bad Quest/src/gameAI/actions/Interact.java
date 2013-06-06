package gameAI.actions;

import gameAI.behaviors.Behavior;
import gameObjects.DrawableObject;
import gameObjects.interfaces.Interactive;

public class Interact extends Action{
	Idle wait;
	Interactive target;
	boolean done;
	public Interact(Behavior parent, Interactive target){
		super(parent);
		this.target = target;
		wait = new Idle(parent);
	}

	@Override
	public Action transition(){
		if(done)
			return new Idle(parent);
		return this;
	}

	@Override
	public void update(double elapsedSeconds){
		host.setAngle(((DrawableObject)target).getPosition().sub(parent.getPosition()).ang());
		wait.update(elapsedSeconds);
		if(wait.getTimeSpentIdle() > 1.5){
			done = true;
			target.interact();
		}
	}
}
