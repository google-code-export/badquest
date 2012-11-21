package world;

import gameObjects.Actor;
import gameObjects.DrawableObject;
import graphics.Camera;

import java.awt.Graphics2D;
import java.util.ArrayDeque;
import java.util.TreeMap;

import util.Collision;
import util.Vector;
import world.tile.Stone;
import world.tile.Tile;
import world.tile.Wall;

public class Room implements Comparable<Room>{
	private Tile[][] map;
	private TreeMap<Integer, DrawableObject> entityMap;
	
	private Vector position;
	private double layer;
	
	public final int R,C;
	private final int RID;
	/**
	 * Initialize a room with R row and C columns
	 * @param R
	 * @param C
	 */
	public Room(int R, int C, double d){
		RID = RoomManager.register(this);
		
		layer = d;
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
	
	public Room(int selector, Vector v, double d){
		RID = RoomManager.register(this);
		position = new Vector(v);
		layer = d;
		
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
	
	public double getLayer(){
		return layer;
	}
	
	public double getDepth(double currentLayer){
		return layer - currentLayer;
	}
	
	public void setPosition(Vector v){
		position = new Vector(v);
		for(int i = 0; i < R; i++)
			for(int j = 0; j < C; j++)
				map[i][j].updatePosition(i,j);
	}
	
	public void setLayer(double d){
		layer = d;
	}
	
	public void addEntity(DrawableObject obj){
		obj.setCurrentRoom(this);
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
		addEntity(obj);
	}
	
	public void removeEntity(int OID){
		synchronized(entityMap){
			entityMap.remove(OID);
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
	
	//TODO: Consolidate into getEntitiesWithinCircle
	public boolean actorWithinCircle(Vector p, double r){
		boolean flag = false;
		synchronized(entityMap){
			for(Integer x:entityMap.keySet())
				if(entityMap.get(x) instanceof Actor && entityMap.get(x).getPosition().dis2(p) <= r*r)
					flag = true;
		}
		return flag;
	}
	
	/**
	 * Retrieve a list of entities within the specified circle
	 * @param p the center of the circle
	 * @param r the radius of the circle
	 */
	public ArrayDeque<DrawableObject> getEntitiesWithinCircle(Vector p, double r){
		ArrayDeque<DrawableObject> ret = new ArrayDeque<DrawableObject>();
		synchronized(entityMap){
			for(Integer x:entityMap.keySet())
				if(entityMap.get(x).getPosition().dis2(p) <= r*r)
					ret.add(entityMap.get(x));
		}
		return ret;
	}
	
	public void collideWithSolids(DrawableObject obj, double elapsedSeconds){
		synchronized(entityMap){
			Collision.collideObjectWithRoom(map, obj, elapsedSeconds);
		}
	}
	
	public void clean(){
		ArrayDeque<Integer> remove = new ArrayDeque<Integer>();
		synchronized(entityMap){
			for(Integer e:entityMap.keySet())
				if(entityMap.get(e).isDead())
					remove.add(e);
		}
		
		for(Integer e:remove)
			entityMap.get(e).delete();
	}
	
	public void updateAll(double elapsedSeconds){
		for(Tile[] row:map)
			for(Tile t:row)
				t.update(elapsedSeconds);
		
		synchronized(entityMap){
//			for(Integer oid:entityMap.keySet())
//				if(entityMap.get(oid).isSolid())
//					collideWithSolids(entityMap.get(oid), elapsedSeconds);
			
			for(Integer oid:entityMap.keySet())
				entityMap.get(oid).update(elapsedSeconds);
		}
	}
	
	public void drawAll(Graphics2D g, double elapsedSeconds, Camera cam){
		//Get bounding box
		double[] bound = cam.getBoundingBox();
		//Compute draw bounds
		int left = (int)Math.floor((bound[0] - position.x)/Tile.SIZE);
		int top = (int)Math.floor((bound[1] - position.y)/Tile.SIZE);
		int right = (int)Math.floor((bound[2] - position.x)/Tile.SIZE);
		int bot = (int)Math.floor((bound[3] - position.y)/Tile.SIZE);
		
		//Only draw visible tiles
		for(int i = Math.max(top,0); i <= Math.min(bot,R-1); i++)
			for(int j = Math.max(left,0); j <= Math.min(right,C-1); j++)
				map[i][j].drawBody(g, elapsedSeconds, cam);
		
		synchronized(entityMap){
			for(Integer e:entityMap.keySet())
				entityMap.get(e).drawBody(g, elapsedSeconds, cam);
		}
	}
	
	public int compareTo(Room r){
		return (int)Math.signum(r.getLayer()-layer);
	}
	
	public String toString(){
		return "Room " + RID;
	}
}
