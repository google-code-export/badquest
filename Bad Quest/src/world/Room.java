package world;

import gameObjects.Actor;
import gameObjects.DrawableObject;

import java.awt.Graphics2D;
import java.util.ArrayDeque;
import java.util.TreeMap;

import util.Collision;
import util.Vector;
import client.Camera;

public class Room {
	private Tile[][] map;
	private TreeMap<Integer, DrawableObject> entityMap;
	
	private Vector position;
	
	public final int R,C;
	private final int RID;
	/**
	 * Initialize a room with R row and C columns
	 * @param R
	 * @param C
	 */
	public Room(int R, int C){
		RID = RoomManager.register(this);
		
		this.R = R;
		this.C = C;
		position = new Vector(0,0);
		
		map = new Tile[R][C];
		for(int i = 0; i < R; i++)
			for(int j = 0; j < C; j++)
				map[i][j] = new Stone(i,j,this);
		for(int i = 0; i < R; i++){
			map[i][0] = new Wall(i,0,this);
			map[i][C-1] = new Wall(i,C-1,this);
		}
		for(int i = 0; i < C; i++){
			map[0][i] = new Wall(0,i,this);
			map[R-1][i] = new Wall(R-1,i,this);
		}
		
		map[7][5] = new Wall(7,5,this);
		map[7][6] = new Wall(7,6,this);
		map[7][7] = new Wall(7,7,this);
		map[8][7] = new Wall(8,7,this);
		map[9][7] = new Wall(9,7,this);
		map[9][6] = new Wall(9,6,this);
		
		entityMap = new TreeMap<Integer, DrawableObject>();
	}
	
	public Room(int selector, Vector v){
		RID = RoomManager.register(this);
		position = new Vector(v);
		
		map = DebugRoomMaker.selectPrebuilt(selector,this);
		R = map.length;
		C = map[0].length;
		
		entityMap = new TreeMap<Integer, DrawableObject>();
	}
	
	public int getRID(){
		return RID;
	}
	
	public Vector getPosition(){
		return new Vector(position);
	}
	
	public void addEntity(DrawableObject obj){
		synchronized(entityMap){
			entityMap.put(obj.getOID(),obj);
		}
	}
	
	/**
	 * Add a drawable object to this Room at the specified room coordinates.
	 * @param obj
	 * @param v Coordinates relative to the top-left corner of the room.
	 */
	public void addEntityAt(DrawableObject obj, Vector v){
		obj.setPosition(getPosition().add(v));
		synchronized(entityMap){
			entityMap.put(obj.getOID(),obj);
		}
	}
	
	public ArrayDeque<DrawableObject> getEntityList(){
		ArrayDeque<DrawableObject> entityList = new ArrayDeque<DrawableObject>();
		synchronized(entityMap){
			for(Integer x:entityMap.keySet())
				entityList.add(entityMap.get(x));
		}
		return entityList;
	}
	
	public TreeMap<Integer, DrawableObject> getEntityMap(){
		return entityMap;
	}
	
	public boolean actorWithinCircle(Vector p, double r){
		boolean flag = false;
		synchronized(entityMap){
			for(Integer x:entityMap.keySet())
				if(entityMap.get(x) instanceof Actor && entityMap.get(x).getPosition().dis2(p) <= r*r)
					flag = true;
		}
		return flag;
	}
	
	public void collideWithSolids(DrawableObject obj, double elapsedSeconds){
		synchronized(entityMap){
			Collision.collideObjectWithRoom(map, obj, elapsedSeconds);
		}
	}
	
	public void drawAll(Graphics2D g, double elapsedSeconds, Camera cam){
		for(Tile[] row:map)
			for(Tile t:row)
				t.drawBody(g, elapsedSeconds, cam);
		
		synchronized(entityMap){
			for(Integer e:entityMap.keySet())
				entityMap.get(e).drawBody(g, elapsedSeconds, cam);
		}
	}
	
	public String toString(){
		return "Room " + RID;
	}
}
