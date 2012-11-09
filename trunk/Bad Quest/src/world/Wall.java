package world;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import util.Vector;
import client.Camera;

public class Wall extends Tile {
	public Wall(int y, int x, Room owner){
		super(new Vector(x*Tile.SIZE,y*Tile.SIZE), TileType.WALL, owner);
//		color = Color.RED.darker().darker().darker();
	}
	
	@Override
	public boolean isSolid() {
		return true;
	}
	
	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam){
		AffineTransform prev = g.getTransform();
		
		g.translate(cam.xTranslatePosition(position.x), cam.yTranslatePosition(position.y));
		g.scale(cam.scale(), cam.scale());
		g.setClip(0, 0, Tile.SIZE, Tile.SIZE);
		
		g.setColor(color.darker());
		g.fillRect(0, 0, Tile.SIZE+1, Tile.SIZE+1);
		g.setColor(color.brighter());
		g.fillRect(0,0,Tile.SIZE-1,Tile.SIZE-1);
		g.setColor(color);
		g.fillRect(1, 1, Tile.SIZE-2, Tile.SIZE-2);
		
		g.setClip(null);
		g.setTransform(prev);
	}
}
