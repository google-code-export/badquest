package world.tile;

import graphics.Camera;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import world.Room;

public class Stone extends Tile{
	public Stone(int y, int x, Room owner){
		super(x, y, TileType.STONE, owner);
		color = new Color(33,45,52);
	}
	
	public Stone(int y, int x, Room owner, Color c){
		super(x, y, TileType.STONE, owner);
		color = c;
	}
	
	@Override
	public void update(double elapsedSeconds) {
	
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
