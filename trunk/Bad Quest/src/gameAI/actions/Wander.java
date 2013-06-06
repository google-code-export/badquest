package gameAI.actions;

import gameAI.Node;
import gameAI.behaviors.Behavior;

import java.util.ArrayList;

public class Wander extends Action{
	private MoveTo currentWalk;
	private boolean done;
	
	public Wander(Behavior parent){
		super(parent);
		
		try{
			ArrayList<Node> potential = new ArrayList<Node>();
			potential.addAll(parent.getRoom().getNodesInTileRing(host.getPosition(), 10, 20));		
			currentWalk = new MoveTo(parent, potential.get((int)(Math.random()*potential.size())).getPosition());
		}catch(Exception e){
			done = true;
		}
	}
	
	@Override
	public Action transition(){
		if(done)
			return new Idle(parent, 2, new Wander(parent));
		return this;
	}

	@Override
	public void update(double elapsedSeconds){
		if(!done){
			currentWalk.update(elapsedSeconds);
			done |= currentWalk.transition() instanceof Idle;
		}
	}
}
