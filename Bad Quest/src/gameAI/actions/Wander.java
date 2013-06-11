package gameAI.actions;

import gameAI.Node;
import gameAI.behaviors.Behavior;

import java.util.ArrayList;

import util.Vector;

public class Wander extends Action{
	private Vector goTo;
	private MoveTo currentWalk;
	private boolean done;
	
	public Wander(Behavior parent){
		super(parent);
		
		try{
			ArrayList<Node> potential = new ArrayList<Node>();
			potential.addAll(parent.getRoom().getNodesInTileRadius(host.getPosition(), 8));
			goTo = potential.get((int)(Math.random()*potential.size())).getPosition();
			currentWalk = new MoveTo(parent, goTo);
		}catch(Exception e){
			done = true;
		}
	}
	
	public Vector target(){
		return goTo;
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
