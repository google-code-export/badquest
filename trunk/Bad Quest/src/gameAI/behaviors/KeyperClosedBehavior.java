package gameAI.behaviors;

import gameAI.states.WanderState;
import gameObjects.Keyper;

public class KeyperClosedBehavior extends Behavior {
	Keyper host;

	public KeyperClosedBehavior(Keyper host){
		super(host);
		this.host = host;
	}
	
	@Override
	protected void init() {
		setCurrentState(new WanderState(this));
	}
	
	@Override
	public void update(double elapsedSeconds){
		setCurrentState(currentState.transition());
		currentState.update(elapsedSeconds);
	}
}
