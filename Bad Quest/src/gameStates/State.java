package gameStates;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

/** 
 * Abstraction of a state of the game.
 * Must be able to be drawn, updated, 
 * and handle input.
 */
public abstract class State {
	private boolean isDone = false;
	
	private LinkedList<State> children = new LinkedList<State>();
	
	protected State(){
	}
	
	protected final void addChildState(State s){
		synchronized(children){
			children.add(s);
		}
	}
	
	protected final boolean removeChildState(State s){
		synchronized(children){
			return children.remove(s);
		}
	}
	
	protected final void markFinished(){
		isDone = true;
	}
	
	protected final boolean finished(){
		return isDone;
	}
	
	/**
	 * Draws this state followed by each of its children.
	 * @param g
	 * @param elapsedSeconds
	 */
	protected final void drawAll(Graphics2D g, double elapsedSeconds){
		draw(g, elapsedSeconds);
		synchronized(children){
			for(State s:children)
				s.drawAll(g, elapsedSeconds);
		}
	}
	
	/**
	 * Updates this state followed by each of its children.
	 * @param elapsedSeconds
	 */
	protected final void updateAll(double elapsedSeconds){
		update(elapsedSeconds);
		synchronized(children){
			LinkedList<State> next = new LinkedList<State>();
			while(!children.isEmpty()){
				State cur = children.remove();
				cur.updateAll(elapsedSeconds);
				if(cur.finished())
					continue;
				next.add(cur);
			}
			children = next;
		}
	}
	
	public final void mouseClickedAll(MouseEvent mouseEvent){
		synchronized(children){
			for(State c : children)
				if(!mouseEvent.isConsumed())
					c.mouseClickedAll(mouseEvent);
				else 
					return;
			
			if(!mouseEvent.isConsumed())
				mouseClicked(mouseEvent);
		}
	}
	
	//Mouse events
	protected void mouseDragged(MouseEvent e){	
	}

	protected final void mouseDraggedAll(MouseEvent e){
		synchronized(children){
			for(State c : children)
				if(!e.isConsumed())
					c.mouseDraggedAll(e);
				else 
					return;
			
			if(!e.isConsumed())
				mouseDragged(e);
		}
	}
	
	protected void mouseMoved(MouseEvent e){	
	}

	protected final void mouseMovedAll(MouseEvent e){
		synchronized(children){
			for(State c : children)
				if(!e.isConsumed())
					c.mouseMovedAll(e);
				else 
					return;
			
			if(!e.isConsumed())
				mouseMoved(e);
		}
	}
	
	
	protected void mouseClicked(MouseEvent e){	
	}

	protected final void mouseEnteredAll(MouseEvent mouseEvent){
		synchronized(children){
			for(State c : children)
				if(!mouseEvent.isConsumed())
					c.mouseEnteredAll(mouseEvent);
				else 
					return;
			
			if(!mouseEvent.isConsumed())
				mouseEntered(mouseEvent);
		}
	}
	
	protected void mouseEntered(MouseEvent e){
	}

	protected final void mouseExitedAll(MouseEvent mouseEvent) {
		synchronized(children){
			for(State c : children)
				if(!mouseEvent.isConsumed())
					c.mouseExitedAll(mouseEvent);
				else
					break;
			
			if(!mouseEvent.isConsumed())
				mouseExited(mouseEvent);
		}
	}
	
	protected void mouseExited(MouseEvent e){
	}

	protected final void mousePressedAll(MouseEvent mouseEvent) {
		synchronized(children)
		{
			for(State c : children)
				if(!mouseEvent.isConsumed())
					c.mousePressedAll(mouseEvent);
				else
					break;
			
			if(!mouseEvent.isConsumed())
				mousePressed(mouseEvent);
		}	
	}
	
	protected void mousePressed(MouseEvent e){
	}

	protected final void mouseReleasedAll(MouseEvent mouseEvent){
		synchronized(children){
			for(State c : children)
				if(!mouseEvent.isConsumed())
					c.mouseReleasedAll(mouseEvent);
				else
					break;
			
			if(!mouseEvent.isConsumed())
				mouseReleased(mouseEvent);
		}
	}
	
	protected void mouseReleased(MouseEvent e){
	}
	
	//Key events
	protected final void keyPressedAll(KeyEvent e){				
		synchronized(children){
			for(State c : children)
				if(!e.isConsumed())
					c.keyPressedAll(e);
				else
					return;
			if(!e.isConsumed())
				keyPressed(e);
		}
	}
	
	protected void keyPressed(KeyEvent e){		
	}
	
	protected final void keyReleasedAll(KeyEvent e){		
		synchronized(children){
			for(State c : children)
				if(!e.isConsumed())
					c.keyReleasedAll(e);
				else
					return;
			if(!e.isConsumed())
				keyReleased(e);
		}
	}
	
	protected void keyReleased(KeyEvent e){		
	}
	
	protected final void keyTypedAll(KeyEvent e){		
		synchronized(children){
			for(State c : children)
				if(!e.isConsumed())
					c.keyTyped(e);
				else
					return;
			if(!e.isConsumed())
				keyTyped(e);
		}
	}
	
	protected void keyTyped(KeyEvent e){		
	}
	
	protected abstract void draw(Graphics2D g, double elapsedSeconds);
	protected abstract void update(double elapsedSeconds);
}
