package world;

import java.awt.Graphics2D;

import util.Vector;
import client.Camera;

public class Void extends Tile {
	public Void(Vector position){
		super(position, TileType.VOID);
	}
	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam) {
		
	}
}
