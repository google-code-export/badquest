package gameAI.states;

import util.Vector;
import gameAI.behaviors.Behavior;

public class IdleState extends State{
	private State next;
	private double timeLeft = -1;
	public IdleState(Behavior parent){
		super(parent);
	}
	
	/**
	 * Delayed state transition, actor will remain motionless until waitTime is up.
	 * @param parent
	 * @param waitTime
	 * @param next
	 */
	public IdleState(Behavior parent, double waitTime, State next){
		super(parent);
		timeLeft = waitTime;
		this.next = next;
	}
	
	@Override
	public State transition(){
		if(timeLeft <= 0 && next != null)
			return next;
		return this;
	}

	@Override
	public void update(double elapsedSeconds){
		if(timeLeft > 0)
			timeLeft -= elapsedSeconds;
		host.setInternalVelocity(Vector.ZERO);
	}
}
