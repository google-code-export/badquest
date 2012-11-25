package gameStates;

import gameObjects.Actor;
import gameObjects.DebugEnemy;
import gameObjects.DrawableObject;
import gameObjects.ObjectManager;
import gameObjects.Player;
import gameObjects.Portal;
import graphics.Camera;
import graphics.DimmerGraphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.TreeMap;

import util.Vector;
import world.Room;
import world.RoomManager;
import world.tile.Tile;
import client.GameClient;
import client.KeyBindings;

public class DebugState extends State{
	Room room = new Room(0,new Vector(400,0),0);
	Room fore = new Room(1,new Vector(-50,-50), 0);
	Room background = new Room(50,20,1);
	Room backbackground = new Room(50,50,2);
	ArrayList<Room> roomList = RoomManager.getRoomList();
	
	Player player;
	Actor[] actors;
	TreeMap<Integer, DrawableObject> drawList; 
	Camera cam = new Camera(new Vector(0,0));
	
	int[] dx = new int[]{0,0,-1,1};
	int[] dy = new int[]{-1,1,0,0};
	BitSet keys; //Set of active keyboard presses
	BitSet clicks; //Set of active mouse clicks
	double acc = 225;
	double scale = 2.868;
	double layerSpacing = .75;
	double drawLayers = 4;
	
	int activeActor = 0;
	int mx = 0, my = 0;
	
	public DebugState(){
		keys = new BitSet();
		clicks = new BitSet();
		
		Portal A = new Portal(room, new Vector(500,50));
		Portal B = new Portal(background, new Vector(50,200));
		Portal C = new Portal(background, new Vector(50,500));
		Portal D = new Portal(backbackground, new Vector(50,200));
		Portal E = new Portal(room, new Vector(400,100).add(room.getPosition()));
		Portal F = new Portal(fore, new Vector(14*Tile.SIZE,Tile.SIZE).add(fore.getPosition()));
		
		A.linkAndSetActive(B);
		C.linkAndSetActive(D);
		E.linkAndSetActive(F);
		
		fore.addEntity(F);
		room.addEntity(E);
		room.addEntity(A);
		background.addEntity(B);
		background.addEntity(C);
		backbackground.addEntity(D);
		
		player = new Player("Rawnblade", 10, new Vector(200,200));
		DebugEnemy follower = new DebugEnemy(10);
		DebugEnemy forefollower = new DebugEnemy(10);
		actors = new Actor[]{player, 
							 new Player("Rusty Stranglechain", 10, new Vector(120,100), new Color(.55f, .3f, .02f)),
							 new Actor("Gunther Boneguzzler", 10, new Vector(40,100)),
							 new Actor("Pork Undertow", 10, new Vector(60,100)),
							 new Actor("Kurt Lioncrusher", 10, new Vector(80,100)),
							 new Actor("Horace Elbowdrum", 10, new Vector(100,100)),
							 follower};
		
		RoomManager.setRoom(room.getRID());
		
		background.addEntityAt(new Actor("Big McLargehuge", 4), new Vector(Tile.SIZE*10, Tile.SIZE*15));
		
		int x = 41;
		for(Actor a:actors)
			room.addEntityAt(a,new Vector((x=x+21)-21, 4*Tile.SIZE));
		
		follower.setFollow(player);
		forefollower.setFollow(player);
		fore.addEntityAt(forefollower, new Vector(Tile.SIZE,Tile.SIZE));
		
		DebugEnemy[] masstest = new DebugEnemy[10];
		for(int i = 0; i < 10; i++){
			masstest[i] = new DebugEnemy(10);
			masstest[i].setFollow(i==0?player:masstest[i-1]);
			room.addEntityAt(masstest[i], new Vector(Tile.SIZE*20 + (i+20)*Tile.SIZE, Tile.SIZE*10));
		}
		
		new Room(1, new Vector(-160, 200), 0);
		
		cam.setPosition(player.getPosition());
		roomList = RoomManager.getRoomList();
		drawList = room.getEntityMap();
	}
	
	public void setCameraFollow(DrawableObject focus){
		cam.follow(focus);
	}
	
	public DrawableObject getCameraFollow(){
		return cam.getFocus();
	}

	//**************
	//State stuff
	//**************
	
