package gameAI.states;

import util.Vector;
import gameAI.behaviors.Behavior;

public class WanderState extends State{
	private double direction;
	private double timer;
	public WanderState(Behavior parent){
		super(parent);
		direction = Math.random() * 2*Math.PI;
		timer = Math.random() * 1 + 1;
	}
	
	@Override
	public State transition(){
		if(timer < 0)
			return new IdleState(parent, 2, new WanderState(parent));
		return this;
	}

	@Override
	public void update(double elapsedSeconds){
		timer -= elapsedSeconds;
		setAngle(direction);
		setInternalVelocity(new Vector(direction).scaleTo(50));
	}
}
