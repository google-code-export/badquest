package gameAI.actions;

import util.Vector;
import gameAI.behaviors.Behavior;

public class Idle extends Action{
	private Action next;
	private double timeSpentIdle = 0, timeToWait = -1;
	public Idle(Behavior parent){
		super(parent);
	}
	
	/**
	 * Delayed state transition, actor will remain motionless until waitTime is up.
	 * @param parent
	 * @param waitTime
	 * @param next
	 */
	public Idle(Behavior parent, double waitTime, Action next){
		super(parent);
		timeToWait = waitTime;
		this.next = next;
	}
	
	public double getTimeSpentIdle(){
		return timeSpentIdle;
	}
	
	@Override
	public Action transition(){
		if(timeSpentIdle >= timeToWait && next != null)
			return next;
		return this;
	}

	@Override
	public void update(double elapsedSeconds){
		timeSpentIdle += elapsedSeconds;
		host.setInternalVelocity(Vector.ZERO);
	}
}
