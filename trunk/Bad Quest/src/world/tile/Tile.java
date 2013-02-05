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
	protected int x,y; //The row and column of this tile relative to the room's top left corner
	protected Node node;
	protected boolean floor = true;
	
	public final static int SIZE = 24;
	public final TileType TID;
	
	private static int[] dx = new int[]{0,1,1,0};
	private static int[] dy = new int[]{0,0,1,1};
	
	public Tile(int x, int y, TileType t, Room owner){
		this.position = owner.getPosition().add(new Vector(SIZE*x, SIZE*y));
		this.center = this.position.add(new Vector(SIZE/2.,SIZE/2.));
		this.x = x;
		this.y = y;
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
	
	/**
	 * Returns the 4 corner points of this Tile.
	 * @return an array of size 4 with the corner points of this tile in the order of top left, top right, bottom right, bottom left
	 */
	public Vector[] getCardinalPoints(){
		Vector[] ret = new Vector[4];
		for(int i = 0; i < 4; i++)
			ret[i] = getPosition().add(new Vector(dx[i] * Tile.SIZE, dy[i] * Tile.SIZE));
		return ret;
	}
	
	/**
	 * Returns whether or not a segment with some radius intersects this Tile.
	 * @param a The starting point of the segment.
	 * @param b The ending point of the segment.
	 * @param R The radius of the segment.
	 * @return True if the segment intersects this Tile and the Tile is an obstruction, false otherwise.
	 */
	public boolean intersectsSegment(Vector a, Vector b, double R){
		if(floor && !isSolid())
			return false;
		
		Vector[] p = getCardinalPoints();
		
		for(int i = 0; i < 4; i++){
			Vector c = p[i];
			Vector d = p[(i+1)%4];
			if(Geometry.segmentIntersect(a, b, c, d) || Geometry.plsDist(a, b, p[i]) <= R)
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
