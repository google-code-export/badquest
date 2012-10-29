package gameStates;

import gameObjects.Actor;
import gameObjects.DrawableObject;
import gameObjects.Player;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.BitSet;
import java.util.LinkedList;

import util.Vector;
import world.Room;
import client.Camera;
import client.GameClient;

public class DebugState extends State{
	Room room = new Room(20,30);
	Actor[] actors;
	LinkedList<DrawableObject> drawList; 
	Camera cam = new Camera(new Vector(0,0));
	
	int[] dx = new int[]{0,0,-1,1};
	int[] dy = new int[]{-1,1,0,0};
	BitSet keys; //Set of active keyboard presses
	double acc = 225;
	double scale = 1;
	
	int activeActor = 0;
	int mx = 0, my = 0;
	
	public DebugState(){
		keys = new BitSet();
		drawList = new LinkedList<DrawableObject>();
		
		actors = new Actor[]{new Player("Rawnblade", 10, new Vector(200,200)), 
							 new Actor("Rusty Stranglechain", 10, new Vector(120,100)),
							 new Actor("Gunther Boneguzzler", 10, new Vector(40,100)),
							 new Actor("Pork Undertow", 10, new Vector(60,100)),
							 new Actor("Kurt Lioncrusher", 10, new Vector(80,100)),
							 new Actor("Horace Elbowdrum", 10, new Vector(100,100))};
//		addChildState(new EditorOverlayState());
		for(Actor a:actors)
			drawList.add(a);
	}
	
	public void setCameraFollow(DrawableObject focus){
		cam.follow(focus);
	}
	
	public DrawableObject getCameraFollow(){
		return cam.getFocus();
	}
	
	/**
	 * Returns the player's velocity based on user input
	 * @return
	 */
	public Vector getPlayerVel(){
		Vector ret = new Vector(0,0);
		if(keys.get(KeyEvent.VK_W))
			Vector.add(ret, (new Vector(0,-acc)));
		if(keys.get(KeyEvent.VK_S))
			Vector.add(ret, (new Vector(0,acc)));
		if(keys.get(KeyEvent.VK_A))
			Vector.add(ret, (new Vector(-acc,0)));
		if(keys.get(KeyEvent.VK_D))
			Vector.add(ret, (new Vector(acc,0)));
		return ret;
	}

	//**************
	//State stuff
	//**************
	
	protected void update(double elapsedSeconds){
		Vector velocity = getPlayerVel();
		if(keys.get(KeyEvent.VK_Q))
			scale = scale*9/10.;
		if(keys.get(KeyEvent.VK_E))
			scale = scale*10/9.;
		
		for(Actor a:actors)
			a.setVelocity(new Vector(0,0));
		
//		System.out.println(activeActor);
		if(activeActor > -1){
			if(activeActor == 0)
				((Player)actors[0]).setAngle(Math.atan2(my-cam.worldToScreen(actors[0].getPosition()).y,mx-cam.worldToScreen(actors[0].getPosition()).x));
			actors[activeActor].setVelocity(velocity.scale(1/scale));
			room.collideWithSolids(actors[activeActor], elapsedSeconds);
			cam.follow(actors[activeActor]);
		}else{
			cam.follow(null);
		}
		
		for(DrawableObject d:drawList)
			d.update(elapsedSeconds);
		
		cam.setScale(scale);
		cam.update(elapsedSeconds);
	}
	
	protected void draw(Graphics2D g, double elapsedSeconds){
		AffineTransform prev = g.getTransform();
		
		room.drawAll(g, elapsedSeconds, cam);
		
		for(DrawableObject d:drawList)
			d.drawBody(g, elapsedSeconds, cam);
		
		g.setColor(Color.WHITE);
		g.setStroke(new BasicStroke(2.f));
		g.drawLine(GameClient.frameWidth/2, GameClient.frameHeight/2-20, GameClient.frameWidth/2, GameClient.frameHeight/2+20);
		g.drawLine(GameClient.frameWidth/2-20, GameClient.frameHeight/2, GameClient.frameWidth/2+20, GameClient.frameHeight/2);
		
		g.drawRect(GameClient.frameWidth/2-150, 30, 300, 40);
		FontMetrics f = g.getFontMetrics();
		g.drawString("Following", GameClient.frameWidth/2-(int)(f.getStringBounds("Following", g).getWidth()/2), 45);
		if(cam.getFocus() != null){
			if(cam.getFocus() instanceof Actor)
				g.drawString(((Actor)(cam.getFocus())).getName(), GameClient.frameWidth/2-(int)(f.getStringBounds(((Actor)(cam.getFocus())).getName(), g).getWidth()/2), 62);
			g.drawString(String.format("(%.4f, %.4f)", cam.getFocus().getPosition().x, cam.getFocus().getPosition().y), GameClient.frameWidth/2+5, GameClient.frameHeight/2-7);
			g.drawString(String.format("(%.4f, %.4f)", cam.getFocus().getVelocity().x, cam.getFocus().getVelocity().y), GameClient.frameWidth/2+5, GameClient.frameHeight/2+15);
		}
		
		g.setTransform(prev);
	}
	
	@Override
	protected void keyPressed(KeyEvent e) {
		keys.set(e.getKeyCode());
		if(e.getKeyCode() == KeyEvent.VK_0){
			activeActor = -1;
		}else if(e.getKeyCode() > KeyEvent.VK_0 && e.getKeyCode() < KeyEvent.VK_1+actors.length){
			activeActor = e.getKeyCode()-KeyEvent.VK_1;
		}
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
}
