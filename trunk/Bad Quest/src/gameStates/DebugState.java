package gameStates;

import gameObjects.Actor;
import gameObjects.DebugBall;
import gameObjects.DebugEnemy;
import gameObjects.Door;
import gameObjects.DrawableObject;
import gameObjects.Keyper;
import gameObjects.Player;
import gameObjects.Portal;
import graphics.Camera;
import graphics.DimmerGraphics;

import java.awt.Color;
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
import world.tile.Wall;
import world.tile.Wire;

public class DebugState extends State{
	Room room = new Room(0,new Vector(400,0),0);
//	Room room = new Room(21, 50, 0);
	Room fore = new Room(1,new Vector(-50,-50), 0);
	Room background = new Room(false,30,new Vector(-100,0),1);
	Room backbackground = new Room(true,50,new Vector(-300,0),2);
//	Room backgroundGen = new Room(20, 40, 1);
	ArrayList<Room> roomList = RoomManager.getRoomList();
	
	Player player;
	Actor[] actors;
	TreeMap<Integer, DrawableObject> drawList;
	Camera cam = new Camera(new Vector(0,0));
	
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
		
		room.setPosition(new Vector(400,0));
//		backgroundGen.setPosition(new Vector(700,-200));
		
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
		
		room.addEntity(new Door());
		
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
			room.addEntityAt(a,new Vector((x=x+Tile.SIZE)-Tile.SIZE, 4*Tile.SIZE));
		
		follower.setFollow(player);
		forefollower.setFollow(player);
		fore.addEntityAt(forefollower, new Vector(Tile.SIZE,Tile.SIZE));
		
		DebugBall rock = new DebugBall(new Vector(651,350),13);
		
		DebugEnemy[] masstest = new DebugEnemy[10];
		for(int i = 0; i < 10; i++){
			masstest[i] = new DebugEnemy(10);
//			masstest[i].setFollow(i==0?player:masstest[i-1]);
			masstest[i].setFollow(rock);
			room.addEntityAt(masstest[i], new Vector(Tile.SIZE*20 + (i+20)*Tile.SIZE, Tile.SIZE*9));
		}
		
		room.addEntity(rock);
		room.addEntityAt(new Keyper("Ralph"), new Vector(Tile.SIZE*5, Tile.SIZE*5));
		
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
	
	static double TOGGLE = 0;
	protected void update(double elapsedSeconds){
		if(keys.get(KeyEvent.VK_Q))
			scale = scale*9/10.;
		if(keys.get(KeyEvent.VK_E))
			scale = scale*10/9.;
		
		TOGGLE += elapsedSeconds;
		if(TOGGLE >= 1){
			room.updateTile(0, 1, new Wire(0,1,room));
			room.updateTile(6, 7, new Wire(6,7,room));
			room.updateTile(7, 7, new Wire(7,7,room));
			TOGGLE = 0;
		}else if(TOGGLE >= .5){
			room.updateTile(0, 1, new Wall(0,1,room));
			room.updateTile(6, 7, new Wall(6,7,room));
			room.updateTile(7, 7, new Wall(7,7,room));
		}
		
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
		cam.draw(g, elapsedSeconds);
		
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
		
		if(activeActor != -1 && keys.get(KeyEvent.VK_T))
			actors[activeActor].setSolid(!actors[activeActor].isSolid());
		
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