	protected void update(double elapsedSeconds){
		if(keys.get(KeyEvent.VK_Q))
			scale = scale*9/10.;
		if(keys.get(KeyEvent.VK_E))
			scale = scale*10/9.;
		
//		for(Actor a:actors)
//			a.setInternalVelocity(new Vector(0,0));
		
		if(activeActor > -1){
//			actors[activeActor].setInternalVelocity(velocity.scale(1/scale));
			if(actors[activeActor] instanceof Player){
				((Player)actors[activeActor]).setSpeed(acc/scale);
				((Player)actors[activeActor]).receiveInput(keys, clicks);
			}
			
			if(keys.get(KeyEvent.VK_SPACE))
				actors[activeActor].applyExternalVelocity(new Vector(actors[activeActor].getAngle()).scale(acc/(scale*2)));
			if(clicks.get(MouseEvent.BUTTON1) || clicks.get(MouseEvent.BUTTON3))
				actors[activeActor].setLookAt(cam.screenToWorld(new Vector(mx,my)));
			else if(actors[activeActor].getInternalVelocity().mag2() > 0)
				actors[activeActor].setLookAt(actors[activeActor].getPosition().add(actors[activeActor].getInternalVelocity()));
			else
				actors[activeActor].setLookAt(null);
			cam.follow(actors[activeActor]);
		}else{
			cam.follow(null);
		}
		
		room.updateAll(elapsedSeconds);
		
		Portal transfer = null;
		Actor move = null;
		synchronized(drawList){
			for(Integer oid:drawList.keySet()){
				if(drawList.get(oid) instanceof Player)
				for(Integer pid:drawList.keySet()){
					if(drawList.get(pid) instanceof Portal){
						Portal p = (Portal)drawList.get(pid);
						Player a = (Player)drawList.get(oid);
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
		
		room.clean(); //Delete all dead objects
		
		cam.setScale(scale);
		cam.update(elapsedSeconds);
	}
	
	protected void draw(Graphics2D g, double elapsedSeconds){
		AffineTransform prev = g.getTransform();
		Stroke pStroke = g.getStroke();
		
		DimmerGraphics backgroundGraphics = new DimmerGraphics(g,1);
		
		//Draw non-focus rooms
		for(Room r:roomList){
			double L = r.getDepth(room.getLayer());
			
			if(L < 0)
				break;
			if(r.getRID() == room.getRID())
				continue;
			
			backgroundGraphics.setLevel(1/(2+L*L));
			Camera backCam = new Camera(cam.getPosition(), 1/(1 + L*layerSpacing)*cam.scale());
			r.drawAll(backgroundGraphics, elapsedSeconds, backCam);
		}
		
		backgroundGraphics.dispose();
		
		//Draw current room, above all else
		room.drawAll(g, elapsedSeconds, cam);
		
		//Draw camera information
		g.setColor(Color.WHITE);
		g.setStroke(new BasicStroke(2.f));
		g.drawLine(GameClient.frameWidth/2, GameClient.frameHeight/2-20, GameClient.frameWidth/2, GameClient.frameHeight/2+20);
		g.drawLine(GameClient.frameWidth/2-20, GameClient.frameHeight/2, GameClient.frameWidth/2+20, GameClient.frameHeight/2);
		
		g.drawRect(GameClient.frameWidth/2-150, 30, 300, 40);
		FontMetrics f = g.getFontMetrics();
		g.drawString(String.format("%.2f", 1/elapsedSeconds), GameClient.frameWidth-(int)(f.getStringBounds("Following", g).getWidth()+10), 45);
		g.drawString("Following", GameClient.frameWidth/2-(int)(f.getStringBounds("Following", g).getWidth()/2), 45);
		if(cam.getFocus() != null){
			if(cam.getFocus() instanceof Actor)
				g.drawString(((Actor)(cam.getFocus())).getName(), GameClient.frameWidth/2-(int)(f.getStringBounds(((Actor)(cam.getFocus())).getName(), g).getWidth()/2), 62);
			g.drawString(String.format("(%.4f, %.4f)", cam.getFocus().getPosition().x, cam.getFocus().getPosition().y), GameClient.frameWidth/2+5, GameClient.frameHeight/2-7);
			g.drawString(String.format("(%.4f, %.4f)", cam.getFocus().getVelocity().x, cam.getFocus().getVelocity().y), GameClient.frameWidth/2+5, GameClient.frameHeight/2+15);
			g.drawString(String.format("%.4f", cam.scale()), GameClient.frameWidth/2-5, GameClient.frameHeight-30);
			g.drawString(String.format("%d", ObjectManager.objectCount()), GameClient.frameWidth/2-5, GameClient.frameHeight-45);
		}
		KeyBindings.getInputList(keys, clicks);
		g.setStroke(pStroke);
		g.setTransform(prev);
	}
	
	public void changeActiveRoom(Room next){		
		ArrayDeque<Integer> transfer = new ArrayDeque<Integer>();
		if(activeActor > -1)
			transfer.add(actors[activeActor].getOID());
		
		boolean changed = next.getRID() != room.getRID();
		room = RoomManager.changeRoom(transfer, next.getRID());
		
		synchronized(room.getEntityList()){
			drawList = room.getEntityMap();
		}
		
		if(changed)
			cam.shake(10,.8);
	}
	
	@Override
	protected void keyPressed(KeyEvent e) {
		keys.set(e.getKeyCode());
		
		if(e.getKeyCode() == KeyEvent.VK_0){
			activeActor = -1;
		}else if(e.getKeyCode() > KeyEvent.VK_0 && e.getKeyCode() < KeyEvent.VK_1+actors.length){
			activeActor = e.getKeyCode()-KeyEvent.VK_1;
			changeActiveRoom(actors[activeActor].getCurrentRoom());
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
