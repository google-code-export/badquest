package world.tile;

import graphics.Camera;

import java.awt.Graphics2D;

import world.Room;

public class Void extends Tile {
	public Void(int y, int x, Room owner){
		super(x, y, TileType.VOID, owner);
		floor = false;
	}
	
	@Override
	public void update(double elapsedSeconds) {
		
	}
	
	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam) {
		
	}
}
