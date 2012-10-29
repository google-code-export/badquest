package gameStates;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.BitSet;

import client.Camera;

/**
 * Level editor, allows a user to load and save a level from or to file.
 * @author Mike
 */
public class LevelEditorState extends State {
	private Camera cam;
	
	private BitSet keys; //Set of active keyboard presses
	private BitSet clicks; //Set of active mouse presses
	
	private int mx = 0, my = 0;
	
	@Override
	protected void draw(Graphics2D g, double elapsedSeconds) {
		
	}

	@Override
	protected void update(double elapsedSeconds) {
		
	}
	
	@Override
	protected void mousePressed(MouseEvent e) {
		clicks.set(e.getButton());
		super.mousePressed(e);
	}
	
	@Override
	protected void mouseReleased(MouseEvent e) {
		clicks.clear(e.getButton());
		super.mouseReleased(e);
	}
	
	@Override
	protected void mouseMoved(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
		super.mouseMoved(e);
	}
	
	@Override
	protected void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		super.mouseDragged(e);
	}
	
	@Override
	protected void keyPressed(KeyEvent e) {
		keys.set(e.getKeyCode());
		super.keyPressed(e);
	}
	
	@Override
	protected void keyReleased(KeyEvent e) {
		keys.clear(e.getKeyCode());
		super.keyReleased(e);
	}
}
