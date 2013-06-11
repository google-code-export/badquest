package gameStates;

import gameAI.behaviors.KeyperClosedBehavior;
import gameAI.behaviors.KeyperOpenBehavior;
import gameObjects.Door;
import gameObjects.Keyper;
import gameObjects.Player;
import graphics.Camera;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.BitSet;

import util.Vector;
import world.Room;
import world.RoomManager;
import client.GameClient;

public class AIDemoState extends State{
	Camera cam;
	
	Room room = new Room(7, Vector.ZERO, 0);
	Player player = new Player("Rawnblade", 10, Vector.ZERO);
	Keyper open,closed;
	
	double acc = 255;
	double scale = 2.688;
	
	BitSet keys = new BitSet(), clicks = new BitSet();
	int mx = 0, my = 0;
	
	public AIDemoState(){
		for(int i = 0; i < 3; i++){
			Door a = new Door();
			Door b = new Door();
			Door c = new Door();
			Door d = new Door();
			
			room.addEntity(a);
			room.addEntity(b); b.interact();
			room.addEntity(c); c.interact();
			room.addEntity(d);
			
			a.bindToTile(room.getTileAt(4, i*3 + 7));
			b.bindToTile(room.getTileAt(16, i*3 + 7));
			c.bindToTile(room.getTileAt(i*3 + 7, 4));
			d.bindToTile(room.getTileAt(i*3 + 7, 16));
		}
		
		for(int i = 0; i < 2; i++){
			Door a = new Door();
			Door b = new Door();
			room.addEntity(a);
			room.addEntity(b);
			a.bindToTile(room.getTileAt(8+i*4,10));
			b.bindToTile(room.getTileAt(10,8+i*4));
			if(i == 0)
				a.interact();
			else
				b.interact();
		}
		
		open = new Keyper("OpenDoors",1);
		closed = new Keyper("ClosedDoors",0);
		room.addEntityAt(open, room.R/4, 3*room.C/4);
		room.addEntityAt(closed, 3*room.R/4, room.C/4);
		room.addEntityAt(player, room.R/4, room.C/4);
		
		RoomManager.setRoom(room.getRID());
		
		cam = new Camera(player.getPosition(), scale);
		cam.follow(player);
	}
	
	@Override
	protected void update(double elapsedSeconds){
		if(keys.get(KeyEvent.VK_Q))
			cam.setScale(cam.getScale()*9/10.);
		if(keys.get(KeyEvent.VK_E))
			cam.setScale(cam.getScale()*10/9.);
		cam.setScale(Math.min(Math.max(.1,cam.getScale()), 250));
		
		player.setSpeed(acc/cam.getScale());
		
		if(clicks.get(MouseEvent.BUTTON1) || clicks.get(MouseEvent.BUTTON3))
			player.setLookAt(cam.screenToWorld(new Vector(mx,my)));
		else if(player.getInternalVelocity().mag2() > 0)
			player.setLookAt(player.getPosition().add(player.getInternalVelocity()));
		
		player.receiveInput(keys, clicks);
		
		room.updateAll(elapsedSeconds);
		cam.update(elapsedSeconds);
	}

	@Override
	protected void draw(Graphics2D g, double elapsedSeconds){
		room.drawAll(g, elapsedSeconds, cam);
		cam.draw(g, elapsedSeconds);
		g.setColor(Color.white);
		g.drawString(String.format("%s: %s %s",open,open.getBrain().getCurrentAction().getClass().getSimpleName(),((KeyperOpenBehavior)open.getBrain()).target), GameClient.frameWidth/2-40, GameClient.frameHeight/2+160);
		g.drawString(String.format("%s: %s %s",closed,closed.getBrain().getCurrentAction().getClass().getSimpleName(),((KeyperClosedBehavior)closed.getBrain()).target), GameClient.frameWidth/2-40, GameClient.frameHeight/2+180);
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
	
	@Override
	protected void mouseMoved(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
		super.mouseMoved(e);
	}

	@Override
	protected void mouseDragged(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
		super.mouseDragged(e);
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
}
