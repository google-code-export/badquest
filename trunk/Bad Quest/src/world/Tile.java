package world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;

import util.Vector;
import client.Camera;

public abstract class Tile {
	protected Room owner;
	protected Color color = Color.gray;
	protected Vector position,center;
	public final static int SIZE = 21;
	public final TileType TID;
	public Tile(Vector position, TileType t, Room owner){
		this.position = owner.getPosition().add(position);
		this.center = this.position.add(new Vector(SIZE/2.,SIZE/2.));
		TID = t;
		this.owner = owner;
	}
	
	public boolean isSolid(){
		return false;
	}
	
	public Vector getPosition(){
		return new Vector(position);
	}
	
	public Vector getCenter(){
		return new Vector(center);
	}
	
	public abstract void drawBody(Graphics2D g, double elapsedSeconds, Camera cam);
	
	private static int enumCount = 0;
	public static enum TileType{
		VOID,
		WALL,
		DIRT,
		STONE,
		PIT;
		
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
