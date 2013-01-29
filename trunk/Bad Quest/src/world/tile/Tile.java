package world.tile;

import gameAI.Node;
import graphics.Camera;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;

import util.Geometry;
import util.Vector;
import world.Room;

public abstract class Tile {
	protected Room owner;
	protected Color color = Color.gray;
	protected Vector position,center;
	protected Node node;
	protected boolean floor = true;
	
	public final static int SIZE = 24;
	public final TileType TID;
	
	private static int[] dx = new int[]{0,1,1,0};
	private static int[] dy = new int[]{0,0,1,1};
	
	public Tile(int x, int y, TileType t, Room owner){
		this.position = owner.getPosition().add(new Vector(SIZE*x, SIZE*y));
		this.center = this.position.add(new Vector(SIZE/2.,SIZE/2.));
		TID = t;
		this.owner = owner;
		node = new Node(center, y*owner.C + x);
	}
	
	public boolean isSolid(){
		return false;
	}
	
	public boolean hasFloor(){
		return floor;
	}
	
	public Vector getPosition(){
		return new Vector(position);
	}
	
	public Vector getCenter(){
		return new Vector(center);
	}
	
	public Node getNode(){
		return node;
	}
	
	public boolean intersectsSegment(Vector a, Vector b){
		if(floor && !isSolid())
			return false;
		
		for(int i = 0; i < 4; i++){
			Vector c = getPosition().add(new Vector(dx[i] * Tile.SIZE, dy[i] * Tile.SIZE));
			Vector d = getPosition().add(new Vector(dx[(i+1)%4] * Tile.SIZE, dy[(i+1)%4] * Tile.SIZE));
			if(Geometry.segmentIntersect(a, b, c, d))
				return true;
		}
		return false;
	}
	
	/**
	 * Call if the room's position changes somehow.
	 */
	public void updatePosition(int y, int x){
		this.position = owner.getPosition().add(new Vector(x*SIZE, y*SIZE));
		this.center = this.position.add(new Vector(SIZE/2.,SIZE/2.));
	}
	
	public abstract void update(double elapsedSeconds);
	public abstract void drawBody(Graphics2D g, double elapsedSeconds, Camera cam);
	
	private static int enumCount = 0;
	public static enum TileType{
		VOID,
		WALL,
		WATER,
		DIRT,
		STONE,
		GLASS,
		WOOD_PLANK_H,
		WOOD_PLANK_V,
		SMART;
		
		public final int val;
		private TileType(){
			val = enumCount++;
		}
		
		private static HashMap<Integer, TileType> map = new HashMap<Integer, TileType>();
		static{
			for(TileType tid:TileType.values())
				map.put(tid.val,tid);
		}
		
		public static TileType getTileFromID(int tid){
			return map.get(tid);
		}
	}
}
