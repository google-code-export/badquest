package world;

import gameObjects.DrawableObject;

import java.awt.Graphics2D;
import java.util.ArrayDeque;

import util.Collision;
import client.Camera;

public class Room {
	private Tile[][] map;
	private ArrayDeque<DrawableObject> entityList;
	public final int R,C;
	private int RID;
	/**
	 * Initialize a room with R row and C columns
	 * @param R
	 * @param C
	 */
	public Room(int R, int C){
		this.R = R;
		this.C = C;
		
		map = new Tile[C][R];
		for(int i = 0; i < C; i++)
			for(int j = 0; j < R; j++)
				map[i][j] = new Stone(i,j);
		for(int i = 0; i < C; i++){
			map[i][0] = new Wall(i,0);
			map[i][R-1] = new Wall(i,R-1);
		}
		for(int i = 0; i < R; i++){
			map[0][i] = new Wall(0,i);
			map[C-1][i] = new Wall(C-1,i);
		}
		
		map[7][5] = new Wall(7,5);
		map[7][6] = new Wall(7,6);
		map[7][7] = new Wall(7,7);
		map[8][7] = new Wall(8,7);
		map[9][7] = new Wall(9,7);
		map[9][6] = new Wall(9,6);
		
		entityList = new ArrayDeque<DrawableObject>();
	}
	
	public void setRID(int RID){
		this.RID = RID;
	}
	
	public int getRID(){
		return RID;
	}
	
	public void addEntity(DrawableObject obj){
		entityList.add(obj);
	}
	
	/**
	 * Returns a reference to this room's entity list.
	 * @return the room's entity list.
	 */
	public ArrayDeque<DrawableObject> getEntityList(){
		return entityList;
	}
	
	public void collideWithSolids(DrawableObject obj, double elapsedSeconds){
		Collision.collideObjectWithRoom(map, obj, elapsedSeconds);
	}
	
	public void drawAll(Graphics2D g, double elapsedSeconds, Camera cam){
		for(Tile[] row:map)
			for(Tile t:row)
				t.drawBody(g, elapsedSeconds, cam);
	}
}
