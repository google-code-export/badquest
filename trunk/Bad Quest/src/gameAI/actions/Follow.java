package gameAI.actions;

import gameAI.behaviors.Behavior;
import gameObjects.DrawableObject;

public class Follow extends Action{
	DrawableObject target;
	
	MoveTo currentMove;
	double moveClock = .5;
	double timeToNextMove = 0;
	
	public Follow(Behavior parent, DrawableObject target){
		super(parent);
		this.target = target;
		timeToNextMove = moveClock;
		currentMove = new MoveTo(parent, target.getPosition());
	}
	
	@Override
	public Action transition(){
		if(currentMove.transition() instanceof Idle)
			return currentMove.transition();
		return this;
	}

	@Override
	public void update(double elapsedSeconds){
		timeToNextMove -= elapsedSeconds;
		if(timeToNextMove <= 0){
			timeToNextMove = moveClock;
			currentMove = new MoveTo(parent, target.getPosition());
		}
		currentMove.update(elapsedSeconds);
	}
}
