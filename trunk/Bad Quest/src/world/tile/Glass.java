package world.tile;

import graphics.Camera;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import util.Vector;
import world.Room;

public class Glass extends Tile {
	
	public Glass(int y, int x, Room owner){
		super(new Vector(x*Tile.SIZE,y*Tile.SIZE), TileType.GLASS, owner);
	}
	
	@Override
	public void update(double elapsedSeconds) {
	
	}
	
	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam) {
		AffineTransform prev = g.getTransform();
		
		g.translate(cam.xTranslatePosition(position.x), cam.yTranslatePosition(position.y));
		g.scale(cam.scale(), cam.scale());
		
		g.setClip(0, 0, Tile.SIZE, Tile.SIZE);
		
		g.setColor(new Color(255,255,255,10));
		g.fillRect(0, 0, Tile.SIZE, Tile.SIZE);
		
		g.setColor(new Color(128,128,128,40));
		g.drawArc(-Tile.SIZE/2, -Tile.SIZE/2, Tile.SIZE, Tile.SIZE, 270, 90);
		g.drawArc(Tile.SIZE/2, Tile.SIZE/2, Tile.SIZE, Tile.SIZE, 90, 90);
		
		g.setColor(new Color(64,64,64,200));
		g.drawRect(0, 0, Tile.SIZE, Tile.SIZE);
		
		g.setClip(null);
		
		g.setTransform(prev);
	}
}
