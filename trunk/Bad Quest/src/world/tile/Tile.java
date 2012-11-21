package world.tile;

import graphics.Camera;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;

import util.Vector;
import world.Room;

public abstract class Tile {
	protected Room owner;
	protected Color color = Color.gray;
	protected Vector position,center;
	public final static int SIZE = 24;
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
