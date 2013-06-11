package gameStates;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayDeque;

/**
 *  The workhorse of the app, this static class
 *  handles directing all of the input and
 *  client events to the appropriate place.
 *  It also handles updating and drawing the
 *  current state of the game.
 *  
 *  The states themselves are arranged in a stack,
 *  and each state can have a number of child states.
 *  In essence it is a stack of trees of states.
 *  
 *  See GameState.
 *
 */
public class GameStateManager {
	private static ArrayDeque<State> stateStack = new ArrayDeque<State>();
	
	private GameStateManager()
	{
	}
	
	public static State peekState()
	{
		synchronized (stateStack)
		{
			return stateStack.peek();
		}
	}
	
	public static void pushState(State gs)
	{
		synchronized(stateStack)
		{
			stateStack.push(gs);
		}
	}
	
	public static void replaceTopState(State gs)
	{
		synchronized(stateStack)
		{
			stateStack.pop();
			stateStack.push(gs);
		}
	}
	
	public static void popStateLevel()
	{
		synchronized(stateStack)
		{
			if(!stateStack.isEmpty())
				stateStack.pop();
		}
	}
	
	public static void update(double elapsedSeconds)
	{
		State top;
		
		synchronized(stateStack)
		{
			top = stateStack.peek();
		}
		
		if(top != null)
		{
			top.updateAll(elapsedSeconds);
	
			if(top.finished())
			{
				synchronized(stateStack)
				{
					stateStack.pop();
				}
			}
		}
	}
	
	public static void draw(Graphics2D g, double elapsedSeconds)
	{
		State top;
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		synchronized(stateStack)
		{
			top = stateStack.peek();
		}
		
		if(top != null)
			top.drawAll(g, elapsedSeconds);
	}
	
	//Mouse listener stuff
	public static void mouseDragged(MouseEvent e)
	{
		State top;
		
		synchronized(stateStack)
		{
			top = stateStack.peek();
		}
		
		if(top != null)
			top.mouseDraggedAll(e);
	}
	
	public static void mouseMoved(MouseEvent e)
	{
		State top;
		
		synchronized(stateStack)
		{
			top = stateStack.peek();
		}
		
		if(top != null)
			top.mouseMovedAll(e);
	}
	
	
	public static void mouseClicked(MouseEvent arg0) {
		State top;
		
		synchronized(stateStack)
		{
			top = stateStack.peek();
		}
		
		if(top != null)
			top.mouseClickedAll(arg0);
	}

	public static void mouseEntered(MouseEvent arg0) {
		State top;
		
		synchronized(stateStack)
		{
			top = stateStack.peek();
		}
		
		if(top != null)
			top.mouseEnteredAll(arg0);

	}

	public static void mouseExited(MouseEvent arg0) {
		State top;
		
		synchronized(stateStack)
		{
			top = stateStack.peek();
		}
		
		if(top != null)
			top.mouseExitedAll(arg0);

	}

	public static void mousePressed(MouseEvent arg0) {
		State top;
		
		synchronized(stateStack)
		{
			top = stateStack.peek();
		}
		
		if(top != null)
			top.mousePressedAll(arg0);
	}

	public static void mouseReleased(MouseEvent arg0) {
		State top;
		
		synchronized(stateStack)
		{
			top = stateStack.peek();
		}
		
		if(top != null)
			top.mouseReleasedAll(arg0);
	}
	
	//Key listener stuff
	public static void keyPressed(KeyEvent e) {
		
		State top;
		
		synchronized(stateStack)
		{
			top = stateStack.peek();
		}
		
		if(top != null)
			top.keyPressedAll(e);
	}

	public static void keyReleased(KeyEvent e) {
		State top;
		
		synchronized(stateStack)
		{
			top = stateStack.peek();
		}
		
		if(top != null)
			top.keyReleasedAll(e);	
	}

	public static void keyTyped(KeyEvent e) {
		
		State top;
		
		synchronized(stateStack)
		{
			top = stateStack.peek();
		}
		
		if(top != null)
			top.keyTypedAll(e);
	}
	
	public static class GSM_MouseMotionListener implements MouseMotionListener
	{
		@Override
		public void mouseDragged(MouseEvent arg0)
		{
			GameStateManager.mouseDragged(arg0);
		}

		@Override
		public void mouseMoved(MouseEvent arg0)
		{
			GameStateManager.mouseMoved(arg0);
		}
	}
	
	public static class GSM_MouseListener implements MouseListener 
	{		
		@Override
		public void mouseClicked(MouseEvent arg0) {
			GameStateManager.mouseClicked(arg0);
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			GameStateManager.mouseEntered(arg0);
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			GameStateManager.mouseExited(arg0);
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			GameStateManager.mousePressed(arg0);
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			GameStateManager.mouseReleased(arg0);
		}
	}
	
	public static class GSM_KeyListener implements KeyListener 
	{
		@Override
		public void keyPressed(KeyEvent e) {
			GameStateManager.keyPressed(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			GameStateManager.keyReleased(e);
		}

		@Override
		public void keyTyped(KeyEvent e) {
			GameStateManager.keyTyped(e);
		}
	}

}
