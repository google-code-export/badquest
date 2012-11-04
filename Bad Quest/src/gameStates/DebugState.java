package gameStates;

import gameObjects.Actor;
import gameObjects.DrawableObject;
import gameObjects.Player;
import gameObjects.Portal;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.TreeMap;

import util.Vector;
import world.Room;
import world.RoomManager;
import world.Tile;
import client.Camera;
import client.GameClient;

public class DebugState extends State{
	Room room = new Room(0,new Vector(400,0));
	Room background = new Room(50,20);
	Room backbackground = new Room(20,20);
	
	Actor[] actors;
	TreeMap<Integer, DrawableObject> drawList; 
	Camera cam = new Camera(new Vector(0,0));
	
	int[] dx = new int[]{0,0,-1,1};
	int[] dy = new int[]{-1,1,0,0};
	BitSet keys; //Set of active keyboard presses
	double acc = 225;
	double scale = 1;
	double layerSpacing = .75;
	
	int activeActor = 0;
	int mx = 0, my = 0;
	
	public DebugState(){
		keys = new BitSet();
		
		Portal A = new Portal(room, new Vector(500,40));
		Portal B = new Portal(background, new Vector(50,200));
		Portal C = new Portal(background, new Vector(50,500));
		Portal D = new Portal(backbackground, new Vector(50,200));
		
		A.linkAndSetActive(B);
		C.linkAndSetActive(D);
		
		room.addEntityAt(new Portal(room, new Vector(0)), new Vector(400,100));
		room.addEntity(A);
		background.addEntity(B);
		background.addEntity(C);
		backbackground.addEntity(D);
		
		actors = new Actor[]{new Player("Rawnblade", 10, new Vector(200,200)), 
							 new Actor("Rusty Stranglechain", 10, new Vector(120,100)),
							 new Actor("Gunther Boneguzzler", 10, new Vector(40,100)),
							 new Actor("Pork Undertow", 10, new Vector(60,100)),
							 new Actor("Kurt Lioncrusher", 10, new Vector(80,100)),
							 new Actor("Horace Elbowdrum", 10, new Vector(100,100))};
		
		RoomManager.setRoom(room.getRID());
		
		background.addEntityAt(new Actor("Big McLargehuge", 4), new Vector(Tile.SIZE*10, Tile.SIZE*15));
		
		int x = 41;
		for(Actor a:actors)
			room.addEntityAt(a,new Vector((x=x+21)-21, 4*Tile.SIZE));
		
		drawList = room.getEntityMap();
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
			actors[activeActor].setVelocity(velocity.scale(1/scale));
			cam.follow(actors[activeActor]);
		}else{
			cam.follow(null);
		}
		
		synchronized(drawList){
			for(Integer oid:drawList.keySet())
				if(drawList.get(oid).isSolid())
					room.collideWithSolids(drawList.get(oid), elapsedSeconds);
			
			for(Integer oid:drawList.keySet())
				drawList.get(oid).update(elapsedSeconds);
		}
		
		Portal transfer = null;
		Actor move = null;
		synchronized(drawList){
			for(Integer oid:drawList.keySet()){
				if(drawList.get(oid) instanceof Actor)
				for(Integer pid:drawList.keySet()){
					if(drawList.get(pid) instanceof Portal){
						Portal p = (Portal)drawList.get(pid);
						Actor a = (Actor)drawList.get(oid);
						if(p.getState() == Portal.State.ACTIVE && p.getPosition().dis2(a.getPosition()) <= p.getRadius()*p.getRadius()){
							transfer = p;
							move = a;
						}
					}
				}
			}
		}
		
		if(transfer != null){
			Room next = transfer.getExitRoom();
			Vector pos = transfer.getExitPosition();
			
			move.setPosition(next.getPosition().add(pos));
			
			ArrayDeque<DrawableObject> nearby = next.getEntitiesWithinCircle(move.getPosition(), move.getRadius()+transfer.getRadius());
			for(DrawableObject d:nearby)
				if(d instanceof Portal && ((Portal) d).getState() == Portal.State.ACTIVE)
					((Portal) d).setState(Portal.State.INACTIVE);
			
			changeActiveRoom(next);
		}
		
		cam.setScale(scale);
		cam.update(elapsedSeconds);
	}
	
	double shearx = 0;
	protected void draw(Graphics2D g, double elapsedSeconds){
		AffineTransform prev = g.getTransform();
		Stroke pStroke = g.getStroke();
		
		//Draw non-focus rooms
		//In the future, sort rooms by depth before drawing. Determine proper shadow quantity.
		Camera backbackCam = new Camera(cam.getPosition(), 1/(1 + 2*layerSpacing)*cam.scale());
		backbackground.drawAll(g, elapsedSeconds, backbackCam);
		
		g.setColor(new Color(0,0,0,150));
		g.fillRect(0, 0, GameClient.frameWidth, GameClient.frameHeight);
		
		Camera backCam = new Camera(cam.getPosition(), 1/(1 + layerSpacing)*cam.scale());
		background.drawAll(g, elapsedSeconds, backCam);

		g.setColor(new Color(0,0,0,150));
		g.fillRect(0, 0, GameClient.frameWidth, GameClient.frameHeight);
		
		//Draw current room
		room.drawAll(g, elapsedSeconds, cam);
		
		//Draw camera information
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
			g.drawString(String.format("%.4f", cam.scale()), GameClient.frameWidth/2-5, GameClient.frameHeight-30);
		}
		
		g.setStroke(pStroke);
		g.setTransform(prev);
	}
	
	public void changeActiveRoom(Room next){
		ArrayDeque<Integer> transfer = new ArrayDeque<Integer>();
		if(activeActor > -1)
			transfer.add(actors[activeActor].getOID());
		
		Room temp = room;
		room = RoomManager.changeRoom(transfer, next.getRID());
		background = temp;
		
		synchronized(room.getEntityList()){
			drawList = room.getEntityMap();
		}
	}
	
	@Override
	protected void keyPressed(KeyEvent e) {
		if(!keys.get(e.getKeyCode()) && e.getKeyCode() == KeyEvent.VK_P){
			changeActiveRoom(background);
		}
		
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
