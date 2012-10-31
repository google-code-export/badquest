package world;

import java.awt.Graphics2D;

import util.Vector;
import client.Camera;

public class Void extends Tile {
	public Void(int y, int x, Room owner){
		super(new Vector(x*Tile.SIZE, y*Tile.SIZE), TileType.VOID, owner);
	}
	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam) {
		
	}
}
